package com.example.ui.components

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
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
import androidx.compose.ui.window.Dialog
import com.example.model.ActiveRide
import com.example.ui.theme.CampusAmber
import com.example.ui.theme.CampusEmerald
import java.util.Locale

@Composable
fun RatingTipDialog(
    ride: ActiveRide,
    onSubmit: (stars: Int, tip: Double) -> Unit,
    onDismiss: () -> Unit
) {
    var selectedStars by remember { mutableIntStateOf(5) }
    var selectedTip by remember { mutableStateOf(1.0) } // $1 default student friendly tip
    val selectedTags = remember { mutableStateListOf<String>() }

    val feedbackTags = listOf(
        "Great playlist 🎵",
        "Smooth driving 🚗",
        "Clean car ✨",
        "Helped with books 📚",
        "On-time arrival ⚡"
    )

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("rating_tip_dialog"),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF111927)),
            elevation = CardDefaults.cardElevation(defaultElevation = 24.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Driver icon
                Box(
                    modifier = Modifier
                        .size(58.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF003816))
                        .border(2.dp, CampusEmerald, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = ride.driver?.initials ?: "DR",
                        color = CampusEmerald,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 18.sp
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "Rate your Campus Trip",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "How was your ride with ${ride.driver?.name ?: "your student driver"}?",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF94A3B8),
                    fontSize = 12.sp,
                    modifier = Modifier.padding(top = 2.dp)
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Star Rating
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    for (i in 1..5) {
                        val isFilled = i <= selectedStars
                        Icon(
                            imageVector = if (isFilled) Icons.Default.Star else Icons.Outlined.Star,
                            contentDescription = "$i stars",
                            tint = if (isFilled) CampusAmber else Color(0xFF475569),
                            modifier = Modifier
                                .size(36.dp)
                                .clickable { selectedStars = i }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Compliments Chips
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "ADD A COMPLIMENT",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color(0xFF64748B),
                        fontWeight = FontWeight.Bold,
                        fontSize = 10.sp
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        feedbackTags.take(3).forEach { tag ->
                            val isSelected = selectedTags.contains(tag)
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = if (isSelected) CampusEmerald.copy(alpha = 0.2f) else Color(0xFF1E293B),
                                border = androidx.compose.foundation.BorderStroke(
                                    1.dp,
                                    if (isSelected) CampusEmerald else Color(0xFF334155)
                                ),
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable {
                                        if (isSelected) selectedTags.remove(tag) else selectedTags.add(tag)
                                    }
                            ) {
                                Text(
                                    text = tag,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = if (isSelected) CampusEmerald else Color.White,
                                    fontSize = 10.sp,
                                    modifier = Modifier.padding(vertical = 6.dp, horizontal = 4.dp),
                                    maxLines = 1
                                )
                            }
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        feedbackTags.drop(3).forEach { tag ->
                            val isSelected = selectedTags.contains(tag)
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = if (isSelected) CampusEmerald.copy(alpha = 0.2f) else Color(0xFF1E293B),
                                border = androidx.compose.foundation.BorderStroke(
                                    1.dp,
                                    if (isSelected) CampusEmerald else Color(0xFF334155)
                                ),
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable {
                                        if (isSelected) selectedTags.remove(tag) else selectedTags.add(tag)
                                    }
                            ) {
                                Text(
                                    text = tag,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = if (isSelected) CampusEmerald else Color.White,
                                    fontSize = 10.sp,
                                    modifier = Modifier.padding(vertical = 6.dp, horizontal = 4.dp),
                                    maxLines = 1
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Student Tip Row
                Text(
                    text = "SUPPORT STUDENT DRIVER (TIP)",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color(0xFF64748B),
                    fontWeight = FontWeight.Bold,
                    fontSize = 10.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val tipOptions = listOf(0.0, 1.0, 2.0, 3.0)
                    tipOptions.forEach { tip ->
                        val isSelected = selectedTip == tip
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = if (isSelected) CampusEmerald else Color(0xFF1E293B),
                            modifier = Modifier
                                .weight(1f)
                                .clickable { selectedTip = tip }
                        ) {
                            Text(
                                text = if (tip == 0.0) "No Tip" else "$${tip.toInt()}",
                                style = MaterialTheme.typography.labelMedium,
                                color = if (isSelected) Color(0xFF00240E) else Color.White,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(vertical = 8.dp),
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = { onSubmit(selectedStars, selectedTip) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("submit_rating_button"),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = CampusEmerald)
                ) {
                    Text(
                        text = "Submit Rating" + (if (selectedTip > 0) " & Pay Tip ($${String.format(Locale.US, "%.2f", selectedTip)})" else ""),
                        color = Color(0xFF00240E),
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
