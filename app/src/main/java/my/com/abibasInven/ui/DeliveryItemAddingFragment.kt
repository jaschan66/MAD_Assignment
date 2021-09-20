package my.com.abibasInven.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.view.size
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.logindemo.util.*
import my.com.abibasInven.R
import my.com.abibasInven.data.*
import my.com.abibasInven.databinding.FragmentDeliveryItemAddingBinding
import my.com.abibasInven.databinding.FragmentDeliveryListingBinding
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class DeliveryItemAddingFragment : Fragment() {

    private lateinit var binding: FragmentDeliveryItemAddingBinding
    private val nav by lazy {findNavController()}


    private val deliveryItemvm : DeliveryItemViewModel by activityViewModels()
    private val productvm : ProductViewModel by activityViewModels()
    private val stockOutvm : StockOutViewModel by activityViewModels()
    private val deliveryvm : DeliveryViewModel by activityViewModels()
    private val outletvm : OutletViewModel by activityViewModels()

    private val currentDeliveryID by lazy { requireArguments().getString("currentDeliveryID","N/A") }


    private var currentdeliveryItemID = ""

    //spnProduct
    private val vmSpn : SpinnerViewModel by activityViewModels()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {



        binding = FragmentDeliveryItemAddingBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment

        //spnProduct
        val spnProduct = vmSpn.getProduct()
        val spnArray2 = arrayListOf<String>()




        val adp3 = ArrayAdapter<String>(requireContext(),android.R.layout.simple_spinner_item)
        adp3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spnDeliveryProduct2.adapter = adp3

        spnProduct.observe(viewLifecycleOwner) { list ->
            //list.groupBy { it.ID}
            val num = list.size
            val productSize = vmSpn.calProductSize()
            if (list.size > spnArray2.size) {
                for (i in 0..productSize - 1) {
                    if(list[i].qty>0){
                        adp3.add(list[i].ID)//change here to get value
                        spnArray2.add(list[i].ID) //change here to get value
                    }

                }
            } else if (num <= spnArray2.size ){
                spnArray2.clear()
                adp3.clear()
                for (i in 0..productSize - 1) {
                    if(list[i].qty>0) {
                        adp3.add(list[i].ID)
                        spnArray2.add(list[i].ID)
                    }
                }
            }
        }


        binding.lblCurrentDeliveryID4.text = currentDeliveryID

        val  foundDeliveryData = deliveryvm.get(currentDeliveryID)



        if(foundDeliveryData !=null){
            val foundOutletData  = outletvm.get(foundDeliveryData.outletID)


            if(foundOutletData!=null){
                binding.lblCurrentOutletName.text = foundOutletData.name
            }
        }





        binding.btnAddDeliveryItem.setOnClickListener { addDeliveryItem() }
        binding.btnCancelDeliveryItem.setOnClickListener { nav.navigate(R.id.action_deliveryItemAddingFragment_to_deliveryListingFragment) }




        return binding.root
    }



    private fun addDeliveryItem() {



        // id generator for delivery item
        val id = "DVI" + (deliveryItemvm.calDeliveryItemSize() + 1).toString()
        val deliveryItemID = deliveryItemvm.validID(id)

        currentdeliveryItemID = deliveryItemID


//        // id generator for stockOut
        val id2 = "SO" + (stockOutvm.calStockOutSize() + 1).toString()
        val stockOutID = stockOutvm.validID(id2)

        val currentDateTime = LocalDateTime.now()
        val dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")

        // find the product picture by Delivery Item's product ID store in at here
        val p = productvm.get(binding.spnDeliveryProduct2.selectedItem.toString())



        if(p!=null&&binding.edtDeliveryQty2.text.toString().toInt()>p.qty){
            errorDialog("product ( ${p.ID} ) only have ${p.qty} quantity")
        }
        else{
            if (currentDeliveryID != "N/A" && binding.edtDeliveryQty2.text.toString() != "" && binding.edtDeliveryQty2.text.toString().toInt() != 0 && p!=null ) {
                val newDeliveryItem = DeliveryItem(
                    ID = deliveryItemID,
                    productID = binding.spnDeliveryProduct2.selectedItem.toString(),
                    deliveryQty = binding.edtDeliveryQty2.text.toString().toInt(),
                    deliveryID = currentDeliveryID,
                    deliveryItemPhoto = p!!.photo,
                    stockOutID = stockOutID,
                )
                deliveryItemvm.set(newDeliveryItem)


                val updateProduct = Product(
                    ID = p.ID,
                    name = p.name,
                    qty = p.qty - binding.edtDeliveryQty2.text.toString().toInt(),
                    qtyThreshold = p.qtyThreshold,
                    categoryID = p.categoryID,
                    photo = p.photo,
                    locationID = p.locationID,
                    supplierID = p.supplierID,
                )
                productvm.set(updateProduct)

                val foundDeliveryData = deliveryvm.get(currentDeliveryID)
                if(foundDeliveryData!=null){
                    val newStockOut  = StockOut(
                        ID = stockOutID,
                        dateTime = dtf.format(currentDateTime).toString(),
                        deliveryID = currentDeliveryID,
                        productID = p.ID,
                        qty = binding.edtDeliveryQty2.text.toString().toInt(),
                        outletID = foundDeliveryData.outletID
                    )
                    stockOutvm.set(newStockOut)
                }





                snackbar(binding.spnDeliveryProduct2.selectedItem.toString() + " has added to " + currentDeliveryID)



            }
            else {
                errorDialog("delivery quantity cannot be empty or 0 ")
            }
        }





    }


}