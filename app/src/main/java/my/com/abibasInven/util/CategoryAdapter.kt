package my.com.abibasInven.util

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import my.com.abibasInven.R
import my.com.abibasInven.data.Category

class CategoryAdapter(
    val fn: (ViewHolder, Category) -> Unit = { _, _ -> }
) : ListAdapter<Category, CategoryAdapter.ViewHolder>(DiffCallback) {

    companion object DiffCallback : DiffUtil.ItemCallback<Category>() {
        override fun areItemsTheSame(a: Category, b: Category) = a.ID == b.ID
        override fun areContentsTheSame(a: Category, b: Category) = a == b
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val root = view
        val txtName: TextView = view.findViewById(R.id.lblCategoryName)
        val btnEdit: Button = view.findViewById(R.id.btnCategoryEdit)
        val btnDelete: Button = view.findViewById(R.id.btnCategoryDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryAdapter.ViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.category_rv, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryAdapter.ViewHolder, position: Int) {
        val category = getItem(position)
        holder.txtName.text = category.name

        fn(holder, category)
    }
}