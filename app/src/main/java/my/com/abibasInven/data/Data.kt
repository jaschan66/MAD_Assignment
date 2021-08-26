package my.com.abibasInven.data

import com.google.firebase.firestore.Blob
import com.google.firebase.firestore.DocumentId
import java.util.*

data class Delivery(
    @DocumentId
    var deliveryID     : String = "",
    var orderID        : String = "",
    var deliveryStatus : String = "",
    var userId         : String = "",
    var deliveryDate   : Date = Date()
)

data class Order(
    var orderID          : String = "",
    var orderType        : String = "",
    var orderDestination : String = "",
    var orderQty         : Int = 0
)

data class OrderDetails(
    var orderDetailsID   : String = "",
    var productID        : String = "",
    var orderID          : String = ""
)

data class Outlet (
    var outletID           : String = "",
    var outletName         : String = "",
    var outletLocation     : String = "",
    var outletAvailability : String = "",
    var outletPhoto        : Blob = Blob.fromBytes(ByteArray(0))
)

data class Product (
    var productID           : String = "",
    var productName         : String = "",
    var productQty          : Int = 0,
    var productQtyThreshold : Int = 0,
    var productCategory     : String = "",
    var productPhoto        : Blob = Blob.fromBytes(ByteArray(0)),
    var productLocation     : String = "",
    var supplierID          : String = ""
)

data class Stock (
    var stockID      : String = "",
    var productID    : String = "",
    var stockInQty   : Int = 0,
    var stockOutQty  : Int = 0,
    var dateTime     : Date = Date()
)

data class Supplier (
    var supplierID           : String = "",
    var supplierName         : String = "",
    var phoneNo              : String = "",
    var supplierEmail        : String = "",
    var supplierLocation     : String = "",
    var supplierDetails      : String = "",
    var supplierAvailability : String = ""
)
