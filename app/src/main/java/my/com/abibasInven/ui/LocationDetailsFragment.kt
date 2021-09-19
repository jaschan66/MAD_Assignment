package my.com.abibasInven.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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



        binding.btnBackForLocationEditing2.setOnClickListener { nav.navigateUp() }
        binding.btnCloseLocationDetail.setOnClickListener { nav.navigateUp() }


        //bottom navigation

        binding.bottomNavigationView.selectedItemId = R.id.home

        binding.bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId){

                R.id.home -> nav.navigate(R.id.homeFragment)
                R.id.account -> nav.navigate(R.id.accountFragment)
                R.id.product -> nav.navigate(R.id.productFragment)
            }
            true
        }
        return binding.root
    }

    private fun showExistingCompartmentProduct(buttonID : String) {

        vm.getAllProductHaveLocation().observe(viewLifecycleOwner) { list ->

            //adapter.submitList(it.takeWhile{ it.locationID == buttonID })
            var arrayLocation = list.filter { p ->
                p.locationID == buttonID
            }
            adapter.submitList(arrayLocation)
            binding.lblLocationProductCount.text = "${list.size} product(s)"

        }


    }

}