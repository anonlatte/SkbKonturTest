package com.example.konturtest

import com.example.konturtest.db.model.Contact
import com.example.konturtest.repository.RepositoryProvider
import org.junit.Assert.assertEquals
import org.junit.Test

class UnitTest {
    private val repository = RepositoryProvider.provideRepository()

    @Test
    fun getContacts1() {
        repository.getContacts("generated-01.json")
            .subscribe({ result ->
                result.forEach { contact ->
                    println(contact)
                }
            }, { error ->
                error.printStackTrace()
            })
    }

    @Test
    fun getContacts2() {
        repository.getContacts("generated-02.json")
            .subscribe({ result ->
                result.forEach { contact ->
                    println(contact)
                }
            }, { error ->
                error.printStackTrace()
            })
    }

    @Test
    fun getContacts3() {
        repository.getContacts("generated-03.json")
            .subscribe({ result ->
                result.forEach { contact ->
                    println(contact)
                }
            }, { error ->
                error.printStackTrace()
            })
    }

    @Test
    fun dataParser() {
        val contact = Contact(
            educationPeriod = Contact.EducationPeriod(
                "2012-03-16T03:20:39-06:00",
                "2015-09-30T10:40:55-05:00"
            )
        )
        assertEquals("16.03.2012 - 30.09.2015", contact.getEducationPeriod())
    }
}
