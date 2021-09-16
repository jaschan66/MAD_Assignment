package my.com.abibasInven.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.logindemo.util.errorDialog
import my.com.abibasInven.R
import my.com.abibasInven.data.Category
import my.com.abibasInven.data.CategoryViewModel
import my.com.abibasInven.databinding.FragmentCategoryAddBinding

class CategoryAddFragment : Fragment() {

    private lateinit var binding: FragmentCategoryAddBinding
    private val nav by lazy { findNavController() }
    private val vm: CategoryViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentCategoryAddBinding.inflate(inflater, container, false)

        binding.btnCateReset.setOnClickListener { reset() }
        binding.btnCateAdd.setOnClickListener { createCategory() }

        return binding.root
    }

    // send data to Firestore
    private fun createCategory() {

        var chkID = vm.validID()

        val s = Category(
            ID = chkID,
            name = binding.edtCategoryName.text.toString().trim(),
        )

        val err = vm.validate(s)
        if (err != "") {
            errorDialog(err)
            return
        } else {
            vm.set(s)
            nav.navigate(R.id.categoryListFragment)
        }
    }

    // Reset all edt
    private fun reset() {
        with(binding) {
            edtCategoryName.text.clear()

            edtCategoryName.requestFocus()
        }
    }
}