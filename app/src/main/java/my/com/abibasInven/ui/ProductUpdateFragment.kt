package my.com.abibasInven.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.logindemo.util.cropToBlob
import com.example.logindemo.util.errorDialog
import com.example.logindemo.util.toBitmap
import my.com.abibasInven.data.Product
import my.com.abibasInven.data.ProductViewModel
import my.com.abibasInven.data.SpinnerViewModel
import my.com.abibasInven.data.User
import my.com.abibasInven.databinding.FragmentProductUpdateBinding
import android.R
import com.google.android.material.bottomnavigation.BottomNavigationView


class ProductUpdateFragment : Fragment() {

    private lateinit var binding: FragmentProductUpdateBinding
    private val nav by lazy {findNavController()}
    private val vmPro : ProductViewModel by activityViewModels()
    private val vmSpn : SpinnerViewModel by activityViewModels()
    private val ID by lazy { requireArguments().getString("ID", null) }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = FragmentProductUpdateBinding.inflate(inflater, container, false)

        val bottomNav : BottomNavigationView = requireActivity().findViewById(my.com.abibasInven.R.id.bottomNavigationView)
        bottomNav.visibility = View.GONE
        reset()
        val p = vmPro.get(ID)
        binding.btnProductReset.setOnClickListener { reset() }
        binding.btnUpdateProduct.setOnClickListener {
            if (p != null) {
                update(p)
            }
        }
        binding.btnBackUpdateProduct.setOnClickListener { nav.navigate(my.com.abibasInven.R.id.action_productUpdateFragment_to_productFragment) }

        return binding.root

    }


    private fun update(p: Product) {
        var catR = binding.edtUpdateProductCategory.selectedItem.toString()
        var locR = binding.edtUpdateProductLocation.selectedItem.toString()
        var supR = binding.edtUpdateProductSupplier.selectedItem.toString()

        val up = Product(
            ID = p.ID,
            name = binding.edtUpdateProductName.text.toString().trim(),
            qty = p.qty,
            qtyThreshold = binding.edtUpdateQtyThreshold.text.toString().toInt(),
            categoryID = catR,
            photo = binding.imgUpdateProduct.cropToBlob(300,300),
            locationID = locR,
            supplierID = supR
        )

        val e = vmPro.validate(up)
        if (e != ""){
            errorDialog(e)
            return
        }
        vmPro.set(up)
        nav.navigateUp()
    }
    private fun reset() {

        val p = vmPro.get(ID)
        if (p == null) {
            nav.navigateUp()
            return
        }

        load(p)
    }

    private fun load(p: Product) {

        val spnLoc = vmSpn.getLocation()
        val spnSup = vmSpn.getSupplierName()
        val SupSize = vmSpn.calSupSize()
        val spnArray = arrayListOf<String>()
        val spnArray2 = arrayListOf<String>()
        val spnCategory = vmSpn.getCategory()
        val spnArray3 = arrayListOf<String>()

        val proSup = p.supplierID
        val proLoc = p.locationID
        val proCat = p.categoryID
        val adp2 = ArrayAdapter<String>(requireContext(),R.layout.simple_spinner_item)
        adp2.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)



//        val compareValue = "some value"
//        val adapter = ArrayAdapter.createFromResource(
//            this,
//            R.array.select_state,
//            R.layout.simple_spinner_item
//        )
//        adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
//        mSpinner.setAdapter(adapter)
//        if (compareValue != null) {
//            val spinnerPosition = adapter.getPosition(compareValue)
//            mSpinner.setSelection(spinnerPosition)
//        }

        val adp3 = ArrayAdapter<String>(requireContext(), R.layout.simple_spinner_item)
        adp3.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)


        val adp4 = ArrayAdapter<String>(requireContext(), R.layout.simple_spinner_item)
        adp4.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)


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
                val spinnerPosition = adp2.getPosition(proLoc)
                binding.edtUpdateProductLocation.adapter = adp2
                binding.edtUpdateProductLocation.setSelection(spinnerPosition)
            } else if (num2 <= spnArray.size ){
                spnArray.clear()
                adp2.clear()
                for (i in 0..LocSize - 1) {
                    adp2.add(list[i].ID)
                    spnArray.add(list[i].ID)
                }
                val spinnerPositionLoc = adp2.getPosition(proLoc)
                binding.edtUpdateProductLocation.adapter = adp2
                binding.edtUpdateProductLocation.setSelection(spinnerPositionLoc)
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
                val spinnerPosition = adp3.getPosition(proSup)
                binding.edtUpdateProductSupplier.adapter = adp3
                binding.edtUpdateProductSupplier.setSelection(spinnerPosition)
            } else if (num <= spnArray2.size ){
                spnArray2.clear()
                adp3.clear()
                for (i in 0..SupSize - 1) {
                    adp3.add(list[i].ID)
                    spnArray2.add(list[i].ID)
                }
                val spinnerPositionSup = adp3.getPosition(proSup)
                binding.edtUpdateProductSupplier.adapter = adp3
                binding.edtUpdateProductSupplier.setSelection(spinnerPositionSup)
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
                val spinnerPositionCat = adp4.getPosition(proCat)
                binding.edtUpdateProductCategory.adapter = adp4
                binding.edtUpdateProductCategory.setSelection(spinnerPositionCat)
            } else if (num3 <= spnArray3.size ){
                spnArray3.clear()
                adp4.clear()
                for (i in 0..categorySize - 1) {
                    adp4.add(list[i].ID)
                    spnArray3.add(list[i].ID)
                }
            }
        }
        binding.imgUpdateProduct.setImageBitmap(p.photo.toBitmap())
        binding.edtUpdateProductName.setText(p.name)
        binding.edtUpdateQtyThreshold.setText(p.qtyThreshold.toString())


    }


}