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
import com.example.logindemo.util.toBitmap
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import my.com.abibasInven.data.Outlet
import my.com.abibasInven.data.OutletViewModel
import my.com.abibasInven.databinding.FragmentOutletDetailsBinding

class OutletDetailsFragment : Fragment(), OnMapReadyCallback {

    //Map variables
    private lateinit var mMap: GoogleMap
    private var fusedLocationClient: FusedLocationProviderClient? = null
    private lateinit var location: String

    // Permissions
    private val permissions = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    private lateinit var binding: FragmentOutletDetailsBinding
    private val nav by lazy { findNavController() }
    private val vm: OutletViewModel by activityViewModels()

    private val outletId by lazy { requireArguments().getString("outletId", null) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentOutletDetailsBinding.inflate(inflater, container, false)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {

            val outlet = vm.get(outletId)
            if (outlet == null) {
                nav.navigateUp()
            } else {
                load(outlet)
            }

            //Load map with supplier location from firebase
            binding.mapView.getMapAsync(this)

        } else {
            //when permission is not granted
            //Request permission
            ActivityCompat.requestPermissions(requireActivity(), permissions, 100)
            nav.navigateUp()
        }

        binding.mapView.onCreate(savedInstanceState)
        binding.mapView.onResume()

        return binding.root
    }

    private fun load(o: Outlet) {
        val lat = o.latitude
        val lon = o.longitude
        location = "$lat,$lon"

        with(binding) {
            tvOutletName.text = o.name
            imgOutletPhoto.setImageBitmap(o.photo.toBitmap())
        }
    }

    // Display map output
    override fun onMapReady(googleMap: GoogleMap) {
        val geoPoint = location

        // If notEmpty then load Google Map
        if (geoPoint.isNotEmpty()) {

            binding.mapView.let {
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