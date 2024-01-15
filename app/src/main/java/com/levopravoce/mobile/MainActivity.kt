package com.levopravoce.mobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.levopravoce.mobile.routes.Routes
import com.levopravoce.mobile.routes.authGraph
import com.levopravoce.mobile.routes.homeGraph
import com.levopravoce.mobile.ui.theme.MobileTheme
import dagger.hilt.android.AndroidEntryPoint

val navControllerContext = compositionLocalOf<NavController> { error("No NavController found!") }

@ExperimentalMaterial3Api
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MobileTheme {
                val navHost = rememberNavController()
                CompositionLocalProvider(navControllerContext provides navHost) {
                    NavHost(
                        navController = navHost,
                        startDestination = Routes.Home.ROUTE
                    ) {
                        authGraph()
                        homeGraph()
                    }
                }
            }
        }
    }
}