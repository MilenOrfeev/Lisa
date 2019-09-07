package com.example.lisa.persistence

import androidx.annotation.WorkerThread

class JournalRepository(private val journalDao: JournalDao) {

    @WorkerThread
    suspend fun insert(journalEntry: Journal) {
        journalDao.insert(journalEntry)
    }

    @WorkerThread
    suspend fun findStudyTimeByDate(date: String) : Int {
        return journalDao.findStudyTimeByDate(date)
    }
}
