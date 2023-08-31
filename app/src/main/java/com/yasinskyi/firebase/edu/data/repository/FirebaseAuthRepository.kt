package com.yasinskyi.firebase.edu.data.repository

import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.yasinskyi.firebase.edu.data.AuthRepository
import com.yasinskyi.firebase.edu.data.exception.InternetConnectionException
import com.yasinskyi.firebase.edu.data.exception.auth.AuthInvalidCredentialsException
import com.yasinskyi.firebase.edu.data.exception.auth.AuthInvalidUserException
import com.yasinskyi.firebase.edu.data.exception.auth.AuthSendPasswordResetException
import com.yasinskyi.firebase.edu.data.exception.auth.AuthUserCollisionException
import com.yasinskyi.firebase.edu.data.exception.auth.AuthUserCreationException
import com.yasinskyi.firebase.edu.data.exception.auth.AuthUserResignInException
import com.yasinskyi.firebase.edu.data.exception.auth.AuthUserSignInException
import com.yasinskyi.firebase.edu.data.exception.auth.AuthUserSignOutException
import com.yasinskyi.firebase.edu.presentation.impl.entity.Credentials
import com.yasinskyi.firebase.edu.presentation.impl.exception.validation.WeakPasswordValidationException
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseAuthRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
) : AuthRepository {

    override suspend fun signIn(credentials: Credentials) {
        try {
            firebaseAuth
                .signInWithEmailAndPassword(credentials.email, credentials.password)
                .await()
        } catch (exception: FirebaseException) {
            when (exception) {
                is FirebaseNetworkException -> throw InternetConnectionException()
                is FirebaseAuthInvalidCredentialsException -> throw AuthInvalidCredentialsException()
                is FirebaseAuthInvalidUserException -> throw AuthInvalidUserException()
                else -> throw AuthUserSignInException()
            }
        }
    }

    override suspend fun signOut() {
        try {
            firebaseAuth.signOut()
        } catch (exception: FirebaseException) {
            when (exception) {
                is FirebaseNetworkException -> throw InternetConnectionException()
                else -> throw AuthUserSignOutException()
            }
        }
    }

    override suspend fun resignIn() {
        val user = firebaseAuth.currentUser

        if (user != null) {
            try {
                user.reload().await()
            } catch (exception: FirebaseException) {
                when (exception) {
                    is FirebaseNetworkException -> throw InternetConnectionException()
                    else -> throw AuthUserResignInException()
                }
            }
        } else {
            throw AuthUserSignInException()
        }
    }

    override suspend fun createUser(credentials: Credentials) {
        try {
            firebaseAuth
                .createUserWithEmailAndPassword(credentials.email, credentials.password)
                .await()
        } catch (exception: FirebaseException) {
            when (exception) {
                is FirebaseNetworkException -> throw InternetConnectionException()
                is FirebaseAuthUserCollisionException -> throw AuthUserCollisionException()
                is FirebaseAuthWeakPasswordException -> throw WeakPasswordValidationException()
                else -> throw AuthUserCreationException()
            }
        }
    }

    override suspend fun sendPasswordResetEmail(email: String) {
        try {
            firebaseAuth
                .sendPasswordResetEmail(email)
                .await()
        } catch (exception: Exception) {
            when (exception) {
                is FirebaseNetworkException -> throw InternetConnectionException()
                else -> throw AuthSendPasswordResetException()
            }
        }
    }
}