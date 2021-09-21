package my.com.abibasInven.data

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase

class OutletViewModel : ViewModel() {

    private val col = Firebase.firestore.collection("outlet")
    private val outlet = MutableLiveData<List<Outlet>>()
    private var outletList = listOf<Outlet>()
    private val searchResult = MutableLiveData<List<Outlet>>()

    private var name = "" // for searching purpose


    init {
        col.addSnapshotListener { it, _ ->
            outlet.value = it?.toObjects()
            outletList = it!!.toObjects()
            updateResult()
        }
    }

    fun get(id: String): Outlet? {
        return outlet.value?.find { it -> it.ID == id }
    }

    fun getAllOutlet() = outlet

    fun calSize() = outlet.value?.size ?: 0

    //set will be used for both adding and updating purpose
    fun set(o: Outlet) {
        col.document(o.ID).set(o)
    }

    fun searchOutlet(name: String) {
        this.name = name
        updateResult()
    }

    private fun updateResult() {
        var list = outletList

        //Search
        list = list.filter {
            it.name.contains(name, true)
        }

        searchResult.value = list
    }

    fun getResult() = searchResult

    fun remove(id: String) {
        col.document(id).delete()
    }

    fun deleteAll() {
        outlet.value?.forEach { it -> remove(it.ID) }
    }

    private fun idExists(id: String): Boolean {
        return outlet.value?.any { it -> it.ID == id }
            ?: false // if found return true if not found then return false
    }

    private fun outletExists(name: String): Boolean {
        return outlet.value?.any { it -> it.name.equals(name, true) } ?: false
    }

    fun validate(o: Outlet, insert: Boolean = true): String {
        var errorMessage = ""

        if (insert) {
            errorMessage += if (o.ID == "") "- Outlet ID cannot be empty.\n"
            else if (idExists(o.ID)) "- Outlet ID is duplicated.\n"
            else ""
        }

        errorMessage += if (o.latitude == 0.0) "- Outlet location is required. \n"
        else ""

        errorMessage += if (o.longitude == 0.0) "- Outlet location is required. \n"
        else ""

        errorMessage += when {
            o.name == "" -> "- Outlet name is required. \n"
            outletExists(o.name) -> "- Outlet name exists. \n"
            else -> ""
        }

        errorMessage += if (o.photo.toBytes().isEmpty()) "- Outlet photo is required. \n"
        else ""

        return errorMessage
    }

    fun validID(): String {
        var newID: String

        var getLastOutlet = outlet.value?.lastOrNull()?.ID.toString()
        var num = getLastOutlet.substringAfterLast("OL10")
        newID = "OL10" + (num.toIntOrNull()?.plus(1)).toString()

        if (newID == "OL1010") {
            newID = "OL1" + (num.toIntOrNull()?.plus(1)).toString()
            return newID
        }

        return when (calSize()) {
            0 -> {
                newID = "OL10" + (calSize() + 1)
                newID
            }
            in 1..8 -> {
                getLastOutlet = outlet.value?.lastOrNull()?.ID.toString()
                num = getLastOutlet.substringAfterLast("OL10")
                newID = "OL10" + (num.toIntOrNull()?.plus(1)).toString()
                if (newID == "OL10null") {
                    newID = "OL111"
                    newID
                } else newID
            }
            else -> {
                getLastOutlet = outlet.value?.lastOrNull()?.ID.toString()
                num = getLastOutlet.substringAfterLast("OL1")
                newID = "OL1" + (num.toIntOrNull()?.plus(1)).toString()
                newID
            }
        }
    }
}