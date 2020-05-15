package com.example.konturtest.db.model

import android.os.Parcel
import android.os.Parcelable
import java.text.SimpleDateFormat
import java.util.*

data class Contact(
    val id: String = "",
    val name: String = "",
    val phone: String = "",
    val height: Double = 0.0,
    val biography: String = "",
    val temperament: String = "",
    val educationPeriod: EducationPeriod = EducationPeriod("", "")
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readDouble(),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readParcelable<EducationPeriod>(EducationPeriod::class.java.classLoader)!!
    )

    @ExperimentalStdlibApi
    fun capitalizedTemperament() = temperament.capitalize(Locale.ROOT)

    fun getHeight() = height.toString()

    fun getEducationPeriod(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        val customFormat = SimpleDateFormat("d.MM.yyyy", Locale.getDefault())
        if (educationPeriod.start.isNotEmpty() && educationPeriod.end.isNotEmpty()) {
            val startPeriod = customFormat.format(dateFormat.parse(educationPeriod.start)!!)
            val endPeriod = customFormat.format(dateFormat.parse(educationPeriod.end)!!)
            return "$startPeriod - $endPeriod"
        }
        return ""
    }

    data class EducationPeriod(val start: String = "", val end: String = "") : Parcelable {
        constructor(parcel: Parcel) : this(
            parcel.readString()!!,
            parcel.readString()!!
        )

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            parcel.writeString(start)
            parcel.writeString(end)
        }

        override fun describeContents(): Int {
            return 0
        }

        companion object CREATOR : Parcelable.Creator<EducationPeriod> {
            override fun createFromParcel(parcel: Parcel): EducationPeriod {
                return EducationPeriod(parcel)
            }

            override fun newArray(size: Int): Array<EducationPeriod?> {
                return arrayOfNulls(size)
            }
        }
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(name)
        parcel.writeString(phone)
        parcel.writeDouble(height)
        parcel.writeString(biography)
        parcel.writeString(temperament)
        parcel.writeParcelable(educationPeriod, Parcelable.PARCELABLE_WRITE_RETURN_VALUE)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Contact> {
        override fun createFromParcel(parcel: Parcel): Contact {
            return Contact(parcel)
        }

        override fun newArray(size: Int): Array<Contact?> {
            return arrayOfNulls(size)
        }
    }
}
