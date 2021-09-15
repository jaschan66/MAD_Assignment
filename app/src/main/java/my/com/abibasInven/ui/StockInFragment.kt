package my.com.abibasInven.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
    private val svm : StockInViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentStockInBinding.inflate(inflater, container, false)

        binding.btnBackForLocationEditing3.setOnClickListener { nav.navigateUp() }
        //binding.spnProduct.selectedItem.toString()
        binding.btnStockIn.setOnClickListener { updateStockQty("P001") }



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
            ID  = "S001",
            productID   = rackProductID,
            qty       = binding.edtRackProductQuantity.text.toString().toInt(),
            dateTime = dtf.format(currentDateTime).toString(),
        )
        svm.set(s)
    }


}