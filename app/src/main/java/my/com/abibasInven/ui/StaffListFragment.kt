package my.com.abibasInven.ui

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.logindemo.util.snackbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import my.com.abibasInven.R
import my.com.abibasInven.data.UserViewModel
import my.com.abibasInven.databinding.FragmentHomeBinding
import my.com.abibasInven.databinding.FragmentStaffListBinding
import my.com.abibasInven.util.StaffAdapter


class StaffListFragment : Fragment() {

    private lateinit var binding: FragmentStaffListBinding
    private val nav by lazy {findNavController()}
    private val vm: UserViewModel by activityViewModels()


    private lateinit var adapter: StaffAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {



        binding = FragmentStaffListBinding.inflate(inflater, container, false)

        // TODO

        binding.btnAddStaff.setOnClickListener { nav.navigate(R.id.registerFragment) }

        adapter = StaffAdapter() { holder, user ->

            holder.root.setOnClickListener {
                nav.navigate(R.id.staffDetailsFragment, bundleOf("email" to user.email))
            }
            holder.btnUpdate.setOnClickListener {
                nav.navigate(R.id.updateStaffFragment, bundleOf("email" to user.email))
            }
            holder.btnDelete.setOnClickListener {
                //TODO Need to do the delete process for 2 times in order to delete the 2nd record and those after 2nd record as well
                val builder = AlertDialog.Builder(context)
                builder.setMessage("Are you sure you want to Delete?")
                    .setCancelable(false)
                    .setPositiveButton("Yes") { dialog, id ->
                        // Delete selected note from database
                        var auth : FirebaseAuth = FirebaseAuth.getInstance()
                        FirebaseAuth.getInstance().signInWithEmailAndPassword(user.email, user.password)
                        val user1 = auth.currentUser
                        user1?.delete()
                            ?.addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    snackbar("user deleted successfully")
                                    deleteStaff(user.email)
                                }
                            }
                    }
                    .setNegativeButton("No") { dialog, id ->
                        // Dismiss the dialog
                        dialog.dismiss()
                    }
                val alert = builder.create()
                alert.show()
            }

        }
        binding.staffrv.adapter = adapter
        binding.staffrv.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))

        vm.getAllStaff().observe(viewLifecycleOwner) {
            adapter.submitList(it)
            binding.lblResCount.text = "${it.size} staff(s)"
        }

        return binding.root
    }

    private fun deleteStaff(email: String) {
        vm.remove(email)
    }



}