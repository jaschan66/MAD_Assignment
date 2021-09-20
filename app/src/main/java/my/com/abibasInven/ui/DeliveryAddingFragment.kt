package my.com.abibasInven.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.logindemo.util.snackbar
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


    //private val currentDeliveryID by lazy { requireArguments().getString("deliveryID","N/A") }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentDeliveryAddingBinding.inflate(inflater, container, false)



        //spnOutlet
        val spnOutlet = vmSpn.getOutlet()
        val spnArray2 = arrayListOf<String>()


        val adp3 = ArrayAdapter<String>(requireContext(),android.R.layout.simple_spinner_item)
        adp3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spnDeliveryOutlet.adapter = adp3

        spnOutlet.observe(viewLifecycleOwner) { list ->
            //list.groupBy { it.ID}
            val num = list.size
            val outletSize = vmSpn.calOutletSize()
            if (list.size > spnArray2.size) {
                for (i in 0..outletSize - 1) {
                    adp3.add(list[i].ID)//change here to get value
                    spnArray2.add(list[i].ID) //change here to get value
                }
            } else if (num <= spnArray2.size ){
                spnArray2.clear()
                adp3.clear()
                for (i in 0..outletSize - 1) {
                    adp3.add(list[i].ID)
                    spnArray2.add(list[i].ID)
                }
            }
        }



        binding.btnAddDelivery.setOnClickListener { addDelivery() }
        binding.btnCancelAddDelivery.setOnClickListener { nav.navigate(R.id.action_deliveryAddingFragment_to_deliveryListingFragment) }

        val id1 = "DV"+ (deliveryvm.calDeliverySize() + 1).toString()
        val deliveryID = deliveryvm.validID(id1)
        binding.lblCurrentDeliveryID.text = deliveryID



        return binding.root
    }

    private fun addDelivery() {


        // id generator for delivery item
        val id1 = "DV"+ (deliveryvm.calDeliverySize() + 1).toString()
        val deliveryID = deliveryvm.validID(id1)


//binding.spnDeliveryOutlet.selectedItem.toString(),
        val newDelivery = Delivery(
            ID = deliveryID,
            outletID = binding.spnDeliveryOutlet.selectedItem.toString(),
            deliveryStatus = "ready",
        )
        deliveryvm.set(newDelivery)
        nav.navigate(R.id.action_deliveryAddingFragment_to_deliveryListingFragment)


    }


}