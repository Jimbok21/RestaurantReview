package com.example.courseworkpcversion.utils

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

class CustomTextView(context: Context, attrs:AttributeSet) : AppCompatTextView(context, attrs) {
    init {
        applyFont()
    }

    fun applyFont() {
        val typeface: Typeface = Typeface.createFromAsset(context.assets, "Foodpacker.otf")
        setTypeface(typeface)
    }
}