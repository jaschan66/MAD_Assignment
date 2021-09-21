package my.com.abibasInven.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import my.com.abibasInven.R
import my.com.abibasInven.databinding.FragmentStockMainReportBinding


class StockMainReportFragment : Fragment() {

    private lateinit var binding: FragmentStockMainReportBinding
    private val nav by lazy {findNavController()}


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        // Enable bottom navigation menu
        val bottomNav : BottomNavigationView = requireActivity().findViewById(R.id.bottomNavigationView)
        bottomNav.visibility = View.VISIBLE

        binding = FragmentStockMainReportBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment

        with(binding){
            btnStockMainReport1.setOnClickListener { nav.navigate(R.id.stockInSummaryFragment) }
            btnStockMainReport2.setOnClickListener { nav.navigate(R.id.stockOutSummaryFragment) }
            btnStockMainReport3.setOnClickListener { nav.navigate(R.id.stockInAndStockOutProductSummaryFragment) }
            btnStockMainReport4.setOnClickListener { nav.navigate(R.id.stockOutletSummaryFragment) }
            btnStockMainReport5.setOnClickListener { nav.navigate(R.id.stockLowReportFragment) }
        }
        return binding.root
    }


}