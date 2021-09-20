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
import androidx.recyclerview.widget.DividerItemDecoration
import my.com.abibasInven.R
import my.com.abibasInven.data.*
import my.com.abibasInven.databinding.FragmentStockInAndStockOutProductSummaryBinding
import my.com.abibasInven.databinding.FragmentStockOutSummaryBinding
import my.com.abibasInven.util.StockInSummaryAdapter
import my.com.abibasInven.util.StockOutSummaryAdapter


class StockInAndStockOutProductSummaryFragment : Fragment() {

    private lateinit var binding: FragmentStockInAndStockOutProductSummaryBinding
    private val nav by lazy {findNavController()}

    private lateinit var  stockInAdapter: StockInSummaryAdapter
    private lateinit var  stockOutAdapter: StockOutSummaryAdapter

    private val productvm : ProductViewModel by activityViewModels()
    private val stockinvm : StockInViewModel by activityViewModels()
    private val stockoutvm : StockOutViewModel by activityViewModels()
    private val deliveryitemvm : DeliveryItemViewModel by activityViewModels()
    private val vmSpn : SpinnerViewModel by activityViewModels()







    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentStockInAndStockOutProductSummaryBinding.inflate(inflater, container, false)

        // Inflate the layout for this fragment




        //spnProduct
        val spnProduct = vmSpn.getProduct()
        val spnArray2 = arrayListOf<String>()


        val adp3 = ArrayAdapter<String>(requireContext(),android.R.layout.simple_spinner_item)
        adp3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spnStockSummaryProductID.adapter = adp3

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







        binding.btnStockSummaryStockIn.setOnClickListener { displayStockInByProductID() }
        binding.btnStockSummaryStockOut.setOnClickListener { displayStockOutByProductID() }

        binding.btnCloseStockSummary.setOnClickListener { nav.navigate(R.id.action_stockInAndStockOutProductSummaryFragment_to_stockMainReportFragment) }
        binding.btnCloseStockSummary2.setOnClickListener { nav.navigate(R.id.action_stockInAndStockOutProductSummaryFragment_to_stockMainReportFragment) }



        return binding.root
    }

    private fun displayStockOutByProductID() {

        val foundProductData =  productvm.get(binding.spnStockSummaryProductID.selectedItem.toString())

        if(foundProductData!=null){
            binding.lblStockSummaryCurrentProductName.text = foundProductData.name
        }

        binding.lblStockSummaryTitle.text = "Stock Out Summary"

        binding.rvStockSummaryStockIn.isVisible = false
        binding.rvStockSummaryStockOut.isVisible = true

        binding.lblStockSummaryStockInCount.isVisible = false
        binding.lblStockSummaryStockOutCount.isVisible = true

        binding.btnCloseStockSummary.isVisible = false
        binding.btnCloseStockSummary2.isVisible = true

        stockOutAdapter = StockOutSummaryAdapter()

        binding.rvStockSummaryStockOut.adapter = stockOutAdapter
        binding.rvStockSummaryStockOut.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))

            stockoutvm.getAll().observe(viewLifecycleOwner){ list->

                val array = list.filter { it.productID == binding.spnStockSummaryProductID.selectedItem.toString()}

            stockOutAdapter.submitList(array)
            binding.lblStockSummaryStockOutCount.text = "${array.size} record(s)"

            }

    }

    private fun displayStockInByProductID() {
        val foundProductData =  productvm.get(binding.spnStockSummaryProductID.selectedItem.toString())

        if(foundProductData!=null){
            binding.lblStockSummaryCurrentProductName.text = foundProductData.name
        }

        binding.lblStockSummaryTitle.text = "Stock In Summary"

        binding.rvStockSummaryStockIn.isVisible = true
        binding.rvStockSummaryStockOut.isVisible = false

        binding.lblStockSummaryStockInCount.isVisible = true
        binding.lblStockSummaryStockOutCount.isVisible = false

        binding.btnCloseStockSummary.isVisible = true
        binding.btnCloseStockSummary2.isVisible = false

        stockInAdapter = StockInSummaryAdapter()

        binding.rvStockSummaryStockIn.adapter = stockInAdapter
        binding.rvStockSummaryStockIn.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))



        stockinvm.getAll().observe(viewLifecycleOwner){ list->

            val array = list.filter { it.productID == binding.spnStockSummaryProductID.selectedItem.toString() }
            stockInAdapter.submitList(array)
            binding.lblStockSummaryStockInCount.text = "${array.size} record(s)"

        }



    }


}