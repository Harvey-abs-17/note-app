package com.example.noteapp.ui.fragments.search

import com.example.noteapp.data.model.NoteEntity
import com.example.noteapp.ui.base.BasePresenter
import com.example.noteapp.utils.Utils

interface SearchContract {

    interface View {
        fun showEmptyLayout(isShown: Boolean)
        fun showEmptyHintLayout(isShown: Boolean)
        fun loadSearchRecData(notes: List<NoteEntity>)
        fun showFilterFragment()
        fun showLoading(isShown: Boolean)
    }

    interface Presenter : BasePresenter {
        fun getSearchData(
            searchTxt: String
        )
    }

}