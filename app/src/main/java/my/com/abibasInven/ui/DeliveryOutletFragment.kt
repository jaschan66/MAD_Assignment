package my.com.abibasInven.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.logindemo.util.errorDialog
import com.example.logindemo.util.informationDialog
import com.example.logindemo.util.toBitmap
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import my.com.abibasInven.R
import my.com.abibasInven.data.*
import my.com.abibasInven.databinding.FragmentDeliveryItemListingBinding
import my.com.abibasInven.databinding.FragmentDeliveryOutletBinding
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class DeliveryOutletFragment : Fragment(), OnMapReadyCallback {

    private lateinit var binding: FragmentDeliveryOutletBinding
    private val nav by lazy {findNavController()}
    private val vm: OutletViewModel by activityViewModels()
    private val deliveryvm: DeliveryViewModel by activityViewModels()
    private val stockOutvm: StockOutViewModel by activityViewModels()

    //map
    private lateinit var location: String
    private lateinit var mMap: GoogleMap

    var outletPin  : String = ""



    private val currentOutletID by lazy { requireArguments().getString("currentOutletID","N/A") }
    private val currentDeliveryID by lazy { requireArguments().getString("currentDeliveryID","N/A") }


    // Permissions
    private val permissions = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDeliveryOutletBinding.inflate(inflater, container, false)



        if(currentOutletID!=null){
            binding.lblCurrentOutletDetails.text = currentOutletID + " details"

            //TODO unable to find specific data return null
            val foundOutLetData = vm.get(currentOutletID)

            if(foundOutLetData!=null){
                binding.lblOutletName.text = foundOutLetData.name
//                binding.imgOutletPhoto.setImageBitmap(foundOutLetData.photo.toBitmap())

                //map
                val lat = foundOutLetData.latitude
                val lon = foundOutLetData.longitude
                location = "$lat,$lon"


                outletPin = foundOutLetData.pin

                //map

                if (ContextCompat.checkSelfPermission(
                        requireContext(),
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(
                        requireContext(),
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED
                ) {



                    //Load map with supplier location from firebase
                    binding.mapDeliveryOutlet.getMapAsync(this)

                } else {
                    //when permission is not granted
                    //Request permission
                    ActivityCompat.requestPermissions(requireActivity(), permissions, 100)
                    nav.navigate(R.id.action_deliveryOutletFragment_to_deliveryItemListingFragment)
                }


            }

        }
        else{
            nav.navigate(R.id.action_deliveryOutletFragment_to_deliveryItemListingFragment)
        }
        binding.btnConfirmDeliveryPin.setOnClickListener { checkDeliveryPin(binding.edtDeliveryOutletPin.text.toString()) }

        //map
        binding.mapDeliveryOutlet.onCreate(savedInstanceState)
        binding.mapDeliveryOutlet.onResume()
        // Inflate the layout for this fragment
        binding.btnCloseDeliveryOutlet.setOnClickListener { nav.navigate(R.id.action_deliveryOutletFragment_to_deliveryItemListingFragment) }
        return binding.root
    }

    private fun checkDeliveryPin(enteredPin : String) {

        if(outletPin==""){
            informationDialog("pin has not been generated yet")
        }
        else{
            if(enteredPin==outletPin){

                val d = Delivery(
                    ID =  currentDeliveryID,
                    outletID = currentOutletID,
                    deliveryStatus = "completed"
                )
                deliveryvm.set(d)

                // id generator for stockOut
                val id = "SO" + (stockOutvm.calStockOutSize() + 1).toString()
                val stockOutID = stockOutvm.validID(id)

                val currentDateTime = LocalDateTime.now()
                val dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")

//                val newStockOut = StockOut(
//                    ID = stockOutID,
//                    dateTime = dtf.format(currentDateTime).toString(),
//                    deliveryID = currentDeliveryID,
//                )
//                stockOutvm.set(newStockOut)

                informationDialog("delivery ( $currentDeliveryID ) task has completed")

            }
            else{
                errorDialog("invalid pin")
            }
        }

    }

    // Display map output
    override fun onMapReady(googleMap: GoogleMap) {
        val geoPoint = location

        // If notEmpty then load Google Map
        if (geoPoint.isNotEmpty()) {

            binding.mapDeliveryOutlet.let {
                mMap = googleMap
                val splitPoint = geoPoint.split(",")
                val latitude = splitPoint[0].toDouble()
                val longitude = splitPoint[1].toDouble()

                // Add a marker in Supplier Location and move the camera
                val supplierLocation = LatLng(latitude, longitude)
                mMap.addMarker(
                    MarkerOptions().position(supplierLocation).title("Outlet location")
                )
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(supplierLocation, 15f))
            }
        }
    }


}