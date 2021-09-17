package my.com.abibasInven.data

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase

class StockInViewModel : ViewModel() {

    private val col = Firebase.firestore.collection("stockIn")
    private val stockIn = MutableLiveData<List<StockIn>>()

    init {
        col.addSnapshotListener { it, _ -> stockIn.value = it?.toObjects()  }
    }


    fun get(id: String): StockIn? {
        return stockIn.value?.find { it -> it.ID == id}
    }

    fun getAll() = stockIn

    fun calStockInSize() = stockIn.value?.size ?: 0

    //set will be used for both adding and updating purpose
    fun set(si:StockIn) {
        col.document(si.ID).set(si)
    }

    fun remove(id: String) {
        col.document(id).delete()
    }

    fun deleteAll() {
        stockIn.value?.forEach { it -> remove(it.ID) }
    }

    private fun idExists(id: String): Boolean {
        return stockIn.value?.any { it -> it.ID == id } ?: false // if found return true if not found then return false
    }



    fun validID(id: String): String {
        val newID: String
        val getLastStockIn = stockIn.value?.lastOrNull()?.ID.toString()

        return if (idExists(id)) {
            val num: String = getLastStockIn.substringAfterLast("SI")
            newID = "SI" + (num.toIntOrNull()?.plus(1)).toString()
            newID
        } else {
            newID = "SI" + (calStockInSize() + 1).toString()
            newID
        }
    }



    fun validate(si: StockIn, insert: Boolean = true): String {
        val regexId = Regex("0") //TODO: Add in the regex pattern based on the needs
        var errorMessage = ""

        if (insert)/*if it is true */{
            errorMessage += if (si.ID== "") "- StockIn ID cannot be empty.\n"
            else if (!si.ID.matches(regexId)) "- StockIn ID format is invalid.\n"
            else if (idExists(si.ID)) "- StockIn ID is duplicated.\n" //if the function return true then error message will be added
            else ""
        }

        errorMessage += if (si.qty == 0) "- StockIn quantity is required. \n"
        else ""



        //TODO Add in more validation based on the needs of your fields

        return errorMessage
    }



}