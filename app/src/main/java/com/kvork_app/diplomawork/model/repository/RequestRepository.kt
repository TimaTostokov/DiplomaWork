package com.kvork_app.diplomawork.model.repository

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObject
import com.kvork_app.diplomawork.model.dto.RequestItem
import kotlinx.coroutines.tasks.await

@Suppress("DEPRECATION")
class RequestRepository {

    private val db = FirebaseFirestore.getInstance()
    private val collectionName = "requests"

    suspend fun addRequest(request: RequestItem): String? {
        return try {
            if (request.id.isNotEmpty()) {
                db.collection(collectionName)
                    .document(request.id)
                    .set(request)
                    .await()
                request.id
            } else {
                val docRef = db.collection(collectionName).add(request).await()
                docRef.id
            }
        } catch (e: Exception) {
            Log.e("RequestRepository", "Ошибка при добавлении заявки: ${e.message}", e)
            null
        }
    }

    suspend fun updateRequestById(id: String, newData: RequestItem): Boolean {
        return try {
            val updateMap = mutableMapOf<String, Any>(
                "dateOfRegistration" to newData.dateOfRegistration,
                "address" to newData.address,
                "contact" to newData.contact,
                "status" to newData.status,
                "masterFio" to newData.masterFio,
                "typeOfWork" to newData.typeOfWork,
                "materials" to newData.materials
            )
            if (newData.description.isNotBlank()) {
                updateMap["description"] = newData.description
            }
            db.collection(collectionName)
                .document(id)
                .update(updateMap)
                .await()
            true
        } catch (e: Exception) {
            Log.e("RequestRepository", "Ошибка при обновлении заявки: ${e.message}", e)
            false
        }
    }

    suspend fun getAllRequests(): List<RequestItem> {
        return try {
            val snapshot = db.collection(collectionName)
                .get()
                .await()
            snapshot.documents.mapNotNull { doc ->
                doc.toObject<RequestItem>()?.copy(id = doc.id)
            }
        } catch (e: Exception) {
            Log.e("RequestRepository", "Ошибка при получении заявок: ${e.message}", e)
            emptyList()
        }
    }

    suspend fun getRequestsSortedByDateDesc(): List<RequestItem> {
        return try {
            val snapshot = db.collection(collectionName)
                .orderBy("dateOfRegistration", Query.Direction.DESCENDING)
                .get()
                .await()
            snapshot.documents.mapNotNull { doc ->
                doc.toObject<RequestItem>()?.copy(id = doc.id)
            }
        } catch (e: Exception) {
            Log.e("RequestRepository", "Ошибка при сортировке заявок: ${e.message}", e)
            emptyList()
        }
    }

    suspend fun getRequestsByYear(year: String): List<RequestItem> {
        return try {
            val all = getAllRequests()
            all.filter {
                it.dateOfRegistration.startsWith(year)
            }
        } catch (e: Exception) {
            Log.e("RequestRepository", "Ошибка при фильтрации по го+ду: ${e.message}", e)
            emptyList()
        }
    }

    suspend fun getRequestById(id: String): RequestItem? {
        return try {
            val snapshot = db.collection(collectionName)
                .document(id)
                .get()
                .await()
            snapshot.toObject<RequestItem>()?.copy(id = snapshot.id)
        } catch (e: Exception) {
            Log.e("RequestRepository", "Ошибка при получении заявки по id: ${e.message}", e)
            null
        }
    }
}
