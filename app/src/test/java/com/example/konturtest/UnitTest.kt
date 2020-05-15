package com.example.konturtest

import com.example.konturtest.db.model.Contact
import org.junit.Assert.assertEquals
import org.junit.Test

class UnitTest {
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
