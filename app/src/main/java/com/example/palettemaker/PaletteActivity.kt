package com.example.palettemaker

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.ImageView
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.activity_color_selector.*
import java.io.File
import java.io.IOException
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class PaletteActivity: AppCompatActivity() {
    val REQUEST_IMAGE_CAPTURE = 1
    val COLOR_SELECTOR_INTENT = 2
    var currentPhotoPath: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_palette)
        val cameraButton = findViewById<FloatingActionButton>(R.id.camera_button)
        cameraButton.setOnClickListener { dispatchTakePictureIntent() }
    }

    /**
     * This code has been taken from the guide:
     * https://developer.android.com/training/camera/photobasics
     */
    @Throws(IOException::class)
    private fun createImageFile(): File {
        DateFormat.getDateTimeInstance()
        val timeStamp: String = DateFormat.getDateTimeInstance().format(Date())
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = absolutePath
            galleryAddPic()
        }
    }

    /**
     * This code has been taken from the guide:
     * https://developer.android.com/training/camera/photobasics
     */
    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            // Ensure that there's a camera activity to handle the intent
            takePictureIntent.resolveActivity(packageManager)?.also {
                // Create the File where the photo should go
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    // TODO: tell user we failed
                    null
                }
                // Continue only if the File was successfully created
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                        this,
                        "com.example.android.fileprovider",
                        it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
                }
            }
        }
    }

    /**
     * This code has been taken from the guide:
     * https://developer.android.com/training/camera/photobasics
     */
    private fun galleryAddPic() {
        Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE).also { mediaScanIntent ->
            val f = File(currentPhotoPath)
            mediaScanIntent.data = Uri.fromFile(f)
            sendBroadcast(mediaScanIntent)
        }
    }

    /**
     * This code has been taken from the guide:
     * https://developer.android.com/training/camera/photobasics
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val selectorIntent = Intent(this, ColorSelectorActivity::class.java)
            selectorIntent.putExtra("image_path", currentPhotoPath)
            startActivityForResult(selectorIntent, COLOR_SELECTOR_INTENT)
        } else if (requestCode == COLOR_SELECTOR_INTENT && resultCode == RESULT_OK) {
            val colorHexCode = data?.extras?.get("color")
            if (colorHexCode != null) {
                
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

}