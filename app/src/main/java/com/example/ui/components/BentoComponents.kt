package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Insights
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.CognitiveActionItem
import com.example.model.EmotionalState
import com.example.model.SecurityStatus
import com.example.ui.theme.BentoAmber
import com.example.ui.theme.BentoBackground
import com.example.ui.theme.BentoBlueContainer
import com.example.ui.theme.BentoBlueLight
import com.example.ui.theme.BentoBluePrimary
import com.example.ui.theme.BentoBorder
import com.example.ui.theme.BentoDarkBadge
import com.example.ui.theme.BentoDarkCard
import com.example.ui.theme.BentoDarkTextMuted
import com.example.ui.theme.BentoGreen
import com.example.ui.theme.BentoGreenBg
import com.example.ui.theme.BentoGreenBorder
import com.example.ui.theme.BentoGreenText
import com.example.ui.theme.BentoOnBlueContainer
import com.example.ui.theme.BentoOnRose
import com.example.ui.theme.BentoRoseAccent
import com.example.ui.theme.BentoRoseCard
import com.example.ui.theme.BentoRoseMuted
import com.example.ui.theme.BentoSurface
import com.example.ui.theme.BentoTextPrimary
import com.example.ui.theme.BentoTextSecondary
import com.example.ui.theme.BentoTextTertiary
import kotlinx.coroutines.delay

@Composable
fun BentoHeader(
  securityStatus: SecurityStatus,
  onProfileClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  Row(
    modifier = modifier
      .fillMaxWidth()
      .background(BentoSurface.copy(alpha = 0.92f))
      .border(1.dp, BentoBorder, RoundedCornerShape(bottomStart = 0.dp, bottomEnd = 0.dp))
      .statusBarsPadding()
      .padding(horizontal = 20.dp, vertical = 14.dp),
    horizontalArrangement = Arrangement.SpaceBetween,
    verticalAlignment = Alignment.CenterVertically
  ) {
    // App Brand and Tagline
    Row(
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
      Box(
        modifier = Modifier
          .size(42.dp)
          .clip(RoundedCornerShape(14.dp))
          .background(
            Brush.linearGradient(
              colors = listOf(BentoBluePrimary, BentoBlueLight)
            )
          )
          .shadow(6.dp, RoundedCornerShape(14.dp), spotColor = BentoBlueLight)
          .testTag("app_logo_icon"),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = Icons.Default.Shield,
          contentDescription = "Aura Journal Logo",
          tint = Color.White,
          modifier = Modifier.size(22.dp)
        )
      }

      Column {
        Text(
          text = "Aura Journal",
          fontSize = 18.sp,
          fontWeight = FontWeight.Bold,
          color = BentoTextPrimary,
          letterSpacing = (-0.5).sp
        )
        Text(
          text = "ENCRYPTED CLOUD NODE",
          fontSize = 10.sp,
          fontWeight = FontWeight.Bold,
          color = BentoTextSecondary,
          letterSpacing = 1.sp
        )
      }
    }

    // User Identity Verification Pill & Avatar
    Row(
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.spacedBy(10.dp),
      modifier = Modifier
        .clip(RoundedCornerShape(24.dp))
        .clickable { onProfileClick() }
        .padding(4.dp)
        .testTag("profile_badge_button")
    ) {
      Column(horizontalAlignment = Alignment.End) {
        Text(
          text = securityStatus.currentUserName,
          fontSize = 12.sp,
          fontWeight = FontWeight.SemiBold,
          color = BentoTextPrimary
        )
        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .background(if (securityStatus.currentUserId.contains("guest")) Color(0xFFFFEBEE) else BentoGreenBg)
            .border(
              1.dp,
              if (securityStatus.currentUserId.contains("guest")) Color(0xFFFFCDD2) else BentoGreenBorder,
              RoundedCornerShape(10.dp)
            )
            .padding(horizontal = 7.dp, vertical = 2.dp)
        ) {
          Text(
            text = if (securityStatus.currentUserId.contains("guest")) "Untrusted Actor" else "Verified Identity",
            fontSize = 9.sp,
            fontWeight = FontWeight.Bold,
            color = if (securityStatus.currentUserId.contains("guest")) Color(0xFFB71C1C) else BentoGreenText
          )
        }
      }

      // Avatar Circle
      Box(
        modifier = Modifier
          .size(38.dp)
          .clip(CircleShape)
          .background(BentoBlueContainer)
          .border(2.dp, Color.White, CircleShape)
          .shadow(2.dp, CircleShape),
        contentAlignment = Alignment.Center
      ) {
        Text(
          text = if (securityStatus.currentUserId.contains("guest")) "??" else "AC",
          fontSize = 13.sp,
          fontWeight = FontWeight.Bold,
          color = BentoOnBlueContainer
        )
      }
    }
  }
}

