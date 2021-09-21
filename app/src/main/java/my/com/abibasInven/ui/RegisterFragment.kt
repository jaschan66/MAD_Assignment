package my.com.abibasInven.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.example.logindemo.util.errorDialog
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import my.com.abibasInven.R
import my.com.abibasInven.databinding.FragmentRegisterBinding


class RegisterFragment : Fragment() {

    private lateinit var binding: FragmentRegisterBinding
    private val nav by lazy {findNavController()}

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = FragmentRegisterBinding.inflate(inflater, container, false)


        binding.btnBackRegister.setOnClickListener { nav.navigate(R.id.action_registerFragment_to_staffListFragment) }
        binding.btnRegisterNext.setOnClickListener {

            val email    = binding.edtRegisteremail.text.toString().trim()
            val password = binding.edtRegisterPassword.text.toString().trim()
           var checkResult = checkIfEmpty(email,password)
            if (checkResult == false) {
                val args = bundleOf(
                    "email" to email,
                    "password" to password
                )

                FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,password)
                    .addOnCompleteListener(
                        OnCompleteListener<AuthResult>{ task ->
                            if (task.isSuccessful) {
                                val firebaseUser: FirebaseUser = task.result!!.user!!
                                nav.navigate(R.id.registerUserFragment, args)
                            } else{
                                val err = task.exception!!.message.toString()
                                errorDialog(err)
                            }
                        }
                    )
            }
            else {
                errorDialog("The field cannot be empty")
            }

        }



        return binding.root
    }

    private fun checkIfEmpty(email: String, password: String): Boolean {
        return email == "" || password == ""
    }

    private fun login() {

    }


}