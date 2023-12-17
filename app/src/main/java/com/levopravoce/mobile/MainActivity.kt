package com.levopravoce.mobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.levopravoce.mobile.routes.authGraph
import com.levopravoce.mobile.ui.theme.MobileTheme
import com.levopravoce.mobile.ui.theme.customColorsShema
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
                        startDestination = "auth"
                    ) {
                        authGraph(navHost)
                        composable("home") {
                            HomePreview()
                        }
                    }
                }
            }
        }
    }


    @Composable
    fun HomePreview() {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                Modifier.border(1.dp, MaterialTheme.customColorsShema.placeholder),
            ) {
                Image(
                    painter = painterResource(R.drawable.login_icon),
                    contentDescription = "Logo",
                    modifier = Modifier
                        .width(100.dp)
                        .height(100.dp)
                )
            }
        }
    }
}