package my.com.abibasInven.ui

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.logindemo.util.snackbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import my.com.abibasInven.R
import my.com.abibasInven.data.UserViewModel
import my.com.abibasInven.databinding.FragmentStaffListBinding
import my.com.abibasInven.util.StaffAdapter


class StaffListFragment : Fragment() {

    private lateinit var binding: FragmentStaffListBinding
    private val nav by lazy {findNavController()}
    private val vm: UserViewModel by activityViewModels()
    private var auth : FirebaseAuth = FirebaseAuth.getInstance()

    private lateinit var adapter: StaffAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        vm.search("")
        val bottomNav : BottomNavigationView = requireActivity().findViewById(R.id.bottomNavigationView)
        bottomNav.visibility = View.GONE

        binding = FragmentStaffListBinding.inflate(inflater, container, false)
        binding.btnBackStaffList.setOnClickListener { nav.navigate(R.id.action_staffListFragment_to_accountFragment) }
        binding.svStaff.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(name: String) = true
            override fun onQueryTextChange(name: String): Boolean {
                vm.search(name)
                return true
            }
        })


        binding.btnAddStaff.setOnClickListener { nav.navigate(R.id.registerFragment) }

        adapter = StaffAdapter() { holder, user ->

            holder.root.setOnClickListener {
                nav.navigate(R.id.staffDetailsFragment, bundleOf("email" to user.email))
            }
            holder.btnUpdate.setOnClickListener {
                nav.navigate(R.id.updateStaffFragment, bundleOf("email" to user.email))
            }
            holder.btnDelete.setOnClickListener {
                FirebaseAuth.getInstance().signInWithEmailAndPassword(user.email, user.password)
                val builder = AlertDialog.Builder(context)
                builder.setMessage("Are you sure you want to Delete?")
                    .setCancelable(false)
                    .setPositiveButton("Yes") { dialog, id ->
                        // Delete selected note from database
                        val user1 = auth.currentUser
                        user1?.delete()
                            ?.addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    snackbar("user deleted successfully")
                                    vm.remove(user.email)
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

        vm.getResult().observe(viewLifecycleOwner) {
            adapter.submitList(it)

        }

        return binding.root
    }



}