package my.com.abibasInven.ui

import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.logindemo.util.errorDialog
import com.example.logindemo.util.hideKeyboard
import com.example.logindemo.util.informationDialog
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import my.com.abibasInven.R
import my.com.abibasInven.data.UserViewModel
import my.com.abibasInven.databinding.FragmentForgotPasswordBinding
import xxx.SimpleEmail
import java.text.DecimalFormat


class ForgotPasswordFragment : Fragment() {

    private lateinit var binding: FragmentForgotPasswordBinding
    private val nav by lazy {findNavController()}
    private val vm : UserViewModel by activityViewModels()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = FragmentForgotPasswordBinding.inflate(inflater, container, false)

        val bottomNav : BottomNavigationView = requireActivity().findViewById(R.id.bottomNavigationView)
        bottomNav.visibility = View.GONE

        binding.btnBackForgotPassword.setOnClickListener { nav.navigate(R.id.action_forgotPasswordFragment_to_loginFragment) }

        // TODO
        vm.getAll()

        // OTP
        val n  = (0..999999).random()
        val fmt = DecimalFormat("000000")
        val OTP = fmt.format(n)

        binding.btnOTP.setOnClickListener { sendOTP(OTP) }


        binding.btnResetPass.setOnClickListener { resetPass(OTP) }
        return binding.root
    }

    private fun resetPass(OTP: String) {

        val email = binding.edtForgotPassEmail.text.toString().trim()
        val OtpType = binding.edtOTP.text.toString().trim()
        val u = vm.get(email)
        val password = u?.password
        if (OtpType == OTP){
            val args = bundleOf(
                "email" to email
            )


            if (password != null) {
                FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            informationDialog("OTP Verified")
                            nav.navigate(R.id.resetPasswordFragment,args)
                        } else {
                            errorDialog("Unsuccessful")
                        }
                    }
            }


        }
        else{
            errorDialog("OTP is Invalid")
        }

    }

    private fun sendOTP(OTP: String) {

        val email = binding.edtForgotPassEmail.text.toString().trim()
        val u = vm.get(email)

        if (u == null){
            errorDialog("No account found in the system that associate with the email given")
        }
        else {
            send(email, OTP)
        }
    }


    private fun send(email: String, OTP: String) {

            hideKeyboard()


            if (!isEmail(email)) {
                snackbar("Invalid Email")
                binding.edtForgotPassEmail.requestFocus()
                return
            }

            val subject = "Your OTP to reset your password"
            val content = """
            <p>Your <b>OTP<b> is:</p>
            <h1 style="color: Blue">$OTP</h1>
            <p>Thank you very much.</p>
        """.trimIndent()

            SimpleEmail()
                .to(email)
                .subject(subject)
                .content(content)
                .isHtml()
                .send() {
                    snackbar("Sent")
                    binding.btnOTP.isEnabled = true
                    binding.edtForgotPassEmail.requestFocus()
                }

            snackbar("Sending...")
            binding.btnOTP.isEnabled = false

    }

    private fun isEmail(email: String) = Patterns.EMAIL_ADDRESS.matcher(email).matches()

    private fun snackbar(text: String) {
        Snackbar.make(requireView(), text, Snackbar.LENGTH_SHORT).show()
    }

}