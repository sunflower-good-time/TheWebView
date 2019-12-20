package com.cat.zhou.removtewebview.utils

import android.os.Parcel
import android.os.Parcelable

/**
 * @author: zjf
 * @data:2019/12/19
 */
data class AidlError(var code:Int,var message:String):Parcelable {

    constructor(parcel: Parcel) : this(parcel.readInt(), parcel.readString()!!)


    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest!!.writeInt(code)
        dest.writeString(message)
    }


    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<AidlError> {
        override fun createFromParcel(parcel: Parcel): AidlError {
            return AidlError(parcel)
        }

        override fun newArray(size: Int): Array<AidlError?> {
            return arrayOfNulls(size)
        }
    }
}