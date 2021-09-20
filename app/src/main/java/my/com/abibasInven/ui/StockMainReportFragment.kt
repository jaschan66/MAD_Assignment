package my.com.abibasInven.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import my.com.abibasInven.R
import my.com.abibasInven.databinding.FragmentStockMainReportBinding


class StockMainReportFragment : Fragment() {

    private lateinit var binding: FragmentStockMainReportBinding


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentStockMainReportBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }


}