package com.klk.dailyjournal.data

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room
import java.lang.IllegalStateException
import java.util.concurrent.Executors

class NoteRepository private constructor(private val context: Context) {

    private val database: NotesDatabase = Room.databaseBuilder(context.applicationContext,
        NotesDatabase::class.java,
        "note-database").build()

    private val noteDao = database.noteDao()

    fun getAll(): LiveData<List<NoteEntity>> = noteDao.getAll()

    //fun getAllNames(): LiveData<List<String>> = personDao.getAllNames()

    fun getById(id: Int) = noteDao.getById(id)

    private val executor = Executors.newSingleThreadExecutor()

    fun insert(n: NoteEntity) {
        executor.execute{ noteDao.insert(n) }
    }

    fun update(n: NoteEntity) {
        executor.execute { noteDao.update(n) }
    }

    fun delete(n: NoteEntity) {
        executor.execute { noteDao.delete(n) }
    }

    fun clear() {
        executor.execute { noteDao.deleteAll() }
    }


    companion object {
        private var Instance: NoteRepository? = null

        fun initialize(context: Context) {
            if (Instance == null)
                Instance = NoteRepository(context)
        }

        fun get(): NoteRepository {
            if (Instance != null) return Instance!!
            throw IllegalStateException("Notes repo not initialized")
        }
    }
}