package com.example.noteapp.utils

import android.content.Context
import androidx.core.content.ContextCompat
import com.example.noteapp.R

class Utils {

    // manage note item background
    fun setBackgroundColor(color: Color, context: Context): Int {
        return when (color) {
            Color.White -> {
                ContextCompat.getColor(context, R.color.white)
            }

            Color.Red -> {
                ContextCompat.getColor(context, R.color.Crayola)
            }

            Color.Yellow -> {
                ContextCompat.getColor(context, R.color.SunGlow)
            }

            Color.Orange -> {
                ContextCompat.getColor(context, R.color.SafetyOrange)
            }

            Color.Blue -> {
                ContextCompat.getColor(context, R.color.Blue)
            }

            Color.Green -> {
                ContextCompat.getColor(context, R.color.Emerald)
            }
        }
    }

    enum class Category {
        Home,
        Meetings,
        Misc,
        Personal,
        Work
    }

    enum class Color {
        White,
        Orange,
        Blue,
        Yellow,
        Green,
        Red
    }

}