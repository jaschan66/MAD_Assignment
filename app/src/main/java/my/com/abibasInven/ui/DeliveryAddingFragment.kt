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
import my.com.abibasInven.databinding.FragmentDeliveryAddingBinding
import my.com.abibasInven.databinding.FragmentStockInBinding


class DeliveryAddingFragment : Fragment() {

    private lateinit var binding: FragmentDeliveryAddingBinding
    private val nav by lazy {findNavController()}

    private val deliveryvm : DeliveryViewModel by activityViewModels()
    private val deliveryItemvm : DeliveryItemViewModel by activityViewModels()

    //spnProduct
    private val vmSpn : SpinnerViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentDeliveryAddingBinding.inflate(inflater, container, false)


        binding.btnBackToPrevious.setOnClickListener { nav.navigateUp() }

        //spnCategory
        val spnProduct = vmSpn.getProduct()
        val spnArray2 = arrayListOf<String>()


        val adp3 = ArrayAdapter<String>(requireContext(),android.R.layout.simple_spinner_item)
        adp3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spnDeliveryProduct.adapter = adp3

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

        binding.btnAddDelivery.setOnClickListener { addDelivery() }


        return binding.root
    }

    private fun addDelivery() {


        // id generator for delivery item
        val id1 = "DV"+ (deliveryvm.calDeliverySize() + 1).toString()
        val deliveryID = deliveryvm.validID(id1)

        // id generator for delivery item
        val id2 = "DVI"+ (deliveryItemvm.calDeliveryItemSize() + 1).toString()
        val deliveryItemID = deliveryItemvm.validID(id2)


        val newDeliveryItem = DeliveryItem(
            ID = deliveryItemID,
            productID = binding.spnDeliveryProduct.selectedItem.toString(),
            deliveryQty = binding.edtDeliveryQty.text.toString().toInt(),
            deliveryID =  ""
        )
        deliveryItemvm.set(newDeliveryItem)


    }


}