package my.com.abibasInven.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
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

    //spnProduct
    private val vmSpn : SpinnerViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentStockInBinding.inflate(inflater, container, false)


        binding.btnBackForLocationEditing3.setOnClickListener { nav.navigateUp() }
        //binding.spnProduct.selectedItem.toString()
        binding.btnStockIn.setOnClickListener { updateStockQty("P001") }


        //spnProduct
        val spnProduct = vmSpn.getProduct()
        val spnArray2 = arrayListOf<String>()


        val adp3 = ArrayAdapter<String>(requireContext(),android.R.layout.simple_spinner_item)
        adp3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spnStockInProduct.adapter = adp3


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


        return binding.root
    }

    private fun updateStockQty(rackProductID : String) {
        val currentDateTime = LocalDateTime.now()
        val dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")

//        val p = Product(
//            ID = ""
//
//        )
//        vm.set(p)
        val s = StockIn(
            ID  = "SI001",
            productID   = rackProductID,
            qty       = binding.edtRackProductQuantity.text.toString().toInt(),
            dateTime = dtf.format(currentDateTime).toString(),
        )
        vm2.set(s)
    }


}