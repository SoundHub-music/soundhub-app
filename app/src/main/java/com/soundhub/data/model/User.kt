package com.soundhub.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.soundhub.ui.authentication.state.RegistrationState
import java.time.LocalDate
import java.util.UUID


interface IUser {
    var id: UUID
    var email: String?
    var firstName: String?
    var lastName: String?
    var country: String?
    var birthday: LocalDate?
    var city: String?
    var languages: List<String>
    var description: String?
    var gender: Gender
    var avatarUrl: String?
    var token: String?
}


// room library is used temporarily.
// In the future there will be a simple data class which will communicate with server via retrofit
@Entity
data class User(
    @PrimaryKey
    override var id: UUID = UUID.randomUUID(),

    @ColumnInfo("gender")
    override var gender: Gender = Gender.Unknown,

    @ColumnInfo("avatarUrl")
    override var avatarUrl: String? = null,

    @ColumnInfo("email")
    override var email: String? = "",

    @ColumnInfo("first_name")
    override var firstName: String? = "",

    @ColumnInfo("last_name")
    override var lastName: String? = "",

    @ColumnInfo("country")
    override var country: String? = "",

    @ColumnInfo("birthday")
    override var birthday: LocalDate? = null,

    @ColumnInfo("city")
    override var city: String? = "",

    @ColumnInfo("password")
    var password: String? = null,

    @ColumnInfo("languages")
    override var languages: List<String> = emptyList(),

    @ColumnInfo("description")
    override var description: String? = "",

    @ColumnInfo("token")
    override var token: String? = null
) : IUser {
    constructor(registerState: RegistrationState?) : this() {
        id = registerState?.id ?: UUID.randomUUID()
        password = registerState?.password
        email = registerState?.email ?: ""
        firstName = registerState?.firstName ?: ""
        lastName = registerState?.lastName ?: ""
        country = registerState?.country ?: ""
        birthday = registerState?.birthday
        city = registerState?.city ?: ""
        languages = registerState?.languages ?: emptyList()
        description = registerState?.description ?: ""
    }
}