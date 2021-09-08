package my.com.abibasInven.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import my.com.abibasInven.R
import my.com.abibasInven.data.LocationViewModel
import my.com.abibasInven.data.UserViewModel
import my.com.abibasInven.databinding.FragmentHomeBinding
import my.com.abibasInven.databinding.FragmentLocationListingBinding
import my.com.abibasInven.util.LocationAdapter
import my.com.abibasInven.util.StaffAdapter


class LocationListingFragment : Fragment() {

    private lateinit var binding: FragmentLocationListingBinding
    private val nav by lazy {findNavController()}

    private lateinit var adapter: LocationAdapter
    private val vm: LocationViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = FragmentLocationListingBinding.inflate(inflater, container, false)

        // TODO

        adapter = LocationAdapter() { holder, location ->

            holder.root.setOnClickListener {
                //nav.navigate()
            }
            holder.btnDetail.setOnClickListener {
                nav.navigate(R.id.locationDetailsFragment, bundleOf("locationID" to location.ID))
            }

        }
        binding.rvLocation.adapter = adapter
        binding.rvLocation.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))

        vm.getAll().observe(viewLifecycleOwner) {
            adapter.submitList(it)
            binding.lblLocationCount.text = "${it.size} rack(s)"
        }


        return binding.root
    }

}