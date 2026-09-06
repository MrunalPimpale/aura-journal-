package com.example.data

import android.util.Log
import com.example.model.ChatMessage
import com.example.model.CognitiveActionItem
import com.example.model.ReflectiveSession
import com.example.service.FirebaseManager
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query

/**
 * Production Cloud Firestore Data Service:
 * - Strictly enforces per-user isolation under /users/{authenticatedUserUid}/
 * - The authenticatedUserUid must originate exclusively from FirebaseAuth.currentUser.uid.
 * - Manages sessions, messages subcollection, and cognitive action items with offline persistence.
 */
object FirestoreService {
  private const val TAG = "FirestoreService"

  fun observeSessions(
    userId: String,
    onUpdate: (List<ReflectiveSession>) -> Unit
  ): ListenerRegistration? {
    if (userId.isBlank()) return null
    val firestore = FirebaseManager.firestore ?: return null

    return try {
      firestore.collection("users")
        .document(userId)
        .collection("sessions")
        .orderBy("timestamp", Query.Direction.DESCENDING)
        .addSnapshotListener { snapshot, error ->
          if (error != null) {
            Log.w(TAG, "Sessions snapshot error (isolated fallback active): ${error.javaClass.simpleName}")
            return@addSnapshotListener
          }
          if (snapshot != null) {
            val sessions = snapshot.documents.mapNotNull { doc ->
              val id = doc.getString("id") ?: doc.id
              val title = doc.getString("title") ?: "Reflection"
              val prompt = doc.getString("prompt") ?: ""
              val primaryDistortion = doc.getString("primaryDistortion") ?: "None"
              val reframedInsight = doc.getString("reframedInsight") ?: ""
              val moodBefore = doc.getLong("moodBefore")?.toInt() ?: 4
              val moodAfter = doc.getLong("moodAfter")?.toInt() ?: 8
              val timestamp = doc.getLong("timestamp") ?: System.currentTimeMillis()
              val status = doc.getString("status") ?: "active"

              ReflectiveSession(
                id = id,
                title = title,
                prompt = prompt,
                primaryDistortion = primaryDistortion,
                reframedInsight = reframedInsight,
                moodBefore = moodBefore,
                moodAfter = moodAfter,
                timestamp = timestamp,
                status = status
              )
            }
            onUpdate(sessions)
          }
        }
    } catch (e: Exception) {
      Log.w(TAG, "Failed to attach sessions listener: ${e.javaClass.simpleName}")
      null
    }
  }

  fun observeMessages(
    userId: String,
    sessionId: String,
    onUpdate: (List<ChatMessage>) -> Unit
  ): ListenerRegistration? {
    if (userId.isBlank() || sessionId.isBlank()) return null
    val firestore = FirebaseManager.firestore ?: return null

    return try {
      firestore.collection("users")
        .document(userId)
        .collection("sessions")
        .document(sessionId)
        .collection("messages")
        .orderBy("timestamp", Query.Direction.ASCENDING)
        .addSnapshotListener { snapshot, error ->
          if (error != null) {
            Log.w(TAG, "Messages snapshot error: ${error.javaClass.simpleName}")
            return@addSnapshotListener
          }
          if (snapshot != null) {
            val messages = snapshot.documents.mapNotNull { doc ->
              val id = doc.getString("id") ?: doc.id
              val sender = doc.getString("sender") ?: "user"
              val content = doc.getString("content") ?: ""
              val timestamp = doc.getLong("timestamp") ?: System.currentTimeMillis()
              val distortion = doc.getString("detectedDistortion")
              val reflection = doc.getString("suggestedReflection")
              val action = doc.getString("suggestedAction")
              ChatMessage(
                id = id,
                sender = sender,
                content = content,
                timestamp = timestamp,
                detectedDistortion = distortion,
                suggestedReflection = reflection,
                suggestedAction = action
              )
            }
            onUpdate(messages)
          }
        }
    } catch (e: Exception) {
      Log.w(TAG, "Failed to attach messages listener: ${e.javaClass.simpleName}")
      null
    }
  }

