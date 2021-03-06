package my.com.abibasInven.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.logindemo.util.errorDialog
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.zxing.integration.android.IntentIntegrator
import my.com.abibasInven.R
import my.com.abibasInven.data.LocationViewModel
import my.com.abibasInven.data.ProductViewModel
import my.com.abibasInven.data.StockInViewModel
import my.com.abibasInven.databinding.FragmentLocationListingBinding
import my.com.abibasInven.databinding.FragmentStockInBinding
import my.com.abibasInven.databinding.FragmentStockInSelectMethodBinding


class StockInSelectMethodFragment : Fragment() {

    private lateinit var binding: FragmentStockInSelectMethodBinding
    private val nav by lazy { findNavController() }
    private val productvm : ProductViewModel by activityViewModels()
    private val stockinvm : StockInViewModel by activityViewModels()
    private val locationvm : LocationViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        // Enable bottom navigation menu
        val bottomNav : BottomNavigationView = requireActivity().findViewById(R.id.bottomNavigationView)
        bottomNav.visibility = View.GONE

        // Inflate the layout for this fragment
        binding = FragmentStockInSelectMethodBinding.inflate(inflater, container, false)
        productvm.getAll()
        stockinvm.getAll()
        locationvm.getAll()

        with(binding){

            btnStockInChoiceManual.setOnClickListener { nav.navigate(R.id.stockInFragment,bundleOf("productID" to "N/A") )}

            btnStockInChoiceSelectClose.setOnClickListener { nav.navigateUp() }
        }

        binding.btnStockInChoiceScanQR.setOnClickListener {
            val scanner = IntentIntegrator.forSupportFragment(this)
            scanner.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
            scanner.initiateScan()
        }
        return binding.root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(resultCode == Activity.RESULT_OK){
            val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
            if (result != null) {
                if (result.contents == null) {
                    errorDialog("Result not found")
                } else {
                    nav.navigate(R.id.stockInFragment, bundleOf("productID" to result.contents))//Toast.makeText(this, "Scanned: " + result.contents, Toast.LENGTH_LONG).show()
                }
            } else {
                super.onActivityResult(requestCode, resultCode, data)
            }
        }
    }


}