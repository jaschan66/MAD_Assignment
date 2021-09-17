package my.com.abibasInven.data

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase

class ProductViewModel : ViewModel() {

    private val col = Firebase.firestore.collection("product")
    private var PRODUCT = listOf<Product>()
    private val product = MutableLiveData<List<Product>>()
    private val productHaveLocation = MutableLiveData<List<Product>>()
    private val productLessQty = MutableLiveData<List<Product>>()
    private var supplierProduct = MutableLiveData<List<Product>>()
    private val result = MutableLiveData<List<Product>>()

    private var name = "" // for searching purpose


    init {
        col.addSnapshotListener { it, _ ->
            product.value = it?.toObjects()
            productHaveLocation.value = product.value?.filter { it -> it.locationID != "" }
            productLessQty.value = product.value?.filter { it -> it.qty < it.qtyThreshold }
            PRODUCT = it!!.toObjects<Product>()
            updateResult()
        }
    }

    fun getSupplierProduct(supp: String): List<Product>? {
        return product.value?.filter { it -> it.supplierID == supp }
    }

    fun getCategoryProduct(cate: String): List<Product>? {
        return product.value?.filter { it -> it.categoryID == cate }
    }

    fun get(id: String): Product? {
        return product.value?.find { it -> it.ID == id }
    }

    fun getAll() = product

    fun search(name: String){
        this.name = name
        updateResult()
    }

    fun getProductLessQty() = productLessQty

    private fun updateResult() {
        var list = PRODUCT

        //Search
        list = list.filter {
            it.name.contains(name, true)
        }

        result.value = list
    }

    fun getAllProductHaveLocation() = productHaveLocation

//    fun getSpecificProductForRack(locationID: String): Product?{
//        return productHaveLocation.value?.find { it -> it.locationID == locationID}
//    }

    //set will be used for both adding and updating purpose
    fun set(p: Product) {
        col.document(p.ID).set(p)
    }

    fun remove(id: String) {
        col.document(id).delete()
    }

    fun calSize() = product.value?.size ?: 0

    fun deleteAll() {
        product.value?.forEach { it -> remove(it.ID) }
    }

    fun getResult() = result


    private fun idExists(id: String): Boolean {
        return product.value?.any { it -> it.ID == id }
            ?: false // if found return true if not found then return false
    }

    fun validate(p: Product): String {
        val regxName = Regex("^[\\p{L} .'-]+$")
        var errorMessage = ""

        errorMessage += if (p.categoryID == "") "- Product name is required. \n"
        else ""

        errorMessage += if (p.name == "") "- Name is required. \n"
        else if (!p.name.matches(regxName)) "- Name is invalid. \n"
        else ""

        errorMessage += if (p.locationID == "") "- Product name is required. \n"
        else ""

        errorMessage += if (p.supplierID == "") "- Product name is required. \n"
        else ""

        errorMessage += if (p.photo.toBytes().isEmpty()) "- Product photo is required. \n"
        else ""

        errorMessage += if (p.qtyThreshold == 0) "- Product quantity threshold is required. \n"
        else ""


        //TODO Add in more validation based on the needs of your fields

        return errorMessage
    }

    fun validID(id: String): String {
        val newID: String
        val getLastProduct = product.value?.lastOrNull()?.ID.toString()

        return if (idExists(id)) {
            val num: String = getLastProduct.substringAfterLast("PR")
            newID = "PR" + (num.toIntOrNull()?.plus(1)).toString()
            newID
        } else {
            newID = "PR" + (calSize() + 1).toString()
            newID
        }
    }

}