package com.zxventures.zxapp.model

import PocSearchMethodQuery
import android.os.Parcel
import android.os.Parcelable
import type.Status
import java.io.Serializable

/**
 * Created by rodrigosimoesrosa
 */
object PointOfContactData {

    data class PointOfContact(val id: String?,
                              val status: StatusPoint,
                              val tradingName: String?,
                              val officialName: String?,
                              var address: Address?) : Parcelable {

        constructor(parcel: Parcel) : this(
                parcel.readString(),
                parcel.readSerializable() as StatusPoint,
                parcel.readString(),
                parcel.readString(),
                parcel.readSerializable() as Address)

        override fun writeToParcel(dest: Parcel?, p1: Int) {
            dest?.writeString(id)
            dest?.writeSerializable(status)
            dest?.writeString(tradingName)
            dest?.writeString(officialName)
            dest?.writeSerializable(address)
        }

        override fun describeContents(): Int = 0

        companion object CREATOR : Parcelable.Creator<PointOfContact> {

            val POINT_OF_CONTACT = "POINT_OF_CONTACT"

            fun build(obj: PocSearchMethodQuery.PocSearch) : PointOfContact {
                return PointOfContact(obj.id(),
                        StatusPoint.build(obj.status()),
                        obj.tradingName(),
                        obj.officialName(),
                        Address.build(obj.address()))
            }

            override fun createFromParcel(parcel: Parcel): PointOfContact {
                return PointOfContact(parcel)
            }

            override fun newArray(size: Int): Array<PointOfContact?> {
                return arrayOfNulls(size)
            }
        }
    }

    enum class StatusPoint : Serializable {

        AVAILABLE,
        CLOSED;

        companion object {
            fun build(obj:type.Status): StatusPoint {
                return when(obj){
                    Status.AVAILABLE -> AVAILABLE
                    Status.CLOSED -> CLOSED
                }
            }
        }
    }

    data class Address(val address1: String?,
                       val address2: String?,
                       val city: String?,
                       val province: String?,
                       val zip: String?,
                       val number: String?): Serializable {

        companion object {
            fun build(obj: PocSearchMethodQuery.Address?) : Address? {
                return Address(obj?.address1(),
                        obj?.address2(),
                        obj?.city(),
                        obj?.province(),
                        obj?.zip(),
                        obj?.number())
            }
        }
    }
}


