package com.example.lisa.persistence

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface JournalDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(journal: JournalDao)

    @Query("SELECT study_time from journal WHERE date LIKE :date")
    suspend fun findStudyTimeByDate(date: String)
}