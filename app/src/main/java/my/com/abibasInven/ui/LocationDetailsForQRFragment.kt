package my.com.abibasInven.ui

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.logindemo.util.informationDialog
import com.example.logindemo.util.toBitmap
import my.com.abibasInven.R
import my.com.abibasInven.data.LocationViewModel
import my.com.abibasInven.data.ProductViewModel
import my.com.abibasInven.databinding.FragmentLocationDetailsForQRBinding
import my.com.abibasInven.databinding.FragmentLocationListingBinding


class LocationDetailsForQRFragment : Fragment() {

    private lateinit var binding: FragmentLocationDetailsForQRBinding
    private val nav by lazy { findNavController() }
    private val vm: ProductViewModel by activityViewModels()
    private val locationvm: LocationViewModel by activityViewModels()

    private val productID by lazy { requireArguments().getString("productID","N/A") }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLocationDetailsForQRBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment

        if(productID=="N/A"){
            nav.navigateUp()
            informationDialog("QR code scanned data is not usable")
        }

        vm.getAll()
        locationvm.getAll()

        binding.lblTestingProductID.text = productID



        val foundProductData = vm.get(productID)


        if(foundProductData!=null){
            val foundLocationData = locationvm.get(foundProductData.locationID)
            if(foundLocationData!=null){
                with(binding){
                    lblTitle3.text = "Rack "+ foundLocationData.rackType
                    btnLocationDetailQRCompartment1.text = foundLocationData.rackType+"1"
                    btnLocationDetailQRCompartment2.text = foundLocationData.rackType+"2"
                    btnLocationDetailQRCompartment3.text = foundLocationData.rackType+"3"
                    btnLocationDetailQRCompartment4.text = foundLocationData.rackType+"4"
                    btnLocationDetailQRCompartment5.text = foundLocationData.rackType+"5"
                    btnLocationDetailQRCompartment6.text = foundLocationData.rackType+"6"

                    lblLocationScanQRProductDetail.text = foundProductData.ID + "details"
                    lblLocationScanQRProductName.text = foundProductData.name
                    imgLocationScanQRProductPhoto.setImageBitmap(foundProductData.photo.toBitmap())
                    lblLocationScanQRProductQuantity.text = foundProductData.qty.toString()
                }
                when(foundProductData.locationID){
                    binding.btnLocationDetailQRCompartment1.text -> binding.btnLocationDetailQRCompartment1.setBackgroundColor(Color.rgb(247, 177, 106))
                    binding.btnLocationDetailQRCompartment2.text -> binding.btnLocationDetailQRCompartment2.setBackgroundColor(Color.rgb(247, 177, 106))
                    binding.btnLocationDetailQRCompartment3.text -> binding.btnLocationDetailQRCompartment3.setBackgroundColor(Color.rgb(247, 177, 106))
                    binding.btnLocationDetailQRCompartment4.text -> binding.btnLocationDetailQRCompartment4.setBackgroundColor(Color.rgb(247, 177, 106))
                    binding.btnLocationDetailQRCompartment5.text -> binding.btnLocationDetailQRCompartment5.setBackgroundColor(Color.rgb(247, 177, 106))
                    binding.btnLocationDetailQRCompartment6.text -> binding.btnLocationDetailQRCompartment6.setBackgroundColor(Color.rgb(247, 177, 106))
                }
            }

        }

        //binding.textView27.text = productID

        binding.btnLocationScanQRClose.setOnClickListener { nav.navigate(R.id.action_locationDetailsForQRFragment_to_locationListingFragment) }



        return binding.root
    }

}