package my.com.abibasInven.data

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase

class SupplierViewModel : ViewModel() {

    private val col = Firebase.firestore.collection("supplier")
    private val supplier = MutableLiveData<List<Supplier>>()

    init {
        col.addSnapshotListener { it, _ -> supplier.value = it?.toObjects()  }
    }


    fun get(id: String): Supplier? {
        return supplier.value?.find { it -> it.ID == id}
    }

    fun getAll() = supplier

    //set will be used for both adding and updating purpose
    fun set(s:Supplier) {
        col.document(s.ID).set(s)
    }

    fun remove(id: String) {
        col.document(id).delete()
    }

    fun deleteAll() {
        supplier.value?.forEach { it -> remove(it.ID) }
    }

    private fun idExists(id: String): Boolean {
        return supplier.value?.any { it -> it.ID == id } ?: false // if found return true if not found then return false
    }

    fun validate(s: Supplier, insert: Boolean = true): String {
        //val regexId = Regex("S") //TODO: Add in the regex pattern based on the needs
        var errorMessage = ""

        if (insert)/*if it is true */{
            errorMessage += if (s.ID== "") "- Supplier ID cannot be empty.\n"
            //else if (!s.ID.matches(regexId)) "- Supplier ID format is invalid.\n"
            else if (idExists(s.ID)) "- Supplier ID is duplicated.\n" //if the function return true then error message will be added
            else ""
        }

        errorMessage += if (s.email == "") "- Supplier email is required. \n"
        else ""

        errorMessage += if (s.phoneNo == "") "- Supplier phone no is required. \n"
        else ""

        errorMessage += if (s.name == "") "- Supplier name is required. \n"
        else ""

        errorMessage += if (s.latitude == 0.0) "- Supplier latitude is required. \n"
        else ""

        errorMessage += if (s.longitude == 0.0) "- Supplier longitude is required. \n"
        else ""

        //TODO Add in more validation based on the needs of your fields

        return errorMessage
    }



}