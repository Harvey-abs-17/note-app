package com.example.noteapp.data.model

import androidx.room.Entity
import com.example.noteapp.utils.TABLE_NAME

@Entity(tableName = TABLE_NAME)
data class NoteEntity(
    val id: Int = 0,
    var title: String = "",
    var description: String = "",
    var create_date: String = "",
    var modify_date: String = "",
    var create_time: String = "",
    var modify_time: String = "",
    var color: Color = Color.Green,
    var category: Category = Category.Home,
    var isPinned: Boolean = false
)

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
    Pink,
    Green,
    Red
}
