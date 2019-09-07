package com.example.lisa.persistence

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "journal")
data class Journal (@PrimaryKey @ColumnInfo(name = "date") val date: String
                      , @ColumnInfo(name = "study_time") val studyTime: Int) {

}