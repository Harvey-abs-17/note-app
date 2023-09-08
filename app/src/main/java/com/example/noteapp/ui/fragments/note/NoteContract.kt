package com.example.noteapp.ui.fragments.note

import com.example.noteapp.data.model.NoteEntity
import com.example.noteapp.ui.base.BasePresenter

interface NoteContract {

    interface View{
        fun closeFragment()
        fun loadNoteData( noteEntity: NoteEntity )
    }

    interface Presenter :BasePresenter{
        fun insertNewNotePresenter(noteEntity: NoteEntity)
        fun getNoteByIdPresenter( noteId :Int )
        fun updateNotePresenter( noteEntity: NoteEntity )
    }

}