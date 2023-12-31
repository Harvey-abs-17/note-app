package com.example.noteapp.ui.fragments.note

import android.util.Log
import com.example.noteapp.data.model.NoteEntity
import com.example.noteapp.data.repository.NoteRepository
import com.example.noteapp.ui.base.BasePresenterImpl
import com.example.noteapp.utils.ERROR_TAG
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class NotePresenter @Inject constructor(
    private val repository: NoteRepository,
    private val view: NoteContract.View
) : BasePresenterImpl(), NoteContract.Presenter {

    override fun insertNewNotePresenter(noteEntity: NoteEntity) {
        disposable = repository.insertNewNoteRepository(noteEntity)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                view.closeFragment()
            }, {
                Log.e(ERROR_TAG, it.message.toString())
            })
    }

    override fun getNoteByIdPresenter(noteId: Int) {
        disposable = repository.getNoteByIdRepository(noteId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                view.loadNoteData(it)
            }, {
                Log.e(ERROR_TAG, it.message.toString())
            })
    }

    override fun updateNotePresenter(noteEntity: NoteEntity) {
        disposable = repository.updateNoteRepository(noteEntity)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                view.closeFragment()
            }, {
                Log.e(ERROR_TAG, it.message.toString())
            })
    }

}