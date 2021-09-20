package my.com.abibasInven.ui

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
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
import com.example.logindemo.util.errorDialog
import com.example.logindemo.util.snackbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.zxing.integration.android.IntentIntegrator
import my.com.abibasInven.R
import my.com.abibasInven.data.ProductViewModel
import my.com.abibasInven.data.SupplierViewModel
import my.com.abibasInven.databinding.FragmentSupplierListBinding
import my.com.abibasInven.util.SupplierAdapter

class SupplierListFragment : Fragment() {

    private lateinit var binding: FragmentSupplierListBinding
    private val nav by lazy { findNavController() }
    private val vm: SupplierViewModel by activityViewModels()
    private val vmProduct: ProductViewModel by activityViewModels()

    private lateinit var adapter: SupplierAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        vmProduct.getAll()
        vm.searchSupplier("")

        binding = FragmentSupplierListBinding.inflate(inflater, container, false)

        binding.btnAddSupplier.setOnClickListener { nav.navigate(R.id.supplierAddFragment) }

        val bottomNav : BottomNavigationView = requireActivity().findViewById(R.id.bottomNavigationView)
        bottomNav.visibility = View.VISIBLE

        binding.SupplierSearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(name: String) = true
            override fun onQueryTextChange(name: String): Boolean {
                vm.searchSupplier(name)
                return true
            }
        })

        binding.btnScanSupplierQR.setOnClickListener {
            val scanner = IntentIntegrator.forSupportFragment(this)
            scanner.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
            scanner.initiateScan()
        }

        adapter = SupplierAdapter() { holder, user ->

            holder.root.setOnClickListener {
                nav.navigate(R.id.supplierDetailsFragment, bundleOf("suppId" to user.ID))
            }
            holder.btnUpdate.setOnClickListener {
                nav.navigate(R.id.supplierUpdateFragment, bundleOf("suppId" to user.ID))
            }
            holder.btnDelete.setOnClickListener {
                val builder = AlertDialog.Builder(context)
                builder.setMessage("Are you sure you want to Delete?")
                    .setCancelable(false)
                    .setPositiveButton("Yes") { _, _ ->

                        snackbar("Supplier deleted successfully")
                        deleteSupplier(user.ID)
                    }
                    .setNegativeButton("No") { dialog, _ ->
                        // Dismiss the dialog
                        dialog.dismiss()
                    }
                val alert = builder.create()
                alert.show()
            }
        }

        binding.supplierRv.adapter = adapter

        vm.getAllSupplier().observe(viewLifecycleOwner) {
            adapter.submitList(it)
            binding.lblSupplierCount.text = "${it.size} Supplier(s)"
        }

        vm.getResult().observe(viewLifecycleOwner) {
            adapter.submitList(it)
            binding.lblSupplierCount.text = "${it.size} Supplier(s)"
        }

        return binding.root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
            if (result != null) {
                if (result.contents == null) {
                    errorDialog("Result not found")
                } else {
                    nav.navigate(
                        R.id.supplierDetailsFragment,
                        bundleOf("productId" to result.contents)
                    )
                    snackbar("Result found")
                }
            } else {
                super.onActivityResult(requestCode, resultCode, data)
            }
        }
    }

    private fun deleteSupplier(email: String) {
        vm.remove(email)
    }
}