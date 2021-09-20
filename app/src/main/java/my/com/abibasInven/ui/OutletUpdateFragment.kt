package my.com.abibasInven.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
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
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.logindemo.util.cropToBlob
import com.example.logindemo.util.errorDialog
import com.example.logindemo.util.snackbar
import com.example.logindemo.util.toBitmap
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.theartofdev.edmodo.cropper.CropImage
import my.com.abibasInven.R
import my.com.abibasInven.data.Outlet
import my.com.abibasInven.data.OutletViewModel
import my.com.abibasInven.databinding.FragmentOutletUpdateBinding

class OutletUpdateFragment : Fragment(), OnMapReadyCallback {

    //Map variables
    private lateinit var mMap: GoogleMap
    private var fusedLocationClient: FusedLocationProviderClient? = null
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback

    // Map Permissions
    private val permissions = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    private lateinit var binding: FragmentOutletUpdateBinding
    private val nav by lazy { findNavController() }
    private val vm: OutletViewModel by activityViewModels()

    private val outletId by lazy { requireArguments().getString("outletId", null) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentOutletUpdateBinding.inflate(inflater, container, false)

        reset()
        binding.btnOutletUpdateReset.setOnClickListener { reset() }
        binding.btnOutletUpdateSubmit.setOnClickListener { updateOutlet() }

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
            binding.outletUpdateMap.getMapAsync(this)
            binding.btnOutletUpdateLocation.setOnClickListener { getCurrentLocation() }
        } else {
            //when permission is not granted
            //Request permission
            ActivityCompat.requestPermissions(requireActivity(), permissions, 100)
            nav.navigateUp()
        }


        binding.imgUpdateOutletPhoto.setOnClickListener {
            try {
                CropImage.activity()
                    .start(requireContext(), this)
            } catch (e: ActivityNotFoundException) {

            }
        }

        binding.edtOutletUpdateLoc.isEnabled = false
        binding.outletUpdateMap.onCreate(savedInstanceState)
        binding.outletUpdateMap.onResume()

        return binding.root
    }

    //Update the Outlet ImageView
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (result != null) binding.imgUpdateOutletPhoto.setImageURI(result.uri)
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
                    binding.edtOutletUpdateLoc.isEnabled = true
                    binding.edtOutletUpdateLoc.setText(geopoint)
                    binding.edtOutletUpdateLoc.isEnabled = false

                    //binding.outletUpdateMap.getMapAsync(this)
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
                            binding.edtOutletUpdateLoc.isEnabled = true
                            binding.edtOutletUpdateLoc.setText(testGeoPoint)
                            binding.edtOutletUpdateLoc.isEnabled = false
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

    private fun updateOutlet() {
        if (binding.edtOutletUpdateLoc.text.toString().isNotEmpty()) {

            // Get and split geoPoint into latitude & longitude
            val geoPoint = binding.edtOutletUpdateLoc.text.toString()
            val splitPoint = geoPoint.split(",")
            val latitude = splitPoint[0].toDouble()
            val longitude = splitPoint[1].toDouble()

            // Auto increment ID
            val chkID = vm.validID()

            val o = Outlet(
                ID = outletId,
                name = binding.edtOutletUpdateName.text.toString().trim(),
                latitude = latitude,
                longitude = longitude,
                photo = binding.imgUpdateOutletPhoto.cropToBlob(700, 600),
            )

            val err = vm.validate(o, false)
            if (err != "") {
                errorDialog(err)
                return
            } else {
                vm.set(o)
                nav.navigate(R.id.action_global_outletListFragment)
            }
        } else {
            errorDialog("- Location cannot be empty")
        }
    }

    private fun reset() {
        val outlet = vm.get(outletId)
        if (outlet == null) {
            nav.navigateUp()
            return
        }

        val lat = outlet.latitude
        val lon = outlet.longitude
        val location = "$lat,$lon"

        with(binding) {
            edtOutletUpdateName.setText(outlet.name)
            imgUpdateOutletPhoto.setImageBitmap(outlet.photo.toBitmap())
            edtOutletUpdateLoc.setText(location)
        }
    }

    // Display map output
    override fun onMapReady(googleMap: GoogleMap) {
        val geoPoint = binding.edtOutletUpdateLoc.text.toString()

        // If notEmpty then load Google Map
        if (geoPoint.isNotEmpty()) {
            binding.outletUpdateMap.isVisible = true

            binding.outletUpdateMap.let {
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