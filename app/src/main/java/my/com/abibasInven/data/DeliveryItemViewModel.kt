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

    fun validID(id: String): String {
        val newID: String
        val getLastDeliveryItem = deliveryItem.value?.lastOrNull()?.ID.toString()

        return if (idExists(id)) {
            val num: String = getLastDeliveryItem.substringAfterLast("DVI")
            newID = "DVI" + (num.toIntOrNull()?.plus(1)).toString()
            newID
        } else {
            newID = "DVI" + (calDeliveryItemSize() + 1).toString()
            newID
        }
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