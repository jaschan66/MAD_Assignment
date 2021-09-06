package my.com.abibasInven.util

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.logindemo.util.toBitmap
import my.com.abibasInven.R
import my.com.abibasInven.data.User
import my.com.abibasInven.data.UserViewModel

class StaffAdapter (
    val fn: (ViewHolder, User) -> Unit = { _, _ ->}
        ): ListAdapter<User,StaffAdapter.ViewHolder>(DiffCallback){


    companion object DiffCallback : DiffUtil.ItemCallback<User>() {
        override fun areItemsTheSame(a: User, b:User)      = a.email == b.email
        override fun areContentsTheSame(a: User, b:User) = a == b
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val root = view
        val imgPhoto : ImageView = view.findViewById(R.id.imgStaff)
        val txtName : TextView = view.findViewById(R.id.lblStaffName)
        val btnUpdate : Button = view.findViewById(R.id.btnStaffUpdate)
        val btnDelete : TextView = view.findViewById(R.id.btnStaffDelete)
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StaffAdapter.ViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.staff_rv, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: StaffAdapter.ViewHolder, position: Int) {
        val staff = getItem(position)
        holder.txtName.text = staff.name
        holder.imgPhoto.setImageBitmap(staff.photo.toBitmap())

        fn(holder, staff)


    }
}