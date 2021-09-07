package my.com.abibasInven.data

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase

class LocationViewModel : ViewModel() {

    private val col = Firebase.firestore.collection("location")
    private val location = MutableLiveData<List<Location>>()

    init {
        col.addSnapshotListener { it, _ -> location.value = it?.toObjects()  }
    }


    fun get(id: String): Location? {
        return location.value?.find { it -> it.ID == id}
    }

    fun getAll() = location

    //set will be used for both adding and updating purpose
    fun set(it:Location) {
        col.document(it.ID).set(it)
    }

    fun remove(id: String) {
        col.document(id).delete()
    }

    fun deleteAll() {
        location.value?.forEach { it -> remove(it.ID) }
    }

    private fun idExists(id: String): Boolean {
        return location.value?.any { it -> it.ID == id } ?: false // if found return true if not found then return false
    }

    fun validate(i:Item, insert: Boolean = true): String {
        val regexId = Regex("0") //TODO: Add in the regex pattern based on the needs
        var errorMessage = ""

        //TODO Dunno if this table needs any validation judge it yourself ;)


        return errorMessage
    }



}