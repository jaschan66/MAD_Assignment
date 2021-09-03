package my.com.abibasInven.data

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase

class OutletViewModel : ViewModel() {

    private val col = Firebase.firestore.collection("outlet")
    private val outlet = MutableLiveData<List<Outlet>>()

    init {
        col.addSnapshotListener { it, _ -> outlet.value = it?.toObjects()  }
    }


    fun get(id: String): Outlet? {
        return outlet.value?.find { it -> it.ID == id}
    }

    fun getAll() = outlet

    //set will be used for both adding and updating purpose
    fun set(o:Outlet) {
        col.document(o.ID).set(o)
    }

    fun remove(id: String) {
        col.document(id).delete()
    }

    fun deleteAll() {
        outlet.value?.forEach { it -> remove(it.ID) }
    }

    private fun idExists(id: String): Boolean {
        return outlet.value?.any { it -> it.ID == id } ?: false // if found return true if not found then return false
    }

    fun validate(o: Outlet, insert: Boolean = true): String {
        val regexId = Regex("0") //TODO: Add in the regex pattern based on the needs
        var errorMessage = ""

        if (insert)/*if it is true */{
            errorMessage += if (o.ID== "") "- Outlet ID cannot be empty.\n"
            else if (!o.ID.matches(regexId)) "- Outlet ID format is invalid.\n"
            else if (idExists(o.ID)) "- Outlet ID is duplicated.\n" //if the function return true then error message will be added
            else ""
        }

        errorMessage += if (o.latitude == 0.0) "- Outlet location is required. \n"
        else ""

        errorMessage += if (o.longitude == 0.0) "- Outlet location is required. \n"
        else ""

        errorMessage += if (o.name == "") "- Outlet name is required. \n"
        else ""

        errorMessage += if (o.photo.toBytes().isEmpty()) "- Outlet photo is required. \n"
        else ""

        //TODO Add in more validation based on the needs of your fields

        return errorMessage
    }



}