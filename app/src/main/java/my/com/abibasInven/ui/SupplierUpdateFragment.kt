package my.com.abibasInven.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.logindemo.util.errorDialog
import com.example.logindemo.util.snackbar
import com.example.logindemo.util.toBitmap
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomnavigation.BottomNavigationView
import my.com.abibasInven.R
import my.com.abibasInven.data.Supplier
import my.com.abibasInven.data.SupplierViewModel
import my.com.abibasInven.data.User
import my.com.abibasInven.databinding.FragmentSupplierUpdateBinding

class SupplierUpdateFragment : Fragment(), OnMapReadyCallback {

    //Map variables
    private lateinit var mMap: GoogleMap
    private var fusedLocationClient: FusedLocationProviderClient? = null
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback

    // Permissions
    private val permissions = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    private lateinit var binding: FragmentSupplierUpdateBinding
    private val nav by lazy { findNavController() }
    private val vm: SupplierViewModel by activityViewModels()

    private val suppId by lazy { requireArguments().getString("suppId", null) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Disable bottom navigation menu
        val bottomNav : BottomNavigationView = requireActivity().findViewById(R.id.bottomNavigationView)
        bottomNav.visibility = View.GONE

        binding = FragmentSupplierUpdateBinding.inflate(inflater, container, false)

        reset()
        binding.btnReset.setOnClickListener { reset() }
        binding.btnUpdateSup.setOnClickListener { update() }

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
            //Load map with supplier location from firebase
            binding.map.getMapAsync(this)
            binding.btnLocation.setOnClickListener { getCurrentLocation() }
        } else {
            //when permission is not granted
            //Request permission
            ActivityCompat.requestPermissions(requireActivity(), permissions, 100)
            nav.navigateUp()
        }

        binding.edtSupLoc.isEnabled = false
        binding.map.onCreate(savedInstanceState)
        binding.map.onResume()

        return binding.root
    }

    private fun reset() {
        val supp = vm.get(suppId)
        if (supp == null) {
            nav.navigateUp()
            return
        }

        val lat = supp.latitude
        val lon = supp.longitude
        val location = "$lat,$lon"

        with(binding) {
            edtSupName.setText(supp.name)
            edtSupPhone.setText(supp.phoneNo)
            edtSupEmail.setText(supp.email)
            edtSupLoc.setText(location)
        }
    }

    private fun update() {
        if (binding.edtSupLoc.text.toString().isNotEmpty()) {

            // Get and split geoPoint into latitude & longitude
            val geoPoint = binding.edtSupLoc.text.toString()
            val splitPoint = geoPoint.split(",")
            val latitude = splitPoint[0].toDouble()
            val longitude = splitPoint[1].toDouble()

            val updateSup = Supplier(
                ID = suppId,
                name = binding.edtSupName.text.toString().trim(),
                phoneNo = binding.edtSupPhone.text.toString().trim(),
                email = binding.edtSupEmail.text.toString().trim(),
                latitude = latitude,
                longitude = longitude,
            )

            val e = vm.validate(updateSup, false)
            if (e != "") {
                errorDialog(e)
                return
            } else {
                vm.set(updateSup)
                nav.navigateUp()
            }
        } else {
            errorDialog("- Location cannot be empty")
        }
    }

    //Get current location
    @SuppressLint("MissingPermission")
    private fun getCurrentLocation() {
        val locationManager: LocationManager =
            requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager

        //Check condition
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
            locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        ) {
            fusedLocationClient?.lastLocation?.addOnCompleteListener {

                if (it.result != null) {
                    val location: Location = it.result
                    val geopoint =
                        location.latitude.toString() + "," + location.longitude.toString()

                    snackbar("Location received")
                    binding.edtSupLoc.isEnabled = true
                    binding.edtSupLoc.setText(geopoint)
                    binding.edtSupLoc.isEnabled = false

                } else {
                    //Initialize location request
                    locationRequest = LocationRequest()
                        .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                        .setInterval(10000)
                        .setFastestInterval(1000)
                        .setNumUpdates(1)

                    //Initialize location call back
                    locationCallback = object : LocationCallback() {
                        override fun onLocationResult(locationResult: LocationResult?) {
                            locationResult ?: return

                            // get latest location
                            val lastLocation = locationResult.lastLocation

                            // use your location object
                            // get latitude , longitude and other info from this
                            val testGeoPoint =
                                lastLocation.latitude.toString() + "," + lastLocation.longitude.toString()

                            snackbar(testGeoPoint)
                            binding.edtSupLoc.isEnabled = true
                            binding.edtSupLoc.setText(testGeoPoint)
                            binding.edtSupLoc.isEnabled = false
                        }
                    }
                    //Request location update
                    fusedLocationClient!!.requestLocationUpdates(
                        locationRequest,
                        locationCallback,
                        Looper.myLooper()
                    )
                }
            }
        } else {
            //when location service not enable
            //Open location setting
            startActivity(
                Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            )
        }
    }

    // Display map output
    override fun onMapReady(googleMap: GoogleMap) {
        val geoPoint = binding.edtSupLoc.text.toString()

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