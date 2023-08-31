package com.yasinskyi.firebase.edu.view.impl.fragment.login

import android.os.Bundle
import com.yasinskyi.firebase.edu.R
import com.yasinskyi.firebase.edu.data.exception.auth.AuthSendPasswordResetException
import com.yasinskyi.firebase.edu.presentation.impl.exception.validation.EmailValidationException
import com.yasinskyi.firebase.edu.databinding.FragmentRestorePasswordBinding
import com.yasinskyi.firebase.edu.presentation.impl.viewmodel.login.RestorePasswordViewModel
import com.yasinskyi.firebase.edu.util.extension.lifecycle.observeWhenCreated
import com.yasinskyi.firebase.edu.view.base.fragment.BaseFragment

class RestorePasswordFragment :
    BaseFragment<FragmentRestorePasswordBinding, RestorePasswordViewModel>() {

    private fun observeEmailFieldState(email: String?) {
        binding?.restoreFieldEmail?.editText?.setText(email)
    }

    private fun observeRestoreState(isSuccessful: Boolean) {
        if (isSuccessful) {
            showSuccessMessage()
        }
    }

    private fun showSuccessMessage() {
        showMessage(
            R.string.notice_message_title,
            R.string.restore_password_request_send_message,
            R.string.ok,
        )
    }

    override fun onViewBound(binding: FragmentRestorePasswordBinding, savedInstanceState: Bundle?) {
        super.onViewBound(binding, savedInstanceState)

        with(binding) {
            restorePasswordButton.setOnClickListener {
                restorePassword()
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
                restoreFieldEmail.editText?.text.toString(),
            )
        }
    }

    override fun listenViewModel(viewModel: RestorePasswordViewModel) {
        super.listenViewModel(viewModel)
        observeWhenCreated(viewModel.emailFieldState, ::observeEmailFieldState)
        observeWhenCreated(viewModel.restoreState, ::observeRestoreState)
    }

    private fun restorePassword() {
        saveFields()
        viewModel.restorePassword()
        hideValidationError()
    }

    override fun handleError(throwable: Throwable) {
        when (throwable) {
            is EmailValidationException -> showValidationError()

            is AuthSendPasswordResetException -> {
                showSuccessMessage()
            }

            else -> super.handleError(throwable)
        }
    }

    private fun showValidationError() {
        binding?.restoreFieldEmail?.error = getString(R.string.email_required_message)
    }

    private fun hideValidationError() {
        binding?.restoreFieldEmail?.error = null
    }
}