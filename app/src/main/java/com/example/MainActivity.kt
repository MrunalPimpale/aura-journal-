package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.data.JournalRepository
import com.example.ui.components.BentoBottomNavigation
import com.example.ui.components.BentoHeader
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.InsightsScreen
import com.example.ui.screens.JournalsScreen
import com.example.ui.screens.SecurityScreen
import com.example.ui.theme.BentoBackground
import com.example.ui.theme.MyApplicationTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    val repository = JournalRepository(applicationContext)

    setContent {
      MyApplicationTheme {
        AuraJournalApp(repository = repository)
      }
    }
  }
}

@Composable
fun AuraJournalApp(repository: JournalRepository) {
  var currentTab by remember { mutableStateOf("home") }
  val scope = rememberCoroutineScope()

  val sessions by repository.sessions.collectAsState()
  val activeSessionId by repository.activeSessionId.collectAsState()
  val securityStatus by repository.securityStatus.collectAsState()
  val actionItems by repository.actionItems.collectAsState()
  val auditLogs by repository.auditLogs.collectAsState()
  val isAwaitingAi by repository.isAwaitingAi.collectAsState()
  val selectedEmotion by repository.selectedEmotion.collectAsState()
  val isBreathingActive by repository.breathingActive.collectAsState()
  val completedCycles by repository.groundingCompletedCount.collectAsState()

  val activeSession = sessions.find { it.id == activeSessionId } ?: sessions.firstOrNull()

  Scaffold(
    contentWindowInsets = WindowInsets(0, 0, 0, 0),
    topBar = {
      BentoHeader(
        securityStatus = securityStatus,
        onProfileClick = { currentTab = "security" }
      )
    },
    bottomBar = {
      BentoBottomNavigation(
        currentTab = currentTab,
        onTabSelect = { currentTab = it }
      )
    },
    modifier = Modifier.fillMaxSize()
  ) { innerPadding ->
    Box(
      modifier = Modifier
        .fillMaxSize()
        .background(BentoBackground)
        .padding(innerPadding)
    ) {
      Crossfade(targetState = currentTab, label = "tab_transition") { tab ->
        when (tab) {
          "home" -> HomeScreen(
            activeSession = activeSession,
            actionItems = actionItems,
            securityStatus = securityStatus,
            emotionalStates = repository.emotionalStates,
            selectedEmotion = selectedEmotion,
            isBreathingActive = isBreathingActive,
            completedCycles = completedCycles,
            onSelectEmotion = { emotion ->
              repository.selectEmotion(emotion)
              // Auto-start a targeted reflective session for this emotional state
              repository.createNewSession(
                promptTitle = "${emotion.emoji} ${emotion.label} Check-in",
                initialPrompt = emotion.promptStarter
              )
              currentTab = "journals"
            },
            onToggleBreathing = { repository.toggleBreathingExercise() },
            onCyclePrompt = { repository.cycleNextPrompt() },
            onContinueReflection = { currentTab = "journals" },
            onNewSession = {
              repository.createNewSession("New Evening Reflection")
              currentTab = "journals"
            },
            onSecurityCardClick = { currentTab = "security" },
            onMoodCardClick = { currentTab = "insights" },
            onToggleActionItem = { repository.toggleActionItem(it) },
            onAddActionItem = { title, subtitle -> repository.addActionItem(title, subtitle) }
          )

          "journals" -> JournalsScreen(
            sessions = sessions,
            activeSessionId = activeSessionId,
            isAwaitingAi = isAwaitingAi,
            onSelectSession = { repository.selectSession(it) },
            onNewSession = { repository.createNewSession("New Socratic Reflection") },
            onSendMessage = { sessionId, text ->
              scope.launch {
                repository.sendUserMessage(sessionId, text)
              }
            },
            onAddSuggestedAction = { actionText ->
              repository.addSuggestedActionFromChat(actionText)
            }
          )

          "insights" -> InsightsScreen()

          "security" -> SecurityScreen(
            securityStatus = securityStatus,
            auditLogs = auditLogs,
            onSwitchAccount = {
              repository.switchUserAccount()
            }
          )
        }
      }
    }
  }
}

