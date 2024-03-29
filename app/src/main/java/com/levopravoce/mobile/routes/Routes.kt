package com.levopravoce.mobile.routes

object Routes {
    object Auth {
        val ROUTE = "auth"
        val START = "start"
        val LOGIN = "login"
        val REGISTER = "register"
    }

    object Home {
        val MESSAGES = "messages/{channelId}"
        val CHAT_LIST = "chat_list"
        val ROUTE = "home"
        val INICIAL = "inicial"
        val CONFIGURATION = "configuration"
        val THEME_CUSTOMIZATION = "theme_customization"
    }
}
