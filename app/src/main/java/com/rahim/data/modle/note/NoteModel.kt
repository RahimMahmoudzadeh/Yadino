package com.rahim.data.modle.note

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "tbl_note")
@Parcelize
data class NoteModel(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,
    var name: String,
    var description: String,
    var isChecked: Boolean = false,
    var state: Int = 0
) : Parcelable