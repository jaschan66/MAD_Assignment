package my.com.abibasInven.ui

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.logindemo.util.informationDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.Blob
import my.com.abibasInven.R
import my.com.abibasInven.data.*
import my.com.abibasInven.databinding.FragmentDeliveryOutletBinding
import my.com.abibasInven.databinding.FragmentDeliveryOutletPinGenerateBinding


class DeliveryOutletPinGenerateFragment : Fragment() {

    private lateinit var binding: FragmentDeliveryOutletPinGenerateBinding
    private val nav by lazy {findNavController()}
    private val vm: OutletViewModel by activityViewModels()
    private val outletID by lazy { requireArguments().getString("OutletID", null) }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        binding = FragmentDeliveryOutletPinGenerateBinding.inflate(inflater, container, false)

        // Inflate the layout for this fragment

        with(binding){
            textView43.isVisible = false
            lblOuletGeneratedPin.isVisible = false
            btnOutletGeneratePin.setOnClickListener { generatePin() }
        }

        binding.btnOutletLogout.setOnClickListener { logout() }


        return binding.root
    }

    private fun generatePin() {
        with(binding){
            textView43.isVisible = true
            lblOuletGeneratedPin.isVisible = true
        }
        val pin = (1..100000).random()

        binding.lblOuletGeneratedPin.text = pin.toString()

        val foundOutletData = vm.get(outletID)

        if(foundOutletData!=null){
            val updateOutlet = Outlet(
                ID = outletID,
                latitude = foundOutletData.latitude,
                longitude = foundOutletData.longitude,
                name = foundOutletData.name,
                photo = foundOutletData.photo,
                pin = pin.toString(),
            )
            vm.set(updateOutlet)
        }
        else{
            informationDialog("outletData is null")
        }



    }

    private fun logout() {
        emailLogin = ""
        passwordLogin = ""
        img = Blob.fromBytes(ByteArray(0))
        val sharedPref = activity?.getSharedPreferences("checkBo", Context.MODE_PRIVATE)
        val editor : SharedPreferences.Editor = sharedPref!!.edit()
        editor.putString("remember","false")
        editor.apply()
        val sharedPref1 = activity?.getSharedPreferences("email", Context.MODE_PRIVATE)
        val editor1 : SharedPreferences.Editor = sharedPref1!!.edit()
        editor1.putString("emailLoginRmb","")
        editor1.apply()
        FirebaseAuth.getInstance().signOut()
        nav.navigate(R.id.action_deliveryOutletPinGenerateFragment_to_loginFragment)
    }


}