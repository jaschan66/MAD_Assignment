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
        col.addSnapshotListener { it, _ -> category.value = it?.toObjects() }
    }


    fun get(id: String): Category? {
        return category.value?.find { it -> it.ID == id }
    }

    fun getAllCategory() = category

    fun calSize() = category.value?.size ?: 0

    //set will be used for both adding and updating purpose
    fun set(c: Category) {
        col.document(c.ID).set(c)
    }

    fun remove(id: String) {
        col.document(id).delete()
    }

    fun deleteAll() {
        category.value?.forEach { it -> remove(it.ID) }
    }

    private fun idExists(id: String): Boolean {
        return category.value?.any { it -> it.ID == id }
            ?: false // if found return true if not found then return false
    }

    private fun categoryExists(name: String): Boolean {
        return category.value?.any { it -> it.name == name } ?: false
    }

    fun validate(c: Category, insert: Boolean = true): String {
        var errorMessage = ""

        if (insert)/*if it is true */ {
            errorMessage += if (idExists(c.ID)) "- Category ID is duplicated.\n" //if the function return true then error message will be added
            else ""
        }

        errorMessage += if (c.name == "") "- Category name is required. \n"
        else if (categoryExists(c.name)) "- Category name exists. \n"
        else ""


        //TODO Add in more validation based on the needs of your fields

        return errorMessage
    }

    fun validID(): String {
        var newID: String

        val getLastSupplier = category.value?.lastOrNull()?.ID.toString()
        val num: String = getLastSupplier.substringAfterLast("CA10")
        newID = "CA10" + (num.toIntOrNull()?.plus(1)).toString()

        if (newID == "CA1010") {
            newID = "CA1" + (num.toIntOrNull()?.plus(1)).toString()
            return newID
        }

        return when (calSize()) {
            0 -> {
                newID = "CA10" + (calSize() + 1)
                newID
            }
            in 1..8 -> {
                val getLastSupplier = category.value?.lastOrNull()?.ID.toString()
                val num: String = getLastSupplier.substringAfterLast("CA10")
                newID = "CA10" + (num.toIntOrNull()?.plus(1)).toString()
                if (newID == "CA10null") {
                    newID = "CA111"
                    newID
                } else newID
            }
            else -> {
                val getLastSupplier = category.value?.lastOrNull()?.ID.toString()
                val num: String = getLastSupplier.substringAfterLast("CA1")
                newID = "CA1" + (num.toIntOrNull()?.plus(1)).toString()
                newID
            }
        }

//        if (calSize() <= 1) {
//            newID = "CA10" + (calSize() + 1)
//            return newID
//        } else {
//            val getLastSupplier = category.value?.lastOrNull()?.ID.toString()
//            newID = "CA10" + calSize() + 1
//
//            val num: String = getLastSupplier.substringAfterLast("CA10")
//            num
//
//            if (getLastSupplier != "CA110") {
//                if (idExists(newID)) {
//                    val num: String = getLastSupplier.substringAfterLast("CA10")
//                    num
//                    if (num.toInt() >= 9) {
//                        newID = "CA1" + (num.toIntOrNull()?.plus(1)).toString()
//                        return newID
//                    } else {
//                        newID = "CA10" + (num.toIntOrNull()?.plus(1)).toString()
//                        return newID
//                    }
//                } else {
//                    val num: String = getLastSupplier.substringAfterLast("CA10")
//                    num
//                    if (num.toInt() >= 9) {
//                        newID = "CA1" + (num.toIntOrNull()?.plus(1)).toString()
//                        return newID
//                    } else {
//                        newID = "CA10" + (num.toIntOrNull()?.plus(1)).toString()
//                        return newID
//                    }
//                }
//            } else {
//                if (idExists(newID)) {
//                    val num: String = getLastSupplier.substringAfterLast("CA1")
//                    num
//                    if (num.toInt() >= 9) {
//                        newID = "CA1" + (num.toIntOrNull()?.plus(1)).toString()
//                        return newID
//                    } else {
//                        newID = "CA1" + (num.toIntOrNull()?.plus(1)).toString()
//                        return newID
//                    }
//                } else {
//                    val num: String = getLastSupplier.substringAfterLast("CA1")
//                    num
//                    if (num.toInt() >= 9) {
//                        newID = "CA1" + (num.toIntOrNull()?.plus(1)).toString()
//                        return newID
//                    } else {
//                        newID = "CA1" + (num.toIntOrNull()?.plus(1)).toString()
//                        return newID
//
//                    }
//                }
//            }
//        }
    }

}