  fun observeActionItems(
    userId: String,
    onUpdate: (List<CognitiveActionItem>) -> Unit
  ): ListenerRegistration? {
    if (userId.isBlank()) return null
    val firestore = FirebaseManager.firestore ?: return null

    return try {
      firestore.collection("users")
        .document(userId)
        .collection("action_items")
        .addSnapshotListener { snapshot, error ->
          if (error != null) {
            Log.w(TAG, "Action items snapshot error: ${error.javaClass.simpleName}")
            return@addSnapshotListener
          }
          if (snapshot != null) {
            val items = snapshot.documents.mapNotNull { doc ->
              val id = doc.getString("id") ?: doc.id
              val title = doc.getString("title") ?: ""
              val subtitle = doc.getString("subtitle") ?: ""
              val colorType = doc.getString("colorType") ?: "blue"
              val isCompleted = doc.getBoolean("isCompleted") ?: false
              CognitiveActionItem(
                id = id,
                title = title,
                subtitle = subtitle,
                colorType = colorType,
                isCompleted = isCompleted
              )
            }
            onUpdate(items)
          }
        }
    } catch (e: Exception) {
      Log.w(TAG, "Failed to attach action items listener: ${e.javaClass.simpleName}")
      null
    }
  }

  fun saveSession(userId: String, session: ReflectiveSession) {
    if (userId.isBlank()) return
    val firestore = FirebaseManager.firestore ?: return
    try {
      val map = hashMapOf(
        "id" to session.id,
        "title" to session.title,
        "prompt" to session.prompt,
        "primaryDistortion" to session.primaryDistortion,
        "reframedInsight" to session.reframedInsight,
        "moodBefore" to session.moodBefore,
        "moodAfter" to session.moodAfter,
        "timestamp" to session.timestamp,
        "status" to session.status
      )
      firestore.collection("users")
        .document(userId)
        .collection("sessions")
        .document(session.id)
        .set(map)
    } catch (e: Exception) {
      Log.w(TAG, "Save session error: ${e.javaClass.simpleName}")
    }
  }

  fun saveMessage(userId: String, sessionId: String, message: ChatMessage) {
    if (userId.isBlank() || sessionId.isBlank()) return
    val firestore = FirebaseManager.firestore ?: return
    try {
      val map = hashMapOf(
        "id" to message.id,
        "sender" to message.sender,
        "content" to message.content,
        "timestamp" to message.timestamp,
        "detectedDistortion" to message.detectedDistortion,
        "suggestedReflection" to message.suggestedReflection,
        "suggestedAction" to message.suggestedAction
      )
      firestore.collection("users")
        .document(userId)
        .collection("sessions")
        .document(sessionId)
        .collection("messages")
        .document(message.id)
        .set(map)
    } catch (e: Exception) {
      Log.w(TAG, "Save message error: ${e.javaClass.simpleName}")
    }
  }

  fun saveActionItem(userId: String, item: CognitiveActionItem) {
    if (userId.isBlank()) return
    val firestore = FirebaseManager.firestore ?: return
    try {
      val map = hashMapOf(
        "id" to item.id,
        "title" to item.title,
        "subtitle" to item.subtitle,
        "colorType" to item.colorType,
        "isCompleted" to item.isCompleted,
        "timestamp" to System.currentTimeMillis()
      )
      firestore.collection("users")
        .document(userId)
        .collection("action_items")
        .document(item.id)
        .set(map)
    } catch (e: Exception) {
      Log.w(TAG, "Save action item error: ${e.javaClass.simpleName}")
    }
  }

  fun toggleActionItem(userId: String, itemId: String, isCompleted: Boolean) {
    if (userId.isBlank()) return
    val firestore = FirebaseManager.firestore ?: return
    try {
      firestore.collection("users")
        .document(userId)
        .collection("action_items")
        .document(itemId)
        .update("isCompleted", isCompleted)
    } catch (e: Exception) {
      Log.w(TAG, "Toggle action item error: ${e.javaClass.simpleName}")
    }
  }
}
