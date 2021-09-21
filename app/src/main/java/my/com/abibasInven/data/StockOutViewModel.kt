package my.com.abibasInven.data

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase

class StockOutViewModel : ViewModel() {

    private val col = Firebase.firestore.collection("stockOut")
    private val stockOut = MutableLiveData<List<StockOut>>()

    init {
        col.addSnapshotListener { it, _ -> stockOut.value = it?.toObjects()  }
    }


    fun get(id: String): StockOut? {
        return stockOut.value?.find { it -> it.ID == id}
    }

    fun getAll() = stockOut

    fun calStockOutSize() = stockOut.value?.size ?: 0

    //set will be used for both adding and updating purpose
    fun set(so:StockOut) {
        col.document(so.ID).set(so)
    }

    fun remove(id: String) {
        col.document(id).delete()
    }

    fun deleteAll() {
        stockOut.value?.forEach { it -> remove(it.ID) }
    }

    private fun idExists(id: String): Boolean {
        return stockOut.value?.any { it -> it.ID == id } ?: false // if found return true if not found then return false
    }

    fun validID(): String {
        var newID: String

        val getStockOut = stockOut.value?.lastOrNull()?.ID.toString()
        val num: String = getStockOut.substringAfterLast("SO10")
        newID = "SO10" + (num.toIntOrNull()?.plus(1)).toString()

        if (newID == "SO1010") {
            newID = "SO1" + (num.toIntOrNull()?.plus(1)).toString()
            return newID
        }

        return when (calStockOutSize()) {
            0 -> {
                newID = "SO10" + (calStockOutSize() + 1)
                newID
            }
            in 1..8 -> {
                val getLastStockOut = stockOut.value?.lastOrNull()?.ID.toString()
                val num: String = getLastStockOut.substringAfterLast("SO10")
                newID = "SO10" + (num.toIntOrNull()?.plus(1)).toString()
                if (newID == "SO10null") {
                    newID = "SO111"
                    newID
                } else newID
            }
            else -> {
                val getLastStockOut = stockOut.value?.lastOrNull()?.ID.toString()
                val num: String = getLastStockOut.substringAfterLast("SO1")
                newID = "SO1" + (num.toIntOrNull()?.plus(1)).toString()
                newID
            }
        }
    }

    fun validate(so: StockOut, insert: Boolean = true): String {
        val regexId = Regex("0") //TODO: Add in the regex pattern based on the needs
        var errorMessage = ""

        if (insert)/*if it is true */{
            errorMessage += if (so.ID== "") "- StockOut ID cannot be empty.\n"
            else if (!so.ID.matches(regexId)) "- StockOut ID format is invalid.\n"
            else if (idExists(so.ID)) "- StockOut ID is duplicated.\n" //if the function return true then error message will be added
            else ""
        }




        //TODO Add in more validation based on the needs of your fields

        return errorMessage
    }



}