package just_pouya.data.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import justp.k.domain.entities.Message

@Entity(
    "Message",
    foreignKeys = [
        ForeignKey(
            entity = DeviceData::class,
            parentColumns = ["macAddress"],
            childColumns = ["macAddress"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class MessageData(
    @PrimaryKey(true) val id: Int=0,
    val messageTxt: String,
    val itsMe: Boolean,
    val macAddress: String
)

fun MessageData.toMessage() = Message(id, messageTxt, itsMe, macAddress)