package com.example.noteapp.data.repository

import com.example.noteapp.data.db.NoteDao
import com.example.noteapp.data.model.NoteEntity
import javax.inject.Inject

class HomeRepository @Inject constructor(private val noteDao: NoteDao) {

    fun getAllNotesRepository() = noteDao.getAllNotes()

    fun getNotesByPinnedValueRepository(isPinned: Boolean) = noteDao.getNotesByPinnedValue(isPinned)

    fun getNoteByIdRepository( noteId: Int ) = noteDao.getNoteById(noteId)

    fun deleteNoteRepository( noteEntity: NoteEntity ) = noteDao.deleteNote(noteEntity)


}