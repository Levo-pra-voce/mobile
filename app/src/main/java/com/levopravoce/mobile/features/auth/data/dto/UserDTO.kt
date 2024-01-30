package com.levopravoce.mobile.features.auth.data.dto

data class UserDTO (
    var firstName: String? = null,
    var lastName: String? = null,
    var email: String? = null,
    var password: String? = null,
    var phone: String? = null,
    var cpf: String? = null,
    var city: String? = null,
    var contact: String? = null,
    var state: String? = null,
    var zipCode: String? = null,
    var country: String? = null,
    var status: String? = null,
    var complement: String? = null,
    var neighborhood: String? = null,
    var userType: UserType? = null,
    val token: String? = null,
    var street: String? = null,
    var vehicle: Vehicle? = null
)