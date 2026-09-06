package com.example.model

data class EmotionalState(
  val label: String,
  val emoji: String,
  val promptStarter: String,
  val energyScore: Int // 1 to 10
)

data class ChatMessage(
  val id: String,
  val sender: String, // "user" or "gemini"
  val content: String,
  val timestamp: Long = System.currentTimeMillis(),
  val detectedDistortion: String? = null,
  val suggestedReflection: String? = null,
  val suggestedAction: String? = null
)

data class CognitiveActionItem(
  val id: String,
  val title: String,
  val subtitle: String,
  val colorType: String, // "blue", "amber", "green"
  val isCompleted: Boolean = false
)

data class ReflectiveSession(
  val id: String,
  val title: String,
  val prompt: String,
  val messages: List<ChatMessage> = emptyList(),
  val primaryDistortion: String = "None",
  val reframedInsight: String = "",
  val moodBefore: Int = 4,
  val moodAfter: Int = 8,
  val timestamp: Long = System.currentTimeMillis(),
  val status: String = "active", // "active" or "completed"
  val actionItems: List<CognitiveActionItem> = emptyList()
)

data class SecurityStatus(
  val currentUserId: String = "",
  val currentUserName: String = "",
  val isAnonymous: Boolean = true,
  val isAuthenticated: Boolean = false,
  val userRole: String = "Authenticated Tenant",
  val firestoreIsolationActive: Boolean = true,
  val secretManagerEnforced: Boolean = true,
  val zeroCrossUserLeakage: Boolean = true,
  val activeTenantKey: String = "gcp_secret_manager_isolated"
)

