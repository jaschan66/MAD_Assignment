package my.com.abibasInven.data

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase
import android.text.TextUtils
import android.util.Patterns


class SupplierViewModel : ViewModel() {

    private val col = Firebase.firestore.collection("supplier")
    private val supplier = MutableLiveData<List<Supplier>>()
    private var supp = listOf<Supplier>()
    private val searchResult = MutableLiveData<List<Supplier>>()

    private var name = "" // for searching purpose

    init {
        col.addSnapshotListener { it, _ ->
            supplier.value = it?.toObjects()
            supp = it!!.toObjects<Supplier>()
            updateResult()
        }
    }

    fun get(id: String): Supplier? {
        return supplier.value?.find { it -> it.ID == id }
    }

    fun getAllSupplier() = supplier

    fun calSize() = supplier.value?.size ?: 0

    //set will be used for both adding and updating purpose
    fun set(s: Supplier) {
        col.document(s.ID).set(s)
    }

    fun searchSupplier(name: String) {
        this.name = name
        updateResult()
    }

    private fun updateResult() {
        var list = supp

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
        supplier.value?.forEach { it -> remove(it.ID) }
    }

    private fun idExists(id: String): Boolean {
        return supplier.value?.any { it -> it.ID == id }
            ?: false // if found return true if not found then return false
    }

    fun validate(s: Supplier, insert: Boolean = true): String {
        val regxName = Regex("^[\\p{L} .'-]+$")
        var errorMessage = ""

        //if the function return true then error message will be added
        if (insert)/*if it is true */ {
            errorMessage += if (idExists(s.ID)) "- Supplier ID is duplicated.\n"
            else ""
        }

        errorMessage += if (s.email == "") "- Email is required. \n"
        else if (!isValidEmail(s.email)) "- Email format is invalid. \n"
        else ""

        errorMessage += if (s.phoneNo == "") "- Phone no is required. \n"
        else if (!validCellPhone(s.phoneNo)) "- Phone no format is invalid. \n"
        else if (!validCellPhoneLength(s.phoneNo)) "- Phone no length is invalid. \n"
        else ""

        errorMessage += if (s.name == "") "- Name is required. \n"
        else if (!s.name.matches(regxName)) "- Name is invalid. \n"
        else ""

        errorMessage += if (s.latitude == 0.0) "- Latitude is required. \n"
        else ""

        errorMessage += if (s.longitude == 0.0) "- Longitude is required. \n"
        else ""

        //TODO Add in more validation based on the needs of your fields

        return errorMessage
    }

    private fun isValidEmail(target: CharSequence?): Boolean {
        return !TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches()
    }

    private fun validCellPhone(number: String?): Boolean {
        return Patterns.PHONE.matcher(number).matches()
    }

    private fun validCellPhoneLength(num: String?): Boolean {
        return (num?.length in 10..11)
    }

    fun validID(id: String): String {
        val newID: String
        val getLastSupplier = supplier.value?.lastOrNull()?.ID.toString()

        return if (idExists(id)) {
            val num: String = getLastSupplier.substringAfterLast("SU")
            newID = "SU" + (num.toIntOrNull()?.plus(1)).toString()
            newID
        } else {
            newID = "SU" + (calSize() + 1).toString()
            newID
        }
    }
}