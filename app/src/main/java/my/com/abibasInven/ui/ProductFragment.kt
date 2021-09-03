package my.com.abibasInven.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import my.com.abibasInven.R
import my.com.abibasInven.databinding.FragmentAccountBinding
import my.com.abibasInven.databinding.FragmentProductBinding


class ProductFragment : Fragment() {

    private lateinit var binding: FragmentProductBinding
    private val nav by lazy {findNavController()}

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = FragmentProductBinding.inflate(inflater, container, false)

        binding.bottomNavigationView.selectedItemId = R.id.product


        binding.bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId){
                R.id.home -> nav.navigate(R.id.homeFragment)
                R.id.account -> nav.navigate(R.id.accountFragment)
                R.id.product -> nav.navigate(R.id.productFragment)

            }
            true
        }


        return binding.root
    }


}