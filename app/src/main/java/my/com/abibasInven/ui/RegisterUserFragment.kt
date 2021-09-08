package my.com.abibasInven.ui

import android.app.Activity
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
import my.com.abibasInven.R
import my.com.abibasInven.data.User
import my.com.abibasInven.data.UserViewModel
import my.com.abibasInven.databinding.FragmentRegisterUserBinding



class RegisterUserFragment : Fragment() {

    private lateinit var binding: FragmentRegisterUserBinding
    private val nav by lazy {findNavController()}
    private val email by lazy { requireArguments().getString("email", "")}
    private val password by lazy { requireArguments().getString("password","")}
    private val vm: UserViewModel by activityViewModels()

    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        if (it.resultCode == Activity.RESULT_OK) {
            binding.addImgView.setImageURI(it.data?.data)
        }
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = FragmentRegisterUserBinding.inflate(inflater, container, false)
        // TODO
        binding.addImgView.setImageResource(R.drawable.ic_addimg)
        binding.addImgView.setOnClickListener { chooseImage() }
        binding.btnCreateUser.setOnClickListener { createUser() }


        return binding.root
    }

    private fun createUser() {
        var roleR = binding.RegisterSpinnerRole.selectedItem.toString()

        val u = User(
            email = email,
            password = password,
            name = binding.edtRegisterName.text.toString().trim(),
            photo = binding.addImgView.cropToBlob(300,300),
            role =  roleR,
            attempt = 0
        )

        val err = vm.validate(u)
        if (err != "") {
            errorDialog(err)
            return
        }
        else{
            vm.set(u)
            nav.navigate(R.id.staffListFragment)
        }
    }

    private fun chooseImage() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        launcher.launch(intent)
    }

}