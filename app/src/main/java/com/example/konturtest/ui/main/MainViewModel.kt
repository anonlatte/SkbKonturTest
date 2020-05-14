package com.example.konturtest.ui.main

import android.util.Log
import androidx.databinding.ObservableBoolean
import com.example.konturtest.db.model.Contact
import com.example.konturtest.repository.RepositoryProvider
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers


class MainViewModel : java.util.Observable() {
    private val repository = RepositoryProvider.provideRepository()
    var totalContacts: MutableList<Contact> = mutableListOf()
    private val compositeDisposable = CompositeDisposable()
    val isLoading = ObservableBoolean(false)

    fun getAllContacts() {
        totalContacts.clear()
        val firstContactsList = repository.getContacts("generated-01.json")
        val secondContactsList = repository.getContacts("generated-02.json")
        val thirdContactsList = repository.getContacts("generated-03.json")

        val disposable = Observable.merge(firstContactsList, secondContactsList, thirdContactsList)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                updateContactsDataList(it)
            }, {
                onError()
                Log.e("Response", it?.localizedMessage?.toString()!!)
            })

        compositeDisposable.add(disposable)
    }

    fun onRefresh() {
        isLoading.set(true)
        getAllContacts()
    }

    fun onReady() {
        isLoading.set(false)
    }

    fun onError() {
        isLoading.set(false)
    }

    private fun updateContactsDataList(contacts: List<Contact>) {
        totalContacts.addAll(contacts.toMutableList())
        setChanged()
        onReady()
        notifyObservers()
    }
}
