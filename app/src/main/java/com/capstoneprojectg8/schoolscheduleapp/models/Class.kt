package com.capstoneprojectg8.schoolscheduleapp.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
data class Class(
    val id: Int,
    var classCode: String,
    var className: String,
    val colour: Int,
    var isExpandable: Boolean = false,
) : Parcelable

