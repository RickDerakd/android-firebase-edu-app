package com.yasinskyi.firebase.edu.view.impl.fragment.login

import android.net.Uri
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia.Companion.isPhotoPickerAvailable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.yasinskyi.firebase.edu.R
import com.yasinskyi.firebase.edu.data.exception.auth.AuthInvalidCredentialsException
import com.yasinskyi.firebase.edu.data.exception.auth.AuthUserCollisionException
import com.yasinskyi.firebase.edu.data.exception.auth.AuthUserCreationException
import com.yasinskyi.firebase.edu.data.exception.storage.UnableToUploadPhotoToStorageException
import com.yasinskyi.firebase.edu.presentation.impl.exception.validation.EmailValidationException
import com.yasinskyi.firebase.edu.presentation.impl.exception.validation.PasswordValidationException
import com.yasinskyi.firebase.edu.presentation.impl.exception.validation.UsernameValidationException
import com.yasinskyi.firebase.edu.databinding.FragmentRegistrationBinding
import com.yasinskyi.firebase.edu.presentation.impl.exception.ValidationException
import com.yasinskyi.firebase.edu.presentation.impl.exception.validation.RegistrationValidationException
import com.yasinskyi.firebase.edu.presentation.impl.exception.validation.WeakPasswordValidationException
import com.yasinskyi.firebase.edu.presentation.impl.viewmodel.login.RegistrationViewModel
import com.yasinskyi.firebase.edu.util.extension.lifecycle.observeWhenCreated
import com.yasinskyi.firebase.edu.util.extension.navigation.navigateSafe
import com.yasinskyi.firebase.edu.util.view.photopicker.PhotoPickerUtil.selectImage
import com.yasinskyi.firebase.edu.util.view.photopicker.PhotoPickerUtil.selectImageDeprecated
import com.yasinskyi.firebase.edu.view.base.fragment.BaseFragment

class RegistrationFragment : BaseFragment<FragmentRegistrationBinding, RegistrationViewModel>() {

    private lateinit var getContent: ActivityResultLauncher<String>
    private lateinit var pickMedia: ActivityResultLauncher<PickVisualMediaRequest>

    private fun observeEmailFieldState(email: String?) {
        binding?.signUpFieldEmail?.editText?.setText(email)
    }

    private fun observePasswordFieldState(password: String?) {
        binding?.signUpFieldPassword?.editText?.setText(password)
    }

    private fun observeUsernameFieldState(username: String?) {
        binding?.signUpFieldUsername?.editText?.setText(username)
    }

    private fun observeImageUriState(uri: String?) {
        showImage(uri)
    }

    private fun observeRegistrationState(isSuccessful: Boolean) {
        if (isSuccessful) {
            clearFields()
            navigateToHomeScreen()
        }
    }

    private fun navigateToHomeScreen() {
        navigateSafe(R.id.mainActivityGraph)
    }

    private fun clearFields() {
        binding?.apply {
            signUpFieldEmail.editText?.text?.clear()
            signUpFieldPassword.editText?.text?.clear()
            signUpFieldUsername.editText?.text?.clear()
            showImage(null)
        }
        viewModel.setImageUri(null)
    }

    override fun listenViewModel(viewModel: RegistrationViewModel) {
        super.listenViewModel(viewModel)
        observeWhenCreated(viewModel.emailFieldState, ::observeEmailFieldState)
        observeWhenCreated(viewModel.passwordFieldState, ::observePasswordFieldState)
        observeWhenCreated(viewModel.usernameFieldState, ::observeUsernameFieldState)
        observeWhenCreated(viewModel.registrationState, ::observeRegistrationState)
        observeWhenCreated(viewModel.imageUriState, ::observeImageUriState)
    }

    override fun onViewBound(binding: FragmentRegistrationBinding, savedInstanceState: Bundle?) {
        with(binding) {
            registerButton.setOnClickListener {
                registerUser()
            }

            selectedImage.setOnClickListener {
                if (isPhotoPickerAvailable()) {
                    selectImage(pickMedia)
                } else {
                    selectImageDeprecated(getContent)
                }
            }

            showImage(null)
        }
    }

    private fun registerUser() {
        saveFields()
        viewModel.createUser()
        hideValidationError()
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        launchPhotoPicker()
    }

    override fun onStop() {
        saveFields()
        super.onStop()
    }

    private fun saveFields() {
        binding?.apply {
            viewModel.saveFields(
                signUpFieldEmail.editText?.text?.trimEnd().toString(),
                signUpFieldPassword.editText?.text.toString(),
                signUpFieldUsername.editText?.text?.trimEnd().toString(),
            )
        }
    }

    private fun launchPhotoPicker() {
        if (isPhotoPickerAvailable()) {
            pickMedia =
                registerForActivityResult(ActivityResultContracts.PickVisualMedia(), ::loadImageUri)
        } else {
            getContent =
                registerForActivityResult(ActivityResultContracts.GetContent(), ::loadImageUri)
        }
    }

    private fun loadImageUri(uri: Uri?) {
        if (uri != null) {
            viewModel.setImageUri(uri.toString())
        }
    }

    override fun handleError(throwable: Throwable) {
        when (throwable) {
            is ValidationException -> showValidationError(throwable)

            is AuthInvalidCredentialsException -> {
                showMessage(
                    R.string.error_message_title,
                    R.string.invalid_credentials_message,
                    R.string.ok,
                )
            }

            is AuthUserCollisionException -> {
                showMessage(
                    R.string.error_message_title,
                    R.string.existed_user_message,
                    R.string.ok,
                )
            }

            is AuthUserCreationException -> {
                showMessage(
                    R.string.error_message_title,
                    R.string.unable_to_create_user_message,
                    R.string.ok,
                )
            }

            is UnableToUploadPhotoToStorageException -> {
                showMessage(
                    R.string.error_message_title,
                    R.string.unable_to_upload_photo_to_storage_message,
                    R.string.ok,
                )
            }

            else -> super.handleError(throwable)
        }
    }

    private fun showValidationError(throwable: Throwable) {
        when (throwable) {
            is RegistrationValidationException -> {
                throwable.exceptions.forEach(::showValidationError)
            }

            is EmailValidationException -> {
                binding?.signUpFieldEmail?.error =
                    getString(R.string.email_required_message)
            }

            is UsernameValidationException -> {
                binding?.signUpFieldUsername?.error =
                    getString(R.string.username_registration_required_message)
            }

            is PasswordValidationException -> {
                binding?.signUpFieldPassword?.error =
                    getString(R.string.password_registration_required_message)
            }

            is WeakPasswordValidationException -> {
                binding?.signUpFieldPassword?.error =
                    getString(R.string.weak_password_message)
            }
        }
    }

    private fun hideValidationError() {
        binding?.apply {
            signUpFieldEmail.error = null
            signUpFieldUsername.error = null
            signUpFieldPassword.error = null
        }
    }
}