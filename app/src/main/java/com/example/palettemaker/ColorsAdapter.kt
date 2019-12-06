package com.example.palettemaker

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import androidx.core.graphics.ColorUtils


class ColorsAdapter(context: Context, private var colors: ArrayList<String>) : BaseAdapter() {
    private var context: Context? = context

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val color = colors[position]

        var resultingView = convertView
        if (resultingView == null) {
            val layoutInflater = LayoutInflater.from(context)
            resultingView = layoutInflater.inflate(R.layout.color_grid_cell, null)
        }

        val colorButton = resultingView!!.findViewById<Button>(R.id.color_square)

        colorButton.text = color
        if (ColorUtils.calculateContrast(Color.WHITE, Color.parseColor(color)) < 4) { // 4 is arbitrary
            colorButton.setTextColor(Color.BLACK)
        } else {
            colorButton.setTextColor(Color.WHITE)
        }
        colorButton.background.setTint(Color.parseColor(color))

        return resultingView
    }

    override fun getItem(index: Int): Any {
        return colors[index]
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getCount(): Int {
        return colors.size
    }

    fun setColors(colors: ArrayList<String>) {
        this.colors = colors
    }

    fun addColor(color: String) {
        this.colors.add(color)
    }

    fun removeColor(color: String) {
        this.colors.remove(color)
    }

    fun getColors(): ArrayList<String> {
        return this.colors
    }
}