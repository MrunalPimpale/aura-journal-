package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.ChatMessage
import com.example.model.ReflectiveSession
import com.example.ui.theme.BentoBackground
import com.example.ui.theme.BentoBlueContainer
import com.example.ui.theme.BentoBluePrimary
import com.example.ui.theme.BentoBorder
import com.example.ui.theme.BentoDarkCard
import com.example.ui.theme.BentoGreen
import com.example.ui.theme.BentoGreenBg
import com.example.ui.theme.BentoGreenText
import com.example.ui.theme.BentoOnBlueContainer
import com.example.ui.theme.BentoRoseAccent
import com.example.ui.theme.BentoRoseCard
import com.example.ui.theme.BentoSurface
import com.example.ui.theme.BentoTextPrimary
import com.example.ui.theme.BentoTextSecondary
import com.example.ui.theme.BentoTextTertiary

@Composable
fun JournalsScreen(
  sessions: List<ReflectiveSession>,
  activeSessionId: String?,
  isAwaitingAi: Boolean,
  onSelectSession: (String) -> Unit,
  onNewSession: () -> Unit,
  onSendMessage: (String, String) -> Unit,
  onAddSuggestedAction: ((String) -> Unit)? = null,
  modifier: Modifier = Modifier
) {
  val activeSession = sessions.find { it.id == activeSessionId } ?: sessions.firstOrNull()
  var inputQuery by remember { mutableStateOf("") }
  var selectedDistortionInfo by remember { mutableStateOf<String?>(null) }
  val listState = rememberLazyListState()

  val thoughtStarters = listOf(
    "Worried this architecture won't hold up under load",
    "Feeling overwhelmed by the ideathon submission timeline",
    "What if the judges don't notice our zero-trust isolation?",
    "I'm judging myself too harshly for today's roadblocks"
  )

  LaunchedEffect(activeSession?.messages?.size, isAwaitingAi) {
    if ((activeSession?.messages?.size ?: 0) > 0) {
      listState.animateScrollToItem(activeSession!!.messages.size - 1)
    }
  }

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(BentoBackground)
  ) {
    // Session Tabs Header
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .background(BentoSurface)
        .border(1.dp, BentoBorder)
        .padding(horizontal = 16.dp, vertical = 10.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Row(
        modifier = Modifier.weight(1f),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        sessions.forEach { sess ->
          val isSelected = sess.id == activeSession?.id
          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(12.dp))
              .background(if (isSelected) BentoBlueContainer else Color.Transparent)
              .border(1.dp, if (isSelected) BentoBluePrimary.copy(alpha = 0.4f) else BentoBorder, RoundedCornerShape(12.dp))
              .clickable { onSelectSession(sess.id) }
              .padding(horizontal = 12.dp, vertical = 6.dp)
              .testTag("session_tab_${sess.id}")
          ) {
            Text(
              text = sess.title.take(18) + if (sess.title.length > 18) "..." else "",
              fontSize = 12.sp,
              fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
              color = if (isSelected) BentoOnBlueContainer else BentoTextSecondary
            )
          }
        }
      }

      IconButton(
        onClick = onNewSession,
        modifier = Modifier
          .size(36.dp)
          .clip(CircleShape)
          .background(BentoDarkCard)
          .testTag("add_new_session_button")
      ) {
        Icon(
          imageVector = Icons.Default.Add,
          contentDescription = "New Session",
          tint = Color.White,
          modifier = Modifier.size(18.dp)
        )
      }
    }

    // Active Session Prompt Banner
    activeSession?.let { sess ->
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .background(Color(0xFFF1F3FA))
          .padding(horizontal = 16.dp, vertical = 10.dp)
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          Icon(
            imageVector = Icons.Default.Lock,
            contentDescription = null,
            tint = BentoBluePrimary,
            modifier = Modifier.size(14.dp)
          )
          Text(
            text = "Zero-Trust Encryption: users/usr_alex_chen_9921/sessions/${sess.id}",
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            color = BentoTextSecondary
          )
        }
      }
    }

    // Message Dialogue History
    if (activeSession == null || activeSession.messages.isEmpty()) {
      Box(
        modifier = Modifier
          .weight(1f)
          .fillMaxWidth()
          .padding(24.dp),
        contentAlignment = Alignment.Center
      ) {
        Column(
          horizontalAlignment = Alignment.CenterHorizontally,
          verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
          Box(
            modifier = Modifier
              .size(56.dp)
              .clip(CircleShape)
              .background(BentoBlueContainer),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.Psychology,
              contentDescription = null,
              tint = BentoOnBlueContainer,
              modifier = Modifier.size(30.dp)
            )
          }
          Text(
            text = "Empathetic Socratic Sanctuary",
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold,
            color = BentoTextPrimary
          )
          Text(
            text = "Unburden whatever is clouding your thoughts. Gemini will listen warmly and gently examine any cognitive distortions with zero data leakage.",
            fontSize = 12.sp,
            color = BentoTextSecondary,
            lineHeight = 18.sp,
            modifier = Modifier.padding(horizontal = 20.dp)
          )

          Spacer(modifier = Modifier.height(6.dp))

          Text(
            text = "OR TRY A QUICK THOUGHT STARTER:",
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            color = BentoTextSecondary,
            letterSpacing = 0.8.sp
          )

          Column(
            verticalArrangement = Arrangement.spacedBy(6.dp),
            modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp)
          ) {
            thoughtStarters.forEach { starter ->
              Surface(
                onClick = { inputQuery = starter },
                shape = RoundedCornerShape(12.dp),
                color = BentoSurface,
                border = androidx.compose.foundation.BorderStroke(1.dp, BentoBorder),
                modifier = Modifier.fillMaxWidth()
              ) {
                Row(
                  modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                  verticalAlignment = Alignment.CenterVertically,
                  horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                  Icon(
                    imageVector = Icons.Default.Lightbulb,
                    contentDescription = null,
                    tint = BentoBluePrimary,
                    modifier = Modifier.size(14.dp)
                  )
                  Text(
                    text = starter,
                    fontSize = 11.sp,
                    color = BentoTextPrimary,
                    fontWeight = FontWeight.Medium
                  )
                }
              }
            }
          }
        }
      }
    } else {
      LazyColumn(
        state = listState,
        modifier = Modifier
          .weight(1f)
          .fillMaxWidth()
          .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
      ) {
        items(activeSession.messages) { msg ->
          ChatMessageBubble(
            message = msg,
            onDistortionClick = { distortion ->
              selectedDistortionInfo = distortion
            },
            onAddSuggestedAction = onAddSuggestedAction
          )
        }

        if (isAwaitingAi) {
          item {
            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(18.dp))
                .background(BentoSurface)
                .border(1.dp, BentoBorder, RoundedCornerShape(18.dp))
                .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
              Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
              ) {
                CircularProgressIndicator(
                  modifier = Modifier.size(16.dp),
                  strokeWidth = 2.dp,
                  color = BentoBluePrimary
                )
                Text(
                  text = "Gemini is warmly reframing with Socratic empathy...",
                  fontSize = 12.sp,
                  color = BentoTextSecondary
                )
              }
            }
          }
        }
      }
    }

    // Quick thought chips right above input if in dialogue
    if (activeSession != null && activeSession.messages.isNotEmpty()) {
      LazyRow(
        modifier = Modifier
          .fillMaxWidth()
          .background(BentoBackground)
          .padding(horizontal = 16.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp)
      ) {
        items(thoughtStarters) { starter ->
          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(12.dp))
              .background(BentoSurface)
              .border(1.dp, BentoBorder, RoundedCornerShape(12.dp))
              .clickable { inputQuery = starter }
              .padding(horizontal = 10.dp, vertical = 5.dp)
          ) {
            Text(
              text = starter.take(30) + "...",
              fontSize = 11.sp,
              color = BentoTextSecondary
            )
          }
        }
      }
    }

    // Input Bar
    Surface(
      modifier = Modifier.fillMaxWidth(),
      color = BentoSurface,
      border = androidx.compose.foundation.BorderStroke(1.dp, BentoBorder)
    ) {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        OutlinedTextField(
          value = inputQuery,
          onValueChange = { inputQuery = it },
          placeholder = {
            Text(
              "Express what feels heavy or uncertain...",
              fontSize = 13.sp,
              color = BentoTextTertiary
            )
          },
          shape = RoundedCornerShape(20.dp),
          colors = OutlinedTextFieldDefaults.colors(
            unfocusedContainerColor = BentoBackground,
            focusedContainerColor = BentoSurface,
            unfocusedBorderColor = BentoBorder,
            focusedBorderColor = BentoBluePrimary
          ),
          modifier = Modifier
            .weight(1f)
            .testTag("journal_input_field"),
          maxLines = 3
        )

        IconButton(
          onClick = {
            if (inputQuery.isNotBlank() && activeSession != null && !isAwaitingAi) {
              val text = inputQuery.trim()
              inputQuery = ""
              onSendMessage(activeSession.id, text)
            }
          },
          enabled = inputQuery.isNotBlank() && !isAwaitingAi,
          modifier = Modifier
            .size(46.dp)
            .clip(CircleShape)
            .background(if (inputQuery.isNotBlank() && !isAwaitingAi) BentoBluePrimary else BentoBorder)
            .testTag("journal_send_button")
        ) {
          Icon(
            imageVector = Icons.AutoMirrored.Filled.Send,
            contentDescription = "Send",
            tint = Color.White,
            modifier = Modifier.size(20.dp)
          )
        }
      }
    }
  }

  // Interactive Cognitive Distortion Compass Dialog
  if (selectedDistortionInfo != null) {
    val distortion = selectedDistortionInfo!!
    AlertDialog(
      onDismissRequest = { selectedDistortionInfo = null },
      containerColor = BentoSurface,
      shape = RoundedCornerShape(24.dp),
      title = {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          Icon(
            imageVector = Icons.Default.Info,
            contentDescription = null,
            tint = BentoRoseAccent,
            modifier = Modifier.size(22.dp)
          )
          Text(
            text = "Compass: $distortion",
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold,
            color = BentoTextPrimary
          )
        }
      },
      text = {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
          Text(
            text = when (distortion) {
              "Catastrophizing" -> "Assuming the absolute worst-case scenario will occur, while underestimating your capacity to respond."
              "All-or-Nothing Thinking" -> "Viewing outcomes in binary terms (total success vs complete disaster) with no space for incremental progress."
              "Emotional Reasoning" -> "Treating current feelings of dread or inadequacy as factual proof of objective truth."
              "Mind Reading" -> "Assuming you know what the judges or teammates negatively think without verified feedback."
              else -> "A cognitive shortcut where high stress causes the brain to filter out positive evidence."
            },
            fontSize = 13.sp,
            color = BentoTextPrimary,
            lineHeight = 18.sp
          )

          Box(
            modifier = Modifier
              .fillMaxWidth()
              .clip(RoundedCornerShape(12.dp))
              .background(BentoBlueContainer)
              .padding(12.dp)
          ) {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
              Text(
                text = "3-STEP SOCRATIC REALITY CHECK:",
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = BentoOnBlueContainer,
                letterSpacing = 0.5.sp
              )
              Text(
                text = "1. What is the evidence this outcome is guaranteed?\n2. What is a realistic middle-ground outcome?\n3. What is one small step within your direct control right now?",
                fontSize = 11.sp,
                color = BentoOnBlueContainer,
                lineHeight = 16.sp
              )
            }
          }
        }
      },
      confirmButton = {
        Button(
          onClick = { selectedDistortionInfo = null },
          colors = ButtonDefaults.buttonColors(containerColor = BentoBluePrimary),
          shape = RoundedCornerShape(12.dp)
        ) {
          Text("Got It", fontWeight = FontWeight.Bold)
        }
      }
    )
  }
}

