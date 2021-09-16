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
import my.com.abibasInven.data.CategoryViewModel
import my.com.abibasInven.databinding.FragmentCategoryListBinding
import my.com.abibasInven.util.CategoryAdapter

class CategoryListFragment : Fragment() {

    private lateinit var binding: FragmentCategoryListBinding
    private val nav by lazy { findNavController() }
    private val vm: CategoryViewModel by activityViewModels()

    private lateinit var adapter: CategoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentCategoryListBinding.inflate(inflater, container, false)

        binding.btnCategoryAdd.setOnClickListener { nav.navigate(R.id.categoryAddFragment) }

        adapter = CategoryAdapter() { holder, category ->

            //Haven't do
            holder.root.setOnClickListener {
                nav.navigate(R.id.categoryDetailsFragment, bundleOf("categoryId" to category.ID, "categoryName" to category.name))
            }
            holder.btnEdit.setOnClickListener {
                nav.navigate(R.id.categoryUpdateFragment, bundleOf("categoryId" to category.ID))
            }
            holder.btnDelete.setOnClickListener {
                val builder = AlertDialog.Builder(context)
                builder.setMessage("Are you sure you want to Delete?")
                    .setCancelable(false)
                    .setPositiveButton("Yes") { dialog, id ->

                        snackbar("Category deleted successfully")
                        deleteCategory(category.ID)
                    }
                    .setNegativeButton("No") { dialog, id ->
                        // Dismiss the dialog
                        dialog.dismiss()
                    }
                val alert = builder.create()
                alert.show()
            }
        }

        binding.categoryListRv.adapter = adapter
        binding.categoryListRv.addItemDecoration(
            DividerItemDecoration(
                context,
                DividerItemDecoration.VERTICAL
            )
        )

        vm.getAllCategory().observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        return binding.root
    }

    private fun deleteCategory(id: String) {
        vm.remove(id)
    }

}