package com.example.palettemaker

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class ColorSelectorActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_color_selector)
        val extras = intent.extras
        val imageBitmap = extras?.get("image")
        findViewById<ImageView>(R.id.color_selector_image_view).setImageBitmap(imageBitmap as Bitmap?)
        val selector = findViewById<ImageView>(R.id.selector_button)
        
    }

    private fun selectColor() {
        var result = Intent()
        // TODO: put color selection here and add to result
        setResult(Activity.RESULT_OK, result)
        finish()
    }
}