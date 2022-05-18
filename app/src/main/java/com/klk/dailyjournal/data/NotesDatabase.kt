package com.klk.dailyjournal.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [NoteEntity::class], version=1)
abstract class NotesDatabase : RoomDatabase() {

    abstract fun noteDao(): NoteDao

}