package com.example.noteapp.utils

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager

fun RecyclerView.initRec(myAdapter: RecyclerView.Adapter<*>, layoutManager: LayoutManager) {
    this.adapter = myAdapter
    this.layoutManager = layoutManager
}

fun View.showView(isShown: Boolean) {
    if (isShown) {
        this.visibility = View.VISIBLE
    } else {
        this.visibility = View.GONE
    }
}