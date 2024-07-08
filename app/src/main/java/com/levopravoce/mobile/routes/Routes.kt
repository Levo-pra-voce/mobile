package com.levopravoce.mobile.routes

object Routes {
    object Auth {
        val ROUTE = "auth"
        val START = "start"
        val LOGIN = "login"
        val REGISTER = "register"
        val FORGOT_PASSWORD = "forgot_password"
    }

    object Home {
        val CHAT_LIST = "chat_list"
        val ROUTE = "home"
        val INICIAL = "inicial"
        val CONFIGURATION = "configuration"
        val THEME_CUSTOMIZATION = "theme_customization"
        val USER_EDIT = "user_edit"
        val SELECT_ORDER = "select_order"
        val RELATORY = "relatory"
        val CHANGE_PASSWORD = "change_password"
        val DELIVERY_MAN = "delivery_list/{screen}"
        val DELIVERY_DETAILS = "delivery/{deliveryId}"
        val REQUEST_DETAILS = "request/{deliveryId}"
        val DELIVERY_TRACKING_CLIENT = "delivery_tracking/client"
        val DELIVERY_TRACKING_DRIVER = "delivery_tracking/driver"
        val DELIVERY_PAYMENT = "delivery_payment"
        val CLIENT_PAYMENT = "client_payment"
    }
}
