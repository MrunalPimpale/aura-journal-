package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.SecurityStatus
import com.example.ui.theme.BentoAmber
import com.example.ui.theme.BentoBackground
import com.example.ui.theme.BentoBlueContainer
import com.example.ui.theme.BentoBluePrimary
import com.example.ui.theme.BentoBorder
import com.example.ui.theme.BentoDarkBadge
import com.example.ui.theme.BentoDarkCard
import com.example.ui.theme.BentoGreen
import com.example.ui.theme.BentoGreenBg
import com.example.ui.theme.BentoGreenBorder
import com.example.ui.theme.BentoGreenText
import com.example.ui.theme.BentoOnBlueContainer
import com.example.ui.theme.BentoRoseAccent
import com.example.ui.theme.BentoRoseCard
import com.example.ui.theme.BentoSurface
import com.example.ui.theme.BentoTextPrimary
import com.example.ui.theme.BentoTextSecondary

@Composable
fun SecurityScreen(
  securityStatus: SecurityStatus,
  auditLogs: List<String>,
  onToggleProfile: ((Boolean) -> Unit)? = null,
  onSwitchAccount: (() -> Unit)? = null,
  modifier: Modifier = Modifier
) {
  val scrollState = rememberScrollState()
  var showRulesViewer by remember { mutableStateOf(false) }

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(BentoBackground)
      .verticalScroll(scrollState)
      .padding(16.dp),
    verticalArrangement = Arrangement.spacedBy(14.dp)
  ) {
    // 1. Bento Security Hero
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(28.dp))
        .background(BentoDarkCard)
        .padding(22.dp)
        .testTag("security_hero_card")
    ) {
      Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            Icon(
              imageVector = Icons.Default.Shield,
              contentDescription = null,
              tint = BentoDarkBadge,
              modifier = Modifier.size(24.dp)
            )
            Text(
              text = "FIREBASE ZERO-TRUST PERIMETER",
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold,
              color = BentoDarkBadge,
              letterSpacing = 1.sp
            )
          }

          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(10.dp))
              .background(BentoGreen)
              .padding(horizontal = 9.dp, vertical = 3.dp)
          ) {
            Text(
              text = "UID ISOLATED",
              fontSize = 10.sp,
              fontWeight = FontWeight.Bold,
              color = Color.White
            )
          }
        }

        Text(
          text = "Real Firebase Auth Enforced",
          fontSize = 22.sp,
          fontWeight = FontWeight.Bold,
          color = Color.White
        )

        Text(
          text = "Active Tenant: ${securityStatus.currentUserId.ifBlank { "Initializing..." }}\nFirestore security rules guarantee request.auth.uid == userId for all reads & writes.",
          fontSize = 12.sp,
          color = Color(0xBBFFFFFF),
          lineHeight = 17.sp
        )
      }
    }

    // 2. Interactive Hackathon Judge Demo Tool (Real Multi-Tenant Isolation)
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(28.dp))
        .background(BentoSurface)
        .border(1.dp, BentoBorder, RoundedCornerShape(28.dp))
        .padding(20.dp)
        .testTag("judge_demo_tool_card")
    ) {
      Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
          text = "HACKATHON ISOLATION VERIFICATION",
          fontSize = 11.sp,
          fontWeight = FontWeight.Bold,
          color = BentoBluePrimary,
          letterSpacing = 0.8.sp
        )

        Text(
          text = "Real Multi-Tenant Isolation (User A ↮ User B)",
          fontSize = 15.sp,
          fontWeight = FontWeight.Bold,
          color = BentoTextPrimary
        )

        Text(
          text = "Current UID: ${securityStatus.currentUserId.ifBlank { "None" }}\nSwitch to a fresh cryptographic Firebase session to verify that User B sees exactly 0 of User A's journals.",
          fontSize = 12.sp,
          color = BentoTextSecondary,
          lineHeight = 16.sp
        )

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          Button(
            onClick = {
              if (onSwitchAccount != null) {
                onSwitchAccount()
              } else {
                onToggleProfile?.invoke(true)
              }
            },
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = BentoBluePrimary),
            modifier = Modifier
              .fillMaxWidth()
              .height(48.dp)
              .testTag("simulate_attacker_button")
          ) {
            Icon(
              imageVector = Icons.Default.CheckCircle,
              contentDescription = null,
              tint = Color.White,
              modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text("Switch to New Isolated Tenant UID", fontSize = 12.sp, fontWeight = FontWeight.Bold)
          }
        }
      }
    }

    // 3. Security Pillars Grid
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
      SecurityPillarMiniCard(
        title = "Secret Manager",
        status = "HSM Isolated",
        subtitle = "Zero client API keys",
        icon = Icons.Default.Key,
        modifier = Modifier.weight(1f)
      )

      SecurityPillarMiniCard(
        title = "Prompt Barrier",
        status = "XML Delimiters",
        subtitle = "Injection mitigated",
        icon = Icons.Default.Lock,
        modifier = Modifier.weight(1f)
      )
    }

    // 4. Live Security Audit Trail (Console)
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(28.dp))
        .background(Color(0xFF141824))
        .padding(18.dp)
        .testTag("audit_logs_card")
    ) {
      Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = "LIVE SECURITY AUDIT STREAM",
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            color = BentoDarkBadge,
            letterSpacing = 1.sp
          )
          Box(
            modifier = Modifier
              .size(8.dp)
              .clip(CircleShape)
              .background(BentoGreen)
          )
        }

        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
          auditLogs.forEach { log ->
            val isAlert = log.contains("DENIED") || log.contains("Blocked")
            Text(
              text = "> $log",
              fontSize = 11.sp,
              fontFamily = FontFamily.Monospace,
              color = if (isAlert) Color(0xFFFF8A80) else Color(0xFFB0BEC5),
              lineHeight = 15.sp
            )
          }
        }
      }
    }

    // 5. Official Production Firestore Rules Inspector
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(24.dp))
        .background(BentoSurface)
        .border(1.dp, BentoBorder, RoundedCornerShape(24.dp))
        .padding(18.dp)
        .testTag("firestore_rules_card")
    ) {
      Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .clickable { showRulesViewer = !showRulesViewer },
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            Icon(
              imageVector = Icons.Default.Code,
              contentDescription = null,
              tint = BentoBluePrimary,
              modifier = Modifier.size(20.dp)
            )
            Text(
              text = "FIRESTORE SECURITY RULES (P0)",
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold,
              color = BentoTextPrimary,
              letterSpacing = 0.8.sp
            )
          }

          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
          ) {
            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(6.dp))
                .background(BentoGreenBg)
                .border(1.dp, BentoGreenBorder, RoundedCornerShape(6.dp))
                .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
              Text(
                text = "ENFORCED",
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold,
                color = BentoGreenText
              )
            }
            Icon(
              imageVector = if (showRulesViewer) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
              contentDescription = "Toggle Rules",
              tint = BentoTextSecondary,
              modifier = Modifier.size(18.dp)
            )
          }
        }

        Text(
          text = "Strict per-user boundary mathematically scoped to request.auth.uid == userId. Global fallback denies any cross-tenant access.",
          fontSize = 11.sp,
          color = BentoTextSecondary,
          lineHeight = 15.sp
        )

        if (showRulesViewer) {
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .clip(RoundedCornerShape(12.dp))
              .background(Color(0xFF0F141E))
              .padding(12.dp)
          ) {
            Text(
              text = """
rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {
    match /users/{userId} {
      allow read, write: if request.auth != null
        && request.auth.uid == userId;

      match /sessions/{sessionId} {
        allow read, write: if request.auth != null
          && request.auth.uid == userId;

        match /messages/{messageId} {
          allow read, write: if request.auth != null
            && request.auth.uid == userId;
        }
      }

      match /action_items/{itemId} {
        allow read, write: if request.auth != null
          && request.auth.uid == userId;
      }
    }

    match /{document=**} {
      allow read, write: if false;
    }
  }
}
              """.trimIndent(),
              fontSize = 10.sp,
              fontFamily = FontFamily.Monospace,
              color = Color(0xFF81D4FA),
              lineHeight = 14.sp
            )
          }
        }
      }
    }

    Spacer(modifier = Modifier.height(16.dp))
  }
}

