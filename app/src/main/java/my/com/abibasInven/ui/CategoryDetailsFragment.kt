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
import my.com.abibasInven.R
import my.com.abibasInven.data.ProductViewModel
import my.com.abibasInven.data.SupplierViewModel
import my.com.abibasInven.databinding.FragmentCategoryDetailsBinding
import my.com.abibasInven.util.ProductAdapter

class CategoryDetailsFragment : Fragment() {

    private lateinit var binding: FragmentCategoryDetailsBinding
    private val nav by lazy { findNavController() }
    private val vm: SupplierViewModel by activityViewModels()
    private val vmProduct: ProductViewModel by activityViewModels()
    private lateinit var adapter: ProductAdapter

    private val cateId by lazy { requireArguments().getString("categoryId", null) }
    private val nameId by lazy { requireArguments().getString("categoryName", null) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentCategoryDetailsBinding.inflate(inflater, container, false)

        binding.tvCateName.text = nameId

        // Product's Recycle View
        adapter = ProductAdapter() { holder, product ->

            holder.root.setOnClickListener {
//                nav.navigate(R.id.staffDetailsFragment, bundleOf("product" to product.ID))
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
                        snackbar("Product deleted successfully")
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

        binding.cateProductRv.adapter = adapter
        binding.cateProductRv.addItemDecoration(
            DividerItemDecoration(
                context,
                DividerItemDecoration.VERTICAL
            )
        )

        val product = vmProduct.getCategoryProduct(cateId)
        adapter.submitList(product)

        return binding.root
    }

    private fun deleteProduct(id: String) {
        vmProduct.remove(id)
    }
}