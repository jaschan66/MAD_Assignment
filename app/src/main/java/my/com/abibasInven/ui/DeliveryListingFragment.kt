package my.com.abibasInven.ui

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
import com.example.logindemo.util.informationDialog
import com.google.android.material.bottomnavigation.BottomNavigationView
import my.com.abibasInven.R
import my.com.abibasInven.data.*
import my.com.abibasInven.databinding.FragmentDeliveryAddingBinding
import my.com.abibasInven.databinding.FragmentDeliveryListingBinding
import my.com.abibasInven.util.DeliveryAdapter
import my.com.abibasInven.util.LocationAdapter


class DeliveryListingFragment : Fragment() {

    private lateinit var binding: FragmentDeliveryListingBinding
    private val nav by lazy {findNavController()}
    private val deliveryvm : DeliveryViewModel by activityViewModels()
    private val productvm : ProductViewModel by activityViewModels()
    private val deliveryItemvm: DeliveryItemViewModel by activityViewModels()

    private lateinit var  adapter: DeliveryAdapter




    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Enable bottom navigation menu
        val bottomNav : BottomNavigationView = requireActivity().findViewById(R.id.bottomNavigationView)
        bottomNav.visibility = View.GONE

        // Inflate the layout for this fragment
        binding = FragmentDeliveryListingBinding.inflate(inflater, container, false)

        productvm.getAll()

        val num = productvm.calSize()
        num

        val test =  productvm.getAllProductHaveQty().value
        test

        adapter = DeliveryAdapter(){ holder, delivery ->


            holder.btnDeliveryDetail.setOnClickListener {

                val foundDeliveryItem = deliveryItemvm.getByDeliveryID(delivery.ID)
                if(foundDeliveryItem!=null){
                    nav.navigate(R.id.deliveryItemListingFragment,bundleOf("currentDeliveryID" to delivery.ID))
                }
                else{
                    informationDialog(" there is no relevant details about it")
                }



            }
            holder.btnDeliveryListingAddDeliveryItem.setOnClickListener {

                if(test!!.size == 0){
                    informationDialog("there is no product at store")
                }
                else{
                    nav.navigate(R.id.deliveryItemAddingFragment,bundleOf("currentDeliveryID" to delivery.ID))
                }




            }



        }
        binding.rvDeliveryListing.adapter = adapter
        binding.rvDeliveryListing.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))



        binding.btnDeliveryListingAll.setOnClickListener { displayDeliveryListingAll() }
        binding.btnDeliveryListingReady.setOnClickListener { displayDeliveryListingReady() }
        binding.btnDeliveryListingDelivering.setOnClickListener { displayDeliveryListingDelivering() }
        binding.btnDeliveryListingCompleted.setOnClickListener { displayDeliveryListingCompleted() }

        binding.btnCreateDelivery.setOnClickListener { nav.navigate(R.id.deliveryAddingFragment) }

        //        if(delivery.deliveryStatus=="delivering"||delivery.deliveryStatus=="completed")

        deliveryvm.getAllDelivery().observe(viewLifecycleOwner){ list->
            val array = list.filter { it.deliveryStatus=="ready" }
            adapter.submitList(array)
            binding.lblDeliveryCount.text = "${array.size} delivery(s)"
        }


        return binding.root
    }

    private fun displayDeliveryListingCompleted() {
        deliveryvm.getAllDelivery().observe(viewLifecycleOwner){ list->
            val array = list.filter { it.deliveryStatus=="completed" }
            adapter.submitList(array)
            binding.lblDeliveryCount.text = "${array.size} delivery(s)"
        }
    }

    private fun displayDeliveryListingDelivering() {
        deliveryvm.getAllDelivery().observe(viewLifecycleOwner){ list->
            val array = list.filter { it.deliveryStatus=="delivering" }
            adapter.submitList(array)
            binding.lblDeliveryCount.text = "${array.size} delivery(s)"
        }
    }

    private fun displayDeliveryListingReady() {

        deliveryvm.getAllDelivery().observe(viewLifecycleOwner){ list->
            val array = list.filter { it.deliveryStatus=="ready" }
            adapter.submitList(array)
            binding.lblDeliveryCount.text = "${array.size} delivery(s)"
        }
    }

    private fun displayDeliveryListingAll() {
        deliveryvm.getAllDelivery().observe(viewLifecycleOwner){ list->
            adapter.submitList(list)
            binding.lblDeliveryCount.text = "${list.size} delivery(s)"

        }

    }


}