@Composable
fun BentoEmotionalPulseSelector(
  emotionalStates: List<EmotionalState>,
  selectedEmotion: EmotionalState,
  onSelectEmotion: (EmotionalState) -> Unit,
  modifier: Modifier = Modifier
) {
  Column(
    modifier = modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(24.dp))
      .background(BentoSurface)
      .border(1.dp, BentoBorder, RoundedCornerShape(24.dp))
      .padding(16.dp)
      .testTag("emotional_pulse_selector")
  ) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
      ) {
        Icon(
          imageVector = Icons.Default.Spa,
          contentDescription = null,
          tint = BentoRoseAccent,
          modifier = Modifier.size(16.dp)
        )
        Text(
          text = "HOW IS YOUR ENERGY RIGHT NOW?",
          fontSize = 11.sp,
          fontWeight = FontWeight.Bold,
          color = BentoTextSecondary,
          letterSpacing = 0.8.sp
        )
      }

      Text(
        text = "Tap to Ground",
        fontSize = 10.sp,
        fontWeight = FontWeight.SemiBold,
        color = BentoBluePrimary
      )
    }

    Spacer(modifier = Modifier.height(10.dp))

    LazyRow(
      horizontalArrangement = Arrangement.spacedBy(8.dp),
      modifier = Modifier.fillMaxWidth()
    ) {
      items(emotionalStates) { state ->
        val isSelected = state.label == selectedEmotion.label
        val bgAnim by animateColorAsState(
          targetValue = if (isSelected) BentoBlueContainer else Color(0xFFF4F5FB),
          label = "mood_bg"
        )
        val borderAnim by animateColorAsState(
          targetValue = if (isSelected) BentoBluePrimary else BentoBorder,
          label = "mood_border"
        )

        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(6.dp),
          modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(bgAnim)
            .border(1.dp, borderAnim, RoundedCornerShape(16.dp))
            .clickable { onSelectEmotion(state) }
            .padding(horizontal = 12.dp, vertical = 8.dp)
            .testTag("emotion_chip_${state.label.lowercase()}")
        ) {
          Text(text = state.emoji, fontSize = 15.sp)
          Text(
            text = state.label,
            fontSize = 12.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            color = if (isSelected) BentoOnBlueContainer else BentoTextPrimary
          )
        }
      }
    }

    Spacer(modifier = Modifier.height(10.dp))

    // Empathetic validation note
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(12.dp))
        .background(Color(0xFFF7F8FE))
        .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
      Text(
        text = when (selectedEmotion.label) {
          "Stressed" -> "High pressure often invites catastrophic thinking. Take 3 deep breaths before writing."
          "Overwhelmed" -> "You don't have to solve everything today. Just name the single next piece."
          "Foggy" -> "Mental fog is your brain asking for lower stimulation. Express your thoughts gently."
          "Balanced" -> "A wonderful window of clarity. Capture the strategic decisions you want to anchor."
          else -> "Inspiration is fleeting—channel this momentum into clear reflections."
        },
        fontSize = 11.sp,
        color = BentoTextSecondary,
        lineHeight = 15.sp
      )
    }
  }
}

