package com.yasinskyi.firebase.edu.view.impl.fragment.home

import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.yasinskyi.firebase.edu.R
import com.yasinskyi.firebase.edu.databinding.FragmentAddPhotoBinding
import com.yasinskyi.firebase.edu.presentation.impl.viewmodel.main.AddPhotoViewModel
import com.yasinskyi.firebase.edu.util.extension.lifecycle.observeWhenCreated
import com.yasinskyi.firebase.edu.util.extension.navigation.navigateSafe
import com.yasinskyi.firebase.edu.util.view.photopicker.PhotoPickerUtil
import com.yasinskyi.firebase.edu.view.base.fragment.BaseFragment

class AddPhotoFragment : BaseFragment<FragmentAddPhotoBinding, AddPhotoViewModel>() {

    private lateinit var getContent: ActivityResultLauncher<String>
    private lateinit var pickMedia: ActivityResultLauncher<PickVisualMediaRequest>

    private fun observeImageUriState(uri: String?) {
        showImage(uri)
    }

    private fun observeSignOutState(isSuccessful: Boolean) {
        if (isSuccessful) {
            navigateToSignInScreen()
        }
    }

    private fun navigateToSignInScreen() {
        navigateSafe(R.id.actionAddPhotoFragmentToAuthFragment)
    }

    override fun onViewBound(binding: FragmentAddPhotoBinding, savedInstanceState: Bundle?) {
        with(binding) {
            saveImageButton.setOnClickListener {
                viewModel.updateUser()
            }

            selectedImage.setOnClickListener {
                if (ActivityResultContracts.PickVisualMedia.isPhotoPickerAvailable()) {
                    PhotoPickerUtil.selectImage(pickMedia)
                } else {
                    PhotoPickerUtil.selectImageDeprecated(getContent)
                }
            }

            showImage(null)
        }
    }

    override fun listenViewModel(viewModel: AddPhotoViewModel) {
        super.listenViewModel(viewModel)
        observeWhenCreated(viewModel.imageUriState, ::observeImageUriState)
        observeWhenCreated(viewModel.signOutState, ::observeSignOutState)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        launchPhotoPicker()
    }

    override fun onToolbarItemSelected(menuItem: MenuItem) =
        when (menuItem.itemId) {
            R.id.menuSignOutButton -> {
                viewModel.signOut()
                true
            }

            else -> super.onToolbarItemSelected(menuItem)
        }

    private fun loadImageUri(uri: Uri?) {
        if (uri != null) {
            viewModel.setImageUri(uri.toString())
        }
    }

    private fun launchPhotoPicker() {
        if (ActivityResultContracts.PickVisualMedia.isPhotoPickerAvailable()) {
            pickMedia =
                registerForActivityResult(ActivityResultContracts.PickVisualMedia(), ::loadImageUri)
        } else {
            getContent =
                registerForActivityResult(ActivityResultContracts.GetContent(), ::loadImageUri)
        }
    }

    private fun showImage(imageUri: String?) {
        binding?.let {
            Glide
                .with(this)
                .load(imageUri)
                .placeholder(R.drawable.ic_select_image)
                .transform(
                    CenterCrop(),
                    RoundedCorners(resources.getDimensionPixelSize(R.dimen.corner_radius)),
                ).into(it.selectedImage)
        }
    }
}