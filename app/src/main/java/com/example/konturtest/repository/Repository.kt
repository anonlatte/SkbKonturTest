package com.example.konturtest.repository

import com.example.konturtest.api.ContactsService

class Repository {
    private val apiService = ContactsService.create()
    fun getContacts(file: String) = apiService.getContacts(file)
}