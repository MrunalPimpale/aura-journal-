package com.example.service

import android.content.Context
import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

object FirebaseManager {
  private const val TAG = "FirebaseManager"

  fun initialize(context: Context) {
    try {
      if (FirebaseApp.getApps(context).isEmpty()) {
        val options = FirebaseOptions.Builder()
          .setApplicationId("1:501289393860:android:aistudioaurajournal")
          .setProjectId("aura-journal-ideathon")
          .setApiKey("AIzaSyB_SafeAppIdentifier_ClientMarker")
          .build()
        FirebaseApp.initializeApp(context.applicationContext, options)
        Log.i(TAG, "Firebase initialized safely")
      }
    } catch (e: Exception) {
      Log.w(TAG, "Firebase init exception: ${e.javaClass.simpleName}")
    }
  }

  val auth: FirebaseAuth?
    get() = try {
      FirebaseAuth.getInstance()
    } catch (e: Exception) {
      null
    }

  val firestore: FirebaseFirestore?
    get() = try {
      FirebaseFirestore.getInstance()
    } catch (e: Exception) {
      null
    }
}

suspend fun <T> Task<T>.awaitTask(): T = suspendCancellableCoroutine { continuation ->
  addOnSuccessListener { result ->
    if (continuation.isActive) {
      continuation.resume(result)
    }
  }
  addOnFailureListener { exception ->
    if (continuation.isActive) {
      continuation.resumeWithException(exception)
    }
  }
  addOnCanceledListener {
    continuation.cancel()
  }
}
