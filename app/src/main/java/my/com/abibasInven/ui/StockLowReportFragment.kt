package my.com.abibasInven.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import my.com.abibasInven.R
import my.com.abibasInven.data.DeliveryViewModel
import my.com.abibasInven.data.ProductViewModel
import my.com.abibasInven.databinding.FragmentDeliveryListingBinding
import my.com.abibasInven.databinding.FragmentStockLowReportBinding
import my.com.abibasInven.util.DeliveryAdapter
import my.com.abibasInven.util.StockLowReportAdapter


class StockLowReportFragment : Fragment() {

    private lateinit var binding: FragmentStockLowReportBinding
    private val nav by lazy {findNavController()}
    private val productvm : ProductViewModel by activityViewModels()

    private lateinit var  adapter: StockLowReportAdapter



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentStockLowReportBinding.inflate(inflater, container, false)

        adapter = StockLowReportAdapter()

        binding.rvStockSummaryLowReport.adapter = adapter
        binding.rvStockSummaryLowReport.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))

        productvm.getAll().observe(viewLifecycleOwner){ list->

            val array = list.filter { it.qty < it.qtyThreshold }
            adapter.submitList(array)
            binding.lblStockSummaryLowReportCount.text = "${array.size} delivery(s)"

        }

        binding.btnCloseStockSummaryLowReport.setOnClickListener { nav.navigate(R.id.action_stockLowReportFragment_to_stockMainReportFragment) }

        return binding.root
    }


}