package com.example.noteapp.ui.base

import io.reactivex.rxjava3.disposables.Disposable

open class BasePresenterImpl() : BasePresenter {

    var disposable: Disposable? = null

    override fun onStop() {
        disposable?.let {
            if (!it.isDisposed) {
                it.dispose()
            }
        }
    }
}