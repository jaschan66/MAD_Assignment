package my.com.abibasInven.ui

import android.app.AlertDialog
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
import com.example.logindemo.util.snackbar
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
    private val productvm: ProductViewModel by activityViewModels()
    private val stockOutvm: StockOutViewModel by activityViewModels()

    private val currentDeliveryID by lazy { requireArguments().getString("currentDeliveryID","N/A") }

    private lateinit var  adapter: DeliveryItemAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDeliveryItemListingBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        outletvm.getAllOutlet()


        binding.lblDeliveryItemIDListing.text = "Delivery ID :$currentDeliveryID"

        binding.btnCloseItemDeliveryListing.setOnClickListener { nav.navigate(R.id.action_deliveryItemListingFragment_to_deliveryListingFragment) }


        adapter = DeliveryItemAdapter() { holder, deliveryItem ->
            holder.btnDeliveryItemEdit.setOnClickListener {
                nav.navigate(R.id.deliveryItemUpdateFragment, bundleOf("currentDeliveryItemID" to deliveryItem.ID))
            }
            holder.btnDeliveryItemDelete.setOnClickListener {
                val builder = AlertDialog.Builder(context)
                builder.setMessage("Are you sure you want to Delete?")
                    .setCancelable(false)
                    .setPositiveButton("Yes") { dialog, id ->
                        snackbar("Delivery Item deleted successfully")
                        deleteDeliveryItem(deliveryItem.ID, deliveryItem.deliveryID, deliveryItem.productID)
                    }
                    .setNegativeButton("No") { dialog, id ->
                        // Dismiss the dialog
                        dialog.dismiss()
                    }
                val alert = builder.create()
                alert.show()
            }


        }
        binding.rvDeliveryItemListing.adapter = adapter
        binding.rvDeliveryItemListing.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))

        deliveryItemvm.getAllDeliveryItem().observe(viewLifecycleOwner){ list ->

            var arrayDelivery = list.filter { d ->
                d.deliveryID == currentDeliveryID
            }
            adapter.submitList(arrayDelivery)
            binding.lblDeliveryItemListingCount.text ="${arrayDelivery.size} delivery item(s)"
            if(arrayDelivery.size==0){
                nav.navigate(R.id.action_deliveryItemListingFragment_to_deliveryListingFragment)
                informationDialog("there is no relevant delivery item details")
            }

        }


        binding.btnDeliver.setOnClickListener { deliverProducts() }

        return binding.root
    }

    private fun deleteDeliveryItem(deliveryItemid: String, deliveryitemDeliveryID : String, deliveryitemProductID : String) {
        val foundDeliveryData =deliveryvm.get(deliveryitemDeliveryID)

        val foundDeliveryItemData = deliveryItemvm.get(deliveryItemid)

        val foundProductData = productvm.get(deliveryitemProductID)

        if(foundDeliveryData!=null){

            when(foundDeliveryData.deliveryStatus){
                "delivering"-> snackbar("cannot be cancelled, it's delivering")
                "completed"-> snackbar("cannot be cancelled, delivery is completed")
                "ready"-> {



                    if(foundProductData!=null&&foundDeliveryItemData!=null){
                        val updateProductQty  = Product(
                            ID = foundProductData.ID,
                            name = foundProductData.name,
                            qty = foundProductData.qty+foundDeliveryItemData.deliveryQty,
                            qtyThreshold =  foundProductData.qtyThreshold,
                            categoryID = foundProductData.categoryID,
                            photo = foundProductData.photo,
                            locationID = foundProductData.locationID,
                            supplierID = foundProductData.supplierID,
                        )
                        productvm.set(updateProductQty)
                        stockOutvm.remove(foundDeliveryItemData.stockOutID)

                    }

                    deliveryItemvm.delete(deliveryItemid)


                    snackbar("delivery item ( ${foundDeliveryData.ID} ) has been deleted")
                }
                else -> snackbar("no such status ${foundDeliveryData.deliveryStatus} is found")
            }


        }



    }

    private fun deliverProducts() {

        val foundDeliveryData = deliveryvm.get(currentDeliveryID)


        if(foundDeliveryData!=null){
            val d = Delivery(
                ID = foundDeliveryData.ID,
                outletID = foundDeliveryData.outletID,
                deliveryStatus = "delivering",
            )
            deliveryvm.set(d)
            nav.navigate(R.id.deliveryOutletFragment, bundleOf("currentOutletID" to foundDeliveryData.outletID,"currentDeliveryID" to currentDeliveryID))
        }
        else{
            nav.navigate(R.id.action_deliveryItemListingFragment_to_deliveryListingFragment)
            errorDialog("no current data is found")
        }


    }


}