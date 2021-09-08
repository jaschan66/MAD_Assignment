package my.com.abibasInven.util

import android.os.Build.ID
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
import my.com.abibasInven.data.Location
import my.com.abibasInven.data.User
import my.com.abibasInven.data.UserViewModel

class LocationAddAdapter (
    val fn: (ViewHolder, Location) -> Unit = { _, _ ->}
): ListAdapter<Location,LocationAddAdapter.ViewHolder>(DiffCallback){


    companion object DiffCallback : DiffUtil.ItemCallback<Location>() {
        override fun areItemsTheSame(a: Location, b:Location)      = a.ID == b.ID
        override fun areContentsTheSame(a: Location, b:Location) = a == b
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val root = view
        val lblCompartment : TextView = view.findViewById(R.id.lblRack)
//        val btnAddLocation : Button = view.findViewById(R.id.btnAddLocation)
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationAddAdapter.ViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.locationadd_rv, parent, false)
        return ViewHolder(view)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val locationAdd = getItem(position)
        holder.lblCompartment.text = locationAdd.ID.substring(2,0)
        fn(holder,locationAdd)
    }
}