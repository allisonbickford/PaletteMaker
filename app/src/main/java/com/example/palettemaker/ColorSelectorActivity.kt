package com.example.palettemaker

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import android.graphics.drawable.BitmapDrawable
import androidx.core.graphics.toColor
import com.google.android.material.button.MaterialButton
import kotlin.math.roundToInt


class ColorSelectorActivity: AppCompatActivity() {
    var hexColor = "#000000"

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_color_selector)
        val extras = intent.extras
        val imageBitmap = extras?.get("image")
        val imageView = findViewById<ImageView>(R.id.color_selector_image_view)
        imageView.setImageBitmap(imageBitmap as Bitmap?)

        // move selector to wherever user clicked on image
        val selector = findViewById<MovableFloatingActionButton>(R.id.selector_button)
        selector.x = imageView.width / 2.toFloat()
        selector.y = imageView.height / 2.toFloat()
        imageView.setOnTouchListener { view, event -> moveButton(view, event, selector) }

        // updates the color when dragging the button
        selector.setOnTouchListener { view, motionEvent -> updateColor(); selector.onTouch(view, motionEvent) }

        // confirm selection
        findViewById<MaterialButton>(R.id.confirm_color_button).setOnClickListener { selectColor() }
    }

    private fun moveButton(view: View?, event: MotionEvent?, selector: MovableFloatingActionButton): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                selector.x = event.x
                selector.y = event.y
                updateColor() // change the color
            }
            MotionEvent.ACTION_UP -> {
                view?.performClick()
            }
        }
        return view?.onTouchEvent(event) ?: true
    }

    private fun updateColor() {
        val imageView = findViewById<ImageView>(R.id.color_selector_image_view)
        val bitmap = (imageView.drawable as BitmapDrawable).bitmap
        val selector = findViewById<MovableFloatingActionButton>(R.id.selector_button)

        // ratios to convert imageView coordinate to bitmap coordinate
        // inspired by https://stackoverflow.com/questions/11334984/correspondence-between-imageview-coordinates-and-bitmap-pixels-android
        val widthRatio = bitmap.width.toDouble() / imageView.width.toDouble()
        val heightRatio = bitmap.height.toDouble() / imageView.height.toDouble()

        // account for button size (grab color from middle, not sides)
        val xLoc = (selector.x + (selector.width / 2)) * widthRatio
        val yLoc = (selector.y + (selector.height / 2)) * heightRatio
        val pixel = bitmap.getPixel(xLoc.roundToInt(), yLoc.roundToInt())

        val selectButton = findViewById<MaterialButton>(R.id.confirm_color_button)
        hexColor = String.format("#%06X", 0xFFFFFF and pixel.toColor().toArgb())
        selectButton.background.setTint(pixel.toColor().toArgb())
        selectButton.text = hexColor
    }

    private fun selectColor() {
        var result = Intent()
        // TODO: put color selection here and add to result
        result.putExtra("color", hexColor)
        setResult(Activity.RESULT_OK, result)
        finish()
    }
}