package my.com.abibasInven.data

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase

class SpinnerViewModel : ViewModel() {

    private val col = Firebase.firestore.collection("location")
    private val col1 = Firebase.firestore.collection("supplier")
    private val col2 = Firebase.firestore.collection("category")
    private val col3 = Firebase.firestore.collection("product")
    private val col4 = Firebase.firestore.collection("outlet")
    private val col5 = Firebase.firestore.collection("delivery")

    private val location = MutableLiveData<List<Location>>()
    private val supplier = MutableLiveData<List<Supplier>>()
    private val category = MutableLiveData<List<Category>>()
    private val product = MutableLiveData<List<Product>>()
    private val outlet = MutableLiveData<List<Outlet>>()
    private val delivery = MutableLiveData<List<Delivery>>()

    init {
        col.addSnapshotListener { it, _ -> location.value = it?.toObjects() }
        col1.addSnapshotListener { it, _ -> supplier.value = it?.toObjects() }
        col2.addSnapshotListener { it, _ -> category.value = it?.toObjects() }
        col3.addSnapshotListener { it, _ -> product.value = it?.toObjects() }
        col4.addSnapshotListener { it, _ -> outlet.value = it?.toObjects() }
        col5.addSnapshotListener { it, _ -> delivery.value = it?.toObjects() }
    }

    fun getDelivery() = delivery

    fun getLocation() = location

    fun getSupplierName() = supplier

    fun getCategory() = category

    fun getProduct() = product

    fun getOutlet() = outlet

    fun calSupSize() = supplier.value?.size ?: 0

    fun calLocSize() = location.value?.size ?: 0

    fun calCategorySize() = category.value?.size ?:0

    fun calProductSize() = product.value?.size ?:0

    fun calOutletSize() = outlet.value?.size ?:0

    fun calDeliverySize() = delivery.value?.size ?:0


//    fun get(id: String): Category? {
//        return category.value?.find { it -> it.ID == id}
//    }
//
//    fun getAll() = category
//
//    //set will be used for both adding and updating purpose
//    fun set(c:Category) {
//        col.document(c.ID).set(c)
//    }
//
//    fun remove(id: String) {
//        col.document(id).delete()
//    }
//
//    fun deleteAll() {
//        category.value?.forEach { it -> remove(it.ID) }
//    }
//
//    private fun idExists(id: String): Boolean {
//        return category.value?.any { it -> it.ID == id } ?: false // if found return true if not found then return false
//    }
//
//    fun validate(c: Category, insert: Boolean = true): String {
//        val regexId = Regex("0") //TODO: Add in the regex pattern based on the needs
//        var errorMessage = ""
//
//        if (insert)/*if it is true */{
//            errorMessage += if (c.ID== "") "- Category ID cannot be empty.\n"
//            else if (!c.ID.matches(regexId)) "- Category ID format is invalid.\n"
//            else if (idExists(c.ID)) "- Category ID is duplicated.\n" //if the function return true then error message will be added
//            else ""
//        }
//
//        errorMessage += if (c.name == "") "- Category name is required. \n"
//        else ""
//
//
//
//        //TODO Add in more validation based on the needs of your fields
//
//        return errorMessage
//    }



}