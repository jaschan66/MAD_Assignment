package my.com.abibasInven.ui

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.logindemo.util.snackbar
import my.com.abibasInven.R
import my.com.abibasInven.data.OutletViewModel
import my.com.abibasInven.databinding.FragmentOutletListBinding
import my.com.abibasInven.util.OutletAdapter

class OutletListFragment : Fragment() {

    private lateinit var binding: FragmentOutletListBinding
    private val nav by lazy { findNavController() }
    private val vm: OutletViewModel by activityViewModels()

    private lateinit var adapter: OutletAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        vm.searchOutlet("")

        binding = FragmentOutletListBinding.inflate(inflater, container, false)

        binding.btnOutletAdd.setOnClickListener { nav.navigate(R.id.outletAddFragment) }

        binding.OutletSearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(name: String) = true
            override fun onQueryTextChange(name: String): Boolean {
                vm.searchOutlet(name)
                return true
            }
        })

        adapter = OutletAdapter() { holder, outlet ->

            holder.root.setOnClickListener {
                nav.navigate(
                    R.id.outletDetailsFragment,
                    bundleOf("outletId" to outlet.ID, "outletName" to outlet.name)
                )
            }
            holder.btnUpdate.setOnClickListener {
                nav.navigate(R.id.outletUpdateFragment, bundleOf("outletId" to outlet.ID))
            }
            holder.btnDelete.setOnClickListener {
                val builder = AlertDialog.Builder(context)
                builder.setMessage("Are you sure you want to Delete?")
                    .setCancelable(false)
                    .setPositiveButton("Yes") { _, _ ->
                        snackbar("Outlet deleted successfully")
                        deleteOutlet(outlet.ID)
                    }
                    .setNegativeButton("No") { dialog, _ ->
                        // Dismiss the dialog
                        dialog.dismiss()
                    }
                val alert = builder.create()
                alert.show()
            }
        }
        binding.outletRv.adapter = adapter

        vm.getAllOutlet().observe(viewLifecycleOwner) {
            adapter.submitList(it)
            binding.lblOutletCount.text = "${it.size} Outlet(s)"
        }

        vm.getResult().observe(viewLifecycleOwner) {
            adapter.submitList(it)
            binding.lblOutletCount.text = "${it.size} Outlet(s)"
        }

        return binding.root
    }

    private fun deleteOutlet(id: String) {
        vm.remove(id)
    }
}