@Composable
fun BentoGroundingExerciseCard(
  isBreathingActive: Boolean,
  completedCycles: Int,
  onToggleBreathing: () -> Unit,
  modifier: Modifier = Modifier
) {
  var breathPhase by remember { mutableStateOf("Inhale") }
  var countdown by remember { mutableIntStateOf(4) }

  LaunchedEffect(isBreathingActive) {
    if (isBreathingActive) {
      val phases = listOf("Inhale", "Hold", "Exhale", "Rest")
      var phaseIndex = 0
      while (isBreathingActive) {
        breathPhase = phases[phaseIndex % phases.size]
        for (i in 4 downTo 1) {
          countdown = i
          delay(1000)
        }
        phaseIndex++
      }
    } else {
      breathPhase = "Paused"
      countdown = 4
    }
  }

  val infiniteTransition = rememberInfiniteTransition(label = "pulse")
  val pulseScale by infiniteTransition.animateFloat(
    initialValue = 0.88f,
    targetValue = 1.12f,
    animationSpec = infiniteRepeatable(
      animation = tween(4000, easing = FastOutSlowInEasing),
      repeatMode = RepeatMode.Reverse
    ),
    label = "breath_scale"
  )

  Surface(
    onClick = onToggleBreathing,
    shape = RoundedCornerShape(26.dp),
    color = BentoSurface,
    border = androidx.compose.foundation.BorderStroke(1.dp, BentoBorder),
    modifier = modifier
      .fillMaxWidth()
      .testTag("bento_grounding_card")
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(18.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      // Interactive Animated Breathing Circle
      Box(
        modifier = Modifier
          .size(64.dp)
          .scale(if (isBreathingActive) pulseScale else 1f)
          .clip(CircleShape)
          .background(
            if (isBreathingActive) {
              Brush.radialGradient(listOf(BentoBlueLight, BentoBluePrimary))
            } else {
              Brush.radialGradient(listOf(Color(0xFFE8EAF6), Color(0xFFC5CAE9)))
            }
          ),
        contentAlignment = Alignment.Center
      ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
          Icon(
            imageVector = if (isBreathingActive) Icons.Default.Pause else Icons.Default.PlayArrow,
            contentDescription = null,
            tint = if (isBreathingActive) Color.White else BentoBluePrimary,
            modifier = Modifier.size(20.dp)
          )
          if (isBreathingActive) {
            Text(
              text = "${countdown}s",
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold,
              color = Color.White
            )
          }
        }
      }

      Spacer(modifier = Modifier.width(14.dp))

      Column(modifier = Modifier.weight(1f)) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          Text(
            text = "MINDFUL GROUNDING PULSE",
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            color = BentoBluePrimary,
            letterSpacing = 0.8.sp
          )
          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(6.dp))
              .background(BentoGreenBg)
              .padding(horizontal = 6.dp, vertical = 2.dp)
          ) {
            Text(
              text = "$completedCycles Done",
              fontSize = 9.sp,
              fontWeight = FontWeight.Bold,
              color = BentoGreenText
            )
          }
        }

        Spacer(modifier = Modifier.height(2.dp))

        Text(
          text = if (isBreathingActive) "$breathPhase deeply ($countdown)" else "4-4-4 Socratic Calming Breath",
          fontSize = 15.sp,
          fontWeight = FontWeight.Bold,
          color = BentoTextPrimary
        )

        Spacer(modifier = Modifier.height(2.dp))

        Text(
          text = if (isBreathingActive) "Syncing nervous system for cognitive clarity..." else "Tap to start a 16-second grounding loop before journaling",
          fontSize = 11.sp,
          color = BentoTextSecondary,
          lineHeight = 15.sp
        )
      }
    }
  }
}

