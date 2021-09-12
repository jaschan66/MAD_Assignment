package my.com.abibasInven.util

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import my.com.abibasInven.R
import my.com.abibasInven.data.Location
import my.com.abibasInven.data.LocationViewModel


class LocationAdapter (
    val fn: (ViewHolder, Location) -> Unit = { _, _ ->}
): ListAdapter<Location,LocationAdapter.ViewHolder>(DiffCallback){



    companion object DiffCallback : DiffUtil.ItemCallback<Location>() {
        override fun areItemsTheSame(a: Location, b: Location)    = a.rackType == b.rackType
        override fun areContentsTheSame(a: Location, b: Location) = a.rackType == b.rackType
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val root = view
        val lblRack : TextView = view.findViewById(R.id.lblRack)
        val btnDeleteRack : Button = view.findViewById(R.id.btnDeleteRack)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationAdapter.ViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.location_rv, parent, false)
        return LocationAdapter.ViewHolder(view)
    }

    override fun onBindViewHolder(holder: LocationAdapter.ViewHolder, position: Int) {
        val location = getItem(position)


        holder.lblRack.text = location.rackType

        fn(holder, location)
    }
}