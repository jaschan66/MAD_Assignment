package my.com.abibasInven.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.logindemo.util.errorDialog
import com.example.logindemo.util.informationDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import my.com.abibasInven.R
import my.com.abibasInven.data.UserViewModel
import my.com.abibasInven.databinding.FragmentAccountBinding
import my.com.abibasInven.databinding.FragmentLoginBinding


class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private val nav by lazy {findNavController()}
    private val vm: UserViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = FragmentLoginBinding.inflate(inflater, container, false)

        vm.getAll()

        binding.btnForgetPass.setOnClickListener {
            val email    = binding.edtLoginEmail.text.toString().trim()
            nav.navigate(R.id.forgotPasswordFragment, bundleOf("email" to email))
        }
        binding.btnLogin.setOnClickListener {
            val email    = binding.edtLoginEmail.text.toString().trim()
            val password = binding.edtLoginPassword.text.toString().trim()

            val u = vm.get(email)
            if (u != null) {
                if ( u.attempt < 3 ) {
                    FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                val message = "Successfully login"
                                informationDialog(message)
                                nav.navigate(R.id.homeFragment)
                            } else {
                                u.attempt = u.attempt + 1
                                vm.set(u)
                                val err = task.exception?.message.toString()
                                errorDialog(err)



                            }
                        }
                } else {
                    errorDialog("Number of failed attempt exceeded")
                }
            }
        }



        return binding.root
    }

    private fun login() {

    }


}