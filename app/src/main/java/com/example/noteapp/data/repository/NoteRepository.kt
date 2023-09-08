package com.example.noteapp.data.repository

import com.example.noteapp.data.db.NoteDao
import com.example.noteapp.data.model.NoteEntity
import javax.inject.Inject

class NoteRepository @Inject constructor(private val noteDao: NoteDao) {

    fun insertNewNoteRepository(noteEntity: NoteEntity) = noteDao.insertNewNote(noteEntity)

    fun getNoteByIdRepository( noteId :Int ) = noteDao.getNoteById( noteId )

    fun updateNoteRepository(noteEntity: NoteEntity) = noteDao.updateNote( noteEntity )

}