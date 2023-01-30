package com.mrq.library.auth

data class SocialMediaModel(
    var id: String?,
    var name: String?,
    var email: String?,
    var authType: Int,
    var country: Int,
)
