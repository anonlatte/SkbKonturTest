package com.example.konturtest.db.model

data class Contact(
    val id: String,
    val name: String,
    val phone: String,
    val height: Double,
    val biography: String,
    val temperament: String,
    val educationPeriod: EducationPeriod
) {

    fun getHeight() = height.toString()

    data class EducationPeriod(
        val start: String,
        val end: String
    )
}
