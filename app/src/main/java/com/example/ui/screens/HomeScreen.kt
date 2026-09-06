package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.CognitiveActionItem
import com.example.model.EmotionalState
import com.example.model.ReflectiveSession
import com.example.model.SecurityStatus
import com.example.ui.components.BentoActionPlanCard
import com.example.ui.components.BentoEmotionalPulseSelector
import com.example.ui.components.BentoGroundingExerciseCard
import com.example.ui.components.BentoHeroCard
import com.example.ui.components.BentoMoodCard
import com.example.ui.components.BentoSecurityCard
import com.example.ui.theme.BentoBackground
import com.example.ui.theme.BentoBluePrimary
import com.example.ui.theme.BentoSurface
import com.example.ui.theme.BentoTextPrimary
import com.example.ui.theme.BentoTextSecondary

@Composable
fun HomeScreen(
  activeSession: ReflectiveSession?,
  actionItems: List<CognitiveActionItem>,
  securityStatus: SecurityStatus,
  emotionalStates: List<EmotionalState>,
  selectedEmotion: EmotionalState,
  isBreathingActive: Boolean,
  completedCycles: Int,
  onSelectEmotion: (EmotionalState) -> Unit,
  onToggleBreathing: () -> Unit,
  onCyclePrompt: () -> Unit,
  onContinueReflection: () -> Unit,
  onNewSession: () -> Unit,
  onSecurityCardClick: () -> Unit,
  onMoodCardClick: () -> Unit,
  onToggleActionItem: (String) -> Unit,
  onAddActionItem: (String, String) -> Unit,
  modifier: Modifier = Modifier
) {
  val scrollState = rememberScrollState()
  var showAddDialog by remember { mutableStateOf(false) }
  var newStepTitle by remember { mutableStateOf("") }
  var newStepSubtitle by remember { mutableStateOf("") }

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(BentoBackground)
      .verticalScroll(scrollState)
      .padding(16.dp),
    verticalArrangement = Arrangement.spacedBy(14.dp)
  ) {
    // 1. Bento Emotional Pulse Selector
    BentoEmotionalPulseSelector(
      emotionalStates = emotionalStates,
      selectedEmotion = selectedEmotion,
      onSelectEmotion = onSelectEmotion
    )

    // 2. Bento Hero Card with Prompt Cycling
    BentoHeroCard(
      promptTitle = activeSession?.prompt ?: "How can we turn today's challenges into secure breakthroughs?",
      onContinueClick = onContinueReflection,
      onNewSessionClick = onNewSession,
      onCyclePrompt = onCyclePrompt
    )

    // 3. Interactive Grounding Pulse (4-4-4 Socratic Breath)
    BentoGroundingExerciseCard(
      isBreathingActive = isBreathingActive,
      completedCycles = completedCycles,
      onToggleBreathing = onToggleBreathing
    )

    // 4. Bento Middle Row (Security Card & Mood Analysis Card)
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
      BentoSecurityCard(
        onClick = onSecurityCardClick,
        modifier = Modifier.weight(1f)
      )

      BentoMoodCard(
        onClick = onMoodCardClick,
        modifier = Modifier.weight(1f)
      )
    }

    // 5. Bento Cognitive Action Plan with Add Step
    BentoActionPlanCard(
      actionItems = actionItems,
      onToggleItem = onToggleActionItem,
      onAddNewItem = { showAddDialog = true }
    )

    Spacer(modifier = Modifier.height(16.dp))
  }

  // Interactive Add Action Item Dialog
  if (showAddDialog) {
    AlertDialog(
      onDismissRequest = { showAddDialog = false },
      containerColor = BentoSurface,
      shape = RoundedCornerShape(24.dp),
      title = {
        Text(
          text = "Add Micro-Action Step",
          fontSize = 18.sp,
          fontWeight = FontWeight.Bold,
          color = BentoTextPrimary
        )
      },
      text = {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
          Text(
            text = "Define a small, low-friction action to deconstruct current cognitive resistance.",
            fontSize = 12.sp,
            color = BentoTextSecondary,
            lineHeight = 16.sp
          )
          OutlinedTextField(
            value = newStepTitle,
            onValueChange = { newStepTitle = it },
            label = { Text("Action Step (e.g., Write unit test)") },
            singleLine = true,
            modifier = Modifier
              .fillMaxWidth()
              .testTag("action_step_title_input")
          )
          OutlinedTextField(
            value = newStepSubtitle,
            onValueChange = { newStepSubtitle = it },
            label = { Text("Context or Details (Optional)") },
            singleLine = true,
            modifier = Modifier
              .fillMaxWidth()
              .testTag("action_step_subtitle_input")
          )
        }
      },
      confirmButton = {
        Button(
          onClick = {
            if (newStepTitle.isNotBlank()) {
              onAddActionItem(
                newStepTitle.trim(),
                if (newStepSubtitle.isNotBlank()) newStepSubtitle.trim() else "Added by user"
              )
              newStepTitle = ""
              newStepSubtitle = ""
              showAddDialog = false
            }
          },
          colors = ButtonDefaults.buttonColors(containerColor = BentoBluePrimary),
          shape = RoundedCornerShape(14.dp),
          modifier = Modifier.testTag("submit_new_action_step")
        ) {
          Text("Add Step", fontWeight = FontWeight.Bold)
        }
      },
      dismissButton = {
        OutlinedButton(
          onClick = { showAddDialog = false },
          shape = RoundedCornerShape(14.dp)
        ) {
          Text("Cancel", color = BentoTextSecondary)
        }
      }
    )
  }
}