@Composable
fun BentoHeroCard(
  promptTitle: String,
  onContinueClick: () -> Unit,
  onNewSessionClick: () -> Unit,
  onCyclePrompt: (() -> Unit)? = null,
  modifier: Modifier = Modifier
) {
  Box(
    modifier = modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(28.dp))
      .background(BentoSurface)
      .border(1.dp, BentoBorder, RoundedCornerShape(28.dp))
      .padding(22.dp)
      .testTag("hero_bento_card")
  ) {
    Column(
      verticalArrangement = Arrangement.SpaceBetween
    ) {
      // Top status pill
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          Box(
            modifier = Modifier
              .size(8.dp)
              .clip(CircleShape)
              .background(BentoBluePrimary)
          )
          Text(
            text = "DAILY SOCRATIC PROMPT",
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = BentoTextSecondary,
            letterSpacing = 0.8.sp
          )
        }

        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          if (onCyclePrompt != null) {
            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .background(BentoBlueContainer)
                .clickable { onCyclePrompt() }
                .padding(horizontal = 8.dp, vertical = 4.dp)
                .testTag("cycle_prompt_button")
            ) {
              Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
              ) {
                Icon(
                  imageVector = Icons.Default.Refresh,
                  contentDescription = "Next Prompt",
                  tint = BentoOnBlueContainer,
                  modifier = Modifier.size(13.dp)
                )
                Text(
                  text = "Shuffle",
                  fontSize = 10.sp,
                  fontWeight = FontWeight.Bold,
                  color = BentoOnBlueContainer
                )
              }
            }
          }

          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(12.dp))
              .background(Color(0xFFEFEFF4))
              .padding(horizontal = 8.dp, vertical = 4.dp)
          ) {
            Text(
              text = "Gemini 3.5 Flash Active",
              fontSize = 10.sp,
              fontWeight = FontWeight.Medium,
              color = BentoTextSecondary
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      // Prompt Content
      Text(
        text = "Reflecting on your day...",
        fontSize = 13.sp,
        fontWeight = FontWeight.Medium,
        color = BentoTextSecondary
      )
      Spacer(modifier = Modifier.height(4.dp))
      Text(
        text = "\"$promptTitle\"",
        fontSize = 19.sp,
        fontWeight = FontWeight.Bold,
        color = BentoTextPrimary,
        lineHeight = 26.sp
      )

      Spacer(modifier = Modifier.height(18.dp))

      // Action Buttons
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        // Continue Reflection Main Button
        Surface(
          onClick = onContinueClick,
          shape = RoundedCornerShape(18.dp),
          color = BentoBlueContainer,
          modifier = Modifier
            .weight(1f)
            .height(52.dp)
            .testTag("continue_reflection_button")
        ) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(
              imageVector = Icons.Default.ChatBubble,
              contentDescription = null,
              tint = BentoOnBlueContainer,
              modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = "Open Reflection",
              fontSize = 14.sp,
              fontWeight = FontWeight.Bold,
              color = BentoOnBlueContainer
            )
          }
        }

        // Quick New Prompt Button
        Surface(
          onClick = onNewSessionClick,
          shape = RoundedCornerShape(18.dp),
          color = BentoDarkCard,
          modifier = Modifier
            .size(52.dp)
            .testTag("new_reflection_quick_button")
        ) {
          Box(
            modifier = Modifier.size(52.dp),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.Add,
              contentDescription = "New Reflection",
              tint = Color.White,
              modifier = Modifier.size(24.dp)
            )
          }
        }
      }
    }
  }
}

@Composable
fun BentoSecurityCard(
  onClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  Surface(
    onClick = onClick,
    shape = RoundedCornerShape(26.dp),
    color = BentoDarkCard,
    modifier = modifier
      .height(150.dp)
      .testTag("bento_security_card")
  ) {
    Column(
      modifier = Modifier
        .padding(18.dp),
      verticalArrangement = Arrangement.SpaceBetween
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Box(
          modifier = Modifier
            .size(34.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(Color(0x22FFFFFF)),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = Icons.Default.Shield,
            contentDescription = null,
            tint = BentoDarkBadge,
            modifier = Modifier.size(18.dp)
          )
        }

        Text(
          text = "SECURE",
          fontSize = 10.sp,
          fontWeight = FontWeight.Bold,
          color = BentoDarkBadge,
          letterSpacing = 1.2.sp
        )
      }

      Column {
        Text(
          text = "Firestore Isolation",
          fontSize = 11.sp,
          fontWeight = FontWeight.Normal,
          color = BentoDarkTextMuted
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
          text = "Tier-1 Data Shield Active",
          fontSize = 15.sp,
          fontWeight = FontWeight.SemiBold,
          color = Color.White,
          lineHeight = 19.sp
        )
      }
    }
  }
}

