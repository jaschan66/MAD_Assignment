package my.com.abibasInven.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.logindemo.util.errorDialog
import my.com.abibasInven.data.Category
import my.com.abibasInven.data.CategoryViewModel
import my.com.abibasInven.databinding.FragmentCategoryUpdateBinding

class CategoryUpdateFragment : Fragment() {

    private lateinit var binding: FragmentCategoryUpdateBinding
    private val nav by lazy { findNavController() }
    private val vm: CategoryViewModel by activityViewModels()

    private val cateId by lazy { requireArguments().getString("categoryId", null) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentCategoryUpdateBinding.inflate(inflater, container, false)

        reset()
        val s = vm.get(cateId)
        binding.btnNewCateReset.setOnClickListener { reset() }
        binding.btnCateUpdate.setOnClickListener {
            if (s != null) {
                update(s)
            }
        }

        return binding.root
    }

    private fun reset() {

        val cate = vm.get(cateId)
        if (cate == null) {
            nav.navigateUp()
            return
        }
        with(binding) {
            edtUpdateName.setText((cate.name))
        }
    }

    private fun update(c: Category) {

        val updateCate = Category(
            ID = cateId,
            name = binding.edtUpdateName.text.toString().trim(),
        )

        val e = vm.validate(updateCate, false)
        if (e != "") {
            errorDialog(e)
            return
        } else {
            vm.set(updateCate)
            nav.navigateUp()
        }
    }
}