package my.com.abibasInven.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.logindemo.util.errorDialog
import com.example.logindemo.util.snackbar
import com.sun.mail.imap.protocol.ID
import my.com.abibasInven.R
import my.com.abibasInven.data.*
import my.com.abibasInven.databinding.FragmentStaffListBinding
import my.com.abibasInven.databinding.FragmentStockInBinding
import my.com.abibasInven.databinding.FragmentUserChgPicBinding
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*


class StockInFragment : Fragment() {

    private lateinit var binding: FragmentStockInBinding
    private val nav by lazy {findNavController()}
    private val vm : ProductViewModel by activityViewModels()
    private val vm2 : StockInViewModel by activityViewModels()
    private val locationvm : LocationViewModel by activityViewModels()


    //spnProduct
    private val vmSpn : SpinnerViewModel by activityViewModels()

    private val productID by lazy { requireArguments().getString("productID","N/A") }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentStockInBinding.inflate(inflater, container, false)



        binding.btnBackForLocationEditing3.setOnClickListener { nav.navigate(R.id.action_stockInFragment_to_stockInSelectMethodFragment) }
        //binding.spnProduct.selectedItem.toString()


        //spnProduct
        val spnProduct = vmSpn.getProduct()
        val spnArray2 = arrayListOf<String>()

        //spnLocation
        val spnLocation = vmSpn.getLocation()
        val spnArray3 = arrayListOf<String>()


        val adp3 = ArrayAdapter<String>(requireContext(),android.R.layout.simple_spinner_item)
        adp3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spnStockInProduct.adapter = adp3

        val adp4 = ArrayAdapter<String>(requireContext(),android.R.layout.simple_spinner_item)
        adp4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spnStockInLocation.adapter = adp4


        //spnProduct
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

        //spnLocation
        spnLocation.observe(viewLifecycleOwner) { list ->
            //list.groupBy { it.ID}
            val num = list.size
            val locationSize = vmSpn.calLocSize()
            if (list.size > spnArray2.size) {
                for (i in 0..locationSize - 1) {
                    if(list[i].occupiedCapacity!=list[i].maxCapacity){
                        adp4.add(list[i].ID)//change here to get value
                        spnArray3.add(list[i].ID) //change here to get value
                    }
                }
            } else if (num <= spnArray2.size ){
                spnArray3.clear()
                adp3.clear()
                for (i in 0..locationSize - 1) {
                    if(list[i].occupiedCapacity!=list[i].maxCapacity) {
                        adp4.add(list[i].ID)
                        spnArray3.add(list[i].ID)
                    }
                }
            }
        }

        if(productID!="N/A"){
            binding.lblCurrentProductID.text = productID
            binding.spnStockInProduct.isVisible = false
            binding.btnStockIn.setOnClickListener { updateStockQty(binding.lblCurrentProductID.text.toString()) }
        }
        else{
            binding.lblCurrentProductID.isVisible = false
            binding.btnStockIn.setOnClickListener { updateStockQty(binding.spnStockInProduct.selectedItem.toString()) }
        }

        binding.btnCloseStockIn.setOnClickListener { nav.navigate(R.id.action_stockInFragment_to_stockInSelectMethodFragment) }


        return binding.root
    }

    private fun updateStockQty(rackProductID : String) {

        val foundLocationData = locationvm.get(binding.spnStockInLocation.selectedItem.toString())




        if(binding.edtRackProductQuantity.text.toString() == ""){
            errorDialog("product quantity cannot be 0")
        }
        else if(foundLocationData!=null){
            val foundProductData = vm.get(binding.spnStockInProduct.selectedItem.toString())
            if(foundProductData!=null){
                if(foundLocationData.categoryID != foundProductData.categoryID){
                    errorDialog("Product (${foundProductData.ID}) category is ${foundProductData.categoryID}, but Location category is ${foundLocationData.categoryID}")
                }
                if(binding.edtRackProductQuantity.text.toString().toInt() > foundLocationData.maxCapacity){
                    errorDialog("Location only left (${foundLocationData.maxCapacity}) storage quantity, please lower product quantity to stock in")
                }
            }
        }
        else{
            val currentDateTime = LocalDateTime.now()
            val dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")

            val foundProductData = vm.get(rackProductID)
            if(foundProductData!=null){
                val updateProductQty = Product(
                    ID = foundProductData.ID,
                    name = foundProductData.name,
                    qty = foundProductData.qty+binding.edtRackProductQuantity.text.toString().toInt(),
                    qtyThreshold =  foundProductData.qtyThreshold,
                    categoryID = foundProductData.categoryID,
                    photo = foundProductData.photo,
                    locationID = foundProductData.locationID,
                    supplierID = foundProductData.supplierID,
                )
                vm.set(updateProductQty)
            }
            //id-generator
            val id = "SI" + (vm2.calStockInSize() + 1).toString()
            val stockInID = vm2.validID(id)
            val s = StockIn(
                ID  = stockInID,
                productID   = rackProductID,
                qty       = binding.edtRackProductQuantity.text.toString().toInt(),
                dateTime = dtf.format(currentDateTime).toString(),
            )
            vm2.set(s)

            val foundLocationData = locationvm.get(binding.spnStockInLocation.selectedItem.toString())

            if(foundLocationData!=null){
                val updateLocation = Location(
                    ID = foundLocationData.ID,
                    rackType = foundLocationData.rackType,
                    categoryID = foundLocationData.categoryID,
                    occupiedCapacity = foundLocationData.occupiedCapacity+binding.edtRackProductQuantity.text.toString().toInt(),
                    maxCapacity = foundLocationData.maxCapacity,
                )
            }
            snackbar("product has been stocked in ")
        }


    }


}