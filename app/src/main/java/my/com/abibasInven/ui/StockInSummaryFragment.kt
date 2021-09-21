package my.com.abibasInven.ui

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.google.android.material.bottomnavigation.BottomNavigationView
import my.com.abibasInven.R
import my.com.abibasInven.data.StockInViewModel
import my.com.abibasInven.databinding.FragmentStockInSummaryBinding
import my.com.abibasInven.util.StockInSummaryAdapter
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class StockInSummaryFragment : Fragment() {


    private lateinit var binding: FragmentStockInSummaryBinding
    private val nav by lazy {findNavController()}
    private lateinit var  adapter: StockInSummaryAdapter
    private val stockinvm : StockInViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Enable bottom navigation menu
        val bottomNav : BottomNavigationView = requireActivity().findViewById(R.id.bottomNavigationView)
        bottomNav.visibility = View.GONE

        // Inflate the layout for this fragment
        binding = FragmentStockInSummaryBinding.inflate(inflater, container, false)

        adapter = StockInSummaryAdapter()

        binding.rvStockInSummary.adapter = adapter
        binding.rvStockInSummary.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))

        stockinvm.getAll().observe(viewLifecycleOwner){ list->
            adapter.submitList(list)
            binding.lblStockInSummaryCount.text = "${list.size} record(s)"

        }



        binding.btnStockInSummaryAll.setOnClickListener { generateAllStockOut(binding.btnStockInSummaryAll) }
        binding.btnStockInSummaryDaily.setOnClickListener { generateStockOutDaily(binding.btnStockInSummaryDaily) }
        binding.btnStockInSummaryWeekly.setOnClickListener { generateStockOutWeekly(binding.btnStockInSummaryWeekly) }
        binding.btnStockInSummaryMonthly.setOnClickListener { generateStockOutMonthly(binding.btnStockInSummaryMonthly) }









        binding.btnCloseStockInSummary.setOnClickListener { nav.navigate(R.id.action_stockInSummaryFragment_to_stockMainReportFragment) }


        return binding.root
    }

    private fun btnChangeColor(button : Button) {
        when(button){
            binding.btnStockInSummaryAll ->
            {
                binding.btnStockInSummaryAll.setBackgroundColor(Color.rgb(247, 177, 106))
                binding.btnStockInSummaryAll.setTextColor(Color.BLACK)
                binding.btnStockInSummaryDaily.setBackgroundColor(Color.BLACK)
                binding.btnStockInSummaryDaily.setTextColor(Color.WHITE)
                binding.btnStockInSummaryWeekly.setBackgroundColor(Color.BLACK)
                binding.btnStockInSummaryWeekly.setTextColor(Color.WHITE)
                binding.btnStockInSummaryMonthly.setBackgroundColor(Color.BLACK)
                binding.btnStockInSummaryMonthly.setTextColor(Color.WHITE)

            }
            binding.btnStockInSummaryDaily ->
            {
                binding.btnStockInSummaryDaily.setBackgroundColor(Color.rgb(247, 177, 106))
                binding.btnStockInSummaryDaily.setTextColor(Color.BLACK)
                binding.btnStockInSummaryAll.setBackgroundColor(Color.BLACK)
                binding.btnStockInSummaryAll.setTextColor(Color.WHITE)
                binding.btnStockInSummaryWeekly.setBackgroundColor(Color.BLACK)
                binding.btnStockInSummaryWeekly.setTextColor(Color.WHITE)
                binding.btnStockInSummaryMonthly.setBackgroundColor(Color.BLACK)
                binding.btnStockInSummaryMonthly.setTextColor(Color.WHITE)

            }
            binding.btnStockInSummaryWeekly ->
            {
                binding.btnStockInSummaryWeekly.setBackgroundColor(Color.rgb(247, 177, 106))
                binding.btnStockInSummaryWeekly.setTextColor(Color.BLACK)
                binding.btnStockInSummaryAll.setBackgroundColor(Color.BLACK)
                binding.btnStockInSummaryAll.setTextColor(Color.WHITE)
                binding.btnStockInSummaryDaily.setBackgroundColor(Color.BLACK)
                binding.btnStockInSummaryDaily.setTextColor(Color.WHITE)
                binding.btnStockInSummaryMonthly.setBackgroundColor(Color.BLACK)
                binding.btnStockInSummaryMonthly.setTextColor(Color.WHITE)

            }
            binding.btnStockInSummaryMonthly ->
            {
                binding.btnStockInSummaryMonthly.setBackgroundColor(Color.rgb(247, 177, 106))
                binding.btnStockInSummaryMonthly.setTextColor(Color.BLACK)
                binding.btnStockInSummaryAll.setBackgroundColor(Color.BLACK)
                binding.btnStockInSummaryAll.setTextColor(Color.WHITE)
                binding.btnStockInSummaryDaily.setBackgroundColor(Color.BLACK)
                binding.btnStockInSummaryDaily.setTextColor(Color.WHITE)
                binding.btnStockInSummaryWeekly.setBackgroundColor(Color.BLACK)
                binding.btnStockInSummaryWeekly.setTextColor(Color.WHITE)
            }


        }
    }

    private fun generateAllStockOut(button : Button) {
        btnChangeColor(button)
        stockinvm.getAll().observe(viewLifecycleOwner){ list->

            adapter.submitList(list)
            binding.lblStockInSummaryCount.text = "${list.size} record(s)"

        }
    }


    private fun generateStockOutMonthly(button : Button) {
        btnChangeColor(button)
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")

        val currentDateTime = LocalDateTime.now()

        stockinvm.getAll().observe(viewLifecycleOwner){ list->

            val array= list.filter { LocalDateTime.parse(it.dateTime,formatter).month.value == currentDateTime.month.value }
            adapter.submitList(array)
            binding.lblStockInSummaryCount.text = "${array.size} record(s)"
        }
    }

    private fun generateStockOutWeekly(button : Button) {
        btnChangeColor(button)
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")

        val currentDateTime = LocalDateTime.now()

        stockinvm.getAll().observe(viewLifecycleOwner){ list->

            val array = list.filter { LocalDateTime.parse(it.dateTime,formatter).dayOfMonth in currentDateTime.dayOfMonth.minus(7) .. currentDateTime.dayOfMonth  }
            adapter.submitList(array)
            binding.lblStockInSummaryCount.text = "${array.size} record(s)"
        }

    }

    private fun generateStockOutDaily(button : Button) {
        btnChangeColor(button)

        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")

        val currentDateTime = LocalDateTime.now()

        stockinvm.getAll().observe(viewLifecycleOwner){ list->

            val array = list.filter { LocalDateTime.parse(it.dateTime,formatter).dayOfMonth == currentDateTime.dayOfMonth }
            adapter.submitList(array)
            binding.lblStockInSummaryCount.text = "${array.size} record(s)"
        }
    }


}


