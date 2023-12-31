package br.edu.scl.ifsp.ads.oneMenssageChat.model

import android.os.Parcelable
import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity
data class Contact(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = -1,
    @NonNull
    var name: String = "",
    @NonNull
    var email: String = ""
): Parcelable