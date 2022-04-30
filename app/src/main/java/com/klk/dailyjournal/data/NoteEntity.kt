package com.klk.dailyjournal.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Date

@Entity
class NoteEntity (
    @PrimaryKey(autoGenerate = true) var id:Int,
    var dayOfWeek: String,
    var date: String,
    var mood: String,
    var gratefulFor: String?,
    var bestPartOfDay: String?,
    var note: String?,
    var image: String?,
    var location: String?){
}