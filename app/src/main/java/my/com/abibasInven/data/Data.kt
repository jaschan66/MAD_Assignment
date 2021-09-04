package my.com.abibasInven.data

import com.google.firebase.firestore.Blob
import com.google.firebase.firestore.DocumentId
import java.util.*



data class StockOut(
    @DocumentId
    var ID          : String = "",
    var OutletID    : String = "",
    var DateTime    : Date = Date(),
    var Status      : String = ""
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
    var availability : String = "",
    var latitude     : Double = 0.0,
    var longitude    : Double = 0.0,
    var photo        : Blob = Blob.fromBytes(ByteArray(0))
)

data class Product (
    @DocumentId
    var ID           : String = "",
    var name         : String = "",
    var qty          : Int = 0,
    var qtyThreshold : Int = 0,
    var categoryID     : String = "",
    var photo        : Blob = Blob.fromBytes(ByteArray(0)),
    var locationID     : String = "",
    var supplierID   : String = ""
)

data class StockIn (
    @DocumentId
    var ID           : String = "",
    var productID    : String = "",
    var qty          : Int = 0,
    var dateTime     : Date = Date()
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
    var name         : String = ""
        )
