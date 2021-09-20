package my.com.abibasInven.util

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.logindemo.util.toBitmap
import my.com.abibasInven.R
import my.com.abibasInven.data.Product
import my.com.abibasInven.data.StockIn

class StockLowReportAdapter (
    val fn: (StockLowReportAdapter.ViewHolder, Product) -> Unit = { _, _ ->}
): ListAdapter<Product, StockLowReportAdapter.ViewHolder>(StockLowReportAdapter) {

    companion object DiffCallback : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(a: Product, b: Product)    = a.ID == b.ID
        override fun areContentsTheSame(a: Product, b: Product) = a.ID == b.ID
    }
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val root = view

        val lblStockSummaryLowReportProductName : TextView = view.findViewById(R.id.lblStockSummaryLowReportProductName)
        val imgStockSummaryLowReportProductPhoto : ImageView = view.findViewById(R.id.imgStockSummaryLowReportPhoto)
        val lblStockSummaryLowReportProductQty : TextView = view.findViewById(R.id.lblStockSummaryLowReportProductQty)




    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StockLowReportAdapter.ViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.stocksummarylowreport_rv, parent, false)
        return StockLowReportAdapter.ViewHolder(view)
    }

    override fun onBindViewHolder(holder: StockLowReportAdapter.ViewHolder, position: Int) {
        val product = getItem(position)


        //holder.lblDeliveryID.text = delivery.ID
        holder.lblStockSummaryLowReportProductName.text = product.name
        holder.imgStockSummaryLowReportProductPhoto.setImageBitmap(product.photo.toBitmap())
        holder.lblStockSummaryLowReportProductQty.text = product.qty.toString()



        fn(holder, product)
    }

}