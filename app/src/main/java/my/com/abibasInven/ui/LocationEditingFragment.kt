package my.com.abibasInven.ui

import android.app.AlertDialog
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.logindemo.util.errorDialog
import com.example.logindemo.util.informationDialog
import com.example.logindemo.util.snackbar
import my.com.abibasInven.R
import my.com.abibasInven.data.Location
import my.com.abibasInven.data.LocationViewModel
import my.com.abibasInven.data.SpinnerViewModel
import my.com.abibasInven.databinding.FragmentLocationAddingBinding
import my.com.abibasInven.databinding.FragmentLocationEditingBinding
import my.com.abibasInven.util.LocationAddAdapter


class LocationEditingFragment : Fragment() {


    private lateinit var binding: FragmentLocationEditingBinding
    private val nav by lazy {findNavController()}
    private val vm: LocationViewModel by activityViewModels()
    private val rackID by lazy { requireArguments().getString("rackID","N/A") }
    private val isUpdateRack by lazy { requireArguments().getString("isUpdateRack","N/A") }
    private var currentCompartmentID : String = ""

    //spnCategory
    private val vmSpn : SpinnerViewModel by activityViewModels()




    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        binding = FragmentLocationEditingBinding.inflate(inflater, container, false)
        reset()

        //spnCategory
        val spnCategory = vmSpn.getCategory()
        val spnArray2 = arrayListOf<String>()


        val adp3 = ArrayAdapter<String>(requireContext(),android.R.layout.simple_spinner_item)
        adp3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spnCategory2.adapter = adp3


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




        if(isUpdateRack!="N/A"){
            binding.btnUpdateCompartment7.text="Update"
        }

        with(binding){
            lblTitle2.text = "Rack " + rackID
            btnUpdateCompartment1.text = rackID + "1"
            btnUpdateCompartment2.text = rackID + "2"
            btnUpdateCompartment3.text = rackID + "3"
            btnUpdateCompartment4.text = rackID + "4"
            btnUpdateCompartment5.text = rackID + "5"
            btnUpdateCompartment6.text = rackID + "6"

            btnUpdateCompartment1.setOnClickListener { displayCompartmentDetails(btnUpdateCompartment1.text.toString()) }
            btnUpdateCompartment2.setOnClickListener { displayCompartmentDetails(btnUpdateCompartment2.text.toString()) }
            btnUpdateCompartment3.setOnClickListener { displayCompartmentDetails(btnUpdateCompartment3.text.toString()) }
            btnUpdateCompartment4.setOnClickListener { displayCompartmentDetails(btnUpdateCompartment4.text.toString()) }
            btnUpdateCompartment5.setOnClickListener { displayCompartmentDetails(btnUpdateCompartment5.text.toString()) }
            btnUpdateCompartment6.setOnClickListener { displayCompartmentDetails(btnUpdateCompartment6.text.toString()) }
            btnUpdateCompartment7.setOnClickListener { updateCompartmentDetails(currentCompartmentID) }
            btnBackForLocationEditing.setOnClickListener { nav.navigateUp() }
        }


        return binding.root

    }



    private fun updateCompartmentDetails(btnCompartmentID : String) {


        //TODO update
        if(currentCompartmentID!=null&&binding.edtMaxCapacity2.text.toString().toInt()>0) {
            //val l =vm.get(currentCompartmentID)
            val uLocation = Location(
                ID = currentCompartmentID,
                categoryID = binding.spnCategory2.selectedItem.toString(),
                occupiedCapacity = 0,
                maxCapacity = binding.edtMaxCapacity2.text.toString().toInt()
                //categoryID = binding.spnCategory.selectedItem.toString()
            )
            vm.set(uLocation)
            informationDialog(currentCompartmentID+" update successfully")

            //invisible


            componentDetailInvi()


            when(currentCompartmentID){
                binding.btnUpdateCompartment1.text -> binding.btnUpdateCompartment1.setBackgroundColor(Color.BLUE)
                binding.btnUpdateCompartment2.text -> binding.btnUpdateCompartment2.setBackgroundColor(Color.BLUE)
                binding.btnUpdateCompartment3.text -> binding.btnUpdateCompartment3.setBackgroundColor(Color.BLUE)
                binding.btnUpdateCompartment4.text -> binding.btnUpdateCompartment4.setBackgroundColor(Color.BLUE)
                binding.btnUpdateCompartment5.text -> binding.btnUpdateCompartment5.setBackgroundColor(Color.BLUE)
                binding.btnUpdateCompartment6.text -> binding.btnUpdateCompartment6.setBackgroundColor(Color.BLUE)
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

            binding.lblCompartmentDetails2.text = l.ID + " details"

            binding.edtMaxCapacity2.setText(l.maxCapacity.toString())
            currentCompartmentID = l.ID

            //binding.textView23.text = binding.edtMaxCapacity.text
        }
    }

    private fun reset() {
        with(binding){
            lblTitle2.text = "Rack "
            btnUpdateCompartment1.text = ""
            btnUpdateCompartment2.text = ""
            btnUpdateCompartment3.text = ""
            btnUpdateCompartment4.text = ""
            btnUpdateCompartment5.text = ""
            btnUpdateCompartment6.text = ""
            lblCompartmentDetails2.text = "details"
            //edtMaxCapacity2.text.clear()

            //invisible
            componentDetailInvi()

        }
    }

    private fun componentDetailInvi(){
        with(binding){
            lblCompartmentDetails2.isVisible = false
            textView14.isVisible=false
            spnCategory2.isVisible = false
            textView17.isVisible=false
            edtMaxCapacity2.isVisible = false
            btnUpdateCompartment7.isVisible = false
        }
    }
    private fun componentDetailVisible() {
        with(binding) {
            lblCompartmentDetails2.isVisible = true
            textView14.isVisible = true
            spnCategory2.isVisible = true
            textView17.isVisible = true
            edtMaxCapacity2.isVisible = true
            btnUpdateCompartment7.isVisible = true
        }
    }


}