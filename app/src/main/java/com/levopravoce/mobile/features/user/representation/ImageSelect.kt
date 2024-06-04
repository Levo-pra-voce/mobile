package com.levopravoce.mobile.features.user.representation

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.levopravoce.mobile.features.app.representation.Button

@Composable
fun ImageSelect(
    onChange: (Uri?) -> Unit
) {
    val singlePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri -> onChange(uri) }
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = {
                    singlePhotoPickerLauncher.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )
                },
                text = "Selecionar imagem",
                modifier = Modifier.width(200.dp)
            )
        }
//        if (selectedImageUri == null) {
//            Column(
//                modifier = Modifier.fillMaxWidth(),
//                horizontalAlignment = Alignment.CenterHorizontally
//            ) {
//                Button(
//                    onClick = {
//                        singlePhotoPickerLauncher.launch(
//                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
//                        )
//                    },
//                    text = "Selecionar imagem",
//                    modifier = Modifier.width(200.dp)
//                )
//            }
//        } else {
//            Column(
//                horizontalAlignment = Alignment.CenterHorizontally,
//                modifier = Modifier.fillMaxWidth()
//            ) {
//                AsyncImage(
//                    model = selectedImageUri,
//                    contentDescription = null,
//                    modifier = Modifier
//                        .width(70.dp)
//                        .height(70.dp)
//                        .clip(CircleShape),
//                    contentScale = ContentScale.Crop
//                )
//            }
//        }
    }
}