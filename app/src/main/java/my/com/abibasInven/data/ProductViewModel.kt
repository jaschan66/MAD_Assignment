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
    private val productHaveQty = MutableLiveData<List<Product>>()
    private val productLessQty = MutableLiveData<List<Product>>()
    private var supplierProduct = MutableLiveData<List<Product>>()
    private val result = MutableLiveData<List<Product>>()

    private var name = "" // for searching purpose


    init {
        col.addSnapshotListener { it, _ ->
            product.value = it?.toObjects()
            productHaveLocation.value = product.value?.filter { it -> it.locationID != "" }
            productHaveQty.value = product.value?.filter { it -> it.qty != 0 }
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

    fun getAllProductHaveQty() = productHaveQty

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




    private fun productExists(name: String): Boolean {
        return product.value?.any { it -> it.name.equals(name, true) } ?: false
    }

    fun validate(p: Product): String {
        var errorMessage = ""

        errorMessage += if (p.name == "") "- Name is required. \n"
        else if (productExists(p.name)) "- Product name already exists. \n"
        else ""

        errorMessage += if (p.photo.toBytes().isEmpty()) "- Product photo is required. \n"
        else ""

        errorMessage += if (p.qtyThreshold <= 0) "- Product quantity threshold is required. \n"
        else ""


        //TODO Add in more validation based on the needs of your fields

        return errorMessage
    }

    fun validID(): String {
        var newID: String

        val getLastProduct = product.value?.lastOrNull()?.ID.toString()
        val num: String = getLastProduct.substringAfterLast("PR10")
        newID = "PR10" + (num.toIntOrNull()?.plus(1)).toString()

        if (newID == "PR1010") {
            newID = "PR1" + (num.toIntOrNull()?.plus(1)).toString()
            return newID
        }

        return when (calSize()) {
            0 -> {
                newID = "PR10" + (calSize() + 1)
                newID
            }
            in 1..8 -> {
                val getLastProduct = product.value?.lastOrNull()?.ID.toString()
                val num: String = getLastProduct.substringAfterLast("PR10")
                newID = "PR10" + (num.toIntOrNull()?.plus(1)).toString()
                if (newID == "PR10null") {
                    newID = "PR111"
                    newID
                } else newID
            }
            else -> {
                val getLastProduct = product.value?.lastOrNull()?.ID.toString()
                val num: String = getLastProduct.substringAfterLast("PR1")
                newID = "PR1" + (num.toIntOrNull()?.plus(1)).toString()
                newID
            }
        }

    }

}