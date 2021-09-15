package my.com.abibasInven.ui

import android.R.attr
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.logindemo.util.errorDialog
import my.com.abibasInven.R
import my.com.abibasInven.data.Location
import my.com.abibasInven.data.LocationViewModel

import my.com.abibasInven.databinding.FragmentLocationAddingBinding

import my.com.abibasInven.util.LocationAddAdapter
import android.R.attr.button

import android.graphics.drawable.ColorDrawable
import android.R.attr.button
import android.R.attr.button

import android.graphics.drawable.Drawable
import android.R.attr.button
import android.widget.ArrayAdapter
import com.example.logindemo.util.informationDialog
import my.com.abibasInven.data.SpinnerViewModel


class LocationAddingFragment : Fragment() {


    private lateinit var binding: FragmentLocationAddingBinding
    private val nav by lazy {findNavController()}
    private val vm: LocationViewModel by activityViewModels()
    private val rackID by lazy { requireArguments().getString("rackID","N/A") }


    private var currentCompartmentID : String = ""
    private var successAddingCount : Int = 0


    //spnCategory
    private val vmSpn : SpinnerViewModel by activityViewModels()



    private lateinit var adapter: LocationAddAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = FragmentLocationAddingBinding.inflate(inflater, container, false)

        // TODO





//spnCategory
        val spnCategory = vmSpn.getCategory()
        val spnArray2 = arrayListOf<String>()


        val adp3 = ArrayAdapter<String>(requireContext(),android.R.layout.simple_spinner_item)
        adp3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spnCategory.adapter = adp3


        //spnCategory
        spnCategory.observe(viewLifecycleOwner) { list ->
            //list.groupBy { it.ID}
            val num = list.size
            val categorySize = vmSpn.calCategorySize()
            if (list.size > spnArray2.size) {
                for (i in 0..categorySize - 1) {
                    adp3.add(list[i].name)//change here to get value
                    spnArray2.add(list[i].name) //change here to get value
                }
            } else if (num <= spnArray2.size ){
                spnArray2.clear()
                adp3.clear()
                for (i in 0..categorySize - 1) {
                    adp3.add(list[i].name)
                    spnArray2.add(list[i].name)
                }
            }
        }


        reset()

        with(binding){
            lblTitle.text = "Rack " + rackID
            btnCompartment1.text = rackID + "1"
            btnCompartment2.text = rackID + "2"
            btnCompartment3.text = rackID + "3"
            btnCompartment4.text = rackID + "4"
            btnCompartment5.text = rackID + "5"
            btnCompartment6.text = rackID + "6"

            btnCompartment1.setOnClickListener { displayCompartmentDetails(btnCompartment1.text.toString()) }
            btnCompartment2.setOnClickListener { displayCompartmentDetails(btnCompartment2.text.toString()) }
            btnCompartment3.setOnClickListener { displayCompartmentDetails(btnCompartment3.text.toString()) }
            btnCompartment4.setOnClickListener { displayCompartmentDetails(btnCompartment4.text.toString()) }
            btnCompartment5.setOnClickListener { displayCompartmentDetails(btnCompartment5.text.toString()) }
            btnCompartment6.setOnClickListener { displayCompartmentDetails(btnCompartment6.text.toString()) }

            btnUpdateCompartment.setOnClickListener { updateCompartmentDetails(currentCompartmentID) }
        }

        //check whether it's being added
//            val l = vm.getAll()
//            var j = 0
//            val locationFound = vm.getLocationSize()
//            for (i in 1..locationFound){
//
//                if(l.value?.get(j)?.maxCapacity!! >0)
//                {
//                    when(l.value?.get(j)?.ID){
//                        binding.btnCompartment1.text -> binding.btnCompartment1.setBackgroundColor(Color.BLUE)
//                        binding.btnCompartment2.text -> binding.btnCompartment2.setBackgroundColor(Color.BLUE)
//                        binding.btnCompartment3.text -> binding.btnCompartment3.setBackgroundColor(Color.BLUE)
//                        binding.btnCompartment4.text -> binding.btnCompartment4.setBackgroundColor(Color.BLUE)
//                        binding.btnCompartment5.text -> binding.btnCompartment5.setBackgroundColor(Color.BLUE)
//                        binding.btnCompartment6.text -> binding.btnCompartment6.setBackgroundColor(Color.BLUE)
//                    }
//
//
//
//                }
//                j++
//            }


//        val buttonBackground: Drawable = binding.btnCompartment1.getBackground()
//        val buttonColor = binding.btnCompartment1.getBackground() as ColorDrawable
//         binding.textView23.text = buttonBackground.toString()
//        when(Color.BLUE){
//            (binding.btnCompartment1.background as ColorDrawable).color
//        }

//        binding.textView23.text=addCompartmentCount.toString()
//       if(addCompartmentCount>5){
//           nav.navigate(R.id.locationListingFragment)
//       }



