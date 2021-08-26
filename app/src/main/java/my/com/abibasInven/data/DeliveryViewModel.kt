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
        col.addSnapshotListener { it, _ -> delivery.value = it?.toObjects()  }
    }


    fun get(id: String): Delivery? {
        return delivery.value?.find { f -> f.deliveryID == id}
    }

    fun getAll() = delivery

    //set will be used for both adding and updating purpose
    fun set(d:Delivery) {
        col.document(d.deliveryID).set(d)
    }

    fun remove(id: String) {
        col.document(id).delete()
    }

    fun deleteAll() {
        delivery.value?.forEach { it -> remove(it.deliveryID) }
    }

    private fun idExists(id: String): Boolean {
        return delivery.value?.any { it -> it.deliveryID == id } ?: false // if found return true if not found then return false
    }

    fun validate(d: Delivery, insert: Boolean = true): String {
        val regexId = Regex("0") //TODO: Add in the regex pattern based on the needs
        var errorMessage = ""

        if (insert)/*if it is true */{
            errorMessage += if (d.deliveryID == "") "-Delivery ID cannot be empty.\n"
            else if (!d.deliveryID.matches(regexId)) "- Delivery ID format is invalid.\n"
            else if (idExists(d.deliveryID)) "- Delivery ID is duplicated.\n" //if the function return true then error message will be added
            else ""
        }

       //TODO Add in more validation based on the needs of your fields

       return errorMessage
    }



}