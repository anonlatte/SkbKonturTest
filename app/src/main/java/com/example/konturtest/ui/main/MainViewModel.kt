package com.example.konturtest.ui.main

import android.util.Log
import androidx.databinding.ObservableBoolean
import com.example.konturtest.db.model.Contact
import com.example.konturtest.repository.Repository
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject

class MainViewModel(private val repository: Repository) : java.util.Observable() {

    private val compositeDisposable = CompositeDisposable()
    val isLoading = ObservableBoolean(false)
    val isFirstLoading = ObservableBoolean(true)

    var totalContacts: MutableList<Contact> = mutableListOf()
    var filteredContacts = mutableListOf<Contact>()
    var selectedContact: Subject<Contact> = PublishSubject.create()

    var errorMessages: Subject<String> = PublishSubject.create()

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
                Log.e("Response", it?.localizedMessage.toString())
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
        isFirstLoading.set(false)
        errorMessages.onNext("Нет подключения к сети")
    }

    private fun updateContactsDataList(contacts: List<Contact>) {
        isFirstLoading.set(false)
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
