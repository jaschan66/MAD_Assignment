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
import my.com.abibasInven.data.RackType


class LocationAdapter (
    val fn: (ViewHolder, RackType) -> Unit = { _, _ ->}
): ListAdapter<RackType,LocationAdapter.ViewHolder>(DiffCallback){



    companion object DiffCallback : DiffUtil.ItemCallback<RackType>() {
        override fun areItemsTheSame(a: RackType, b: RackType)    = a.ID == b.ID
        override fun areContentsTheSame(a: RackType, b: RackType) = a.ID == b.ID
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val root = view
        val lblRack : TextView = view.findViewById(R.id.lblRack)
        val btnRackDetail : Button = view.findViewById(R.id.btnRackDetail)
        val btnDeleteRack : Button = view.findViewById(R.id.btnDeleteRack)
        val btnEditRack : Button = view.findViewById(R.id.btnEditRack)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationAdapter.ViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.location_rv, parent, false)
        return LocationAdapter.ViewHolder(view)
    }

    override fun onBindViewHolder(holder: LocationAdapter.ViewHolder, position: Int) {
        val rackType = getItem(position)


        holder.lblRack.text = "Rack "+rackType.ID

        fn(holder, rackType)
    }
}