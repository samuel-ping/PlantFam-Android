package com.planttrack.planttrack_android.service.model

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass
import io.realm.annotations.RealmField
import io.realm.annotations.Required
import org.bson.types.ObjectId
import java.time.LocalDate
import java.time.LocalDate.now
import java.util.*

@RealmClass
open class Plant(
    nickname: String? = null,
    commonName: String? = null,
    scientificName: String? = null,
    adoptionDate: Date = Date(),
    adoptedFrom: String? = null,
    isDeceased: Boolean? = null,
    deceasedDate: Date? = null,
    parent: Plant? = null
) : RealmObject() {
    @PrimaryKey @RealmField("_id") var id : ObjectId = ObjectId()

    var nickname: String? = nickname
    var commonName: String? = commonName
    var scientificName: String? = scientificName
    @Required var adoptionDate: Date = adoptionDate
    var adoptedFrom: String? = adoptedFrom
    var isDeceased: Boolean? = isDeceased
    var deceasedDate: Date? = deceasedDate
    var parent: Plant? = parent
}
