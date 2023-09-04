package com.example.noteapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.noteapp.utils.TABLE_NAME
import com.example.noteapp.utils.Utils

@Entity(tableName = TABLE_NAME)
data class NoteEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var title: String = "",
    var description: String = "",
    var create_date: String = "",
    var modify_date: String = "",
    var create_time: String = "",
    var modify_time: String = "",
    var color: Utils.Color = Utils.Color.Blue,
    var category: Utils.Category = Utils.Category.Home,
    var isPinned: Boolean = false
)
