package my.com.abibasInven.util

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.logindemo.util.toBitmap
import my.com.abibasInven.R
import my.com.abibasInven.data.Outlet

class OutletAdapter(
    val fn: (ViewHolder, Outlet) -> Unit = { _, _ -> }
) : ListAdapter<Outlet, OutletAdapter.ViewHolder>(DiffCallback) {

    companion object DiffCallback : DiffUtil.ItemCallback<Outlet>() {
        override fun areItemsTheSame(a: Outlet, b: Outlet) = a.ID == b.ID
        override fun areContentsTheSame(a: Outlet, b: Outlet) = a == b
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val root = view
        val imgPhoto: ImageView = view.findViewById(R.id.imgOutlet)
        val outletName: TextView = view.findViewById(R.id.lblOutletName)
        val btnUpdate: Button = view.findViewById(R.id.btnOutletUpdate)
        val btnDelete: Button = view.findViewById(R.id.btnOutletDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OutletAdapter.ViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.outlet_rv, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: OutletAdapter.ViewHolder, position: Int) {
        val outlet = getItem(position)
        holder.outletName.text = outlet.name
        holder.imgPhoto.setImageBitmap(outlet.photo.toBitmap())

        fn(holder, outlet)
    }
}