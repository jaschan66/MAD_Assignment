package my.com.abibasInven.data

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase
import com.google.protobuf.LazyStringArrayList
import java.util.*
import kotlin.collections.ArrayList

class LocationViewModel : ViewModel() {

    private val col = Firebase.firestore.collection("location")
    private val colRack = Firebase.firestore.collection("rackType")
    private val rackType = MutableLiveData<List<RackType>>()
    private val location = MutableLiveData<List<Location>>()
    private val rack = MutableLiveData<List<Location>>()
    private val rackMaxCapacity = MutableLiveData<List<Location>>()


    init {

        col.addSnapshotListener { snap, _ -> location.value = snap?.toObjects()
            rack.value = location.value?.filter { it.rackType != it.rackType }
            rackMaxCapacity.value = location.value?.filter{it.maxCapacity!=0}



        }
        colRack.addSnapshotListener { snap, _ -> rackType.value = snap?.toObjects() }


//            var rackList : MutableList<String> = ArrayList()
//            var forLocationList : MutableList<Location> = ArrayList()
//
//            for(loca in location.value!! ){
//                if(loca.rackType in rackList){
//                    continue
//                }else{
//                    rackList.add(loca.rackType)
//                    forLocationList.add(loca)
//                }
//            }


//            var test =  MutableLiveData(forLocationList)

    }

    fun getMaxCapacity() = rackMaxCapacity

    fun getLocationSize() = location.value?.size ?:0

    fun getRackByRackType(id : String): Location?{
        return location.value?.find { l -> l.ID == id }
    }

    fun get(id : String): Location?{

        return location.value?.find { l -> l.ID == id }
    }




    fun getAll() = location

    fun getAllRack() = rackType

    fun delete(id : String){
        col.document(id).delete()
    }

    fun deleteRack(id : String){
        colRack.document(id).delete()
    }

    fun deleteAll(){
        location.value?.forEach{ l -> delete(l.ID)}
    }

    fun deleteAllRack(id: String){
        location.value?.forEach{ l ->
            if( l.rackType == id ){
                delete(l.ID)
            }
        }
    }

    fun set(l: Location){
        col.document(l.ID).set(l)
    }

    fun setRackType(r: RackType){
        colRack.document(r.ID).set(r)
    }

    //-------------------------------------------------------------------
    // Validation

//    private fun nameExists(name: String): Boolean {
//        return rewards.value?.any{ r -> r.rewardName == name } ?: false
//    }
//
//    fun validate(r: Reward, insert: Boolean = true): String {
//
//        var e = ""
//        val Date = Date()
//
//        //name
//        e += if (r.rewardName == "") "- Reward Name is required.\n"
//        else if (r.rewardName.length < 3) "- Reward Name is too short.\n"
//        else if (nameExists(r.rewardName)) "- Reward Name is duplicated.\n"
//        else ""
//
//        //Description
//        e += if (r.rewardDescrip == "") "- Description is required.\n"
//        else if (r.rewardDescrip.length < 3) "- Description is to short.\n"
//        else ""
//
//        //Quantity
//        e += if (r.rewardQuan == 0) "- Quantity cannot be 0. \n"
//        else ""
//
//        //Point
//        e += if (r.rewardPoint == 0 ) "- Point cannot be 0. \n"
//        else ""
//
//        //Expiry Date
//        e += if (r.expiryDate == Date.toString()) "- Expiry date cannot be today. \n"
//        else if (r.expiryDate == "") "- Please Select Expiry Date. \n"
//        else ""
//
//        //Photo
//        e += if (r.rewardPhoto.toBytes().isEmpty()) "- Reward Photo is required.\n"
//        else ""
//
//        return e
//    }

}