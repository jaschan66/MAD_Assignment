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
import my.com.abibasInven.data.UserViewModel
import my.com.abibasInven.databinding.FragmentLocationAddingBinding
import my.com.abibasInven.databinding.FragmentStaffListBinding
import my.com.abibasInven.util.LocationAddAdapter
import my.com.abibasInven.util.StaffAdapter


class LocationAddingFragment : Fragment() {


    private lateinit var binding: FragmentLocationAddingBinding
    private val nav by lazy {findNavController()}
    private val vm: UserViewModel by activityViewModels()

    private lateinit var adapter: LocationAddAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = FragmentLocationAddingBinding.inflate(inflater, container, false)

        // TODO

        binding.btnAddLocation.setOnClickListener { addLocation() }

        adapter = LocationAddAdapter() { holder, user ->





        }
        binding.rvLocationAdd.adapter = adapter
        binding.rvLocationAdd.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))

        return binding.root
    }

    private fun addLocation() {

    }



}