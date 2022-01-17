package com.plantfam.plantfam.service.model

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass
import io.realm.annotations.RealmField
import io.realm.annotations.Required
import org.bson.types.ObjectId
import java.util.*

@RealmClass(embedded = true)
open class ProgressPhoto(
    var imageURL: String? = null,
    var dateTaken: Date = Date()
) : RealmObject()

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
    var parent: Plant? = null,
    var coverPhoto: String? = null,
    var photos: RealmList<ProgressPhoto> = RealmList()
) : RealmObject() {
    @PrimaryKey
    @RealmField("_id")
    var id: ObjectId = ObjectId()
}
