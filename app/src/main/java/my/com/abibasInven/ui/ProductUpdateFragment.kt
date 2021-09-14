package my.com.abibasInven.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import my.com.abibasInven.R
import my.com.abibasInven.databinding.FragmentHomeBinding
import my.com.abibasInven.databinding.FragmentProductUpdateBinding


class ProductUpdateFragment : Fragment() {

    private lateinit var binding: FragmentProductUpdateBinding
    private val nav by lazy {findNavController()}
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = FragmentProductUpdateBinding.inflate(inflater, container, false)



        return binding.root

    }


}