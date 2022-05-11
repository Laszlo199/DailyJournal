package com.klk.dailyjournal.service

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDateTime

object MoodImageStore {
    private var imageId: Int = 0

    fun getImageId() = imageId

    @RequiresApi(Build.VERSION_CODES.O)
    fun add(a : Int) {
        imageId = a
    }
}