package my.com.abibasInven.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.example.logindemo.util.errorDialog
import com.example.logindemo.util.informationDialog
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import my.com.abibasInven.R
import my.com.abibasInven.databinding.FragmentAccountBinding
import my.com.abibasInven.databinding.FragmentLoginBinding
import my.com.abibasInven.databinding.FragmentRegisterBinding


class RegisterFragment : Fragment() {

    private lateinit var binding: FragmentRegisterBinding
    private val nav by lazy {findNavController()}

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = FragmentRegisterBinding.inflate(inflater, container, false)


        binding.btnRegisterNext.setOnClickListener {
            val email    = binding.edtRegisteremail.text.toString().trim()
            val password = binding.edtRegisterPassword.text.toString().trim()

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



        return binding.root
    }

    private fun login() {

    }


}