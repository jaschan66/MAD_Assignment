package my.com.abibasInven.ui

import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.logindemo.util.cropToBlob
import com.example.logindemo.util.errorDialog
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase
import com.theartofdev.edmodo.cropper.CropImage
import kotlinx.coroutines.launch
import my.com.abibasInven.R
import my.com.abibasInven.data.*
import my.com.abibasInven.databinding.FragmentHomeBinding
import my.com.abibasInven.databinding.FragmentProductAddBinding
import my.com.abibasInven.databinding.FragmentProductUpdateBinding
import my.com.abibasInven.util.LocationAdapter



class ProductAddFragment : Fragment() {

    private lateinit var binding: FragmentProductAddBinding
    private val nav by lazy {findNavController()}
    private val vmPro : ProductViewModel by activityViewModels()
    private val vmSpn : SpinnerViewModel by activityViewModels()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = FragmentProductAddBinding.inflate(inflater, container, false)


        val spnLoc = vmSpn.getLocation()
        val spnSup = vmSpn.getSupplierName()
        val SupSize = vmSpn.calSupSize()
        val spnArray = arrayListOf<String>()
        val spnArray2 = arrayListOf<String>()
        val spnCategory = vmSpn.getCategory()
        val spnArray3 = arrayListOf<String>()

        val adp2 = ArrayAdapter<String>(requireContext(),android.R.layout.simple_spinner_item)
        adp2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spnAddProductLocation.adapter = adp2

        val adp3 = ArrayAdapter<String>(requireContext(),android.R.layout.simple_spinner_item)
        adp3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spnAddProductSupplier.adapter = adp3

        val adp4 = ArrayAdapter<String>(requireContext(),android.R.layout.simple_spinner_item)
        adp3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spnAddProductCategory.adapter = adp4

        spnLoc.observe(viewLifecycleOwner) { list ->
            //list.groupBy { it.ID}
            val num2 = list.size - 5
            num2
            val LocSize = vmSpn.calLocSize()
            LocSize
            if (list.size > spnArray.size) {
                for (i in 0..LocSize - 1) {
                    adp2.add(list[i].ID)
                    spnArray.add(list[i].ID)
                }
            } else if (num2 <= spnArray.size ){
                spnArray.clear()
                adp2.clear()
                for (i in 0..LocSize - 1) {
                    adp2.add(list[i].ID)
                    spnArray.add(list[i].ID)
                }
            }
        }

        spnSup.observe(viewLifecycleOwner) { list ->
            //list.groupBy { it.ID}
            val num = list.size
            val SupSize = vmSpn.calSupSize()
            if (list.size > spnArray2.size) {
                for (i in 0..SupSize - 1) {
                    adp3.add(list[i].ID)
                    spnArray2.add(list[i].ID)
                }
            } else if (num <= spnArray2.size ){
                spnArray2.clear()
                adp3.clear()
                for (i in 0..SupSize - 1) {
                    adp3.add(list[i].ID)
                    spnArray2.add(list[i].ID)
                }
            }
        }

        spnCategory.observe(viewLifecycleOwner) { list ->
            //list.groupBy { it.ID}
            val num3 = list.size
            val categorySize = vmSpn.calCategorySize()
            if (list.size > spnArray3.size) {
                for (i in 0..categorySize - 1) {
                    adp4.add(list[i].ID)//change here to get value
                    spnArray3.add(list[i].ID) //change here to get value
                }
            } else if (num3 <= spnArray2.size ){
                spnArray3.clear()
                adp4.clear()
                for (i in 0..categorySize - 1) {
                    adp4.add(list[i].ID)
                    spnArray3.add(list[i].ID)
                }
            }
        }


        binding.imgAddProductPhoto.setOnClickListener {
            try {
                CropImage.activity()
                    .start(requireContext(),this)
            } catch (e: ActivityNotFoundException) {

            }
        }




        binding.btnAddProduct.setOnClickListener {
            val id = "PR" + (vmPro.calSize() + 1).toString()
            val chkID = vmPro.validID(id)
            var locID = binding.spnAddProductLocation.selectedItem.toString()
            var supID = binding.spnAddProductSupplier.selectedItem.toString()
            var catID = binding.spnAddProductCategory.selectedItem.toString()

            val p = Product(
                ID = chkID,
                name = binding.edtAddProductName.text.toString().trim(),
                qty = 0,
                qtyThreshold = binding.edtAddProductQtyThreshold.text.toString().toInt(),
                categoryID = catID,
                photo = binding.imgAddProductPhoto.cropToBlob(300,300),
                locationID = locID,
                supplierID = supID
            )

            val err = vmPro.validate(p)
            if (err != "") {
                errorDialog(err)
            } else {
                vmPro.set(p)
                nav.navigate(R.id.productFragment)
            }
        }

        return binding.root
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            var result = CropImage.getActivityResult(data)
            binding.imgAddProductPhoto.setImageURI(result.uri)

        }
    }

}

