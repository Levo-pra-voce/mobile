package com.levopravoce.mobile.features.payment.representation

import android.graphics.Bitmap
import android.graphics.Color
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.google.gson.Gson
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.WriterException
import com.google.zxing.qrcode.QRCodeWriter
import com.levopravoce.mobile.common.viewmodel.hiltSharedViewModel
import com.levopravoce.mobile.config.Destination
import com.levopravoce.mobile.features.app.representation.Alert
import com.levopravoce.mobile.features.app.representation.Button
import com.levopravoce.mobile.features.app.representation.Screen
import com.levopravoce.mobile.features.app.representation.Title
import com.levopravoce.mobile.features.payment.data.OrderPaymentDTO
import com.levopravoce.mobile.features.payment.domain.PaymentViewModel
import com.levopravoce.mobile.features.tracking.domain.TrackingViewModel
import com.levopravoce.mobile.routes.navControllerContext
import com.levopravoce.mobile.ui.theme.customColorsShema
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Creates a [BitmapPainter] that draws a QR code for the given [content].
 * The [size] parameter defines the size of the QR code in dp.
 * The [padding] parameter defines the padding of the QR code in dp.
 */
@Composable
fun rememberQrBitmapPainter(
    content: String,
    size: Dp = 150.dp,
    padding: Dp = 0.dp
): BitmapPainter {

    check(content.isNotEmpty()) { "Content must not be empty" }
    check(size >= 0.dp) { "Size must be positive" }
    check(padding >= 0.dp) { "Padding must be positive" }

    val density = LocalDensity.current
    val sizePx = with(density) { size.roundToPx() }
    val paddingPx = with(density) { padding.roundToPx() }

    val bitmapState = remember {
        mutableStateOf<Bitmap?>(null)
    }

    // Use dependency on 'content' to re-trigger the effect when content changes
    LaunchedEffect(content) {
        val bitmap = generateQrBitmap(content, sizePx, paddingPx)
        bitmapState.value = bitmap
    }

    val bitmap = bitmapState.value ?: createDefaultBitmap(sizePx)

    return remember(bitmap) {
        BitmapPainter(bitmap.asImageBitmap())
    }
}


/**
 * Generates a QR code bitmap for the given [content].
 * The [sizePx] parameter defines the size of the QR code in pixels.
 * The [paddingPx] parameter defines the padding of the QR code in pixels.
 * Returns null if the QR code could not be generated.
 * This function is suspendable and should be called from a coroutine is thread-safe.
 */
private suspend fun generateQrBitmap(
    content: String,
    sizePx: Int,
    paddingPx: Int
): Bitmap? = withContext(Dispatchers.IO) {
    val qrCodeWriter = QRCodeWriter()

    // Set the QR code margin to the given padding
    val encodeHints = mutableMapOf<EncodeHintType, Any?>()
        .apply {
            this[EncodeHintType.MARGIN] = paddingPx
        }

    try {
        val bitmapMatrix = qrCodeWriter.encode(
            content, BarcodeFormat.QR_CODE,
            sizePx, sizePx, encodeHints
        )

        val matrixWidth = bitmapMatrix.width
        val matrixHeight = bitmapMatrix.height

        val colors = IntArray(matrixWidth * matrixHeight) { index ->
            val x = index % matrixWidth
            val y = index / matrixWidth
            val shouldColorPixel = bitmapMatrix.get(x, y)
            if (shouldColorPixel) Color.BLACK else Color.WHITE
        }

        Bitmap.createBitmap(colors, matrixWidth, matrixHeight, Bitmap.Config.ARGB_8888)
    } catch (ex: WriterException) {
        null
    }
}

/**
 * Creates a default bitmap with the given [sizePx].
 * The bitmap is transparent.
 * This is used as a fallback if the QR code could not be generated.
 * The bitmap is created on the UI thread.
 */
private fun createDefaultBitmap(sizePx: Int): Bitmap {
    return Bitmap.createBitmap(sizePx, sizePx, Bitmap.Config.ARGB_8888).apply {
        eraseColor(Color.TRANSPARENT)
    }
}

@Composable
fun ClientPayment(
    paymentViewModel: PaymentViewModel = hiltSharedViewModel(),
    trackingViewModel: TrackingViewModel = hiltSharedViewModel()
) {
    val messageState by paymentViewModel.webSocketState.collectAsState()
    val navController = navControllerContext.current
    val coroutineScope = rememberCoroutineScope()
    val isPaid = remember {
        mutableStateOf(false)
    }
    val qrCodeLink = remember {
        mutableStateOf("")
    }
    LaunchedEffect(Unit) {
        paymentViewModel.connectWebSocket()
        qrCodeLink.value = paymentViewModel.generateQrCodeLink()
    }
    LaunchedEffect(messageState) {
        if (messageState?.destination == Destination.ORDER_PAYMENT) {
            val gson = Gson()
            val orderPaymentDTO = gson.fromJson(messageState?.message, OrderPaymentDTO::class.java)
            isPaid.value = orderPaymentDTO.isPaid
        }
    }
    Alert(show = isPaid, message = "Pagamento realizado com sucesso!")
    Screen(padding = 32.dp) {
        Column(
            Modifier
                .fillMaxWidth()
                .fillMaxHeight()
        ) {
            Title(text = "Pagamento")
            Column(
                modifier = Modifier
                    .fillMaxHeight(0.9f)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Escaneie o QR Code abaixo",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.customColorsShema.title,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(bottom = 32.dp)
                )
                if (qrCodeLink.value.isNotEmpty()) {
                    Image(
                        painter = rememberQrBitmapPainter(
                            content = qrCodeLink.value,
                            size = 300.dp,
                            padding = 1.dp
                        ),
                        contentDescription = null,
                        modifier = Modifier.clickable {
                            coroutineScope.launch(Dispatchers.Main) {
                                val response = paymentViewModel.sendPaymentRequest()
                                if (response.isSuccessful) {
                                    isPaid.value = true
                                }
                            }
                        }
                    )
                }
            }
            Button(text = "Voltar", modifier = Modifier.fillMaxWidth()) {
                trackingViewModel.setRedirectToTracking(false)
                navController?.navigateUp()
            }
        }
    }
}