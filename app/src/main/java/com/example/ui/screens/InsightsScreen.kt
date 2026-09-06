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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Equalizer
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.BentoAmber
import com.example.ui.theme.BentoBackground
import com.example.ui.theme.BentoBlueContainer
import com.example.ui.theme.BentoBluePrimary
import com.example.ui.theme.BentoBorder
import com.example.ui.theme.BentoGreen
import com.example.ui.theme.BentoOnBlueContainer
import com.example.ui.theme.BentoOnRose
import com.example.ui.theme.BentoRoseAccent
import com.example.ui.theme.BentoRoseCard
import com.example.ui.theme.BentoSurface
import com.example.ui.theme.BentoTextPrimary
import com.example.ui.theme.BentoTextSecondary

@Composable
fun InsightsScreen(
  modifier: Modifier = Modifier
) {
  val scrollState = rememberScrollState()

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(BentoBackground)
      .verticalScroll(scrollState)
      .padding(16.dp),
    verticalArrangement = Arrangement.spacedBy(14.dp)
  ) {
    // 1. Bento Metric Header Card
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(28.dp))
        .background(BentoBlueContainer)
        .padding(22.dp)
        .testTag("insights_hero_card")
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column(modifier = Modifier.weight(1f)) {
          Text(
            text = "COGNITIVE TRAJECTORY",
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = BentoOnBlueContainer,
            letterSpacing = 1.sp
          )
          Spacer(modifier = Modifier.height(4.dp))
          Text(
            text = "+24% Resilience",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            color = BentoOnBlueContainer
          )
          Spacer(modifier = Modifier.height(6.dp))
          Text(
            text = "Average reframing shift: 3.8 → 8.4/10 mood score after Socratic reflection.",
            fontSize = 12.sp,
            color = BentoOnBlueContainer.copy(alpha = 0.85f),
            lineHeight = 17.sp
          )
        }

        Box(
          modifier = Modifier
            .size(52.dp)
            .clip(CircleShape)
            .background(Color.White.copy(alpha = 0.6f)),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = Icons.AutoMirrored.Filled.TrendingUp,
            contentDescription = null,
            tint = BentoOnBlueContainer,
            modifier = Modifier.size(28.dp)
          )
        }
      }
    }

    // 2. Bento Distortion Breakdown Card
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(28.dp))
        .background(BentoSurface)
        .border(1.dp, BentoBorder, RoundedCornerShape(28.dp))
        .padding(20.dp)
        .testTag("distortion_breakdown_card")
    ) {
      Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
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
              imageVector = Icons.Default.Psychology,
              contentDescription = null,
              tint = BentoRoseAccent,
              modifier = Modifier.size(20.dp)
            )
            Text(
              text = "Deconstructed Distortions",
              fontSize = 14.sp,
              fontWeight = FontWeight.Bold,
              color = BentoTextPrimary
            )
          }

          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(8.dp))
              .background(BentoRoseCard)
              .padding(horizontal = 8.dp, vertical = 2.dp)
          ) {
            Text(
              text = "AI Verified",
              fontSize = 10.sp,
              fontWeight = FontWeight.Bold,
              color = BentoOnRose
            )
          }
        }

        // Distortion Bars
        DistortionProgressRow("Catastrophizing", 0.45f, "45%", BentoRoseAccent)
        DistortionProgressRow("All-or-Nothing", 0.28f, "28%", BentoAmber)
        DistortionProgressRow("Mind Reading", 0.17f, "17%", BentoBluePrimary)
        DistortionProgressRow("Emotional Reasoning", 0.10f, "10%", BentoGreen)
      }
    }

    // 3. Bento Socratic Breakthrough Card
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(28.dp))
        .background(BentoSurface)
        .border(1.dp, BentoBorder, RoundedCornerShape(28.dp))
        .padding(20.dp)
    ) {
      Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          Icon(
            imageVector = Icons.Default.AutoAwesome,
            contentDescription = null,
            tint = BentoBluePrimary,
            modifier = Modifier.size(18.dp)
          )
          Text(
            text = "Active Socratic Reframe",
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = BentoTextPrimary
          )
        }

        Box(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(BentoBackground)
            .border(1.dp, BentoBorder, RoundedCornerShape(16.dp))
            .padding(14.dp)
        ) {
          Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(
              text = "THE REFRAME PRINCIPLE",
              fontSize = 10.sp,
              fontWeight = FontWeight.Bold,
              color = BentoBluePrimary
            )
            Text(
              text = "\"Security critiques during hackathon review are validation checkpoints, not project failures.\"",
              fontSize = 13.sp,
              fontWeight = FontWeight.Medium,
              color = BentoTextPrimary,
              lineHeight = 18.sp
            )
          }
        }
      }
    }

    Spacer(modifier = Modifier.height(16.dp))
  }
}

@Composable
fun DistortionProgressRow(
  name: String,
  progress: Float,
  percentage: String,
  color: Color
) {
  Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween
    ) {
      Text(
        text = name,
        fontSize = 12.sp,
        fontWeight = FontWeight.Medium,
        color = BentoTextPrimary
      )
      Text(
        text = percentage,
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold,
        color = color
      )
    }

    LinearProgressIndicator(
      progress = { progress },
      modifier = Modifier
        .fillMaxWidth()
        .height(8.dp)
        .clip(RoundedCornerShape(4.dp)),
      color = color,
      trackColor = BentoBackground
    )
  }
}
