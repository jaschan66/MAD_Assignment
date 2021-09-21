package my.com.abibasInven.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.logindemo.util.informationDialog
import com.google.android.material.bottomnavigation.BottomNavigationView
import my.com.abibasInven.R
import my.com.abibasInven.data.*
import my.com.abibasInven.databinding.FragmentStockInAndStockOutProductSummaryBinding
import my.com.abibasInven.databinding.FragmentStockOutletSummaryBinding
import my.com.abibasInven.util.StockInSummaryAdapter
import my.com.abibasInven.util.StockOutSummaryAdapter


class StockOutletSummaryFragment : Fragment() {

    private lateinit var binding: FragmentStockOutletSummaryBinding
    private val nav by lazy {findNavController()}

    private lateinit var  stockOutAdapter: StockOutSummaryAdapter

    private val productvm : ProductViewModel by activityViewModels()
    private val stockinvm : StockInViewModel by activityViewModels()
    private val stockoutvm : StockOutViewModel by activityViewModels()
    private val deliveryitemvm : DeliveryItemViewModel by activityViewModels()
    private val outletvm : OutletViewModel by activityViewModels()
    private val deliveryvm : DeliveryViewModel by activityViewModels()



    private val vmSpn : SpinnerViewModel by activityViewModels()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Enable bottom navigation menu
        val bottomNav : BottomNavigationView = requireActivity().findViewById(R.id.bottomNavigationView)
        bottomNav.visibility = View.GONE

        binding = FragmentStockOutletSummaryBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment

        //spnDelivery
        val spnOutlet = vmSpn.getDelivery()
        val spnArray2 = arrayListOf<String>()

        val adp3 = ArrayAdapter<String>(requireContext(),android.R.layout.simple_spinner_item)
        adp3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spnStockOutletSummaryOutletID.adapter = adp3

        spnOutlet.observe(viewLifecycleOwner) { list ->
            //list.groupBy { it.ID}
            val num = list.size
            val deliverySize = vmSpn.calDeliverySize()
            if (list.size > spnArray2.size) {
                for (i in 0..deliverySize - 1) {

                    if(list[i].deliveryStatus=="completed"){
                        adp3.add(list[i].ID)//change here to get value
                        spnArray2.add(list[i].ID) //change here to get value
//                        if(spnArray2.size==0){
//                            nav.navigateUp()
//                            informationDialog("there is no delivery is done yet")
//                        }
                    }

                }
            } else if (num <= spnArray2.size ){
                spnArray2.clear()
                adp3.clear()
                for (i in 0..deliverySize - 1) {
                    if(list[i].deliveryStatus=="completed"){
                    adp3.add(list[i].ID)
                    spnArray2.add(list[i].ID)
//                        if(spnArray2.size==0){
//                            nav.navigateUp()
//                            informationDialog("there is no delivery is done yet")
//                        }
                    }
                }
            }
        }

        binding.btnStockOutletSummarySearch.setOnClickListener { displayStockOutletSummary() }
        binding.btnCloseStockOutletSummary.setOnClickListener { nav.navigate(R.id.action_stockOutletSummaryFragment_to_stockMainReportFragment) }

        return binding.root
    }

    private fun displayStockOutletSummary() {
        val foundDeliveryData = deliveryvm.get(binding.spnStockOutletSummaryOutletID.selectedItem.toString())
        if(foundDeliveryData!=null){
            val foundoutletData = outletvm.get(foundDeliveryData.outletID)
            if(foundoutletData!=null){
                binding.lblStockOutletSummaryOutletName.text = foundoutletData.name

            val foundStockOutData = stockoutvm.get(foundDeliveryData.ID)

            stockOutAdapter = StockOutSummaryAdapter()
            binding.rvStockOutletSummaryStockOut.adapter = stockOutAdapter
            binding.rvStockOutletSummaryStockOut.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))


            stockoutvm.getAll().observe(viewLifecycleOwner){ list->

                val array = list .filter { it.outletID == foundDeliveryData.outletID}

                stockOutAdapter.submitList(array)
                binding.lblStockOutletSummaryStockInCount.text = "${array.size} record(s)"

            }
            }

        }
    }


}