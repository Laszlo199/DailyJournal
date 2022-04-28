package com.klk.dailyjournal

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.ListAdapter
import android.widget.TextView
import com.klk.dailyjournal.data.NoteEntity
import com.klk.dailyjournal.data.NoteRepository
import java.util.*
import androidx.lifecycle.Observer
import com.klk.dailyjournal.R
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        NoteRepository.initialize(this)
        insertTestData()

        setupDataObserver()
    }

    private fun insertTestData() {
        val repo = NoteRepository.get()
        repo.insert(NoteEntity(
            0, "Monday", "April 22nd", "face_temporary.png",
            "nice weather",
            "nutella for breakfast",
            "I met my friends today, we watch some movies, then we went to have fun. long long long long lllloooong",
            null, null))
        repo.insert(NoteEntity(
            0, "Tuesday", "April 21st", "face_temporary.png",
            null,
            null,
            "I met my friends today, we watch some movies, then we went to have fun",
            null, null))
        repo.insert(NoteEntity(
            0, "Wednesday", "April 20th", "face_temporary.png",
            "nice weather",
            "nutella for breakfast",
            "wow wow",
            null, null))
    }


    private fun setupDataObserver() {
        val repo = NoteRepository.get()
        val getAllObserver = Observer<List<NoteEntity>>{ notes ->
            val adapter: ListAdapter = NotesAdapter(this, notes)
            notesList.adapter = adapter
        }
        repo.getAll().observe(this, getAllObserver)

        //lvNames.onItemClickListener = AdapterView.OnItemClickListener { parent, view, pos, id -> onClickPerson(parent, pos)}
    }

    internal class NotesAdapter(context: Context, private val notes: List<NoteEntity>)
        : ArrayAdapter<NoteEntity>(context, 0, notes)
    {

        override fun getView(position: Int, v: View?, parent: ViewGroup): View {
            var v1: View? = v
            if (v1 == null) {
                val mInflater = LayoutInflater.from(context)
                v1 = mInflater.inflate(R.layout.notes_card, null)

            }
            val resView: View = v1!!

            val n = notes[position]
            val dateView = resView.findViewById<TextView>(R.id.tvDate)
            val textView = resView.findViewById<TextView>(R.id.tvNote)
            val moodView = resView.findViewById<ImageView>(R.id.imgMoodIcon)

            val date = n.dayOfWeek + ", " + n.date
            dateView.text = date
            textView.text = n.note
            moodView.setImageResource(R.drawable.face_temporary)

            return resView
        }
    }
}