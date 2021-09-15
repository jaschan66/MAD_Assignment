package my.com.abibasInven.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import my.com.abibasInven.R
import my.com.abibasInven.data.ProductViewModel
import my.com.abibasInven.data.StockInViewModel
import my.com.abibasInven.databinding.FragmentDeliveryAddingBinding
import my.com.abibasInven.databinding.FragmentStockInBinding


class DeliveryAddingFragment : Fragment() {

    private lateinit var binding: FragmentDeliveryAddingBinding
    private val nav by lazy {findNavController()}
    private val vm : ProductViewModel by activityViewModels()
    private val svm : StockInViewModel by activityViewModels()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_delivery_adding, container, false)
    }



}