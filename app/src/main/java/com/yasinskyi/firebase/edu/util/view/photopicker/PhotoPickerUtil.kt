package com.yasinskyi.firebase.edu.util.view.photopicker

import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts

object PhotoPickerUtil {

    private const val IMAGE_PATH = "image/*"

    fun selectImage(activityResultLauncher: ActivityResultLauncher<PickVisualMediaRequest>) {
        activityResultLauncher
            .launch(
                PickVisualMediaRequest(
                    ActivityResultContracts
                        .PickVisualMedia
                        .ImageOnly
                )
            )
    }

    fun selectImageDeprecated(activityResultLauncher: ActivityResultLauncher<String>) {
        activityResultLauncher
            .launch(IMAGE_PATH)
    }
}