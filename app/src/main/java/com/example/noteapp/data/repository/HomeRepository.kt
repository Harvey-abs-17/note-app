package com.example.noteapp.data.repository

import com.example.noteapp.data.db.NoteDao
import javax.inject.Inject

class HomeRepository @Inject constructor(private val noteDao: NoteDao) {

    fun getAllNotesRepository() = noteDao.getAllNotes()

    fun getNotesByPinnedValueRepository(isPinned: Boolean) = noteDao.getNotesByPinnedValue(isPinned)

}