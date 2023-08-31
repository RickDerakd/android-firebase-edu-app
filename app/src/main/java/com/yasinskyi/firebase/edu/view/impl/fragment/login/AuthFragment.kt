package com.yasinskyi.firebase.edu.view.impl.fragment.login

import android.os.Bundle
import com.yasinskyi.firebase.edu.R
import com.yasinskyi.firebase.edu.data.exception.auth.AuthInvalidCredentialsException
import com.yasinskyi.firebase.edu.data.exception.auth.AuthInvalidUserException
import com.yasinskyi.firebase.edu.data.exception.auth.AuthUserSignInException
import com.yasinskyi.firebase.edu.presentation.impl.exception.validation.EmailValidationException
import com.yasinskyi.firebase.edu.presentation.impl.exception.validation.PasswordValidationException
import com.yasinskyi.firebase.edu.databinding.FragmentAuthBinding
import com.yasinskyi.firebase.edu.presentation.impl.exception.ValidationException
import com.yasinskyi.firebase.edu.presentation.impl.exception.validation.CredentialsValidationException
import com.yasinskyi.firebase.edu.presentation.impl.viewmodel.login.AuthViewModel
import com.yasinskyi.firebase.edu.util.extension.lifecycle.observeWhenCreated
import com.yasinskyi.firebase.edu.util.extension.navigation.navigateSafe
import com.yasinskyi.firebase.edu.view.base.fragment.BaseFragment

class AuthFragment : BaseFragment<FragmentAuthBinding, AuthViewModel>() {

    private fun observeEmailFieldState(email: String?) {
        binding?.signInFieldEmail?.editText?.setText(email)
    }

    private fun observePasswordFieldState(password: String?) {
        binding?.signInFieldPassword?.editText?.setText(password)
    }

    private fun observeAuthState(isSuccessful: Boolean) {
        if (isSuccessful) {
            navigateToHomeScreen()
        }
    }

    override fun listenViewModel(viewModel: AuthViewModel) {
        super.listenViewModel(viewModel)
        observeWhenCreated(viewModel.emailFieldState, ::observeEmailFieldState)
        observeWhenCreated(viewModel.passwordFieldState, ::observePasswordFieldState)
        observeWhenCreated(viewModel.authState, ::observeAuthState)
    }

    private fun navigateToHomeScreen() {
        navigateSafe(R.id.actionAuthFragmentToMainActivityGraph)
    }

    override fun onViewBound(binding: FragmentAuthBinding, savedInstanceState: Bundle?) {
        with(binding) {
            signInButton.setOnClickListener {
                signInUser(binding)
            }

            signUpButton.setOnClickListener {
                navigateToRegistrationScreen()
            }

            restorePasswordButton.setOnClickListener {
                navigateToRestorePasswordScreen()
            }
        }
    }

    override fun onStop() {
        saveFields()
        super.onStop()
    }

    private fun saveFields() {
        binding?.apply {
            viewModel.saveFields(
                signInFieldEmail.editText?.text?.trimEnd().toString(),
                signInFieldPassword.editText?.text.toString(),
            )
        }
    }

    private fun signInUser(binding: FragmentAuthBinding) {
        saveFields()
        viewModel.signIn()
        hideValidationError(binding)
    }

    private fun navigateToRegistrationScreen() {
        navigateSafe(R.id.actionAuthFragmentToRegistrationFragment)
    }

    private fun navigateToRestorePasswordScreen() {
        navigateSafe(R.id.actionAuthFragmentToRestorePasswordFragment)
    }

    override fun handleError(throwable: Throwable) {
        when (throwable) {
            is ValidationException -> showValidationError(throwable)

            is AuthUserSignInException -> {
                showMessage(
                    R.string.error_message_title,
                    R.string.non_authorized_message,
                    R.string.ok,
                )
            }

            is AuthInvalidUserException -> {
                showMessage(
                    R.string.error_message_title,
                    R.string.invalid_user_message,
                    R.string.ok,
                )
            }

            is AuthInvalidCredentialsException -> {
                showMessage(
                    R.string.error_message_title,
                    R.string.invalid_credentials_message,
                    R.string.ok,
                )
            }

            else -> super.handleError(throwable)
        }
    }

    private fun showValidationError(throwable: Throwable) {
        when (throwable) {
            is CredentialsValidationException -> {
                throwable.exceptions.forEach(::showValidationError)
            }

            is EmailValidationException -> {
                binding?.signInFieldEmail?.error =
                    getString(R.string.email_required_message)
            }

            is PasswordValidationException -> {
                binding?.signInFieldPassword?.error =
                    getString(R.string.password_required_message)
            }
        }
    }

    private fun hideValidationError(binding: FragmentAuthBinding) {
        binding.apply {
            signInFieldEmail.error = null
            signInFieldPassword.error = null
        }
    }
}