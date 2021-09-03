package my.com.abibasInven.data

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase

class CategoryViewModel : ViewModel() {

    private val col = Firebase.firestore.collection("category")
    private val category = MutableLiveData<List<Category>>()

    init {
        col.addSnapshotListener { it, _ -> category.value = it?.toObjects()  }
    }


    fun get(id: String): Category? {
        return category.value?.find { it -> it.ID == id}
    }

    fun getAll() = category

    //set will be used for both adding and updating purpose
    fun set(c:Category) {
        col.document(c.ID).set(c)
    }

    fun remove(id: String) {
        col.document(id).delete()
    }

    fun deleteAll() {
        category.value?.forEach { it -> remove(it.ID) }
    }

    private fun idExists(id: String): Boolean {
        return category.value?.any { it -> it.ID == id } ?: false // if found return true if not found then return false
    }

    fun validate(c: Category, insert: Boolean = true): String {
        val regexId = Regex("0") //TODO: Add in the regex pattern based on the needs
        var errorMessage = ""

        if (insert)/*if it is true */{
            errorMessage += if (c.ID== "") "- Category ID cannot be empty.\n"
            else if (!c.ID.matches(regexId)) "- Category ID format is invalid.\n"
            else if (idExists(c.ID)) "- Category ID is duplicated.\n" //if the function return true then error message will be added
            else ""
        }

        errorMessage += if (c.name == "") "- Category name is required. \n"
        else ""



        //TODO Add in more validation based on the needs of your fields

        return errorMessage
    }



}