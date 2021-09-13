package my.com.abibasInven.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import my.com.abibasInven.R
import my.com.abibasInven.data.Location
import my.com.abibasInven.data.LocationViewModel

import my.com.abibasInven.databinding.FragmentLocationAddingBinding

import my.com.abibasInven.util.LocationAddAdapter



class LocationAddingFragment : Fragment() {


    private lateinit var binding: FragmentLocationAddingBinding
    private val nav by lazy {findNavController()}
    private val vm: LocationViewModel by activityViewModels()
    private val rackID by lazy { requireArguments().getString("rackID","N/A") }

    private lateinit var adapter: LocationAddAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = FragmentLocationAddingBinding.inflate(inflater, container, false)

        // TODO


        val l = vm.getRackByRackType(rackID)

        reset()
        with(binding){
            lblTitle.text = "Rack " + rackID
            btnCompartment1.text = rackID + "1"
            btnCompartment2.text = rackID + "2"
            btnCompartment3.text = rackID + "3"
            btnCompartment4.text = rackID + "4"
            btnCompartment5.text = rackID + "5"
            btnCompartment6.text = rackID + "6"

            btnCompartment1.setOnClickListener { displayCompartmentDetails(btnCompartment1.text.toString() ) }
            btnCompartment2.setOnClickListener { displayCompartmentDetails(btnCompartment2.text.toString()) }
            btnCompartment3.setOnClickListener { displayCompartmentDetails(btnCompartment3.text.toString()) }
            btnCompartment4.setOnClickListener { displayCompartmentDetails(btnCompartment4.text.toString()) }
            btnCompartment5.setOnClickListener { displayCompartmentDetails(btnCompartment5.text.toString()) }
            btnCompartment6.setOnClickListener { displayCompartmentDetails(btnCompartment6.text.toString()) }
        }

        return binding.root
    }

    fun displayCompartmentDetails( btnCompartmentID : String) {
        val l = vm.getRackByRackType(btnCompartmentID)

        if (l != null) {
            binding.lblCompartmentDetails.text = l.ID + " details"

            binding.edtMaxCapacity.setText(l.maxCapacity.toString())
        }
    }

    private fun reset() {
        with(binding){
            lblTitle.text = "Rack "
            btnCompartment1.text = ""
            btnCompartment2.text = ""
            btnCompartment3.text = ""
            btnCompartment4.text = ""
            btnCompartment5.text = ""
            btnCompartment6.text = ""
            lblCompartmentDetails.text = "details"
            edtMaxCapacity.text.clear()

        }
    }



}