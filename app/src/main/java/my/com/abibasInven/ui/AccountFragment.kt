package my.com.abibasInven.ui

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import com.example.logindemo.util.cropToBlob
import com.example.logindemo.util.errorDialog
import com.example.logindemo.util.informationDialog
import com.example.logindemo.util.toBitmap
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.Blob
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import my.com.abibasInven.R
import my.com.abibasInven.data.*
import my.com.abibasInven.databinding.FragmentAccountBinding


class AccountFragment : Fragment() {

    private lateinit var binding: FragmentAccountBinding
    private val nav by lazy {findNavController()}

    private val vm : UserViewModel by activityViewModels()
    private val productvm : ProductViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = FragmentAccountBinding.inflate(inflater, container, false)

        productvm.getAll()

        val bottomNav : BottomNavigationView = requireActivity().findViewById(R.id.bottomNavigationView)
        bottomNav.visibility = View.VISIBLE



        val preferences = activity?.getSharedPreferences("email", Context.MODE_PRIVATE)
        val emailLoginRMB = preferences?.getString("emailLoginRmb","")
//        if (emailLoginRMB != null) {
//            emailLogin = emailLoginRMB
//        }
//        if (emailLoginRMB != null) {
//            vm.get(emailLoginRMB)
//        }



        val u = emailLoginRMB?.let { vm.get(it) }
        val password = u?.password
        val args = bundleOf(
            "email" to emailLoginRMB
        )

        val s = emailLoginRMB?.let { vm.get(it) }

        if(img == Blob.fromBytes(ByteArray(0))){
            if (s != null) {
                binding.lblAccName.text = s.name
                binding.lblAccountRole.text = s.role
                binding.accountImg.setImageBitmap(s.photo.toBitmap())
            }
        } else {
            if (s != null) {
                binding.lblAccName.text = s.name
                binding.lblAccountRole.text = s.role
                binding.accountImg.setImageBitmap(img.toBitmap())
            }
        }
        if (u?.photo == Blob.fromBytes(ByteArray(0))) {
            binding.accountImg.setImageResource(R.drawable.ic_addimg)
        }




        binding.accountChgPass.setOnClickListener {
            if (password != null) {
                FirebaseAuth.getInstance().signInWithEmailAndPassword(u.email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            nav.navigate(R.id.resetPasswordFragment,args)
                        } else {
                            errorDialog("Unsuccessful")
                        }
                    }
            }
        }
        binding.accountChgPhoto.setOnClickListener { nav.navigate(R.id.userChgPicFragment, args) }
        if (u?.role == "Manager"){
            binding.view3.isVisible = true
            binding.accountManageStaff.isVisible = true
        }

        binding.categoryMaintenance.setOnClickListener { nav.navigate(R.id.categoryListFragment) }
        binding.locationMaintenance.setOnClickListener { nav.navigate(R.id.locationListingFragment) }
        binding.deliveryMaintenance.setOnClickListener { nav.navigate(R.id.deliveryListingFragment) }
        binding.stockMaintenance.setOnClickListener { nav.navigate(R.id.stockInSelectMethodFragment) }

        binding.accountManageStaff.setOnClickListener { nav.navigate(R.id.staffListFragment) }

        binding.acountLogOut.setOnClickListener { logout() }

        return binding.root
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
        nav.navigate(R.id.action_accountFragment_to_loginFragment)


    }
//    fun reset() {
//
//        val col = Firebase.firestore.collection("user")
//        col.document(email).get().addOnSuccessListener {
//        }
//
//
//
//
//        val s = vm.get(email)
//        if (s != null) {
//            binding.lblAccName.text = s.name
//            binding.lblAccountRole.text = s.role
//            binding.accountImg.setImageBitmap(s.photo.toBitmap())
//
//        }
//    }

}