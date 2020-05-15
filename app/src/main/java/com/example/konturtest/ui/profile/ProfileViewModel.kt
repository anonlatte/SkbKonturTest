package com.example.konturtest.ui.profile

import com.example.konturtest.db.model.Contact

class ProfileViewModel(var contact: Contact = Contact()) : java.util.Observable()
