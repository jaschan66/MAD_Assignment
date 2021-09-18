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
import com.example.logindemo.util.informationDialog
import my.com.abibasInven.R
import my.com.abibasInven.data.*
import my.com.abibasInven.databinding.FragmentDeliveryItemListingBinding
import my.com.abibasInven.databinding.FragmentDeliveryListingBinding
import my.com.abibasInven.util.DeliveryItemAdapter


class DeliveryItemListingFragment : Fragment() {

    private lateinit var binding: FragmentDeliveryItemListingBinding
    private val nav by lazy {findNavController()}
    private val vm: ProductViewModel by activityViewModels()
    private val deliveryvm : DeliveryViewModel by activityViewModels()
    private val deliveryItemvm: DeliveryItemViewModel by activityViewModels()
    private val outletvm: OutletViewModel by activityViewModels()

    private val currentDeliveryID by lazy { requireArguments().getString("currentDeliveryID","N/A") }

    private lateinit var  adapter: DeliveryItemAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDeliveryItemListingBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        outletvm.getAll()


        binding.lblDeliveryItemIDListing.text = "Delivery ID :$currentDeliveryID"

        binding.btnCloseItemDeliveryListing.setOnClickListener { nav.navigateUp() }


        adapter = DeliveryItemAdapter()
        binding.rvDeliveryItemListing.adapter = adapter
        binding.rvDeliveryItemListing.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))

        deliveryItemvm.getAllDeliveryItem().observe(viewLifecycleOwner){ list ->

            var arrayDelivery = list.filter { d ->
                d.deliveryID == currentDeliveryID
            }
            adapter.submitList(arrayDelivery)
            binding.lblDeliveryItemListingCount.text ="${arrayDelivery.size} delivery item(s)"
            if(arrayDelivery.size==0){
                nav.navigateUp()
                informationDialog("there is no relevant delivery item details")
            }

        }


        binding.btnDeliver.setOnClickListener { deliverProducts() }

        return binding.root
    }

    private fun deliverProducts() {

        val foundDeliveryData = deliveryvm.get(currentDeliveryID)

        if(foundDeliveryData!=null){
            val d = Delivery(
                ID = foundDeliveryData.ID,
                outletID = foundDeliveryData.outletID,
                deliveryStatus = "delivering"
            )
            nav.navigate(R.id.deliveryOutletFragment, bundleOf("currentOutletID" to foundDeliveryData.outletID,"currentDeliveryID" to currentDeliveryID))
        }

        else{
            nav.navigateUp()
            errorDialog("no current data is found")
        }


    }


}