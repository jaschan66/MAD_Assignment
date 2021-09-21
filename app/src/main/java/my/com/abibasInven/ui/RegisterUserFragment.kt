package my.com.abibasInven.ui

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.logindemo.util.cropToBlob
import com.example.logindemo.util.errorDialog
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.theartofdev.edmodo.cropper.CropImage
import my.com.abibasInven.R
import my.com.abibasInven.data.User
import my.com.abibasInven.data.UserViewModel
import my.com.abibasInven.databinding.FragmentRegisterUserBinding


class RegisterUserFragment : Fragment() {

    private lateinit var binding: FragmentRegisterUserBinding
    private val nav by lazy { findNavController() }
    private val email by lazy { requireArguments().getString("email", "") }
    private val password by lazy { requireArguments().getString("password", "") }
    private val vm: UserViewModel by activityViewModels()

    private val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                binding.addImgView.setImageURI(it.data?.data)
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentRegisterUserBinding.inflate(inflater, container, false)
        // TODO
        binding.addImgView.setOnClickListener { try {
            CropImage.activity()
                .start(requireContext(),this)
        } catch (e: ActivityNotFoundException) {

        } }
        binding.btnCreateUser.setOnClickListener { createUser() }
        binding.btnBackRegisterUser.setOnClickListener { nav.navigate(R.id.action_registerUserFragment_to_registerFragment) }

        return binding.root
    }

    private fun createUser() {
        var roleR = binding.RegisterSpinnerRole.selectedItem.toString()

        val u = User(
            email = email,
            password = password,
            name = binding.edtRegisterName.text.toString().trim(),
            photo = binding.addImgView.cropToBlob(300, 300),
            role = roleR,
            attempt = 0
        )

        val err = vm.validate(u)
        if (err != "") {
            errorDialog(err)
            return
        } else {
            vm.set(u)
            nav.navigate(R.id.action_registerUserFragment_to_staffListFragment)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            var result = CropImage.getActivityResult(data)
            if (result != null) binding.addImgView.setImageURI(result.uri)

        }
    }

}