package com.example.noteapp.ui.fragments.home

import com.example.noteapp.data.model.NoteEntity
import com.example.noteapp.ui.base.BasePresenter

interface HomeContract {

    interface View {
        fun loadPinnedNotes(notes: List<NoteEntity>)
        fun loadUpcomingNotes(notes: List<NoteEntity>)
        fun showLoading(isShown: Boolean)
        fun showEmpty()
    }

    interface Presenter : BasePresenter {
        fun getNotesPresenter(isPinned: Boolean)
    }

}