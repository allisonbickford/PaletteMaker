package com.example.palettemaker

import android.content.Intent
import android.os.Bundle
import android.widget.GridView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton

class AllPalettesActivity: AppCompatActivity() {
    val NEW_PALETTE_ACTIVITY = 1
    var palettesAdapter = PalettesAdapter(this, ArrayList())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_palettes)

        val gridView = findViewById<GridView>(R.id.all_palettes_view)
        gridView.adapter = palettesAdapter

        val newPaletteBtn = findViewById<FloatingActionButton>(R.id.add_palette_btn)
        newPaletteBtn.setOnClickListener {
            val paletteIntent = Intent(this, PaletteActivity::class.java)
            startActivityForResult(paletteIntent, NEW_PALETTE_ACTIVITY)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == NEW_PALETTE_ACTIVITY) {
            val palette = data?.extras?.get("colors") as ArrayList<String>
            val name = data.extras?.get("name") as String
            palettesAdapter.addPalette(Palette(name, palette))
            palettesAdapter.notifyDataSetChanged()
        }
    }
}