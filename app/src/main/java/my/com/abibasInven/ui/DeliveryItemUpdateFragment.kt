package my.com.abibasInven.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.logindemo.util.errorDialog
import com.example.logindemo.util.snackbar
import com.example.logindemo.util.toBitmap
import my.com.abibasInven.R
import my.com.abibasInven.data.*
import my.com.abibasInven.databinding.FragmentDeliveryItemListingBinding
import my.com.abibasInven.databinding.FragmentDeliveryItemUpdateBinding
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class DeliveryItemUpdateFragment : Fragment() {


    private lateinit var binding: FragmentDeliveryItemUpdateBinding
    private val nav by lazy {findNavController()}
    private val deliveryItemvm: DeliveryItemViewModel by activityViewModels()
    private val productvm: ProductViewModel by activityViewModels()
    private val stockOutvm: StockOutViewModel by activityViewModels()

    private val currentDeliveryItemID by lazy { requireArguments().getString("currentDeliveryItemID","N/A") }

    private var foundDeliveryItemProductID : String = ""

    private var deliveryQty : Int = 0


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        binding = FragmentDeliveryItemUpdateBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment

        binding.lblCurrentDeliveryItemID.text = currentDeliveryItemID + " details"

         val foundDeliveryItem = deliveryItemvm.get(currentDeliveryItemID)

        if(foundDeliveryItem!=null){
            binding.imgEditDeliveryItemPhoto.setImageBitmap(foundDeliveryItem.deliveryItemPhoto.toBitmap())
            binding.edtUpdateDeliveryItemQty.setText(foundDeliveryItem.deliveryQty.toString())
            foundDeliveryItemProductID = foundDeliveryItem.productID

            deliveryQty = foundDeliveryItem.deliveryQty

            val foundProductData = productvm.get(foundDeliveryItemProductID)

        }
        else{
            nav.navigateUp()
        }


        binding.btnEditDeliveryItem.setOnClickListener{ editDeliveryItem() }
        binding.btnCancelEditDeliveryItem.setOnClickListener{ nav.navigateUp() }


        return binding.root
    }

    private fun editDeliveryItem() {

        val foundProductData = productvm.get(foundDeliveryItemProductID)
        val foundDeliveryItem = deliveryItemvm.get(currentDeliveryItemID)
        val num = foundDeliveryItem!!.deliveryQty
        num
        var qtyChanges = 0
//
//            26 > 27
        if (num > binding.edtUpdateDeliveryItemQty.text.toString().toInt()){
            // minus 1 from delivery qty and add into product qty
//                                    26 - 25
            qtyChanges = binding.edtUpdateDeliveryItemQty.text.toString().toInt() - num
        } else {
            // minus 1 from product qty and add into delivery qty
//                                  26 - 27
            qtyChanges = binding.edtUpdateDeliveryItemQty.text.toString().toInt() - num
        }

        qtyChanges

        val currentDateTime = LocalDateTime.now()
        val dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")

        if(foundProductData!=null&&foundDeliveryItem!=null){

            if (foundProductData.qty - qtyChanges >= 0){
                if (qtyChanges < 0){
                    // minus 1 from delivery qty and add into product qty
                    var newProductQty = 0
                    //            26
                    val testingQty = foundProductData.qty
                    //  27  =         26 + 1
                    newProductQty = testingQty - qtyChanges

                    val updateDeliveryItem = DeliveryItem(
                        ID = currentDeliveryItemID,
                        productID = foundProductData.ID,
                        deliveryQty = binding.edtUpdateDeliveryItemQty.text.toString().toInt(),
                        deliveryID = foundDeliveryItem.deliveryID,
                        deliveryItemPhoto = foundDeliveryItem.deliveryItemPhoto,
                    )
                    deliveryItemvm.set(updateDeliveryItem)

                    val updateProductQty = Product(
                        ID = foundProductData.ID,
                        name = foundProductData.name,
                        qty = newProductQty,
                        qtyThreshold =  foundProductData.qtyThreshold,
                        categoryID = foundProductData.categoryID,
                        photo = foundProductData.photo,
                        locationID = foundProductData.locationID,
                        supplierID = foundProductData.supplierID,
                    )
                    productvm.set(updateProductQty)
                    snackbar("updated new delivery item quantity")

                } else if (qtyChanges > 0 && foundProductData.qty != 0) {
                    // minus 1 from delivery qty and add into product qty
                    var newProductQty = 0
                    //            26
                    val testingQty = foundProductData.qty
                    //  27  =         26 + 1
                    newProductQty = testingQty - qtyChanges

                    val updateDeliveryItem = DeliveryItem(
                        ID = currentDeliveryItemID,
                        productID = foundProductData.ID,
                        deliveryQty = binding.edtUpdateDeliveryItemQty.text.toString().toInt(),
                        deliveryID = foundDeliveryItem.deliveryID,
                        deliveryItemPhoto = foundDeliveryItem.deliveryItemPhoto,
                        stockOutID = foundDeliveryItem.stockOutID,
                    )
                    deliveryItemvm.set(updateDeliveryItem)

                    val updateProductQty = Product(
                        ID = foundProductData.ID,
                        name = foundProductData.name,
                        qty = newProductQty,
                        qtyThreshold =  foundProductData.qtyThreshold,
                        categoryID = foundProductData.categoryID,
                        photo = foundProductData.photo,
                        locationID = foundProductData.locationID,
                        supplierID = foundProductData.supplierID,
                    )
                    productvm.set(updateProductQty)

                    val newStockOut  = StockOut(
                        ID = foundDeliveryItem.stockOutID,
                        dateTime = dtf.format(currentDateTime).toString(),
                        deliveryID = foundDeliveryItem.deliveryID,
                        qty = newProductQty,
                    )
                    stockOutvm.set(newStockOut)

                    snackbar("updated new delivery item quantity")
                }
            } else {
                    snackbar("Current product is out of stock ")
            }


//            val num = foundProductData.qty - qtyChanges
//            num
//
//            if( foundProductData.qty - qtyChanges !=0 ){
//
//                var newProductQty = 0
//                val testingQty = foundProductData.qty
//
//                newProductQty = testingQty + qtyChanges
//
//            //TODO the increase of product qty is still not functioning
//
//                val updateDeliveryItem = DeliveryItem(
//                    ID = currentDeliveryItemID,
//                    productID = foundProductData.ID,
//                    deliveryQty = binding.edtUpdateDeliveryItemQty.text.toString().toInt(),
//                    deliveryID = foundDeliveryItem.deliveryID,
//                    deliveryItemPhoto = foundDeliveryItem.deliveryItemPhoto,
//                )
//                deliveryItemvm.set(updateDeliveryItem)
//                val updateProductQty = Product(
//                    ID = foundProductData.ID,
//                    name = foundProductData.name,
//                    qty = newProductQty,
//                    qtyThreshold =  foundProductData.qtyThreshold,
//                    categoryID = foundProductData.categoryID,
//                    photo = foundProductData.photo,
//                    locationID = foundProductData.locationID,
//                    supplierID = foundProductData.supplierID,
//                )
//                productvm.set(updateProductQty)
//                snackbar("updated new delivery item quantity")


//                if(qtyChanges>0){
//                    val updateDeliveryItem = DeliveryItem(
//                        ID = currentDeliveryItemID,
//                        productID = foundProductData.ID,
//                        deliveryQty = binding.edtUpdateDeliveryItemQty.text.toString().toInt(),
//                        deliveryID = foundDeliveryItem.deliveryID,
//                        deliveryItemPhoto = foundDeliveryItem.deliveryItemPhoto,
//                    )
//                    deliveryItemvm.set(updateDeliveryItem)
//                    val updateProductQty = Product(
//                        ID = foundProductData.ID,
//                        name = foundProductData.name,
//                        qty = foundProductData.qty + qtyChanges,
//                        qtyThreshold =  foundProductData.qtyThreshold,
//                        categoryID = foundProductData.categoryID,
//                        photo = foundProductData.photo,
//                        locationID = foundProductData.locationID,
//                        supplierID = foundProductData.supplierID,
//                    )
//                    productvm.set(updateProductQty)
//                    snackbar("updated new delivery item quantity")
//                }
//                else{
//                    val updateDeliveryItem = DeliveryItem(
//                        ID = currentDeliveryItemID,
//                        productID = foundProductData.ID,
//                        deliveryQty = binding.edtUpdateDeliveryItemQty.text.toString().toInt(),
//                        deliveryID = foundDeliveryItem.deliveryID,
//                        deliveryItemPhoto = foundDeliveryItem.deliveryItemPhoto,
//                    )
//                    deliveryItemvm.set(updateDeliveryItem)
//                    val updateProductQty = Product(
//                        ID = foundProductData.ID,
//                        name = foundProductData.name,
//                        qty = foundProductData.qty + qtyChanges,
//                        qtyThreshold =  foundProductData.qtyThreshold,
//                        categoryID = foundProductData.categoryID,
//                        photo = foundProductData.photo,
//                        locationID = foundProductData.locationID,
//                        supplierID = foundProductData.supplierID,
//                    )
//                    productvm.set(updateProductQty)
//                    snackbar("updated new delivery item quantity")
//                }





//            }
//            else{
//                errorDialog("current product(${foundProductData.ID}) only have ${foundProductData.qty}, please choose a lower quantity")
//            }
        }



    }


}