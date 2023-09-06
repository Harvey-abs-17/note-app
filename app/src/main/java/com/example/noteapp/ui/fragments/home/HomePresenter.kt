package com.example.noteapp.ui.fragments.home

import android.util.Log
import com.example.noteapp.data.model.NoteEntity
import com.example.noteapp.data.repository.HomeRepository
import com.example.noteapp.ui.base.BasePresenterImpl
import com.example.noteapp.utils.ERROR_TAG
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class HomePresenter @Inject constructor(
    private val repository: HomeRepository,
    private val view: HomeContract.View
) : BasePresenterImpl(), HomeContract.Presenter {


    //get pinned notes
    private fun getPinnedNotesPresenter() {
        disposable = repository.getNotesByPinnedValueRepository(true)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (it.isEmpty()) {
                    view.visiblePinnedView(false)
                } else {
                    view.visiblePinnedView(true)
                    view.loadPinnedNotes(it)
                }
            }, {
                Log.e(ERROR_TAG, it.message.toString())
            })
    }

    //get upcoming notes
    private fun getUpcomingNotesPresenter() {
        disposable = repository.getNotesByPinnedValueRepository(false)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (it.isEmpty()) {
                    view.visibleUpcomingView(false)
                } else {
                    view.visibleUpcomingView(true)
                    view.loadUpcomingNotes(it)
                }
            }, {
                Log.e(ERROR_TAG, it.message.toString())
            })
    }

    //get all notes
    override fun getAllNotePresenter() {
        view.showLoading(true)
        disposable = Observable.timer(2000, TimeUnit.MILLISECONDS)
            .flatMap {
                return@flatMap repository.getAllNotesRepository()
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                view.showLoading(false)
                if (it.isNotEmpty()) {
                    view.showEmpty(false)
                    getPinnedNotesPresenter()
                    getUpcomingNotesPresenter()
                } else {
                    view.showEmpty(true)
                }
            }, {
                Log.e(ERROR_TAG, it.message.toString())
            })

    }


}