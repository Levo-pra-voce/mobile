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
        val MESSAGES = "messages/{channelId}/{channelName}"
        val CHAT_LIST = "chat_list"
        val ROUTE = "home"
        val INICIAL = "inicial"
        val CONFIGURATION = "configuration"
        val THEME_CUSTOMIZATION = "theme_customization"
        val USER_EDIT = "user_edit"
        val SELECT_ORDER = "select_order"
    }
}
