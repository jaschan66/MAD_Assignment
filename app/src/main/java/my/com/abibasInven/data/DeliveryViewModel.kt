package my.com.abibasInven.data

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase

class DeliveryViewModel : ViewModel() {

    private val col = Firebase.firestore.collection("delivery")
    private val delivery = MutableLiveData<List<Delivery>>()

    init {
        col.addSnapshotListener { it, _ -> delivery.value = it?.toObjects()
        }
    }

    fun get(id: String): Delivery? {
        return delivery.value?.find { it -> it.ID == id }
    }

    fun getByOutletID(outletID: String): Delivery? {
        return delivery.value?.find { it -> it.outletID == outletID }
    }

    fun getAllDelivery() = delivery

    fun calDeliverySize() = delivery.value?.size ?: 0

    fun set(d: Delivery) {
        col.document(d.ID).set(d)
    }

    fun remove(id: String) {
        col.document(id).delete()
    }

    fun deleteAll() {
        delivery.value?.forEach { it -> remove(it.ID) }
    }

    private fun idExists(id: String): Boolean {
        return delivery.value?.any { it -> it.ID == id }
            ?: false // if found return true if not found then return false
    }

    fun validID(id: String): String {
        val newID: String
        val getLastDelivery = delivery.value?.lastOrNull()?.ID.toString()

        return if (idExists(id)) {
            val num: String = getLastDelivery.substringAfterLast("DV")
            newID = "DV" + (num.toIntOrNull()?.plus(1)).toString()
            newID
        } else {
            newID = "DV" + (calDeliverySize() + 1).toString()
            newID
        }
    }

}