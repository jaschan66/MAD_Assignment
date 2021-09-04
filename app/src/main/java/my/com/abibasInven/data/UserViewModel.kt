package my.com.abibasInven.data

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase

class UserViewModel : ViewModel() {

    private val col = Firebase.firestore.collection("user")
    private val user = MutableLiveData<List<User>>()

    init {
        col.addSnapshotListener { it, _ -> user.value = it?.toObjects()  }
    }


    fun get(email: String): User? {
        return user.value?.find { it -> it.email == email}
    }

    fun getAll() = user

    //set will be used for both adding and updating purpose
    fun set(u:User) {
        col.document(u.email).set(u)
    }

    fun remove(id: String) {
        col.document(id).delete()
    }

    fun deleteAll() {
        user.value?.forEach { it -> remove(it.email) }
    }

    private fun idExists(email: String): Boolean {
        return user.value?.any { it -> it.email == email } ?: false // if found return true if not found then return false
    }

    fun validate(u: User, insert: Boolean = true): String {
        //val regexId = Regex("") //TODO: Add in the regex pattern based on the needs
        var errorMessage = ""


        errorMessage += if (u.role == "") "- User role is required. \n"
        else ""

        errorMessage += if (u.name == "") "- User name is required. \n"
        else ""

        errorMessage += if (u.photo.toBytes().isEmpty()) "- User photo is required. \n"
        else ""

        errorMessage += if (u.password == "") "- User password is required. \n"
        else ""

        //TODO Add in more validation based on the needs of your fields

        return errorMessage
    }



}