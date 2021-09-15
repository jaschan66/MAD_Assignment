package my.com.abibasInven.data

import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase

class DeliveryViewModel {

    private val col = Firebase.firestore.collection("delivery")
    private val delivery = MutableLiveData<List<Delivery>>()

    init {
        col.addSnapshotListener { it, _ -> delivery.value = it?.toObjects() }
    }

    fun get(id: String): Delivery? {
        return delivery.value?.find { it -> it.ID == id }
    }

    fun getAllDelivery() = delivery

    fun callDeliverySize() = delivery.value?.size ?: 0

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

}