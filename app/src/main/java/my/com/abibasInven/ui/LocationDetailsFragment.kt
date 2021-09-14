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
import my.com.abibasInven.data.ProductViewModel
import my.com.abibasInven.data.SupplierViewModel
import my.com.abibasInven.databinding.FragmentHomeBinding
import my.com.abibasInven.databinding.FragmentLocationDetailsBinding
import my.com.abibasInven.util.LocationDetailsAdapter
import my.com.abibasInven.util.SupplierAdapter


class LocationDetailsFragment : Fragment() {

    private lateinit var binding: FragmentLocationDetailsBinding
    private val nav by lazy {findNavController()}
    private lateinit var adapter: LocationDetailsAdapter
    private val vm: ProductViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = FragmentLocationDetailsBinding.inflate(inflater, container, false)

        // TODO

        adapter = LocationDetailsAdapter() { holder, user ->


        }
        binding.rvLocationDetails.adapter = adapter
        binding.rvLocationDetails.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))


        vm.getAll().observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }


        binding.btnBackForLocationEditing2.setOnClickListener { nav.navigateUp() }
        //bottom navigation

        binding.bottomNavigationView.selectedItemId = R.id.home

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