@Composable
fun ChatMessageBubble(
  message: ChatMessage,
  onDistortionClick: ((String) -> Unit)? = null,
  onAddSuggestedAction: ((String) -> Unit)? = null
) {
  val isUser = message.sender == "user"
  var isActionAdded by remember { mutableStateOf(false) }

  Row(
    modifier = Modifier.fillMaxWidth(),
    horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start
  ) {
    Box(
      modifier = Modifier
        .fillMaxWidth(0.88f)
        .clip(
          RoundedCornerShape(
            topStart = 20.dp,
            topEnd = 20.dp,
            bottomStart = if (isUser) 20.dp else 4.dp,
            bottomEnd = if (isUser) 4.dp else 20.dp
          )
        )
        .background(if (isUser) BentoBluePrimary else BentoSurface)
        .border(1.dp, if (isUser) BentoBluePrimary else BentoBorder, RoundedCornerShape(20.dp))
        .padding(16.dp)
        .testTag("chat_bubble_${message.id}")
    ) {
      Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        if (!isUser) {
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
                imageVector = Icons.Default.AutoAwesome,
                contentDescription = null,
                tint = BentoBluePrimary,
                modifier = Modifier.size(14.dp)
              )
              Text(
                text = "AURA SOCRATIC REFRAMER",
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = BentoBluePrimary,
                letterSpacing = 0.5.sp
              )
            }

            Text(
              text = "Gemini 3.5 Flash",
              fontSize = 9.sp,
              color = BentoTextTertiary
            )
          }
        }

        Text(
          text = message.content,
          fontSize = 14.sp,
          color = if (isUser) Color.White else BentoTextPrimary,
          lineHeight = 20.sp
        )

        // Cognitive Distortion Badge (Interactive)
        if (!isUser && message.detectedDistortion != null && message.detectedDistortion != "None") {
          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(8.dp))
              .background(BentoRoseCard)
              .clickable { onDistortionClick?.invoke(message.detectedDistortion) }
              .padding(horizontal = 8.dp, vertical = 4.dp)
              .testTag("distortion_badge_${message.id}")
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
              Text(
                text = "DISTORTION: ${message.detectedDistortion.uppercase()}",
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold,
                color = BentoRoseAccent
              )
              Icon(
                imageVector = Icons.Default.Info,
                contentDescription = "Explain Distortion",
                tint = BentoRoseAccent,
                modifier = Modifier.size(11.dp)
              )
            }
          }
        }

        // Suggested Socratic Reflection
        if (!isUser && !message.suggestedReflection.isNullOrBlank()) {
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .clip(RoundedCornerShape(10.dp))
              .background(BentoBackground)
              .border(1.dp, BentoBorder, RoundedCornerShape(10.dp))
              .padding(10.dp)
          ) {
            Column {
              Text(
                text = "Socratic Prompt for You:",
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = BentoBluePrimary
              )
              Spacer(modifier = Modifier.height(2.dp))
              Text(
                text = message.suggestedReflection,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = BentoTextPrimary,
                lineHeight = 16.sp
              )
            }
          }
        }

        // Interactive Suggested Micro-Action Pill
        if (!isUser && !message.suggestedAction.isNullOrBlank()) {
          Surface(
            shape = RoundedCornerShape(10.dp),
            color = if (isActionAdded) BentoGreenBg else Color(0xFFF0F4FF),
            border = androidx.compose.foundation.BorderStroke(
              1.dp,
              if (isActionAdded) BentoGreen else BentoBluePrimary.copy(alpha = 0.3f)
            ),
            modifier = Modifier.fillMaxWidth()
          ) {
            Row(
              modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Column(modifier = Modifier.weight(1f)) {
                Text(
                  text = "SUGGESTED MICRO-ACTION",
                  fontSize = 9.sp,
                  fontWeight = FontWeight.Bold,
                  color = if (isActionAdded) BentoGreenText else BentoBluePrimary
                )
                Text(
                  text = message.suggestedAction,
                  fontSize = 11.sp,
                  fontWeight = FontWeight.SemiBold,
                  color = BentoTextPrimary
                )
              }

              Box(
                modifier = Modifier
                  .clip(RoundedCornerShape(8.dp))
                  .background(if (isActionAdded) BentoGreen else BentoBluePrimary)
                  .clickable(enabled = !isActionAdded) {
                    onAddSuggestedAction?.invoke(message.suggestedAction)
                    isActionAdded = true
                  }
                  .padding(horizontal = 8.dp, vertical = 4.dp)
                  .testTag("add_suggested_action_button")
              ) {
                Row(
                  verticalAlignment = Alignment.CenterVertically,
                  horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                  Icon(
                    imageVector = if (isActionAdded) Icons.Default.Check else Icons.Default.Add,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(12.dp)
                  )
                  Text(
                    text = if (isActionAdded) "Saved" else "Add Step",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                  )
                }
              }
            }
          }
        }
      }
    }
  }
}
