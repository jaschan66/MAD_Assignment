package my.com.abibasInven.data

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase

class DeliveryItemViewModel : ViewModel() {

    private val col = Firebase.firestore.collection("deliveryItem")
    private val deliveryItem = MutableLiveData<List<DeliveryItem>>()

    init {
        col.addSnapshotListener { it, _ -> deliveryItem.value = it?.toObjects() }
    }

    fun get(id: String): DeliveryItem? {
        return deliveryItem.value?.find { it -> it.ID == id }
    }

    fun getByDeliveryID(id: String): DeliveryItem? {
        return deliveryItem.value?.find { it -> it.deliveryID == id }
    }

    fun getAllDeliveryItem() = deliveryItem

    fun calDeliveryItemSize() = deliveryItem.value?.size ?: 0

    fun set(d: DeliveryItem) {
        col.document(d.ID).set(d)
    }

    fun remove(id: String) {
        col.document(id).delete()
    }

    fun deleteAll() {
        deliveryItem.value?.forEach { it -> remove(it.ID) }
    }

    private fun idExists(id: String): Boolean {
        return deliveryItem.value?.any { it -> it.ID == id }
            ?: false // if found return true if not found then return false
    }

    fun validID(): String {
        var newID: String

        val getLastDeliveryItem = deliveryItem.value?.lastOrNull()?.ID.toString()
        val num: String = getLastDeliveryItem.substringAfterLast("DVI10")
        newID = "DVI10" + (num.toIntOrNull()?.plus(1)).toString()

        if (newID == "DVI1010") {
            newID = "DVI1" + (num.toIntOrNull()?.plus(1)).toString()
            return newID
        }

        return when (calDeliveryItemSize()) {
            0 -> {
                newID = "DVI10" + (calDeliveryItemSize() + 1)
                newID
            }
            in 1..8 -> {
                val getLastDeliveryItem = deliveryItem.value?.lastOrNull()?.ID.toString()
                val num: String = getLastDeliveryItem.substringAfterLast("DVI10")
                newID = "DVI10" + (num.toIntOrNull()?.plus(1)).toString()
                if (newID == "DVI10null") {
                    newID = "DVI111"
                    newID
                } else newID
            }
            else -> {
                val getLastDelivery = deliveryItem.value?.lastOrNull()?.ID.toString()
                val num: String = getLastDelivery.substringAfterLast("DVI1")
                newID = "DVI1" + (num.toIntOrNull()?.plus(1)).toString()
                newID
            }
        }
    }

    fun delete(id : String){
        col.document(id).delete()
    }

    fun validate(d: Delivery, insert: Boolean = true): String {

        var errorMessage = ""

        if (insert)/*if it is true */ {
            errorMessage += if (idExists(d.ID)) "- Delivery ID is duplicated.\n"
            else ""
        }
        return errorMessage
    }








}