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
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import my.com.abibasInven.R
import my.com.abibasInven.data.ProductViewModel
import my.com.abibasInven.data.SpinnerViewModel
import my.com.abibasInven.data.UserViewModel
import my.com.abibasInven.databinding.FragmentHomeBinding
import my.com.abibasInven.databinding.FragmentProductBinding
import my.com.abibasInven.databinding.FragmentStaffListBinding
import my.com.abibasInven.util.ProductAdapter
import my.com.abibasInven.util.StaffAdapter


class ProductFragment : Fragment() {

    private lateinit var binding: FragmentProductBinding
    private val nav by lazy {findNavController()}
    private val vm: ProductViewModel by activityViewModels()
    private val vmSpn : SpinnerViewModel by activityViewModels()


    private lateinit var adapter: ProductAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {



        binding = FragmentProductBinding.inflate(inflater, container, false)

        // TODO
        vmSpn.getLocation()
        vmSpn.getSupplierName()


       binding.btnProductAdd.setOnClickListener { nav.navigate(R.id.productAddFragment) }

        adapter = ProductAdapter() { holder, product ->

            holder.root.setOnClickListener {
//                nav.navigate(R.id.staffDetailsFragment, bundleOf("product" to product.ID))
            }
            holder.btnUpdate.setOnClickListener {
//                nav.navigate(R.id.updateStaffFragment, bundleOf("product" to product.ID))
            }
            holder.btnDelete.setOnClickListener {

                val builder = AlertDialog.Builder(context)
                builder.setMessage("Are you sure you want to Delete?")
                    .setCancelable(false)
                    .setPositiveButton("Yes") { dialog, id ->
                        // Delete selected note from database
                                    snackbar("user deleted successfully")
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

        vm.getAll().observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        return binding.root
    }

    private fun deleteProduct(id: String) {
        vm.remove(id)
    }



}