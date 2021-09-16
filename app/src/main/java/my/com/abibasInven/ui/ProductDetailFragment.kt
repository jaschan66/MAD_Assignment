package my.com.abibasInven.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import my.com.abibasInven.R
import my.com.abibasInven.databinding.FragmentHomeBinding
import my.com.abibasInven.databinding.FragmentProductDetailBinding


class ProductDetailFragment : Fragment() {

    private lateinit var binding: FragmentProductDetailBinding
    private val nav by lazy {findNavController()}
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = FragmentProductDetailBinding.inflate(inflater, container, false)

        // TODO

        val bottomNav : BottomNavigationView = requireActivity().findViewById(R.id.bottomNavigationView)
        bottomNav.visibility = View.GONE


        return binding.root
    }

}