package my.com.abibasInven.ui

import android.R.attr
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
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
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.zxing.integration.android.IntentIntegrator
import my.com.abibasInven.R
import my.com.abibasInven.data.ProductViewModel
import my.com.abibasInven.data.SpinnerViewModel
import my.com.abibasInven.data.UserViewModel
import my.com.abibasInven.databinding.FragmentProductBinding
import my.com.abibasInven.util.ProductAdapter
import my.com.abibasInven.util.StaffAdapter
import android.R.attr.data
import android.widget.SearchView

import android.widget.Toast
import com.example.logindemo.util.errorDialog

import com.google.zxing.integration.android.IntentResult





class ProductFragment : Fragment() {

    private lateinit var binding: FragmentProductBinding
    private val nav by lazy {findNavController()}
    private val vm: ProductViewModel by activityViewModels()
    private val vmSpn : SpinnerViewModel by activityViewModels()


    private lateinit var adapter: ProductAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        vm.search("")

        binding = FragmentProductBinding.inflate(inflater, container, false)

        val bottomNav : BottomNavigationView = requireActivity().findViewById(R.id.bottomNavigationView)
        bottomNav.visibility = View.VISIBLE

        // TODO
        vmSpn.getLocation()
        vmSpn.getSupplierName()
        val lessQty = vm.getProductLessQty()
        lessQty.observe(viewLifecycleOwner){
            if(it.isNotEmpty()){
                binding.btnNotification.isEnabled = true
                binding.btnNotification.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_notification_important_24,0,0,0)
            }
            else{
              binding.btnNotification.isEnabled = false
                binding.btnNotification.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_notifications_24,0,0,0)
            }
        }




        binding.productSearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(name: String) = true
            override fun onQueryTextChange(name: String): Boolean {
                vm.search(name)
                return true
            }
        })
        binding.btnNotification.setOnClickListener { nav.navigate(R.id.productNotificationFragment) }

        binding.btnScanQR.setOnClickListener {
         val scanner = IntentIntegrator.forSupportFragment(this)
            scanner.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
            scanner.initiateScan()
        }

        binding.btnProductAdd.setOnClickListener { nav.navigate(R.id.productAddFragment) }

        adapter = ProductAdapter() { holder, product ->

            holder.root.setOnClickListener {
               nav.navigate(R.id.productDetailFragment, bundleOf("ID" to product.ID))
            }
            holder.btnUpdate.setOnClickListener {
                nav.navigate(R.id.productUpdateFragment, bundleOf("ID" to product.ID))
            }
            holder.btnDelete.setOnClickListener {

                val builder = AlertDialog.Builder(context)
                builder.setMessage("Are you sure you want to Delete?")
                    .setCancelable(false)
                    .setPositiveButton("Yes") { dialog, id ->
                        // Delete selected note from database
                                    snackbar("product deleted successfully")
                                    deleteProduct(product.ID)
                    }
                    .setNegativeButton("No") { dialog, id ->
                        // Dismiss the dialog
                        dialog.dismiss()
                    }
                val alert = builder.create()
                alert.show()
            }
        }
        binding.productrv.adapter = adapter
        binding.productrv.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        binding.productrv.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.HORIZONTAL))

        vm.getResult().observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        return binding.root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(resultCode == Activity.RESULT_OK){
            val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
            if (result != null) {
                if (result.contents == null) {
                    errorDialog("Result not found")
                } else {
                    nav.navigate(R.id.productDetailFragment, bundleOf("ID" to result.contents))//Toast.makeText(this, "Scanned: " + result.contents, Toast.LENGTH_LONG).show()
                }
            } else {
                super.onActivityResult(requestCode, resultCode, data)
            }
        }
    }

    private fun deleteProduct(id: String) {
        vm.remove(id)
    }



}