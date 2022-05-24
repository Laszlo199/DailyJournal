package com.klk.dailyjournal

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import androidx.core.graphics.drawable.toBitmap
import com.klk.dailyjournal.data.NoteEntity
import com.klk.dailyjournal.data.NoteRepository
import com.klk.dailyjournal.entities.Feeling
import kotlinx.android.synthetic.main.second_activity.*
import com.klk.dailyjournal.service.MoodImageStore
import java.io.File
import java.io.FileOutputStream
import java.lang.reflect.Array
import java.text.SimpleDateFormat
import java.util.*


class SecondActivity: AppCompatActivity(){
    val calendar = Calendar.getInstance()

    var photo: File? = null
    var pathPhoto: String? = null


    var mood: Feeling?= null
    var location: String? = null
    var address: String? = null
    val repo = NoteRepository.get()


    val TAG = "xyz"
    private val TAKE_PHOTO_PERMISSION_REQUEST_CODE = 1

    fun saveNote(view: View){
        val gratefulnessEdit = findViewById<TextView>(R.id.gratefulness_edit)
        val todayEdit = findViewById<TextView>(R.id.today_edit)
        val myNoteEdit = findViewById<TextView>(R.id.my_note_edit)

       /* val path = if (pathPhoto==null) photo?.path else pathPhoto*/
        var path: String? =null

        if(pathPhoto==null && photo?.path==null)
            path = null
        else if(pathPhoto!=null){
            path = "/storage/emulated/0/DCIM/camera/$pathPhoto"
        }
        else if(photo!=null){
            path =  photo?.path
        }
            
        
        repo.insert(NoteEntity(
            0,
            SimpleDateFormat("EEEE", Locale.ENGLISH).format(getDate()),
            "${getDayAsString()} ${getMonthName()}",
            MoodImageStore.getImageId().toString(),
            gratefulnessEdit.text.toString(),
            todayEdit.text.toString(),
            myNoteEdit.text.toString(),
            path,
            location,
            address))
    }





    fun pickImageFromGallery(view: View){
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false)
        singleImageResultLauncher.launch(Intent.createChooser(intent, "Select Picture"))
    }

    val singleImageResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                val selectedImageUri: Uri? = data?.data
                if (null != selectedImageUri) {
                    findViewById<ImageView>(R.id.take_photo_img).setImageURI(selectedImageUri)
                    photo=null
                    pathPhoto = getPathFromURI(selectedImageUri)
                }
            }
        }

    private fun getPathFromURI(uri: Uri?): String {
        var path = ""
        if (contentResolver != null) {
            val cursor = contentResolver.query(uri!!, null, null, null, null)
            if (cursor != null) {
                cursor.moveToFirst()
                val idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DISPLAY_NAME)
                path = cursor.getString(idx)
                cursor.close()
            }
        }
        return path
    }


    private fun getDate(): Date{
        return calendar.time
    }

    private fun getMonthName():String{
        val month_date = SimpleDateFormat("MMMM")
        return month_date.format(calendar.time)
    }

    private fun  getDayAsString(): String{
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        var dayStr = ""
        if(day.toString().endsWith('1'))
            dayStr = day.toString() +"st"
        else if(day.toString().endsWith('2'))
            dayStr = day.toString() +"nd"
        else if(day.toString().endsWith('3'))
            dayStr = day.toString() +"rd"
        else
            dayStr = day.toString() + "th"

        return dayStr
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.second_activity)

        val name = intent.getStringExtra("feeling_passed")
        mood = name?.let { getEnum(it) }

        val date = SimpleDateFormat("EEEE", Locale.ENGLISH).
        format(getDate()) +", "+ getMonthName() +
                " "+ getDayAsString()
        val dateT = findViewById<TextView>(R.id.date)
        dateT.text = date

        val imageView = findViewById<ImageView>(R.id.imgMood)
        imageView.setImageResource(GetImageId(MoodImageStore.getImageId()))

        checkPermissions()
    }

    fun GetImageId(eyes: Int): Int {
        if (eyes == 1) return R.drawable.mood_icon1
        if (eyes == 2) return R.drawable.mood_icon2
        if (eyes == 3) return R.drawable.mood_icon3
        if (eyes == 4) return R.drawable.mood_icon4
        return R.drawable.mood_icon5
    }

    private fun getEnum(name: String): Feeling{
        for(i in Feeling.values())
            if(name == i.name)
                return i

        return Feeling.OK
    }


    val fileCallback = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){ activityResult ->
        val mImage = findViewById<ImageView>(R.id.take_photo_img)
        if (activityResult.resultCode == RESULT_OK){
            pathPhoto=null
            showImageFromFile(mImage, photo!!)
            saveToGallery(photo!!)
        }
        else handleOther(activityResult.resultCode)
    }

    private fun saveToGallery(f: File){
        val bitmap = take_photo_img.drawable.toBitmap()
        val outputStream = FileOutputStream(f)
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        outputStream.flush()
        outputStream.close()

    }



    private fun showImageFromFile(img: ImageView,  f: File) {
        img.setImageURI(Uri.fromFile(f))
        img.setBackgroundColor(Color.WHITE)
    }

    fun onTakeByFile(view: View) {
        photo = getOutputMediaFile("Camera02")

        if (photo == null) {
            Toast.makeText(this, "Could not create file...", Toast.LENGTH_LONG).show()
            return
        }

        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        // Add extra to inform the app where to put the image.
        val applicationId = "com.klk.dailyjournal"
        intent.putExtra(
            MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(
            this,
            "${applicationId}.provider",  //use your app signature + ".provider"
            photo!!))

        fileCallback.launch(intent)
    }

    private fun getOutputMediaFile(folder: String): File? {
        // in an emulated device you can see the external files in /sdcard/Android/data/<your app>.
        //in this location pictures are stored
        val mediaStorageDir = File(this.getExternalFilesDir(Environment.DIRECTORY_PICTURES), folder)
        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(TAG, "failed to create directory")
                return null
            }
        }
        // Create a media file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val postfix = "jpg"
        val prefix = "IMG"

        return File(mediaStorageDir.path +
                File.separator + prefix +
                "_" + timeStamp + "." + postfix)
    }

    //Checks if the app has the required permissions, and prompts the user with the ones missing.
    private fun checkPermissions() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return
        val permissions = mutableListOf<String>()
        if ( ! isGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE) ) permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        if ( ! isGranted(Manifest.permission.CAMERA) ) permissions.add(Manifest.permission.CAMERA)
        if (permissions.size > 0)
            ActivityCompat.requestPermissions(this, permissions.toTypedArray(), TAKE_PHOTO_PERMISSION_REQUEST_CODE)
    }

    private fun isGranted(permission: String): Boolean =
        ActivityCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED

    private fun handleOther(resultCode: Int) {
        if (resultCode == RESULT_CANCELED)
            Toast.makeText(this, "Canceled...", Toast.LENGTH_LONG).show()
        else Toast.makeText(this, "Picture NOT taken - unknown error...", Toast.LENGTH_LONG).show()
    }

    fun goToLocationView(view: View){
        val intent = Intent(this, MapsActivity::class.java)
        resultLauncher.launch(intent)
    }

    private var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            location = result.data?.getStringExtra("latlng")
            address = result.data?.getStringExtra("address")

            locationAddressTv.text = address
        }
    }

}