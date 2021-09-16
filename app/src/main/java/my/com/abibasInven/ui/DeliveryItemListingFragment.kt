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
import my.com.abibasInven.data.DeliveryItem
import my.com.abibasInven.data.DeliveryItemViewModel
import my.com.abibasInven.data.ProductViewModel
import my.com.abibasInven.databinding.FragmentDeliveryItemListingBinding
import my.com.abibasInven.databinding.FragmentDeliveryListingBinding
import my.com.abibasInven.util.DeliveryItemAdapter


class DeliveryItemListingFragment : Fragment() {

    private lateinit var binding: FragmentDeliveryItemListingBinding
    private val nav by lazy {findNavController()}
    private val vm: ProductViewModel by activityViewModels()
    private val deliveryItemvm: DeliveryItemViewModel by activityViewModels()

    private val currentDeliveryID by lazy { requireArguments().getString("deliveryID","N/A") }

    private lateinit var  adapter: DeliveryItemAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDeliveryItemListingBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment


        binding.lblDeliveryItemIDListing.text = "Delivery ID :$currentDeliveryID"

        binding.btnCloseItemDeliveryListing.setOnClickListener { nav.navigateUp() }


        adapter = DeliveryItemAdapter()
        binding.rvDeliveryItemListing.adapter = adapter
        binding.rvDeliveryItemListing.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))




//        deliveryItemvm.getAllDeliveryItem().observe(viewLifecycleOwner){
//
//        }
//        vm.getAll().observe(viewLifecycleOwner){
//
//            adapter.submitList(it.takeWhile { it.ID ==  })
//            binding.lblDeliveryItemListingCount.text = "${it.size} delivery item(s)"
//
//        }


        return binding.root
    }


}