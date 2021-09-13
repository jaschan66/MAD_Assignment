package my.com.abibasInven.ui

import android.app.Activity
import android.os.Bundle
import android.provider.SyncStateContract.Helpers.update
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.logindemo.util.errorDialog
import com.example.logindemo.util.toBitmap
import com.google.android.material.bottomnavigation.BottomNavigationView
import my.com.abibasInven.R
import my.com.abibasInven.data.User
import my.com.abibasInven.data.UserViewModel
import my.com.abibasInven.databinding.FragmentHomeBinding
import my.com.abibasInven.databinding.FragmentUpdateStaffBinding


class UpdateStaffFragment : Fragment() {

    private lateinit var binding: FragmentUpdateStaffBinding
    private val nav by lazy {findNavController()}
    private val vm : UserViewModel by activityViewModels()

    private val email by lazy { requireArguments().getString("email", null) }



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {


        binding = FragmentUpdateStaffBinding.inflate(inflater, container, false)

        reset()
        val s = vm.get(email)
        binding.edtUpdateStaffName.isEnabled = false
        binding.btnReset.setOnClickListener { reset() }
        binding.btnUpdate.setOnClickListener {
            if (s != null) {
                update(s)
            }
        }



        return binding.root
    }


    private fun reset() {

        val s = vm.get(email)
        if (s == null) {
            nav.navigateUp()
            return
        }

        load(s)
    }


    private fun load(s: User) {
        binding.imgStaffPhoto.setImageBitmap(s.photo.toBitmap())
        binding.edtUpdateStaffName.setText(s.name)
        binding.spnRole.setSelection(1)
    }

    private fun update(s: User) {
        val us = User(
            email = email,
            role = binding.spnRole.selectedItem.toString(),
            attempt = s.attempt,
            name = s.name,
            password = s.password,
            photo = s.photo
        )

//        val e = vm.validate(s, false)
//        if (e != ""){
//            errorDialog(e)
//            return
//        }
        vm.set(us)
        nav.navigateUp()
    }

}