package com.example.noteapp.ui.fragments.search

import android.util.Log
import com.example.noteapp.data.repository.SearchRepository
import com.example.noteapp.ui.base.BasePresenterImpl
import com.example.noteapp.utils.ERROR_TAG
import com.example.noteapp.utils.Utils
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class SearchPresenter @Inject constructor(
    private val repository: SearchRepository,
    private val view: SearchContract.View
) : BasePresenterImpl(), SearchContract.Presenter {

    override fun getSearchData(
        searchTxt: String
    ) {
        view.showLoading(true)
        disposable = Observable.timer(2000, TimeUnit.MILLISECONDS)
            .flatMap {
                return@flatMap repository.searchNoteByTxtRepository(searchTxt)
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                view.showLoading(false)
                if (it.isEmpty()) {
                    view.showEmptyLayout(true)
                } else {
                    view.showEmptyLayout(false)
                    view.loadSearchRecData(it)
                }
            }, {
                Log.e(ERROR_TAG, it.message.toString())
            })
    }

}