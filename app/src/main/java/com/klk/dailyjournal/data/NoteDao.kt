package com.klk.dailyjournal.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface NoteDao {

    @Query("SELECT * from NoteEntity order by id")
    fun getAll(): LiveData<List<NoteEntity>>

    //@Query("SELECT name from NoteEntity order by name")
    //fun getAllNames(): LiveData<List<String>>

    @Query("SELECT * from NoteEntity where id = (:id)")
    fun getById(id: Int): LiveData<NoteEntity>

    @Insert
    fun insert(n: NoteEntity)

    @Update
    fun update(n: NoteEntity)

    @Delete
    fun delete(n: NoteEntity)

    @Query("DELETE from NoteEntity")
    fun deleteAll()
}