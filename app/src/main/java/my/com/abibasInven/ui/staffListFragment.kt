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
import my.com.abibasInven.databinding.FragmentHomeBinding
import my.com.abibasInven.databinding.FragmentStaffListBinding
import my.com.abibasInven.util.StaffAdapter


class staffListFragment : Fragment() {

    private lateinit var binding: FragmentStaffListBinding
    private val nav by lazy {findNavController()}
    private val vm: UserViewModel by activityViewModels()

    private lateinit var adapter: StaffAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = FragmentStaffListBinding.inflate(inflater, container, false)

        // TODO

        binding.btnAddStaff.setOnClickListener { nav.navigate(R.id.registerFragment) }

        adapter = StaffAdapter() { holder, user ->

            holder.root.setOnClickListener {
                //nav.navigate()
            }
            holder.btnUpdate.setOnClickListener {
                nav.navigate(R.id.updateStaffFragment, bundleOf("email" to user.email))
            }
            holder.btnDelete.setOnClickListener { deleteStaff(user.email) }

        }
        binding.staffrv.adapter = adapter
        binding.staffrv.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))

        vm.getAllStaff().observe(viewLifecycleOwner) {
            adapter.submitList(it)
            binding.lblResCount.text = "${it.size} staff(s)"
        }

        return binding.root
    }

    private fun deleteStaff(email: String) {
        vm.remove(email)
    }



}