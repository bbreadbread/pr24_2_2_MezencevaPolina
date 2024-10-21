package com.example.pract24_var2_var2

import android.os.Parcel
import android.os.Parcelable

class HistoryItem(
    val type: String,
    val money: String,
    val bank: String,
    val date: String
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(type)
        parcel.writeString(money)
        parcel.writeString(bank)
        parcel.writeString(date)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<HistoryItem> {
        override fun createFromParcel(parcel: Parcel): HistoryItem {
            return HistoryItem(parcel)
        }

        override fun newArray(size: Int): Array<HistoryItem?> {
            return arrayOfNulls(size)
        }
    }
}