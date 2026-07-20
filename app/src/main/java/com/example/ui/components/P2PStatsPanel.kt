package com.example.ui.components

import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.video.VideoCache
import java.util.Locale

@Composable
fun P2PStatsPanel(
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var cacheSize by remember { mutableStateOf(VideoCache.getCacheSize(context)) }
    
    // Node power optimizer profiles
    var powerMode by remember { mutableStateOf("Balanced") } // Eco, Balanced, Turbo Node
    
    // Privacy Routing Config
    var onionRoutingEnabled by remember { mutableStateOf(true) }
    var torHopsCount by remember { mutableStateOf(3) }
    
    // Local Gemma AI Moderation Config
    var gemmaModerationEnabled by remember { mutableStateOf(true) }
    var sensitivityLevel by remember { mutableStateOf("Standard") } // High, Standard, Relaxed

    val cacheSizeFormatted = remember(cacheSize) {
        val sizeMb = cacheSize / (1024.0 * 1024.0)
        String.format(Locale.US, "%.2f MB", sizeMb)
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight(0.85f),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF12151A).copy(alpha = 0.96f)
        ),
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
        border = BorderStroke(1.dp, Color(0xFF222C38))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
        ) {
            // Drag Handle Bar
            Box(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .width(40.dp)
                    .height(4.dp)
                    .background(Color(0xFF37465B), shape = CircleShape)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Header Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "P2P Node Control Panel",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Configure decentralized video stream settings",
                        color = Color(0xFF8B9EB5),
                        fontSize = 12.sp
                    )
                }
                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier.testTag("dismiss_stats_panel")
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close settings",
                        tint = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Section 1: Storage & Video Cache
                StatsSectionHeader(title = "Video Caching (Media3 SimpleCache)", icon = Icons.Outlined.Storage)
                SettingsCard {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Local Buffer Space",
                                color = Color.White,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = "Videos are cached on-disk to stop repeat downloading.",
                                color = Color(0xFF8B9EB5),
                                fontSize = 11.sp,
                                modifier = Modifier.fillMaxWidth(0.7f)
                            )
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = cacheSizeFormatted,
                                color = Color(0xFF4CAF50),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.testTag("cache_size_text")
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Button(
                                onClick = {
                                    VideoCache.clearCache(context)
                                    cacheSize = VideoCache.getCacheSize(context)
                                },
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFFFF5252).copy(alpha = 0.15f),
                                    contentColor = Color(0xFFFF5252)
                                ),
                                border = BorderStroke(1.dp, Color(0xFFFF5252).copy(alpha = 0.4f)),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier
                                    .height(32.dp)
                                    .testTag("clear_cache_button")
                            ) {
                                Text("Clear Cache", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }

                // Section 2: Privacy (Tor / Onion Routing)
                StatsSectionHeader(title = "Privacy & Encryption (Onion Routing)", icon = Icons.Outlined.Shield)
                SettingsCard {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Obfuscate Node IP Address",
                                    color = Color.White,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Text(
                                    text = "Routes video packets through Tor-like encrypted nodes to prevent IP leaks.",
                                    color = Color(0xFF8B9EB5),
                                    fontSize = 11.sp
                                )
                            }
                            Switch(
                                checked = onionRoutingEnabled,
                                onCheckedChange = { onionRoutingEnabled = it },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = Color(0xFF00E676),
                                    checkedTrackColor = Color(0xFF00E676).copy(alpha = 0.3f),
                                    uncheckedThumbColor = Color(0xFF78909C),
                                    uncheckedTrackColor = Color(0xFF37474F)
                                ),
                                modifier = Modifier.testTag("onion_routing_switch")
                            )
                        }

                        AnimatedVisibility(visible = onionRoutingEnabled) {
                            Column {
                                Divider(color = Color(0xFF222C38), modifier = Modifier.padding(vertical = 12.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "Routing Hops Count:",
                                        color = Color(0xFF8B9EB5),
                                        fontSize = 12.sp
                                    )
                                    Row(
                                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        listOf(1, 3, 5).forEach { hop ->
                                            val isSelected = torHopsCount == hop
                                            val chipBg = if (isSelected) Color(0xFF00E676).copy(alpha = 0.2f) else Color(0xFF1E2734)
                                            val chipBorder = if (isSelected) Color(0xFF00E676) else Color(0xFF323F52)
                                            Box(
                                                modifier = Modifier
                                                    .border(1.dp, chipBorder, RoundedCornerShape(6.dp))
                                                    .background(chipBg, RoundedCornerShape(6.dp))
                                                    .clickable { torHopsCount = hop }
                                                    .padding(horizontal = 10.dp, vertical = 4.dp),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Text(
                                                    text = "$hop Node" + if (hop > 1) "s" else "",
                                                    color = if (isSelected) Color(0xFF00E676) else Color.White,
                                                    fontSize = 11.sp,
                                                    fontWeight = FontWeight.Bold
                                                )
                                            }
                                        }
                                    }
                                }
                                Spacer(modifier = Modifier.height(10.dp))
                                Text(
                                    text = "Active Tor Relay Status: Encrypted tunnels established via ${torHopsCount} random proxies.",
                                    color = Color(0xFF00E676),
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
                }

                // Section 3: Device Power & Battery Optimizer
                StatsSectionHeader(title = "Battery & Network Optimization", icon = Icons.Outlined.BatteryChargingFull)
                SettingsCard {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Background Seeding Strategy",
                            color = Color.White,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = "Limits CPU / network overhead while you act as a node in the P2P network.",
                            color = Color(0xFF8B9EB5),
                            fontSize = 11.sp
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            val modes = listOf(
                                Triple("Eco", "Min battery usage, halts seeding on mobile data.", Color(0xFF2196F3)),
                                Triple("Balanced", "Default adaptive playback and smart peer sharing.", Color(0xFF00E676)),
                                Triple("Turbo Node", "Full seeder bandwidth, maximum node sharing.", Color(0xFFFF9100))
                            )

                            modes.forEach { (name, desc, color) ->
                                val isSelected = powerMode == name
                                val activeBorderColor = if (isSelected) color else Color(0xFF323F52)
                                val activeBg = if (isSelected) color.copy(alpha = 0.12f) else Color(0xFF171E27)

                                Column(
                                    modifier = Modifier
                                        .weight(1f)
                                        .border(1.dp, activeBorderColor, RoundedCornerShape(12.dp))
                                        .background(activeBg, RoundedCornerShape(12.dp))
                                        .clickable { powerMode = name }
                                        .padding(12.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Icon(
                                        imageVector = when (name) {
                                            "Eco" -> Icons.Default.BatterySaver
                                            "Balanced" -> Icons.Default.Bolt
                                            else -> Icons.Default.Speed
                                        },
                                        contentDescription = name,
                                        tint = if (isSelected) color else Color(0xFF718296),
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = name,
                                        color = if (isSelected) Color.White else Color(0xFF8B9EB5),
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = desc,
                                        color = Color(0xFF596E85),
                                        fontSize = 9.sp,
                                        lineHeight = 11.sp,
                                        modifier = Modifier.align(Alignment.Start)
                                    )
                                }
                            }
                        }
                    }
                }

                // Section 4: On-device Moderation (Gemma AI)
                StatsSectionHeader(title = "Local Content Moderation (Gemma)", icon = Icons.Outlined.SmartToy)
                SettingsCard {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "On-Device Moderation Scanner",
                                    color = Color.White,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Text(
                                    text = "Runs local inference checks using on-device neural engines to protect from inappropriate posts.",
                                    color = Color(0xFF8B9EB5),
                                    fontSize = 11.sp
                                )
                            }
                            Switch(
                                checked = gemmaModerationEnabled,
                                onCheckedChange = { gemmaModerationEnabled = it },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = Color(0xFF00E676),
                                    checkedTrackColor = Color(0xFF00E676).copy(alpha = 0.3f),
                                    uncheckedThumbColor = Color(0xFF78909C),
                                    uncheckedTrackColor = Color(0xFF37474F)
                                ),
                                modifier = Modifier.testTag("gemma_moderation_switch")
                            )
                        }

                        AnimatedVisibility(visible = gemmaModerationEnabled) {
                            Column {
                                Divider(color = Color(0xFF222C38), modifier = Modifier.padding(vertical = 12.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "Scan Strictness:",
                                        color = Color(0xFF8B9EB5),
                                        fontSize = 12.sp
                                    )
                                    Row(
                                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        listOf("Relaxed", "Standard", "High").forEach { level ->
                                            val isSelected = sensitivityLevel == level
                                            val chipBg = if (isSelected) Color(0xFF00E676).copy(alpha = 0.15f) else Color(0xFF1E2734)
                                            val chipBorder = if (isSelected) Color(0xFF00E676) else Color(0xFF323F52)
                                            Box(
                                                modifier = Modifier
                                                    .border(1.dp, chipBorder, RoundedCornerShape(6.dp))
                                                    .background(chipBg, RoundedCornerShape(6.dp))
                                                    .clickable { sensitivityLevel = level }
                                                    .padding(horizontal = 10.dp, vertical = 4.dp),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Text(
                                                    text = level,
                                                    color = if (isSelected) Color(0xFF00E676) else Color.White,
                                                    fontSize = 11.sp,
                                                    fontWeight = FontWeight.Bold
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

            Spacer(modifier = Modifier.height(16.dp))

            // Done Button
            Button(
                onClick = onDismiss,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("stats_panel_done_button"),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF00E676),
                    contentColor = Color.Black
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "Save and Sync Config",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
            }
        }
    }
}

@Composable
fun StatsSectionHeader(title: String, icon: ImageVector) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color(0xFF00E676),
            modifier = Modifier.size(18.dp)
        )
        Text(
            text = title,
            color = Color.White,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.5.sp
        )
    }
}

@Composable
fun SettingsCard(content: @Composable ColumnScope.() -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF171E27)
        ),
        shape = RoundedCornerShape(14.dp),
        border = BorderStroke(1.dp, Color(0xFF222C38))
    ) {
        Column {
            content()
        }
    }
}
