package just_pouya.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import justp.k.domain.entities.Device

@Entity("Device")
data class DeviceData(
    @PrimaryKey
    val macAddress:String,
    val name :String
)

fun DeviceData.toDevice() = Device(macAddress,name)