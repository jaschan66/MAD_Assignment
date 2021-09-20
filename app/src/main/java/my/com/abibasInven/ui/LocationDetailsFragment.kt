package my.com.abibasInven.ui

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import my.com.abibasInven.R
import my.com.abibasInven.data.ProductViewModel
import my.com.abibasInven.databinding.FragmentLocationDetailsBinding
import my.com.abibasInven.util.LocationDetailsAdapter



class LocationDetailsFragment : Fragment() {

    private lateinit var binding: FragmentLocationDetailsBinding
    private val nav by lazy {findNavController()}
    private lateinit var adapter: LocationDetailsAdapter
    private val vm: ProductViewModel by activityViewModels()

    private val rackID by lazy { requireArguments().getString("rackID","N/A") }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = FragmentLocationDetailsBinding.inflate(inflater, container, false)

        // TODO

        adapter = LocationDetailsAdapter()

        binding.rvLocationDetails.adapter = adapter
        binding.rvLocationDetails.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))


        with(binding){

            lblTitle4.text = "Rack " + rackID
            btnLocationDetailCompartment1.text = rackID + "1"
            btnLocationDetailCompartment2.text = rackID + "2"
            btnLocationDetailCompartment3.text = rackID + "3"
            btnLocationDetailCompartment4.text = rackID + "4"
            btnLocationDetailCompartment5.text = rackID + "5"
            btnLocationDetailCompartment6.text = rackID + "6"

            btnLocationDetailCompartment1.setOnClickListener { showExistingCompartmentProduct(btnLocationDetailCompartment1.text.toString()) }
            btnLocationDetailCompartment2.setOnClickListener { showExistingCompartmentProduct(btnLocationDetailCompartment2.text.toString()) }
            btnLocationDetailCompartment3.setOnClickListener { showExistingCompartmentProduct(btnLocationDetailCompartment3.text.toString()) }
            btnLocationDetailCompartment4.setOnClickListener { showExistingCompartmentProduct(btnLocationDetailCompartment4.text.toString()) }
            btnLocationDetailCompartment5.setOnClickListener { showExistingCompartmentProduct(btnLocationDetailCompartment5.text.toString()) }
            btnLocationDetailCompartment6.setOnClickListener { showExistingCompartmentProduct(btnLocationDetailCompartment6.text.toString()) }

        }



        binding.btnCloseLocationDetail.setOnClickListener { nav.navigate(R.id.action_locationDetailsFragment_to_locationListingFragment)}





        return binding.root
    }


    private fun showExistingCompartmentProduct(buttonID : String) {

        btnChangeColor(buttonID)

        vm.getAllProductHaveLocation().observe(viewLifecycleOwner) { list ->

            //adapter.submitList(it.takeWhile{ it.locationID == buttonID })
            var arrayLocation = list.filter { p ->
                p.locationID == buttonID
            }
            adapter.submitList(arrayLocation)
            binding.lblLocationProductCount.text = "${arrayLocation.size} product(s)"

        }


    }
    private fun btnChangeColor(buttonID : String){
        when(buttonID){
            binding.btnLocationDetailCompartment1.text ->{
                binding.btnLocationDetailCompartment1.setBackgroundColor(Color.rgb(247, 177, 106))
                binding.btnLocationDetailCompartment1.setTextColor(Color.BLACK)

                binding.btnLocationDetailCompartment2.setBackgroundColor(Color.BLACK)
                binding.btnLocationDetailCompartment2.setTextColor(Color.WHITE)
                binding.btnLocationDetailCompartment3.setBackgroundColor(Color.BLACK)
                binding.btnLocationDetailCompartment3.setTextColor(Color.WHITE)
                binding.btnLocationDetailCompartment4.setBackgroundColor(Color.BLACK)
                binding.btnLocationDetailCompartment4.setTextColor(Color.WHITE)
                binding.btnLocationDetailCompartment5.setBackgroundColor(Color.BLACK)
                binding.btnLocationDetailCompartment5.setTextColor(Color.WHITE)
                binding.btnLocationDetailCompartment6.setBackgroundColor(Color.BLACK)
                binding.btnLocationDetailCompartment6.setTextColor(Color.WHITE)
            }
            binding.btnLocationDetailCompartment2.text ->{
                binding.btnLocationDetailCompartment2.setBackgroundColor(Color.rgb(247, 177, 106))
                binding.btnLocationDetailCompartment2.setTextColor(Color.BLACK)

                binding.btnLocationDetailCompartment1.setBackgroundColor(Color.BLACK)
                binding.btnLocationDetailCompartment1.setTextColor(Color.WHITE)
                binding.btnLocationDetailCompartment3.setBackgroundColor(Color.BLACK)
                binding.btnLocationDetailCompartment3.setTextColor(Color.WHITE)
                binding.btnLocationDetailCompartment4.setBackgroundColor(Color.BLACK)
                binding.btnLocationDetailCompartment4.setTextColor(Color.WHITE)
                binding.btnLocationDetailCompartment5.setBackgroundColor(Color.BLACK)
                binding.btnLocationDetailCompartment5.setTextColor(Color.WHITE)
                binding.btnLocationDetailCompartment6.setBackgroundColor(Color.BLACK)
                binding.btnLocationDetailCompartment6.setTextColor(Color.WHITE)
            }
            binding.btnLocationDetailCompartment3.text ->{
                binding.btnLocationDetailCompartment3.setBackgroundColor(Color.rgb(247, 177, 106))
                binding.btnLocationDetailCompartment3.setTextColor(Color.BLACK)

                binding.btnLocationDetailCompartment1.setBackgroundColor(Color.BLACK)
                binding.btnLocationDetailCompartment1.setTextColor(Color.WHITE)
                binding.btnLocationDetailCompartment2.setBackgroundColor(Color.BLACK)
                binding.btnLocationDetailCompartment2.setTextColor(Color.WHITE)
                binding.btnLocationDetailCompartment4.setBackgroundColor(Color.BLACK)
                binding.btnLocationDetailCompartment4.setTextColor(Color.WHITE)
                binding.btnLocationDetailCompartment5.setBackgroundColor(Color.BLACK)
                binding.btnLocationDetailCompartment5.setTextColor(Color.WHITE)
                binding.btnLocationDetailCompartment6.setBackgroundColor(Color.BLACK)
                binding.btnLocationDetailCompartment6.setTextColor(Color.WHITE)
            }
            binding.btnLocationDetailCompartment4.text ->{
                binding.btnLocationDetailCompartment4.setBackgroundColor(Color.rgb(247, 177, 106))
                binding.btnLocationDetailCompartment4.setTextColor(Color.BLACK)

                binding.btnLocationDetailCompartment1.setBackgroundColor(Color.BLACK)
                binding.btnLocationDetailCompartment1.setTextColor(Color.WHITE)
                binding.btnLocationDetailCompartment2.setBackgroundColor(Color.BLACK)
                binding.btnLocationDetailCompartment2.setTextColor(Color.WHITE)
                binding.btnLocationDetailCompartment3.setBackgroundColor(Color.BLACK)
                binding.btnLocationDetailCompartment3.setTextColor(Color.WHITE)
                binding.btnLocationDetailCompartment5.setBackgroundColor(Color.BLACK)
                binding.btnLocationDetailCompartment5.setTextColor(Color.WHITE)
                binding.btnLocationDetailCompartment6.setBackgroundColor(Color.BLACK)
                binding.btnLocationDetailCompartment6.setTextColor(Color.WHITE)
            }
            binding.btnLocationDetailCompartment5.text ->{
                binding.btnLocationDetailCompartment5.setBackgroundColor(Color.rgb(247, 177, 106))
                binding.btnLocationDetailCompartment5.setTextColor(Color.BLACK)

                binding.btnLocationDetailCompartment2.setBackgroundColor(Color.BLACK)
                binding.btnLocationDetailCompartment2.setTextColor(Color.WHITE)
                binding.btnLocationDetailCompartment3.setBackgroundColor(Color.BLACK)
                binding.btnLocationDetailCompartment3.setTextColor(Color.WHITE)
                binding.btnLocationDetailCompartment4.setBackgroundColor(Color.BLACK)
                binding.btnLocationDetailCompartment4.setTextColor(Color.WHITE)
                binding.btnLocationDetailCompartment1.setBackgroundColor(Color.BLACK)
                binding.btnLocationDetailCompartment1.setTextColor(Color.WHITE)
                binding.btnLocationDetailCompartment6.setBackgroundColor(Color.BLACK)
                binding.btnLocationDetailCompartment6.setTextColor(Color.WHITE)
            }
            binding.btnLocationDetailCompartment6.text ->{
                binding.btnLocationDetailCompartment6.setBackgroundColor(Color.rgb(247, 177, 106))
                binding.btnLocationDetailCompartment6.setTextColor(Color.BLACK)

                binding.btnLocationDetailCompartment2.setBackgroundColor(Color.BLACK)
                binding.btnLocationDetailCompartment2.setTextColor(Color.WHITE)
                binding.btnLocationDetailCompartment3.setBackgroundColor(Color.BLACK)
                binding.btnLocationDetailCompartment3.setTextColor(Color.WHITE)
                binding.btnLocationDetailCompartment4.setBackgroundColor(Color.BLACK)
                binding.btnLocationDetailCompartment4.setTextColor(Color.WHITE)
                binding.btnLocationDetailCompartment5.setBackgroundColor(Color.BLACK)
                binding.btnLocationDetailCompartment5.setTextColor(Color.WHITE)
                binding.btnLocationDetailCompartment1.setBackgroundColor(Color.BLACK)
                binding.btnLocationDetailCompartment1.setTextColor(Color.WHITE)
            }

        }
    }

}