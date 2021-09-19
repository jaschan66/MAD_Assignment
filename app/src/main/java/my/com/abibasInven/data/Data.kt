package my.com.abibasInven.data

import com.google.firebase.firestore.Blob
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.Exclude
import java.util.*
import kotlin.collections.ArrayList

var img : Blob = Blob.fromBytes(ByteArray(0))
var emailLogin : String = ""
var passwordLogin : String = ""
//var dli : ArrayList<DeliveryItem> = ArrayList<DeliveryItem>()



data class StockOut(
    @DocumentId
    var ID          : String = "",
    var dateTime    : String = "",
    var deliveryID  : String = "",
    var qty         : Int = 0,
)

data class Item(
    @DocumentId
    var ID               : String = "",
    var productID        : String = "",
    var stockOutID          : String = "",
    var qty              : Int = 0
)

data class Outlet (
    @DocumentId
    var ID           : String = "",
    var name         : String = "",
    var latitude     : Double = 0.0,
    var longitude    : Double = 0.0,
    var photo        : Blob = Blob.fromBytes(ByteArray(0)),
    var pin          : String = "",
)

data class Product (
    @DocumentId
    var ID           : String = "",
    var name         : String = "",
    var qty          : Int = 0,
    var qtyThreshold : Int = 0,
    var categoryID   : String = "",
    var photo        : Blob = Blob.fromBytes(ByteArray(0)),
    var locationID   : String = "",
    var supplierID   : String = ""
)

data class StockIn (
    @DocumentId
    var ID           : String = "",
    var productID    : String = "",
    var qty          : Int = 0,
    var dateTime     : String = "",
)

data class Supplier (
    @DocumentId
    var ID           : String = "",
    var name         : String = "",
    var phoneNo      : String = "",
    var email        : String = "",
    var latitude     : Double = 0.0,
    var longitude    : Double = 0.0,
)

data class User (
    @DocumentId
    var email        : String = "",
    var role         : String = "",
    var name         : String = "",
    var photo        : Blob = Blob.fromBytes(ByteArray(0)),
    var attempt      : Int = 0,
    var password     : String = "",
)

data class Category (
    @DocumentId
    var ID           : String = "",
    var name         : String = "",
)

data class Location (
    @DocumentId
    val ID       : String = "",
    val rackType : String = "",
    val categoryID       : String = "",
    val occupiedCapacity : Int = 0,
    val maxCapacity : Int = 0,
)

data class RackType (
    @DocumentId
    var ID: String = "",
    var rackData: String = "",
)

data class Delivery(
    @DocumentId
    var ID: String = "",
    var outletID : String = "",
    var deliveryStatus : String = "",
)

data class DeliveryItem(
    @DocumentId
    var ID: String = "",
    var productID : String = "",
    var deliveryQty : Int = 0,
    var deliveryID : String = "",
    var deliveryItemPhoto : Blob = Blob.fromBytes(ByteArray(0)),
    var stockOutID : String = "",
)
