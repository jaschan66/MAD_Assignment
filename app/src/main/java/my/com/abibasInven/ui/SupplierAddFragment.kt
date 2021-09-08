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
import my.com.abibasInven.data.Supplier
import my.com.abibasInven.data.SupplierViewModel
import my.com.abibasInven.databinding.FragmentSupplierAddBinding

class SupplierAddFragment : Fragment() {

    private lateinit var binding: FragmentSupplierAddBinding
    private val nav by lazy { findNavController() }
    private val vm: SupplierViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentSupplierAddBinding.inflate(inflater, container, false)

        binding.btnReset.setOnClickListener { reset() }
        binding.btnSubmit.setOnClickListener { createSupplier() }

        return binding.root
    }

    private fun createSupplier() {
        var checkResult = checkIfEmpty(binding.edtSupLoc.text.toString())
        if (checkResult == false) {
            val geopoint = binding.edtSupLoc.text.toString()
            val splitpoint = geopoint.split(",")
            val latitude = splitpoint[0].toDouble()
            val longitude = splitpoint[1].toDouble()


            val s = Supplier(
                ID = "S001",
                name = binding.edtSupName.text.toString().trim(),
                phoneNo = binding.edtSupPhone.text.toString().trim(),
                email = binding.edtSupEmail.text.toString().trim(),
                latitude = latitude,
                longitude = longitude,
            )


            val err = vm.validate(s)
            if (err != "") {
                errorDialog(err)
                return
            } else {
                vm.set(s)
                nav.navigate(R.id.staffListFragment)
            }

        } else {
            errorDialog("The field cannot be empty")
        }
    }

    private fun checkIfEmpty(geopoint: String): Boolean {
        return geopoint == ""
    }

    private fun reset() {
        binding.edtSupName.text.clear()
        binding.edtSupPhone.text.clear()
        binding.edtSupEmail.text.clear()
        binding.edtSupLoc.text.clear()

        binding.edtSupName.requestFocus()
    }
}