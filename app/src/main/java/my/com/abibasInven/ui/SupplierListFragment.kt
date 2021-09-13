package my.com.abibasInven.ui

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.logindemo.util.snackbar
import com.google.firebase.auth.FirebaseAuth
import my.com.abibasInven.R
import my.com.abibasInven.data.SupplierViewModel
import my.com.abibasInven.databinding.FragmentStaffListBinding
import my.com.abibasInven.databinding.FragmentSupplierAddBinding
import my.com.abibasInven.databinding.FragmentSupplierListBinding
import my.com.abibasInven.util.StaffAdapter
import my.com.abibasInven.util.SupplierAdapter

class SupplierListFragment : Fragment() {

    private lateinit var binding: FragmentSupplierListBinding
    private val nav by lazy { findNavController() }
    private val vm: SupplierViewModel by activityViewModels()


    private lateinit var adapter: SupplierAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentSupplierListBinding.inflate(inflater, container, false)

        binding.btnAddSupplier.setOnClickListener { nav.navigate(R.id.supplierAddFragment) }

        adapter = SupplierAdapter() { holder, user ->

            //Haven't do
//            holder.root.setOnClickListener {
//                nav.navigate(R.id.staffDetailsFragment, bundleOf("suppId" to user.ID))
//            }
            holder.btnUpdate.setOnClickListener {
                nav.navigate(R.id.supplierUpdateFragment, bundleOf("suppId" to user.ID))
            }
            holder.btnDelete.setOnClickListener {
                val builder = AlertDialog.Builder(context)
                builder.setMessage("Are you sure you want to Delete?")
                    .setCancelable(false)
                    .setPositiveButton("Yes") { dialog, id ->

                        snackbar("Supplier deleted successfully")
                        deleteSupplier(user.ID)
                    }
                    .setNegativeButton("No") { dialog, id ->
                        // Dismiss the dialog
                        dialog.dismiss()
                    }
                val alert = builder.create()
                alert.show()
            }
        }

        binding.supplierRv.adapter = adapter
        binding.supplierRv.addItemDecoration(
            DividerItemDecoration(
                context,
                DividerItemDecoration.VERTICAL
            )
        )

        vm.getAllSupplier().observe(viewLifecycleOwner) {
            adapter.submitList(it)
            binding.lblSupplierCount.text = "${it.size} Supplier(s)"
        }

        return binding.root
    }

    private fun deleteSupplier(email: String) {
        vm.remove(email)
    }
}