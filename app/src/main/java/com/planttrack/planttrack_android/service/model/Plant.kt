package com.planttrack.planttrack_android.service.model

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass
import io.realm.annotations.RealmField
import io.realm.annotations.Required
import org.bson.types.ObjectId
import java.util.*

@RealmClass
open class Plant(
    @Required var _partition: String? = "",
    var nickname: String? = null,
    var commonName: String? = null,
    var scientificName: String? = null,
    @Required var adoptionDate: Date = Date(),
    var adoptedFrom: String? = null,
    var isDeceased: Boolean? = null,
    var deceasedDate: Date? = null,
    var parent: Plant? = null
) : RealmObject() {
    @PrimaryKey @RealmField("_id") var id : ObjectId = ObjectId()
}
