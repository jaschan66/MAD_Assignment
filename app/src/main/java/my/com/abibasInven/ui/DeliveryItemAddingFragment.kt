package my.com.abibasInven.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.logindemo.util.cropToBlob
import com.example.logindemo.util.errorDialog
import com.example.logindemo.util.snackbar
import com.example.logindemo.util.toBitmap
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

    private val currentDeliveryID by lazy { requireArguments().getString("currentDeliveryID","N/A") }

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
                    adp3.add(list[i].ID)//change here to get value
                    spnArray2.add(list[i].ID) //change here to get value
                }
            } else if (num <= spnArray2.size ){
                spnArray2.clear()
                adp3.clear()
                for (i in 0..productSize - 1) {
                    adp3.add(list[i].ID)
                    spnArray2.add(list[i].ID)
                }
            }
        }

        binding.lblCurrentDeliveryID4.text = currentDeliveryID
        binding.btnAddDeliveryItem.setOnClickListener { addDeliveryItem() }
        binding.btnCancelDeliveryItem.setOnClickListener { nav.navigateUp() }

        return binding.root
    }



    private fun addDeliveryItem() {

        // id generator for delivery item
        val id = "DVI" + (deliveryItemvm.calDeliveryItemSize() + 1).toString()
        val deliveryItemID = deliveryItemvm.validID(id)

//        // id generator for stockOut
//        val id2 = "SO" + (stockOutvm.calStockOutSize() + 1).toString()
//        val stockOutID = stockOutvm.validID(id2)

        val currentDateTime = LocalDateTime.now()
        val dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")

        // find the product picture by Delivery Item's product ID store in at here
            val p = productvm.get(binding.spnDeliveryProduct2.selectedItem.toString())




        if (currentDeliveryID != "N/A" && binding.edtDeliveryQty2.text.toString() != "" && binding.edtDeliveryQty2.text.toString().toInt() != 0 && p!=null ) {
            val newDeliveryItem = DeliveryItem(
                ID = deliveryItemID,
                productID = binding.spnDeliveryProduct2.selectedItem.toString(),
                deliveryQty = binding.edtDeliveryQty2.text.toString().toInt(),
                deliveryID = currentDeliveryID,
                deliveryItemPhoto = p!!.photo
            )

//            deliveryItemvm.set(newDeliveryItem)
//            val newStockOut = StockOut(
//                ID = stockOutID,
//                dateTime = dtf.format(currentDateTime).toString(),
//                deliveryID = currentDeliveryID,
//            )
//            stockOutvm.set(newStockOut)

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
            snackbar(binding.spnDeliveryProduct2.selectedItem.toString() + " has added to " + currentDeliveryID)
        }
        else {
        errorDialog("delivery quantity cannot be empty or 0 ")
    }

    }


}