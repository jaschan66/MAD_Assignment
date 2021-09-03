package my.com.abibasInven.data

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase

class ProductViewModel : ViewModel() {

    private val col = Firebase.firestore.collection("product")
    private val product = MutableLiveData<List<Product>>()

    init {
        col.addSnapshotListener { it, _ -> product.value = it?.toObjects()  }
    }


    fun get(id: String): Product? {
        return product.value?.find { it -> it.ID == id}
    }

    fun getAll() = product

    //set will be used for both adding and updating purpose
    fun set(p:Product) {
        col.document(p.ID).set(p)
    }

    fun remove(id: String) {
        col.document(id).delete()
    }

    fun deleteAll() {
        product.value?.forEach { it -> remove(it.ID) }
    }

    private fun idExists(id: String): Boolean {
        return product.value?.any { it -> it.ID == id } ?: false // if found return true if not found then return false
    }

    fun validate(p: Product, insert: Boolean = true): String {
        val regexId = Regex("0") //TODO: Add in the regex pattern based on the needs
        var errorMessage = ""

        if (insert)/*if it is true */{
            errorMessage += if (p.ID== "") "- Product ID cannot be empty.\n"
            else if (!p.ID.matches(regexId)) "- Product ID format is invalid.\n"
            else if (idExists(p.ID)) "- Product ID is duplicated.\n" //if the function return true then error message will be added
            else ""
        }

        errorMessage += if (p.name == "") "- Product name is required. \n"
        else ""

        errorMessage += if (p.qty == 0) "- Product quantity is required. \n"
        else ""

        errorMessage += if (p.photo.toBytes().isEmpty()) "- Product photo is required. \n"
        else ""

        errorMessage += if (p.qtyThreshold == 0) "- Product quantity threshold is required. \n"
        else ""


        //TODO Add in more validation based on the needs of your fields

        return errorMessage
    }



}