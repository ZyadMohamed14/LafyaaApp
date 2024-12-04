package com.example.ecommerceapp.features.user.domain

import android.os.Parcelable
import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.Timestamp
import com.google.firebase.firestore.PropertyName
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "user_details")
data class UserDetailsModel(
    @get:PropertyName("created_at")
    @set:PropertyName("created_at")
    @SerializedName("created_at")
    var createdAt: Timestamp? = Timestamp.now(),
  @PrimaryKey var id: String,
    var email: String? = null,
    var name: String? = null,
    var image:String?=null,
    var phone: String? = null,
    var disabled: Boolean? = null,
    var reviews: List<String>? = null,
) : Parcelable{
    constructor() : this(createdAt = Timestamp.now(), id = "")
    @Override
    override fun toString(): String {
        return "UserDetailsModel(createdAt=$createdAt, id=$id, email=$email, name=$name, image=$image, phone=$phone, disabled=$disabled, reviews=$reviews)"
    }

}
data class UserDetailsResponse(
    val code: Int,
    val data: UserDetailsModel,
    val message: String
)