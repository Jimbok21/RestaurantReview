package com.example.courseworkpcversion.utils

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

//a custom textView with a custom font
class CustomTextView(context: Context, attrs:AttributeSet) : AppCompatTextView(context, attrs) {
    init {
        applyFont()
    }

    private fun applyFont() {
        val typeface: Typeface = Typeface.createFromAsset(context.assets, "Bakeapple.ttf")
        setTypeface(typeface)
    }
}