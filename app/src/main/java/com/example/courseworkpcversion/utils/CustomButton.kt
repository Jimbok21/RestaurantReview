package com.example.courseworkpcversion.utils

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatButton

class CustomButton(context: Context, attrs: AttributeSet):AppCompatButton(context, attrs) {

    //a custom button for the login page with a custom font
    init {
        applyFont()
    }

    private fun applyFont() {
        val typeface: Typeface =
            Typeface.createFromAsset(context.assets, "Bakeapple.ttf")
        setTypeface(typeface)
    }
}