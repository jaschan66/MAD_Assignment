package my.com.abibasInven.ui

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.logindemo.util.errorDialog
import com.example.logindemo.util.informationDialog
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import my.com.abibasInven.R
import my.com.abibasInven.data.UserViewModel
import my.com.abibasInven.databinding.FragmentAccountBinding
import my.com.abibasInven.databinding.FragmentLoginBinding


class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private val nav by lazy {findNavController()}
    private val vm: UserViewModel by activityViewModels()
//    private lateinit var auth: FirebaseAuth
   // private val PREFS_NAME = "PrefsFile"


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = FragmentLoginBinding.inflate(inflater, container, false)

        vm.getAll()



        binding.btnForgetPass.setOnClickListener {
            val email    = binding.edtLoginEmail.text.toString().trim()
            nav.navigate(R.id.forgotPasswordFragment, bundleOf("email" to email))
        }



//        auth = Firebase.auth
//        val currentUser = auth.currentUser
//        if (currentUser != null) {
//            nav.navigate(R.id.homeFragment)
//        }
//        else{
//            super.onCreate(savedInstanceState)
//        }



        binding.btnLogin.setOnClickListener {
            val email    = binding.edtLoginEmail.text.toString().trim()
            val password = binding.edtLoginPassword.text.toString().trim()
            var checkResult = checkIfEmpty(email,password)
            if (checkResult == false) {
                val u = vm.get(email)
                if (u != null) {
                    if ( u.attempt < 3 ) {
                        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    u.attempt = 0
                                    vm.set(u)
                                    val message = "Successfully login"
                                    informationDialog(message)
                                    nav.navigate(R.id.accountFragment, bundleOf("email" to u.email))
                                }
                                else {
                                    u.attempt = u.attempt + 1
                                    vm.set(u)
                                    val err = task.exception?.message.toString()
                                    errorDialog(err)
                                }
                            }
                    }
                    else {
                        errorDialog("Number of failed attempt exceeded")
                    }
                }
                else {
                    errorDialog("Email or password entered might be invalid")
                }
            }
            else {
                errorDialog("The field cannot be empty")

//            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
//                .addOnCompleteListener { task ->
//                    if (task.isSuccessful) {
//                        nav.navigate(R.id.accountFragment)
//                    } else {
//                        val err = task.exception?.message.toString()
//                        errorDialog(err)
//                    }
            }
        }



        return binding.root
    }

    private fun checkIfEmpty(email: String, password: String): Boolean {
        return email == "" || password == ""
    }


}