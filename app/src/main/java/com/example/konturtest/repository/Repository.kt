package com.example.konturtest.repository

import android.content.Context
import com.example.konturtest.api.ContactsService

class Repository(context: Context) {
    private val apiService = ContactsService.create(context)
    fun getContacts(file: String) = apiService.getContacts(file)
}