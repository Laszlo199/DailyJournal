package com.klk.dailyjournal

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import com.klk.dailyjournal.data.NoteEntity
import com.klk.dailyjournal.data.NoteRepository
import com.klk.dailyjournal.service.EditIdStore
import com.klk.dailyjournal.service.MoodImageStore
import kotlinx.android.synthetic.main.activity_edit.*
import java.net.URI


class EditActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)


        updateNote()
        delete()
    }


    fun updateNote(){
        val up = NoteRepository.get()
        val id = EditIdStore.getNoteId()
        val moodID = intent.getStringExtra("mood").toString()
        val date = intent.getStringExtra("date").toString()
        val day = intent.getStringExtra("dayOfWeek").toString()
        val image = intent.getStringExtra("image").toString()
        val location = intent.getStringExtra("location").toString()
        val toka = intent.getStringExtra("image").toString()
        val uri = Uri.parse(toka)

        editNote.setText(intent.getStringExtra("note"))
        editBest.setText(intent.getStringExtra("best"))
        setdate.setText(intent.getStringExtra("dayOfWeek")+", "+intent.getStringExtra("date"))
        editAddress.setText(intent.getStringExtra("address"))
        editGrateFul.setText(intent.getStringExtra("grate"))
        imgMood.setImageResource(GetImageId(MoodImageStore.getImageId()))
        photo.setImageURI(uri)

        updateBtn.setOnClickListener{
            up.update(NoteEntity(id,
                day,
                date,
                moodID,
                editGrateFul.text.toString(),
                editBest.text.toString(),
                editNote.text.toString(),
                image,
                location,
                editAddress.text.toString()))
            finish()
        }


    }

    fun delete(){
        val delete = NoteRepository.get()
        val id = EditIdStore.getNoteId()
        val day = intent.getStringExtra("dayOfWeek").toString()
        val moodID = intent.getStringExtra("mood").toString()
        val date = intent.getStringExtra("date").toString()
        val image = intent.getStringExtra("image").toString()
        val location = intent.getStringExtra("location").toString()
        val note =  intent.getStringExtra("note").toString()
        val best = intent.getStringExtra("best").toString()
        val address = intent.getStringExtra("address").toString()
        val grate = intent.getStringExtra("grate").toString()

        deleteBtn.setOnClickListener{
            delete.delete(
                NoteEntity(
                    id,
                    day,
                    date,
                    moodID,
                    grate,
                    best,
                    note,
                    image,
                    location,
                    address
                )
            )
            finish()
        }
    }


    fun GetImageId(eyes: Int): Int {
        if (eyes == 1) return R.drawable.mood_icon1
        if (eyes == 2) return R.drawable.mood_icon2
        if (eyes == 3) return R.drawable.mood_icon3
        if (eyes == 4) return R.drawable.mood_icon4
        return R.drawable.mood_icon5
    }
}