package ru.yandex.hrfriend.data.dto.sign_in_up

data class SignUpResponse(
    val refresh_token : String,
    val access_token: String,
    val firstname: String,
    val lastname: String,
    val role: String,
    val email: String
) {
    override fun toString(): String {
        return "SignUpResponse(refresh_token='$refresh_token', access_token='$access_token', firstname='$firstname', lastname='$lastname', role='$role', email='$email')"
    }
}
