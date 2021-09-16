package my.com.abibasInven.ui

import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.logindemo.util.errorDialog
import com.example.logindemo.util.generateQRCode
import com.example.logindemo.util.toBitmap
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import my.com.abibasInven.R
import my.com.abibasInven.data.ProductViewModel
import my.com.abibasInven.databinding.FragmentHomeBinding
import my.com.abibasInven.databinding.FragmentProductDetailBinding


class ProductDetailFragment : Fragment() {

    private lateinit var binding: FragmentProductDetailBinding
    private val nav by lazy {findNavController()}
    private val vmPro : ProductViewModel by activityViewModels()
    private val ID by lazy { requireArguments().getString("ID", null) }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = FragmentProductDetailBinding.inflate(inflater, container, false)

        // TODO

        val bottomNav : BottomNavigationView = requireActivity().findViewById(R.id.bottomNavigationView)
        bottomNav.visibility = View.GONE

        val p = vmPro.get(ID)

        if (p != null) {
            val bitmap = generateQRCode(p.ID)
            binding.imgProductQrCode.setImageBitmap(bitmap)
            binding.imgProductDetail.setImageBitmap(p.photo.toBitmap())
            binding.lblDetailProductName.text = p.name
            binding.lblDetailProductQty.text = p.qty.toString()
            binding.lblDetailProductQtyThreshold.text = p.qtyThreshold.toString()
            binding.lblDetailProductCategory.text = p.categoryID
            binding.lblDetailProductLocation.text = p.locationID
            binding.lblDetailProductSupplier.text = p.supplierID
        }
        else{
            errorDialog("No product information found")
            nav.navigateUp()
        }
        binding.btnDetailProductClose.setOnClickListener {
            nav.navigateUp()
        }

        return binding.root
    }


}