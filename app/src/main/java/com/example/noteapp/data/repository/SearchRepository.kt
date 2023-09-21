package com.example.noteapp.data.repository

import com.example.noteapp.data.db.NoteDao
import com.example.noteapp.utils.Utils
import javax.inject.Inject

class SearchRepository @Inject constructor(private val noteDao: NoteDao) {

    fun searchNoteByTxtRepository( searchTxt :String ) = noteDao.searchNoteByTxt(searchTxt)

}