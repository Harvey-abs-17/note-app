package com.example.noteapp.ui.fragments.note

import com.example.noteapp.data.model.NoteEntity
import com.example.noteapp.ui.base.BasePresenter

interface NoteContract {

    interface View{
        fun closeFragment()
    }

    interface Presenter :BasePresenter{
        fun insertNewNote(noteEntity: NoteEntity)
    }

}