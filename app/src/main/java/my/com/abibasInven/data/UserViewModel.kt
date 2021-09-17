package my.com.abibasInven.data

import android.content.Context
import android.content.SharedPreferences
import android.text.TextUtils
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase

class UserViewModel : ViewModel() {

    private val col = Firebase.firestore.collection("user")
    private val user = MutableLiveData<List<User>>()
    private var USER = listOf<User>()
    private val staff = MutableLiveData<List<User>>()
    private val userLiveData = MutableLiveData<User>()
    private val result = MutableLiveData<List<User>>()

    private var name = "" // for searching purpose

    init {
        col.addSnapshotListener { it, _ -> user.value = it?.toObjects()
            USER = user.value?.filter { it -> it.role == "Staff" }!!
            updateResult()
        }
    }

    private fun updateResult() {
        var list = USER

        //Search
        list = list.filter {
            it.name.contains(name, true)
        }

        result.value = list
    }

    fun get(email: String): User? {
        return user.value?.find { it -> it.email == email}
    }

    fun getResult() = result

    fun getAll() = user

    fun getAllStaff() = staff

    //set will be used for both adding and updating purpose
    fun set(u:User) {
        col.document(u.email).set(u)
    }

    fun search(name: String){
        this.name = name
        updateResult()
    }


    fun remove(email: String) {
        col.document(email).delete()
    }

    fun deleteAll() {
        user.value?.forEach { it -> remove(it.email) }
    }

    fun deleteAllStaff() {
        staff.value?.forEach { it -> remove(it.email) }
    }

    private fun idExists(email: String): Boolean {
        return user.value?.any { it -> it.email == email } ?: false // if found return true if not found then return false
    }




    fun validate(u: User, insert: Boolean = true): String {
        val regxName = Regex("^[\\p{L} .'-]+$")
        var errorMessage = ""

        errorMessage += if (u.email == "") "- Email is required. \n"
        else if (!isValidEmail(u.email)) "- Email format is invalid. \n"
        else ""

        errorMessage += if (u.role == "") "- User role is required. \n"
        else ""

        errorMessage += if (u.name == "") "- Name is required. \n"
        else if (!u.name.matches(regxName)) "- Name is invalid. \n"
        else ""

        errorMessage += if (u.photo.toBytes().isEmpty()) "- User photo is required. \n"
        else ""

        errorMessage += if (u.password == "") "- User password is required. \n"
        else ""

        //TODO Add in more validation based on the needs of your fields

        return errorMessage
    }

    private fun isValidEmail(target: CharSequence?): Boolean {
        return !TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches()
    }





}