package my.com.abibasInven.data

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase

class OrderDetailsViewModel : ViewModel() {

    private val col = Firebase.firestore.collection("orderdetails")
    private val orderDetails = MutableLiveData<List<OrderDetails>>()

    init {
        col.addSnapshotListener { it, _ -> orderDetails.value = it?.toObjects()  }
    }


    fun get(id: String): OrderDetails? {
        return orderDetails.value?.find { it -> it.orderDetailsID == id}
    }

    fun getAll() = orderDetails

    //set will be used for both adding and updating purpose
    fun set(od:OrderDetails) {
        col.document(od.orderDetailsID).set(od)
    }

    fun remove(id: String) {
        col.document(id).delete()
    }

    fun deleteAll() {
        orderDetails.value?.forEach { it -> remove(it.orderDetailsID) }
    }

    private fun idExists(id: String): Boolean {
        return orderDetails.value?.any { it -> it.orderDetailsID == id } ?: false // if found return true if not found then return false
    }

    fun validate(od:OrderDetails, insert: Boolean = true): String {
        val regexId = Regex("0") //TODO: Add in the regex pattern based on the needs
        var errorMessage = ""

        //TODO Dunno if this table needs any validation judge it yourself ;)


        return errorMessage
    }



}