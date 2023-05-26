package ru.yandex.hrfriend.data.dto.sign_in_up

data class LoginResponse(
    val firstname: String,
    val lastname: String,
    val role: String,
    val email: String,
    val refresh_token : String,
    val access_token: String
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as LoginResponse

        if (role != other.role) return false
        if (refresh_token != other.refresh_token) return false
        if (access_token != other.access_token) return false

        return true
    }

    override fun hashCode(): Int {
        var result = role.hashCode()
        result = 31 * result + refresh_token.hashCode()
        result = 31 * result + access_token.hashCode()
        return result
    }

    override fun toString(): String {
        return "LoginResponse(permission='$role', refresh_token='$refresh_token', access_token='$access_token')"
    }

}
