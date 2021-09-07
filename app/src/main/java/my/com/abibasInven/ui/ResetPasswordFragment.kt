package my.com.abibasInven.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.logindemo.util.errorDialog
import com.example.logindemo.util.informationDialog
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import my.com.abibasInven.R
import my.com.abibasInven.data.UserViewModel
import my.com.abibasInven.databinding.FragmentForgotPasswordBinding
import my.com.abibasInven.databinding.FragmentHomeBinding
import my.com.abibasInven.databinding.FragmentResetPasswordBinding


class ResetPasswordFragment : Fragment() {

    private lateinit var auth : FirebaseAuth
    private lateinit var binding: FragmentResetPasswordBinding
    private val nav by lazy {findNavController()}
    private val email by lazy { requireArguments().getString("email", "")}
    private val vm: UserViewModel by activityViewModels()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = FragmentResetPasswordBinding.inflate(inflater, container, false)

        // TODO
        auth = FirebaseAuth.getInstance()
        vm.getAll()

        binding.btnConfirm.setOnClickListener { changePassword() }
        return binding.root
    }


    private fun changePassword() {
        var newPass = binding.edtNewPassword.text.toString().trim()
        var newConfirmPass = binding.edtNewConfirmPassword.text.toString().trim()
        var checkResult = checkIfEmpty(newPass,newConfirmPass)
        if (checkResult == false) {
            var u = vm.get(email)
            if (newPass == newConfirmPass) {
                val user = auth.currentUser
                if (user != null) {
                    val credential = EmailAuthProvider
                        .getCredential(u!!.email, u!!.password)
                    user.reauthenticate(credential)
                        .addOnCompleteListener {
                            if (it.isSuccessful) {

                                user.updatePassword(newPass)
                                    .addOnCompleteListener { task ->
                                        if (task.isSuccessful) {
                                            u.attempt = 0
                                            vm.set(u)
                                            informationDialog("Successful change the password")
                                            auth.signOut()
                                            nav.navigate(R.id.loginFragment)
                                        }
                                    }
                            } else {
                                errorDialog("Failed to change the password")
                            }
                        }
                }

                user!!.updatePassword(newPass)

                u?.password = newPass
                vm.set(u!!)
            } else {
                errorDialog("Password does not match")
            }

        }
        else{
            errorDialog("The field cannot be empty")
        }
    }
    private fun checkIfEmpty(newPass: String, newConPass: String): Boolean {
        return newPass == "" || newConPass == ""
    }

}