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
import com.example.logindemo.util.errorDialog
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import my.com.abibasInven.R
import my.com.abibasInven.data.Location
import my.com.abibasInven.data.LocationViewModel
import my.com.abibasInven.data.RackType
import my.com.abibasInven.data.UserViewModel
import my.com.abibasInven.databinding.FragmentHomeBinding
import my.com.abibasInven.databinding.FragmentLocationListingBinding
import my.com.abibasInven.util.LocationAdapter
import my.com.abibasInven.util.StaffAdapter
import java.util.*


class LocationListingFragment : Fragment() {

    private lateinit var binding: FragmentLocationListingBinding
    private val nav by lazy { findNavController() }
    private val vm: LocationViewModel by activityViewModels()

    private lateinit var adapter: LocationAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentLocationListingBinding.inflate(inflater, container, false)

        // TODO
        adapter = LocationAdapter() { holder, rackType ->
            // Item click
            holder.btnRackDetail.setOnClickListener {
                nav.navigate(R.id.locationDetailsFragment, bundleOf("rackID" to rackType.ID))
            }
            holder.btnEditRack.setOnClickListener {
                nav.navigate(R.id.locationEditingFragment, bundleOf("rackID" to rackType.ID, "isUpdateRack" to "true"))
            }
            // Delete button click
            holder.btnDeleteRack.setOnClickListener { delete(rackType.ID) }

        }

        binding.rvLocationListing.adapter = adapter
        binding.rvLocationListing.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))

        binding.btnCreateLocation.setOnClickListener { createNewLocation() }



        vm.getAllRack().observe(viewLifecycleOwner) { list ->
            //list.groupBy { it.ID}
            adapter.submitList(list)
            binding.lblRackCount.text = "${list.size} location(s)"
            binding.lblTotalRack.text = list.size.toString()


//                list.filter { l -> l.rackType == l.rackType }


        }



        return binding.root
    }


    private fun createNewLocation() {

        // TODO: create location at firebase
        var save = ""

        if (vm.getLocationSize() == 0){
            for(i in 1..6){
                val l = Location(
                    ID   = generateAlphabet(binding.lblTotalRack.text.toString().toInt()).toString()+i,
                    rackType =  generateAlphabet(binding.lblTotalRack.text.toString().toInt()).toString(),
                    categoryID  = "",
                    occupiedCapacity = 0,
                    maxCapacity = 0,
                )
                vm.set(l)
                save += generateAlphabet(binding.lblTotalRack.text.toString().toInt()).toString()+i + ","
                if( i == 6 ){
                    val r = RackType(
                        ID = generateAlphabet(binding.lblTotalRack.text.toString().toInt()).toString(),
                        rackData = save
                    )
                    vm.setRackType(r)
                }
                nav.navigate(R.id.locationAddingFragment, bundleOf("rackID" to generateAlphabet(binding.lblTotalRack.text.toString().toInt()).toString()))
            }
        } else {
            val t = vm.validID()

            val alpha = t
            val num =  alpha.first().code + 1

            for(i in 1..6){
                val l = Location(
                    ID   = num.toChar().toString() + i,
                    rackType =  num.toChar().toString(),
                    categoryID  = "",
                    occupiedCapacity = 0,
                    maxCapacity = 0,
                )
                vm.set(l)
                save += num.toChar().toString()+i + ","
                if( i == 6 ){
                    val r = RackType(
                        ID = num.toChar().toString(),
                        rackData = save
                    )
                    vm.setRackType(r)
                }
            }
            nav.navigate(R.id.locationAddingFragment, bundleOf("rackID" to num.toChar().toString()))

        }




    }



    private fun generateAlphabet(num: Int) :Char{



        var ascii = 0
        if(num in 0..25){
            ascii = num + 65

        }
        return ascii.toChar()

    }

    private fun delete(id: String) {
        // TODO: Delete
        vm.deleteAllRack(id)
        vm.deleteRack(id)

    }
}