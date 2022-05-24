package com.klk.dailyjournal

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.klk.dailyjournal.data.NoteEntity
import com.klk.dailyjournal.data.NoteRepository
import com.klk.dailyjournal.entities.Feeling
import com.klk.dailyjournal.service.MoodImageStore
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    val feeling: Feeling = Feeling.OK

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContentView(R.layout.activity_main)
        val imageButton1 = findViewById<ImageButton>(R.id.imgFace1)
        val imageButton2 = findViewById<ImageButton>(R.id.imgFace2)
        val imageButton3 = findViewById<ImageButton>(R.id.imgFace3)
        val imageButton4 = findViewById<ImageButton>(R.id.imgFace4)
        val imageButton5 = findViewById<ImageButton>(R.id.imgFace5)
        imageButton1.setOnClickListener {
            MoodImageStore.add(1)
        }
        imageButton2.setOnClickListener {
            MoodImageStore.add(2)
        }
        imageButton3.setOnClickListener {
            MoodImageStore.add(3)
        }
        imageButton4.setOnClickListener {
            MoodImageStore.add(4)
        }
        imageButton5.setOnClickListener {
            MoodImageStore.add(5)
        }

        NoteRepository.initialize(this)
        //insertTestData()
        //val repo = NoteRepository.get()
        //repo.clear()

        setupDataObserver()
    }

     fun makeJournalNote(view: View){

        //pass info to the second intent
        val intent = Intent(this, SecondActivity::class.java)
        intent.putExtra("feeling_passed", feeling.name)
        startActivity(intent)
    }

    //for now we also reset the data !! (as long as we work with test data, to not add the same objects with every refresh)
    private fun insertTestData() {
        val repo = NoteRepository.get()
        repo.clear()
        repo.insert(NoteEntity(
            0, "Monday", "April 22nd", "5",
            "nice weather",
            "nutella for breakfast",
            "I met my friends today, we watch some movies, then we went to have fun. long long long long lllloooong",
            null, null, "Skolegade 209, Esbjerg"))
        repo.insert(NoteEntity(
            0, "Tuesday", "April 21st", "3",
            null,
            null,
            "I met my friends today, we watch some movies, then we went to have fun",
            null, null, null))
        repo.insert(NoteEntity(
            0, "Wednesday", "April 20th", "2",
            "nice weather",
            "nutella for breakfast",
            "wow wow",
            null, null, null))
    }


    private fun setupDataObserver() {
        val repo = NoteRepository.get()
        val getAllObserver = Observer<List<NoteEntity>>{ notes ->
            val adapter: ListAdapter = NotesAdapter(this, notes)
            notesList.adapter = adapter
        }
        repo.getAll().observe(this, getAllObserver)

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
            val addressView = resView.findViewById<TextView>(R.id.tvAddress)
            val imageView = resView.findViewById<ImageView>(R.id.takenPhoto1)

            val date = n.dayOfWeek + ", " + n.date
            dateView.text = date
            textView.text = n.note
            moodView.setImageResource(GetImageId(n.mood.toInt()))
            addressView.text = n.address



            if(n.image?.isNotEmpty() == true){
                setImage(n, imageView)
            }

            return resView
        }

        private fun setImage(n: NoteEntity, imageView: ImageView?) {
            val bmOptions = BitmapFactory.Options()
            var bitmap = BitmapFactory.decodeFile(n.image?.toString(), bmOptions)
            val ratio = bitmap.height / bitmap.width
            val heigh = 400
            val width = heigh/ratio
            bitmap = Bitmap.createScaledBitmap(bitmap, width, heigh, false)
            imageView?.setImageBitmap(bitmap)
        }



        fun GetImageId(eyes: Int): Int {
            if (eyes == 1) return R.drawable.mood_icon1
            if (eyes == 2) return R.drawable.mood_icon2
            if (eyes == 3) return R.drawable.mood_icon3
            if (eyes == 4) return R.drawable.mood_icon4
            return R.drawable.mood_icon5
        }
    }
}