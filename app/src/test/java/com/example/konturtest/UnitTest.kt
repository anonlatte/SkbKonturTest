package com.example.konturtest

import com.example.konturtest.repository.RepositoryProvider
import org.junit.Test

class UnitTest {
    val repository = RepositoryProvider.provideRepository()

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
}
