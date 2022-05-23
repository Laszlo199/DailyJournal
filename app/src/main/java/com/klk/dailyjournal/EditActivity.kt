package com.klk.dailyjournal

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import com.klk.dailyjournal.data.NoteEntity
import com.klk.dailyjournal.data.NoteRepository
import com.klk.dailyjournal.service.EditIdStore
import com.klk.dailyjournal.service.MoodImageStore
import kotlinx.android.synthetic.main.activity_edit.*


class EditActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)



        editNote.setText(intent.getStringExtra("note"))
        editBest.setText(intent.getStringExtra("best"))
        editDate.setText(intent.getStringExtra("dayOfWeek")+","+intent.getStringExtra("date"))
        editAddress.setText(intent.getStringExtra("address"))
        editGrateFull.setText(intent.getStringExtra("grate"))
        imgMood.setImageResource(GetImageId(MoodImageStore.getImageId()))
        updateNote()
    }


    fun updateNote(){
        val up = NoteRepository.get()
        val id = EditIdStore.getNoteId()
        saveNew.setOnClickListener{
            up.update(NoteEntity(id, "",editDate.text.toString(), MoodImageStore.getImageId().toString(), editGrateFull.text.toString(),editBest.text.toString(),editNote.text.toString(),null,null,editAddress.text.toString()))
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
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