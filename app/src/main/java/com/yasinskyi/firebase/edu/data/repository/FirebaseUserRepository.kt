package com.yasinskyi.firebase.edu.data.repository

import androidx.core.net.toUri
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.yasinskyi.firebase.edu.data.UserRepository
import com.yasinskyi.firebase.edu.data.exception.InternetConnectionException
import com.yasinskyi.firebase.edu.data.exception.auth.AuthInvalidUserException
import com.yasinskyi.firebase.edu.data.exception.database.UnableLoadUserListException
import com.yasinskyi.firebase.edu.data.exception.storage.UnableToUploadPhotoToStorageException
import com.yasinskyi.firebase.edu.presentation.impl.entity.User
import com.yasinskyi.firebase.edu.util.formatter.DateFormatter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import java.util.Date
import java.util.UUID
import javax.inject.Inject

class FirebaseUserRepository @Inject constructor(
    private val firebaseStorage: FirebaseStorage,
    private val firebaseFirestore: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth,
) : UserRepository {

    override suspend fun loadUsers(limit: Int) = flow<List<User>?> {
        val documentSnapshot = firebaseFirestore
            .collection(USER_PATH)
            .orderBy(ORDER_BY_FIELD)
            .limit(limit.toLong())
            .get()
            .await()

        emit(documentSnapshot.toObjects(User::class.java))
    }.catch {
        when (it) {
            is FirebaseNetworkException -> throw InternetConnectionException()
            else -> throw UnableLoadUserListException()
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun updateUserImage(imageUri: String) {
        val storageReference =
            firebaseStorage
                .getReference(IMAGE_PATH + generatePhotoName())

        val currentUser = requestCurrentUser()
        if (!currentUser.imageUri.isNullOrEmpty()) {
            deleteUserImageToFirebaseStorage(currentUser.imageUri)
        }

        updateUserImageToFirebaseStorage(imageUri, storageReference)
    }

    override suspend fun addUser(user: User) {
        val storageReference =
            firebaseStorage
                .getReference(IMAGE_PATH + generatePhotoName())

        if (user.imageUri != null) {
            try {
                saveUserImageToFirebaseStorage(user, storageReference)
            } catch (exception: Exception) {
                when (exception) {
                    is FirebaseNetworkException -> throw InternetConnectionException()
                    else -> throw UnableToUploadPhotoToStorageException()
                }
            }
        } else {
            saveUserToFirebaseDatabase(
                user.copy(
                    id = generateUserId(),
                    imageUri = EMPTY_URI,
                )
            )
        }
    }

    private suspend fun saveUserImageToFirebaseStorage(
        user: User,
        storageReference: StorageReference,
    ) {
        user.imageUri?.let {
            storageReference
                .putFile(it.toUri())
                .addOnSuccessListener {
                    storageReference
                        .downloadUrl
                        .addOnSuccessListener { uri ->
                            saveUserToFirebaseDatabase(
                                user.copy(
                                    id = generateUserId(),
                                    imageUri = uri.toString(),
                                )
                            )
                        }
                }
                .await()
        }
    }

    private suspend fun updateUserImageToFirebaseStorage(
        imageUri: String,
        storageReference: StorageReference,
    ) {
        storageReference
            .putFile(imageUri.toUri())
            .addOnSuccessListener {
                storageReference
                    .downloadUrl
                    .addOnSuccessListener { uri ->
                        updateUserImageUriToFirebaseDatabase(uri.toString())
                    }
            }
            .await()
    }

    private suspend fun requestCurrentUser(): User {
        return firebaseFirestore
            .collection(USER_PATH)
            .document(generateUserId())
            .get()
            .await()
            .toObject(User::class.java) ?: throw AuthInvalidUserException()
    }

    private suspend fun deleteUserImageToFirebaseStorage(imageUri: String) {
        firebaseStorage
            .getReferenceFromUrl(imageUri)
            .delete()
            .await()
    }

    private fun updateUserImageUriToFirebaseDatabase(imageUri: String) {
        firebaseFirestore
            .collection(USER_PATH)
            .document(generateUserId())
            .update(
                mapOf(
                    "imageUri" to imageUri,
                )
            )
    }

    private fun saveUserToFirebaseDatabase(user: User) {
        firebaseFirestore
            .collection(USER_PATH)
            .document(user.id)
            .set(user)
    }

    private fun generateUserId(): String {
        return firebaseAuth.currentUser?.uid ?: throw AuthInvalidUserException()
    }

    private fun generatePhotoName(): String {
        return UUID.randomUUID().toString() + DateFormatter.formatDate(Date())
    }

    companion object {

        private const val ORDER_BY_FIELD = "username"
        private const val IMAGE_PATH = "images/"
        private const val USER_PATH = "users/"
        private const val EMPTY_URI = ""
    }
}