@Composable
fun BentoMoodCard(
  onClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  Surface(
    onClick = onClick,
    shape = RoundedCornerShape(26.dp),
    color = BentoRoseCard,
    modifier = modifier
      .height(150.dp)
      .testTag("bento_mood_card")
  ) {
    Column(
      modifier = Modifier
        .padding(18.dp),
      verticalArrangement = Arrangement.SpaceBetween
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Box(
          modifier = Modifier
            .size(34.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(BentoRoseAccent.copy(alpha = 0.15f)),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = Icons.AutoMirrored.Filled.TrendingUp,
            contentDescription = null,
            tint = BentoRoseAccent,
            modifier = Modifier.size(18.dp)
          )
        }

        Text(
          text = "ANALYSIS",
          fontSize = 10.sp,
          fontWeight = FontWeight.Bold,
          color = BentoOnRose.copy(alpha = 0.75f),
          letterSpacing = 1.2.sp
        )
      }

      Column {
        Text(
          text = "Cognitive Pattern",
          fontSize = 11.sp,
          fontWeight = FontWeight.Normal,
          color = BentoRoseMuted
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
          text = "High Focus Momentum",
          fontSize = 15.sp,
          fontWeight = FontWeight.SemiBold,
          color = BentoOnRose,
          lineHeight = 19.sp
        )
      }
    }
  }
}

@Composable
fun BentoActionPlanCard(
  actionItems: List<CognitiveActionItem>,
  onToggleItem: (String) -> Unit,
  onAddNewItem: (() -> Unit)? = null,
  modifier: Modifier = Modifier
) {
  val completedCount = actionItems.count { it.isCompleted }
  val totalCount = actionItems.size
  val progress = if (totalCount > 0) completedCount.toFloat() / totalCount else 0f

  Box(
    modifier = modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(28.dp))
      .background(BentoSurface)
      .border(1.dp, BentoBorder, RoundedCornerShape(28.dp))
      .padding(20.dp)
      .testTag("bento_action_plan_card")
  ) {
    Column {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Text(
            text = "COGNITIVE ACTION PLAN",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = BentoTextSecondary,
            letterSpacing = 0.8.sp
          )
          Spacer(modifier = Modifier.height(2.dp))
          Text(
            text = "$completedCount of $totalCount micro-actions completed",
            fontSize = 11.sp,
            color = BentoTextSecondary
          )
        }

        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          if (onAddNewItem != null) {
            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(BentoBlueContainer)
                .clickable { onAddNewItem() }
                .padding(horizontal = 8.dp, vertical = 4.dp)
                .testTag("add_action_plan_button")
            ) {
              Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
              ) {
                Icon(
                  imageVector = Icons.Default.Add,
                  contentDescription = "Add Step",
                  tint = BentoOnBlueContainer,
                  modifier = Modifier.size(13.dp)
                )
                Text(
                  text = "Add Step",
                  fontSize = 10.sp,
                  fontWeight = FontWeight.Bold,
                  color = BentoOnBlueContainer
                )
              }
            }
          }

          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(8.dp))
              .background(Color(0xFFEBF3FE))
              .padding(horizontal = 8.dp, vertical = 4.dp)
          ) {
            Text(
              text = "Cloud Syncing...",
              fontSize = 10.sp,
              fontWeight = FontWeight.SemiBold,
              color = BentoBluePrimary
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      // Progress bar
      LinearProgressIndicator(
        progress = { progress },
        modifier = Modifier
          .fillMaxWidth()
          .height(4.dp)
          .clip(RoundedCornerShape(2.dp)),
        color = BentoGreen,
        trackColor = BentoBorder
      )

      Spacer(modifier = Modifier.height(14.dp))

      if (actionItems.isEmpty()) {
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
          contentAlignment = Alignment.Center
        ) {
          Text(
            text = "No micro-actions yet. Reflect on a thought to generate one!",
            fontSize = 12.sp,
            color = BentoTextTertiary
          )
        }
      } else {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
          actionItems.forEach { item ->
            ActionItemRow(
              item = item,
              onToggle = { onToggleItem(item.id) }
            )
          }
        }
      }
    }
  }
}

