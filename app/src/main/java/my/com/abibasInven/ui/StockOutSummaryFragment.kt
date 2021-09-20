package my.com.abibasInven.ui

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import my.com.abibasInven.R
import my.com.abibasInven.data.StockInViewModel
import my.com.abibasInven.data.StockOutViewModel
import my.com.abibasInven.databinding.FragmentStockInSummaryBinding
import my.com.abibasInven.databinding.FragmentStockOutSummaryBinding
import my.com.abibasInven.util.StockInSummaryAdapter
import my.com.abibasInven.util.StockOutSummaryAdapter
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class StockOutSummaryFragment : Fragment() {

    private lateinit var binding: FragmentStockOutSummaryBinding
    private val nav by lazy {findNavController()}
    private lateinit var  adapter: StockOutSummaryAdapter
    private val stockoutvm : StockOutViewModel by activityViewModels()
    private val stockinvm : StockInViewModel by activityViewModels()




    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentStockOutSummaryBinding.inflate(inflater, container, false)

        // Inflate the layout for this fragment

        adapter = StockOutSummaryAdapter()
        binding.rvStockOutSummary.adapter = adapter
        binding.rvStockOutSummary.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))

        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")

        val currentDateTime = LocalDateTime.now()


            stockoutvm.getAll().observe(viewLifecycleOwner){ list->

                adapter.submitList(list)
                binding.lblStockOutSummaryCount.text = "${list.size} record(s)"

            }



        binding.btnStockOutSummaryAll.setOnClickListener { generateAllStockOut(binding.btnStockOutSummaryAll) }
        binding.btnStockOutSummaryDaily.setOnClickListener { generateStockOutDaily(binding.btnStockOutSummaryDaily) }
        binding.btnStockOutSummaryWeekly.setOnClickListener { generateStockOutWeekly(binding.btnStockOutSummaryWeekly) }
        binding.btnStockOutSummaryMonthly.setOnClickListener { generateStockOutMonthly(binding.btnStockOutSummaryMonthly) }






        binding.btnCloseStouckOutSummary.setOnClickListener { nav.navigate(R.id.action_stockOutSummaryFragment_to_stockMainReportFragment) }


        return binding.root
    }

    private fun btnChangeColor(button : Button) {
        when(button){
            binding.btnStockOutSummaryAll ->
            {
                binding.btnStockOutSummaryAll.setBackgroundColor(Color.rgb(247, 177, 106))
                binding.btnStockOutSummaryAll.setTextColor(Color.BLACK)
                binding.btnStockOutSummaryDaily.setBackgroundColor(Color.BLACK)
                binding.btnStockOutSummaryDaily.setTextColor(Color.WHITE)
                binding.btnStockOutSummaryWeekly.setBackgroundColor(Color.BLACK)
                binding.btnStockOutSummaryWeekly.setTextColor(Color.WHITE)
                binding.btnStockOutSummaryMonthly.setBackgroundColor(Color.BLACK)
                binding.btnStockOutSummaryMonthly.setTextColor(Color.WHITE)

            }
            binding.btnStockOutSummaryDaily ->
            {
                binding.btnStockOutSummaryDaily.setBackgroundColor(Color.rgb(247, 177, 106))
                binding.btnStockOutSummaryDaily.setTextColor(Color.BLACK)
                binding.btnStockOutSummaryAll.setBackgroundColor(Color.BLACK)
                binding.btnStockOutSummaryAll.setTextColor(Color.WHITE)
                binding.btnStockOutSummaryWeekly.setBackgroundColor(Color.BLACK)
                binding.btnStockOutSummaryWeekly.setTextColor(Color.WHITE)
                binding.btnStockOutSummaryMonthly.setBackgroundColor(Color.BLACK)
                binding.btnStockOutSummaryMonthly.setTextColor(Color.WHITE)

            }
            binding.btnStockOutSummaryWeekly ->
            {
                binding.btnStockOutSummaryWeekly.setBackgroundColor(Color.rgb(247, 177, 106))
                binding.btnStockOutSummaryWeekly.setTextColor(Color.BLACK)
                binding.btnStockOutSummaryAll.setBackgroundColor(Color.BLACK)
                binding.btnStockOutSummaryAll.setTextColor(Color.WHITE)
                binding.btnStockOutSummaryDaily.setBackgroundColor(Color.BLACK)
                binding.btnStockOutSummaryDaily.setTextColor(Color.WHITE)
                binding.btnStockOutSummaryMonthly.setBackgroundColor(Color.BLACK)
                binding.btnStockOutSummaryMonthly.setTextColor(Color.WHITE)

            }
            binding.btnStockOutSummaryMonthly ->
            {
                binding.btnStockOutSummaryMonthly.setBackgroundColor(Color.rgb(247, 177, 106))
                binding.btnStockOutSummaryMonthly.setTextColor(Color.BLACK)
                binding.btnStockOutSummaryAll.setBackgroundColor(Color.BLACK)
                binding.btnStockOutSummaryAll.setTextColor(Color.WHITE)
                binding.btnStockOutSummaryDaily.setBackgroundColor(Color.BLACK)
                binding.btnStockOutSummaryDaily.setTextColor(Color.WHITE)
                binding.btnStockOutSummaryWeekly.setBackgroundColor(Color.BLACK)
                binding.btnStockOutSummaryWeekly.setTextColor(Color.WHITE)
            }


        }
    }

    private fun generateAllStockOut(button : Button) {
        btnChangeColor(button)
        stockoutvm.getAll().observe(viewLifecycleOwner){ list->

            adapter.submitList(list)
            binding.lblStockOutSummaryCount.text = "${list.size} record(s)"

        }
    }


    private fun generateStockOutMonthly(button : Button) {
        btnChangeColor(button)
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")

        val currentDateTime = LocalDateTime.now()

        stockoutvm.getAll().observe(viewLifecycleOwner){ list->

            adapter.submitList(list.filter { LocalDateTime.parse(it.dateTime,formatter).month.value == currentDateTime.month.value })
            binding.lblStockOutSummaryCount.text = "${list.size} record(s)"
        }
    }

    private fun generateStockOutWeekly(button : Button) {
        btnChangeColor(button)
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")

        val currentDateTime = LocalDateTime.now()

        stockoutvm.getAll().observe(viewLifecycleOwner){ list->

            adapter.submitList(list.filter { LocalDateTime.parse(it.dateTime,formatter).dayOfMonth in currentDateTime.dayOfMonth.minus(7) .. currentDateTime.dayOfMonth  })
            binding.lblStockOutSummaryCount.text = "${list.size} record(s)"
        }

    }

    private fun generateStockOutDaily(button : Button) {
        btnChangeColor(button)

        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")

        val currentDateTime = LocalDateTime.now()

        stockoutvm.getAll().observe(viewLifecycleOwner){ list->


            adapter.submitList(list.filter { LocalDateTime.parse(it.dateTime,formatter).dayOfMonth == currentDateTime.dayOfMonth })
            binding.lblStockOutSummaryCount.text = "${list.size} record(s)"
        }
    }


}