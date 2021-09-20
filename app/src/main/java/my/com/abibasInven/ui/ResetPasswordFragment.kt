package my.com.abibasInven.ui

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.logindemo.util.errorDialog
import com.example.logindemo.util.informationDialog
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.Blob
import my.com.abibasInven.R
import my.com.abibasInven.data.UserViewModel
import my.com.abibasInven.data.emailLogin
import my.com.abibasInven.data.img
import my.com.abibasInven.data.passwordLogin
import my.com.abibasInven.databinding.FragmentForgotPasswordBinding
import my.com.abibasInven.databinding.FragmentResetPasswordBinding


class ResetPasswordFragment : Fragment() {

    private lateinit var auth : FirebaseAuth
    private lateinit var binding: FragmentResetPasswordBinding
    private val nav by lazy {findNavController()}
    private val email by lazy { requireArguments().getString("email", "")}
    private val vm: UserViewModel by activityViewModels()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {


        binding = FragmentResetPasswordBinding.inflate(inflater, container, false)

        val bottomNav : BottomNavigationView = requireActivity().findViewById(R.id.bottomNavigationView)
        bottomNav.visibility = View.GONE
        // TODO
        auth = FirebaseAuth.getInstance()
        vm.getAll()
        binding.btnBackResetPassword.setOnClickListener { nav.navigateUp() }

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
                                            logout()
                                            nav.navigate(R.id.action_resetPasswordFragment_to_loginFragment)
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

    private fun logout() {
        emailLogin = ""
        passwordLogin = ""
        img = Blob.fromBytes(ByteArray(0))
        val sharedPref = activity?.getSharedPreferences("checkBo", Context.MODE_PRIVATE)
        val editor : SharedPreferences.Editor = sharedPref!!.edit()
        editor.putString("remember","false")
        editor.apply()
        val sharedPref1 = activity?.getSharedPreferences("email", Context.MODE_PRIVATE)
        val editor1 : SharedPreferences.Editor = sharedPref1!!.edit()
        editor1.putString("emailLoginRmb","")
        editor1.apply()
        FirebaseAuth.getInstance().signOut()


    }

}