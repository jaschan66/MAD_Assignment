package my.com.abibasInven.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import my.com.abibasInven.data.Supplier
import my.com.abibasInven.data.SupplierViewModel
import my.com.abibasInven.databinding.FragmentSupplierDetailsBinding
import kotlin.properties.Delegates

class SupplierDetailsFragment : Fragment(), OnMapReadyCallback {

    //Map variables
    private lateinit var mMap: GoogleMap
    private var fusedLocationClient: FusedLocationProviderClient? = null
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback
    private lateinit var location: String

    // Permissions
    private val permissions = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    private lateinit var binding: FragmentSupplierDetailsBinding
    private val nav by lazy { findNavController() }
    private val vm: SupplierViewModel by activityViewModels()

    private val suppId by lazy { requireArguments().getString("suppId", null) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentSupplierDetailsBinding.inflate(inflater, container, false)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        binding.btnDial.setOnClickListener { openPhone() }

        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {

            val supp = vm.get(suppId)
            if (supp == null) {
                nav.navigateUp()
            } else {
                load(supp)
            }
            //Load map with supplier location from firebase
            binding.map.getMapAsync(this)

        } else {
            //when permission is not granted
            //Request permission
            ActivityCompat.requestPermissions(requireActivity(), permissions, 100)
            nav.navigateUp()
        }

        binding.map.onCreate(savedInstanceState)
        binding.map.onResume()

        return binding.root
    }

    private fun openPhone() {
        val suppPhoneNo = binding.tvPhoneNo.text.toString()
        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$suppPhoneNo"))
        startActivity(intent)
    }

    private fun load(s: Supplier) {
        val lat = s.latitude
        val lon = s.longitude
        location = "$lat,$lon"

        with(binding) {
            tvSuppName.text = s.name
            tvPhoneNo.text = s.phoneNo
            tvEmail.text = s.email
        }
    }

    // Display map output
    override fun onMapReady(googleMap: GoogleMap) {
        val geoPoint = location

        // If notEmpty then load Google Map
        if (geoPoint.isNotEmpty()) {

            binding.map.let {
                mMap = googleMap
                val splitPoint = geoPoint.split(",")
                val latitude = splitPoint[0].toDouble()
                val longitude = splitPoint[1].toDouble()

                // Add a marker in Supplier Location and move the camera
                val supplierLocation = LatLng(latitude, longitude)
                mMap.addMarker(
                    MarkerOptions().position(supplierLocation).title("Supplier location")
                )
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(supplierLocation, 15f))
            }
        }
    }
}