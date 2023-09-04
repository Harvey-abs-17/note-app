package com.example.noteapp.ui.fragments.home

import com.example.noteapp.data.repository.HomeRepository
import com.example.noteapp.ui.base.BasePresenterImpl
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class HomePresenter @Inject constructor(
    private val repository: HomeRepository,
    private val view: HomeContract.View
) : BasePresenterImpl(), HomeContract.Presenter {

    //get notes from repository ( pinned and upcoming )
    override fun getNotesPresenter(isPinned: Boolean) {
        //show loading progress bar
        view.showLoading(true)
        //get note data depends on is pinned value
        disposable = repository.getNotesRepository(isPinned = isPinned)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .delay(2000, TimeUnit.MILLISECONDS)
            .subscribe {
                view.showLoading(false)
                if (it.isEmpty()) {
                    view.showEmpty()
                } else {
                    if (isPinned) {
                        view.loadPinnedNotes(it)
                    } else {
                        view.loadUpcomingNotes(it)
                    }
                }
            }
    }


}