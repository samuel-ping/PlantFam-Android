package com.planttrack.planttrack_android.service.model

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass
import io.realm.annotations.RealmField
import org.bson.types.ObjectId
import java.util.*

@RealmClass
open class Plant : RealmObject() {
    @PrimaryKey @RealmField("_id") var _id : ObjectId = ObjectId()
//    var partition: String? = null

    var nickname: String? = null
    var commonName: String? = null
    var scientificName: String? = null
    var adoptionDate: Date = Date()
    var adoptedFrom: String? = null
    var isDeceased: Boolean? = null
    var deceasedDate: Date = Date()
    var parent: Plant? = null
}
