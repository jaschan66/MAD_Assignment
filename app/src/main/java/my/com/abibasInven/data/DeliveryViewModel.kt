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

    fun validID(): String {
        var newID: String

        val getLastDelivery = delivery.value?.lastOrNull()?.ID.toString()
        val num: String = getLastDelivery.substringAfterLast("DV10")
        newID = "DV10" + (num.toIntOrNull()?.plus(1)).toString()

        if (newID == "DV1010") {
            newID = "DV1" + (num.toIntOrNull()?.plus(1)).toString()
            return newID
        }

        return when (calDeliverySize()) {
            0 -> {
                newID = "DV10" + (calDeliverySize() + 1)
                newID
            }
            in 1..8 -> {
                val getLastDelivery = delivery.value?.lastOrNull()?.ID.toString()
                val num: String = getLastDelivery.substringAfterLast("DV10")
                newID = "DV10" + (num.toIntOrNull()?.plus(1)).toString()
                if (newID == "DV10null") {
                    newID = "DV111"
                    newID
                } else newID
            }
            else -> {
                val getLastDelivery = delivery.value?.lastOrNull()?.ID.toString()
                val num: String = getLastDelivery.substringAfterLast("DV1")
                newID = "DV1" + (num.toIntOrNull()?.plus(1)).toString()
                newID
            }
        }
    }

}