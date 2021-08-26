package my.com.abibasInven.data

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase

class OrderViewModel : ViewModel() {

    private val col = Firebase.firestore.collection("order")
    private val order = MutableLiveData<List<Order>>()

    init {
        col.addSnapshotListener { it, _ -> order.value = it?.toObjects()  }
    }


    fun get(id: String): Order? {
        return order.value?.find { it -> it.orderID == id}
    }

    fun getAll() = order

    //set will be used for both adding and updating purpose
    fun set(o:Order) {
        col.document(o.orderID).set(o)
    }

    fun remove(id: String) {
        col.document(id).delete()
    }

    fun deleteAll() {
        order.value?.forEach { it -> remove(it.orderID) }
    }

    private fun idExists(id: String): Boolean {
        return order.value?.any { it -> it.orderID == id } ?: false // if found return true if not found then return false
    }

    fun validate(o: Order, insert: Boolean = true): String {
        val regexId = Regex("0") //TODO: Add in the regex pattern based on the needs
        var errorMessage = ""

        if (insert)/*if it is true */{
            errorMessage += if (o.orderID== "") "- Order ID cannot be empty.\n"
            else if (!o.orderID.matches(regexId)) "- Order ID format is invalid.\n"
            else if (idExists(o.orderID)) "- Order ID is duplicated.\n" //if the function return true then error message will be added
            else ""
        }

        errorMessage += if (o.orderDestination == "") "- Order destination is required. \n"
        else ""

        errorMessage += if (o.orderType == "") "- Order type is required. \n"
        else ""

        errorMessage += if (o.orderQty == 0) "- Order quantity is requiired. \n"
        else ""

        //TODO Add in more validation based on the needs of your fields

        return errorMessage
    }



}