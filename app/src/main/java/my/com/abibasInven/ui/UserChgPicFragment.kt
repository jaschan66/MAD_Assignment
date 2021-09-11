package my.com.abibasInven.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.logindemo.util.cropToBlob
import com.example.logindemo.util.toBitmap
import my.com.abibasInven.R
import my.com.abibasInven.data.User
import my.com.abibasInven.data.UserViewModel
import my.com.abibasInven.data.img
import my.com.abibasInven.databinding.FragmentHomeBinding
import my.com.abibasInven.databinding.FragmentUserChgPicBinding


class UserChgPicFragment : Fragment() {

    private lateinit var binding: FragmentUserChgPicBinding
    private val nav by lazy {findNavController()}
    private val email by lazy { requireArguments().getString("email", "") }
    private val vm : UserViewModel by activityViewModels()
    private val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                binding.imgChgProfile.setImageURI(it.data?.data)
            }
        }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {


        binding = FragmentUserChgPicBinding.inflate(inflater, container, false)

        val u = vm.get(email)

        if (u?.photo != null) {
            binding.imgChgProfile.setImageBitmap(u.photo.toBitmap())
        }
        else{
            binding.imgChgProfile.setImageResource(R.drawable.ic_addimg)
        }

        binding.imgChgProfile.setOnClickListener { chooseImage() }


        // TODO
        binding.btnSet.setOnClickListener {
            if (u != null) {
                val addU = User(
                    email = email,
                    password = u.password,
                    name = u.name,
                    photo = binding.imgChgProfile.cropToBlob(300, 300),
                    role = u.role,
                    attempt = u.attempt
                )
                vm.set(addU)
                img = binding.imgChgProfile.cropToBlob(300,300)
                val args = bundleOf(
                    "email" to email,
                )
                nav.navigate(R.id.accountFragment,args)
            }

        }

        return binding.root
    }

    private fun chooseImage() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        launcher.launch(intent)
    }

}