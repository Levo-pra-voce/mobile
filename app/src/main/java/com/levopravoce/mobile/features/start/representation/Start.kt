package com.levopravoce.mobile.features.start.representation;

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.levopravoce.mobile.R
import com.levopravoce.mobile.navControllerContext
import com.levopravoce.mobile.routes.Routes
import com.levopravoce.mobile.ui.theme.Black100

@Composable
fun Start() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(20.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Column(
                modifier = Modifier.padding(top = 156.dp)
            ) {
                Icon()
            }
            Column(
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxHeight()
            ) {
                StartButton()
                RegisterLink()
            }
        }
    }
}

@Composable
private fun RegisterLink() {
    val navControllerContext = navControllerContext.current
    Row(
        modifier = Modifier
            .padding(32.dp)
    ) {
        TextButton(
            onClick = {
                navControllerContext.navigate(
                    route = Routes.Auth.REGISTER,
                )
            },
        ) {
            Text(
                text = "Não possui uma conta?",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = Black100,
                modifier = Modifier.bottomBorder(1.dp, Black100)
            )
        }
    }
}

fun Modifier.bottomBorder(strokeWidth: Dp, color: Color) = composed(
    factory = {
        val density = LocalDensity.current
        val strokeWidthPx = density.run { strokeWidth.toPx() }

        Modifier.drawBehind {
            val width = size.width
            val height = size.height - strokeWidthPx / 2

            drawLine(
                color = color,
                start = Offset(x = 0f, y = height),
                end = Offset(x = width, y = height),
                strokeWidth = strokeWidthPx
            )
        }
    }
)

@Preview
@Composable
private fun RegisterLinkPreview() {
    RegisterLink()
}

@Composable
private fun StartButton() {
    val navController = navControllerContext.current
    Button(
        onClick = {
            navController.navigate(Routes.Auth.LOGIN)
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp),
        shape = MaterialTheme.shapes.small,
        colors = ButtonDefaults.buttonColors(
            containerColor = Black100,
        ),
    ) {
        Row(
            horizontalArrangement = Arrangement.Center
        ) {
            Row(modifier = Modifier.offset(x = 16.dp)) {
                Text(text = "Iniciar", fontSize = 22.sp)
            }
            Row(
                modifier = Modifier
                    .offset(x = (72).dp, y = 4.dp)
                    .zIndex(1f)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.right_arrow),
                    contentDescription = "seta para direita",
                    Modifier.height(20.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun StartButtonPreview() {
    StartButton()
}

@Composable
private fun Icon() {
    Image(
        painter = painterResource(R.drawable.login_icon),
        contentDescription = "Logo",
        modifier = Modifier
            .width(320.dp)
            .height(320.dp)
    )
}

@Preview
@Composable
private fun ImagePreview() {
    Icon()
}