package com.example.konturtest.ui.main

import android.util.Log
import androidx.databinding.ObservableBoolean
import com.example.konturtest.db.model.Contact
import com.example.konturtest.repository.RepositoryProvider
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject

class MainViewModel : java.util.Observable() {
    private val repository = RepositoryProvider.provideRepository()
    var errorMessages: Subject<String> = PublishSubject.create()
    var totalContacts: MutableList<Contact> = mutableListOf()
    var filteredContacts = mutableListOf<Contact>()
    private val compositeDisposable = CompositeDisposable()
    val isLoading = ObservableBoolean(true)
    var selectedContact: Subject<Contact> = PublishSubject.create()
    fun getAllContacts() {
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
                errorMessages.onNext("Нет подключения к сети")
                Log.e("Response", it?.cause.toString())
            })

        compositeDisposable.add(disposable)
    }

    fun onRefresh() {
        totalContacts.clear()
        isLoading.set(true)
        getAllContacts()
    }

    private fun onReady() {
        isLoading.set(false)
    }

    private fun onError() {
        isLoading.set(false)
    }

    private fun updateContactsDataList(contacts: List<Contact>) {
        totalContacts.addAll(contacts.toMutableList())
        filteredContacts = totalContacts
        setChanged()
        onReady()
        notifyObservers()
    }

    fun onItemClick(position: Int) {
        selectedContact.onNext(filteredContacts[position])
    }
}
