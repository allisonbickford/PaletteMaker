package com.example.palettemaker

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.GridView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.DialogFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.io.File
import java.io.IOException
import java.text.DateFormat
import java.util.*
import kotlin.collections.ArrayList


class PaletteActivity: AppCompatActivity(), GalleryDialogFragment.GalleryDialogListener {
    val REQUEST_IMAGE_CAPTURE = 1
    val COLOR_SELECTOR_INTENT = 2
    val REQUEST_GALLERY_SELECT = 3
    var currentPhotoPath: String = ""
    var colors: ArrayList<String> = ArrayList()
    var colorsAdapter = ColorsAdapter(this, colors)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_palette)
        val cameraButton = findViewById<FloatingActionButton>(R.id.camera_button)
        cameraButton.setOnClickListener { dispatchTakePictureIntent() }

        val galleryButton = findViewById<FloatingActionButton>(R.id.gallery_select)
        val galleryDialog = GalleryDialogFragment()
        galleryButton.setOnClickListener {
            if (currentPhotoPath.isEmpty()) {
                dispatchGalleryImageIntent()
            } else {
                galleryDialog.show(supportFragmentManager, "GalleryDialogFragment")
            }
        }

        val gridView = findViewById<GridView>(R.id.palette_view)
        gridView.adapter = colorsAdapter
    }

    override fun onDialogGalleryClick(dialog: DialogFragment) {
        dispatchGalleryImageIntent()
    }

    override fun onDialogLastImageClick(dialog: DialogFragment) {
        val selectorIntent = Intent(this, ColorSelectorActivity::class.java)
        selectorIntent.putExtra("image_path", currentPhotoPath)
        startActivityForResult(selectorIntent, COLOR_SELECTOR_INTENT)
    }

    private fun dispatchGalleryImageIntent() {
        getGalleryPermission()
        val photoPickerIntent = Intent(Intent.ACTION_PICK)
        photoPickerIntent.type = "image/*"
        startActivityForResult(photoPickerIntent, REQUEST_GALLERY_SELECT)
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
        } else if (requestCode == COLOR_SELECTOR_INTENT) {
            val colorHexCode = data?.extras?.get("color")
            if (colorHexCode != null) {
                this.colorsAdapter.addColor(colorHexCode as String)
                this.colorsAdapter.notifyDataSetChanged()
            }
        } else if (requestCode == REQUEST_GALLERY_SELECT && resultCode == RESULT_OK) {
            val selectorIntent = Intent(this, ColorSelectorActivity::class.java)
            Log.d("DATA****", data?.data.toString())
            val selectedImage = data?.data
            if (selectedImage != null) {
                selectorIntent.putExtra("image_path", getRealPathFromURI(selectedImage))
                startActivityForResult(selectorIntent, COLOR_SELECTOR_INTENT)
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    /**
     * refactored from
     * https://stackoverflow.com/questions/36336498/get-real-path-from-android-uri-after-selecting-image-from-gallery
     */
    private fun getRealPathFromURI(contentURI: Uri): String {
        val path: String
        val cursor = contentResolver.query(contentURI, null, null, null, null)
        if (cursor == null) {
            path = contentURI.path.orEmpty()
        } else {
            cursor.moveToFirst()
            val idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
            path = cursor.getString(idx)
            cursor.close()
        }
        return path
    }

    private fun getGalleryPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED||
            ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ),
                1052)
        }
    }
}