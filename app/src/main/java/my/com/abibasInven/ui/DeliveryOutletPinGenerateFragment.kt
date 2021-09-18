package my.com.abibasInven.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.logindemo.util.informationDialog
import my.com.abibasInven.R
import my.com.abibasInven.data.Outlet
import my.com.abibasInven.data.OutletViewModel
import my.com.abibasInven.databinding.FragmentDeliveryOutletBinding
import my.com.abibasInven.databinding.FragmentDeliveryOutletPinGenerateBinding


class DeliveryOutletPinGenerateFragment : Fragment() {

    private lateinit var binding: FragmentDeliveryOutletPinGenerateBinding
    private val nav by lazy {findNavController()}
    private val vm: OutletViewModel by activityViewModels()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        binding = FragmentDeliveryOutletPinGenerateBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment

        with(binding){
            textView43.isVisible = false
            lblOuletGeneratedPin.isVisible = false
            btnOutletGeneratePin.setOnClickListener { generatePin() }
        }

        return binding.root
    }

    private fun generatePin() {
        with(binding){
            textView43.isVisible = true
            lblOuletGeneratedPin.isVisible = true
        }
        val pin = (1..100000).random()

        binding.lblOuletGeneratedPin.text = pin.toString()

        val foundOutletData = vm.get("OL1")

        if(foundOutletData!=null){
            val updateOutlet = Outlet(
                ID = "OL1",
                pin = pin.toString(),
            )
            vm.set(updateOutlet)
        }
        else{
            informationDialog("outletData is null")
        }





    }


}