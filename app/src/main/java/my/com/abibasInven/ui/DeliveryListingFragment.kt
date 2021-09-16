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
import my.com.abibasInven.data.Delivery
import my.com.abibasInven.data.DeliveryViewModel
import my.com.abibasInven.databinding.FragmentDeliveryAddingBinding
import my.com.abibasInven.databinding.FragmentDeliveryListingBinding
import my.com.abibasInven.util.DeliveryAdapter
import my.com.abibasInven.util.LocationAdapter


class DeliveryListingFragment : Fragment() {

    private lateinit var binding: FragmentDeliveryListingBinding
    private val nav by lazy {findNavController()}
    private val deliveryvm : DeliveryViewModel by activityViewModels()

    private lateinit var  adapter: DeliveryAdapter




    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentDeliveryListingBinding.inflate(inflater, container, false)

        adapter = DeliveryAdapter(){ holder, delivery ->


            holder.btnDeliveryDetail.setOnClickListener {

            }
            holder.btnDeliveryListingAddDeliveryItem.setOnClickListener {
                nav.navigate(R.id.deliveryItemAddingFragment,bundleOf("currentDeliveryID" to delivery.ID))

            }
            holder.btnEditDelivery.setOnClickListener {

            }
            holder.btnDeleteDelivery.setOnClickListener {

            }
        }

        binding.rvDeliveryListing.adapter = adapter
        binding.rvDeliveryListing.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))

        deliveryvm.getAllDelivery().observe(viewLifecycleOwner){ list->
            adapter.submitList(list)
            binding.lblDeliveryCount.text = "${list.size} delivery(s)"

        }

        binding.btnCreateDelivery.setOnClickListener { nav.navigate(R.id.deliveryAddingFragment) }


        return binding.root
    }



}