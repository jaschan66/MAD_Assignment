package my.com.abibasInven.data

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase

class ProductViewModel : ViewModel() {

    private val col = Firebase.firestore.collection("product")
    private val product = MutableLiveData<List<Product>>()
    private val productHaveLocation = MutableLiveData<List<Product>>()

    init {
        col.addSnapshotListener { it, _ -> product.value = it?.toObjects()
        productHaveLocation.value = product.value?.filter { it -> it.locationID !="" }

        }
    }


    fun get(id: String): Product? {
        return product.value?.find { it -> it.ID == id}
    }

    fun getAll() = product

    fun getAllProductHaveLocation() = productHaveLocation

//    fun getSpecificProductForRack(locationID: String): Product?{
//        return productHaveLocation.value?.find { it -> it.locationID == locationID}
//    }

    //set will be used for both adding and updating purpose
    fun set(p:Product) {
        col.document(p.ID).set(p)
    }

    fun remove(id: String) {
        col.document(id).delete()
    }

    fun calSize() = product.value?.size ?: 0

    fun deleteAll() {
        product.value?.forEach { it -> remove(it.ID) }
    }

    private fun idExists(id: String): Boolean {
        return product.value?.any { it -> it.ID == id } ?: false // if found return true if not found then return false
    }

    fun validate(p: Product): String {
        val regexId = Regex("0") //TODO: Add in the regex pattern based on the needs
        var errorMessage = ""

        errorMessage += if (p.categoryID == "") "- Product name is required. \n"
        else ""

        errorMessage += if (p.locationID == "") "- Product name is required. \n"
        else ""

        errorMessage += if (p.supplierID == "") "- Product name is required. \n"
        else ""

        errorMessage += if (p.name == "") "- Product name is required. \n"
        else ""

        errorMessage += if (p.name == "") "- Product name is required. \n"
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
            val num: String = getLastProduct.substringAfterLast("P")
            newID = "P00" + (num.toIntOrNull()?.plus(1)).toString()
            newID
        } else {
            newID = "P00" + (calSize() + 1).toString()
            newID
        }
    }



}