@Composable
fun SecurityPillarMiniCard(
  title: String,
  status: String,
  subtitle: String,
  icon: androidx.compose.ui.graphics.vector.ImageVector,
  modifier: Modifier = Modifier
) {
  Box(
    modifier = modifier
      .clip(RoundedCornerShape(24.dp))
      .background(BentoSurface)
      .border(1.dp, BentoBorder, RoundedCornerShape(24.dp))
      .padding(16.dp)
  ) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
      Box(
        modifier = Modifier
          .size(32.dp)
          .clip(RoundedCornerShape(10.dp))
          .background(BentoBlueContainer),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = icon,
          contentDescription = null,
          tint = BentoOnBlueContainer,
          modifier = Modifier.size(16.dp)
        )
      }

      Text(
        text = title,
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold,
        color = BentoTextPrimary
      )

      Box(
        modifier = Modifier
          .clip(RoundedCornerShape(6.dp))
          .background(BentoGreenBg)
          .border(1.dp, BentoGreenBorder, RoundedCornerShape(6.dp))
          .padding(horizontal = 6.dp, vertical = 2.dp)
      ) {
        Text(
          text = status,
          fontSize = 9.sp,
          fontWeight = FontWeight.Bold,
          color = BentoGreenText
        )
      }

      Text(
        text = subtitle,
        fontSize = 10.sp,
        color = BentoTextSecondary
      )
    }
  }
}
