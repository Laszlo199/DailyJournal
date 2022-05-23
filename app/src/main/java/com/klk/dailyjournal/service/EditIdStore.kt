package com.klk.dailyjournal.service

import android.os.Build
import androidx.annotation.RequiresApi

object EditIdStore {
    private var noteId: Int = 0

    fun getNoteId() = noteId

    @RequiresApi(Build.VERSION_CODES.O)
    fun add(a : Int) {
        noteId = a
    }
}