        return binding.root
    }

    private fun updateCompartmentDetails(btnCompartmentID : String) {


        //TODO update
        if(currentCompartmentID!=null&&binding.edtMaxCapacity.text.toString().toInt()>0) {
            //val l =vm.get(currentCompartmentID)
            val uLocation = Location(
                ID = currentCompartmentID,
                categoryID = binding.spnCategory.selectedItem.toString(),
                occupiedCapacity = 0,
                maxCapacity = binding.edtMaxCapacity.text.toString().toInt()
                //categoryID = binding.spnCategory.selectedItem.toString()
            )
            vm.set(uLocation)
            successAddingCount++
            //invisible
            componentDetailInvi()

            if(successAddingCount==6){
                nav.navigate(R.id.locationListingFragment)
            }



            

            when(currentCompartmentID){
                binding.btnCompartment1.text -> {
                    binding.btnCompartment1.setBackgroundColor(Color.BLUE)
                    binding.btnCompartment1.isEnabled = false
                    binding.btnCompartment1.setTextColor(Color.WHITE)
                }
                binding.btnCompartment2.text -> {
                    binding.btnCompartment2.setBackgroundColor(Color.BLUE)
                    binding.btnCompartment2.isEnabled = false
                    binding.btnCompartment2.setTextColor(Color.WHITE)
                }
                binding.btnCompartment3.text -> {
                    binding.btnCompartment3.setBackgroundColor(Color.BLUE)
                    binding.btnCompartment3.isEnabled = false
                    binding.btnCompartment3.setTextColor(Color.WHITE)
                }
                binding.btnCompartment4.text -> {
                    binding.btnCompartment4.setBackgroundColor(Color.BLUE)
                    binding.btnCompartment4.isEnabled = false
                    binding.btnCompartment4.setTextColor(Color.WHITE)
                }
                binding.btnCompartment5.text -> {
                    binding.btnCompartment5.setBackgroundColor(Color.BLUE)
                    binding.btnCompartment5.isEnabled = false
                    binding.btnCompartment5.setTextColor(Color.WHITE)
                }
                binding.btnCompartment6.text -> {
                    binding.btnCompartment6.setBackgroundColor(Color.BLUE)
                    binding.btnCompartment6.isEnabled = false
                    binding.btnCompartment6.setTextColor(Color.WHITE)
                }
            }


        }
        else{
            val err = "Max capacity cannot be 0"

            errorDialog(err)
        }

    }

    fun displayCompartmentDetails( btnCompartmentID : String) {
        val l = vm.getRackByRackType(btnCompartmentID)

        if (l != null) {

            //visible
            componentDetailVisible()

            binding.lblCompartmentDetails.text = l.ID + " details"

            binding.edtMaxCapacity.setText(l.maxCapacity.toString())
            currentCompartmentID = l.ID

            //binding.textView23.text = binding.edtMaxCapacity.text
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

            //invisible
            componentDetailInvi()

        }
    }

    private fun componentDetailInvi(){
        with(binding){
            lblCompartmentDetails.isVisible = false
            textView23.isVisible=false
            spnCategory.isVisible = false
            textView24.isVisible=false
            edtMaxCapacity.isVisible = false
            btnUpdateCompartment.isVisible = false
        }
    }
    private fun componentDetailVisible(){
        with(binding){
            lblCompartmentDetails.isVisible = true
            textView23.isVisible=true
            spnCategory.isVisible = true
            textView24.isVisible=true
            edtMaxCapacity.isVisible = true
            btnUpdateCompartment.isVisible = true
        }
    }



}