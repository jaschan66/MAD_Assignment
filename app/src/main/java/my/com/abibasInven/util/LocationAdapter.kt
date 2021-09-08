package my.com.abibasInven.util

import android.os.Build.ID
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import my.com.abibasInven.R
import my.com.abibasInven.data.Location


class LocationAdapter (
    val fn: (ViewHolder, Location) -> Unit = { _, _ ->}
): ListAdapter<Location,LocationAdapter.ViewHolder>(DiffCallback){


    companion object DiffCallback : DiffUtil.ItemCallback<Location>() {
        override fun areItemsTheSame(a: Location, b:Location)      = a.ID == b.ID
        override fun areContentsTheSame(a: Location, b:Location) = a == b
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val root = view
        val lblRack : TextView = view.findViewById(R.id.lblRack)
        val btnDetail : Button = view.findViewById(R.id.btnDetail)
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationAdapter.ViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.location_rv, parent, false)
        return ViewHolder(view)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val location = getItem(position)
        holder.lblRack.text = location.ID.substring(2,0)
        fn(holder,location)
    }
}