@Composable
fun ActionItemRow(
  item: CognitiveActionItem,
  onToggle: () -> Unit
) {
  val indicatorColor = when (item.colorType) {
    "amber" -> BentoAmber
    "green" -> BentoGreen
    else -> BentoBluePrimary
  }

  Row(
    modifier = Modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(14.dp))
      .clickable { onToggle() }
      .padding(vertical = 4.dp, horizontal = 2.dp)
      .testTag("action_item_${item.id}"),
    verticalAlignment = Alignment.CenterVertically
  ) {
    // Colored Vertical Pillar
    Box(
      modifier = Modifier
        .width(4.dp)
        .height(34.dp)
        .clip(RoundedCornerShape(2.dp))
        .background(indicatorColor)
    )

    Spacer(modifier = Modifier.width(12.dp))

    Column(modifier = Modifier.weight(1f)) {
      Text(
        text = item.title,
        fontSize = 13.sp,
        fontWeight = FontWeight.SemiBold,
        color = if (item.isCompleted) BentoTextTertiary else BentoTextPrimary
      )
      Text(
        text = item.subtitle,
        fontSize = 11.sp,
        fontWeight = FontWeight.Normal,
        color = BentoTextSecondary,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
      )
    }

    Box(
      modifier = Modifier
        .size(24.dp)
        .clip(CircleShape)
        .background(if (item.isCompleted) indicatorColor else Color.Transparent)
        .border(1.5.dp, if (item.isCompleted) indicatorColor else BentoBorder, CircleShape),
      contentAlignment = Alignment.Center
    ) {
      if (item.isCompleted) {
        Icon(
          imageVector = Icons.Default.Check,
          contentDescription = "Completed",
          tint = Color.White,
          modifier = Modifier.size(14.dp)
        )
      }
    }
  }
}

@Composable
fun BentoBottomNavigation(
  currentTab: String,
  onTabSelect: (String) -> Unit,
  modifier: Modifier = Modifier
) {
  Surface(
    modifier = modifier
      .fillMaxWidth()
      .border(1.dp, BentoBorder, RoundedCornerShape(0.dp)),
    color = BentoSurface,
    shadowElevation = 8.dp
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .navigationBarsPadding()
        .height(68.dp)
        .padding(horizontal = 16.dp),
      horizontalArrangement = Arrangement.SpaceAround,
      verticalAlignment = Alignment.CenterVertically
    ) {
      val tabs = listOf(
        Triple("home", "Home", Icons.Default.Home),
        Triple("journals", "Journals", Icons.AutoMirrored.Filled.MenuBook),
        Triple("insights", "Insights", Icons.Default.Insights),
        Triple("security", "Security", Icons.Default.Security)
      )

      tabs.forEach { (id, label, icon) ->
        val isActive = currentTab == id
        Column(
          modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .clickable { onTabSelect(id) }
            .padding(horizontal = 12.dp, vertical = 6.dp)
            .testTag("nav_tab_$id"),
          horizontalAlignment = Alignment.CenterHorizontally,
          verticalArrangement = Arrangement.Center
        ) {
          if (isActive) {
            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(14.dp))
                .background(BentoBlueContainer)
                .padding(horizontal = 18.dp, vertical = 4.dp)
            ) {
              Icon(
                imageVector = icon,
                contentDescription = label,
                tint = BentoOnBlueContainer,
                modifier = Modifier.size(20.dp)
              )
            }
          } else {
            Icon(
              imageVector = icon,
              contentDescription = label,
              tint = BentoTextSecondary,
              modifier = Modifier.size(20.dp)
            )
          }

          Spacer(modifier = Modifier.height(2.dp))
          Text(
            text = label,
            fontSize = 10.sp,
            fontWeight = if (isActive) FontWeight.Bold else FontWeight.Medium,
            color = if (isActive) BentoOnBlueContainer else BentoTextSecondary
          )
        }
      }
    }
  }
}
