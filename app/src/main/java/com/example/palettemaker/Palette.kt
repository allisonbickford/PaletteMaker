package com.example.palettemaker

class Palette(private var name: String, private var colors: ArrayList<String> = ArrayList()) {

    fun getColors(): ArrayList<String> {
        return this.colors
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

    fun setName(name: String) {
        this.name = name
    }

    fun getName(): String {
        return name
    }
}