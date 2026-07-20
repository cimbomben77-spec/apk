package com.example.ui.components

import androidx.compose.animation.*
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.video.VideoPost
import com.example.video.VideoPlayer
import kotlinx.coroutines.delay

@Composable
fun VideoFeedItem(
    video: VideoPost,
    isPageActive: Boolean,
    onOpenP2PSettings: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isPlaying by remember { mutableStateOf(isPageActive) }
    var isLiked by remember { mutableStateOf(false) }
    var isMuted by remember { mutableStateOf(false) }
    var localLikesCount by remember { mutableStateOf(video.likesCount) }
    var showPlayPauseIndicator by remember { mutableStateOf(false) }
    var playerBuffering by remember { mutableStateOf(true) }

    // Sync play state with page active state
    LaunchedEffect(isPageActive) {
        isPlaying = isPageActive
    }

    // Temporary splash play/pause overlay animator
    LaunchedEffect(showPlayPauseIndicator) {
        if (showPlayPauseIndicator) {
            delay(600)
            showPlayPauseIndicator = false
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        // Full-screen Video Player
        VideoPlayer(
            videoUrl = video.url,
            isPlaying = isPlaying,
            isMuted = isMuted,
            onPlayerStateChanged = { playbackState ->
                // 2 is buffering, 3 is ready to play
                playerBuffering = (playbackState == 2)
            },
            modifier = Modifier
                .fillMaxSize()
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) {
                    isPlaying = !isPlaying
                    showPlayPauseIndicator = true
                }
                .testTag("video_player_surface_${video.id}")
        )

        // Dimming Gradients (for UI elements readability)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.3f)
                .align(Alignment.TopCenter)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Black.copy(alpha = 0.6f),
                            Color.Transparent
                        )
                    )
                )
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.45f)
                .align(Alignment.BottomCenter)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.Black.copy(alpha = 0.8f)
                        )
                    )
                )
        )

        // Buffering Indicator
        if (playerBuffering) {
            CircularProgressIndicator(
                color = Color(0xFF00E676),
                strokeWidth = 3.dp,
                modifier = Modifier
                    .size(48.dp)
                    .align(Alignment.Center)
                    .testTag("video_player_buffering_${video.id}")
            )
        }

        // Play/Pause Large Floating Action Indicator
        AnimatedVisibility(
            visible = showPlayPauseIndicator,
            enter = fadeIn(animationSpec = tween(150)) + scaleIn(initialScale = 0.5f, animationSpec = spring()),
            exit = fadeOut(animationSpec = tween(200)) + scaleOut(targetScale = 1.5f, animationSpec = tween(200)),
            modifier = Modifier.align(Alignment.Center)
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .background(Color.Black.copy(alpha = 0.5f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (isPlaying) Icons.Default.PlayArrow else Icons.Default.Pause,
                    contentDescription = if (isPlaying) "Playing" else "Paused",
                    tint = Color.White,
                    modifier = Modifier.size(42.dp)
                )
            }
        }

        // Top Privacy / Encryption Status Indicator Badge
        Row(
            modifier = Modifier
                .align(Alignment.TopStart)
                .statusBarsPadding()
                .padding(horizontal = 20.dp, vertical = 12.dp)
                .background(Color.Black.copy(alpha = 0.45f), RoundedCornerShape(20.dp))
                .border(1.dp, Color(0xFF00E676).copy(alpha = 0.3f), RoundedCornerShape(20.dp))
                .clickable { onOpenP2PSettings() }
                .padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .background(Color(0xFF00E676), CircleShape)
            )
            Text(
                text = "P2P Shield: Onion Routing Active",
                color = Color.White,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
            )
        }

        // Bottom Details & Info Card (TikTok Bottom Panel)
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .navigationBarsPadding()
                .padding(start = 16.dp, end = 90.dp, bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Author Name & Network Address
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .background(
                            Brush.linearGradient(listOf(Color(0xFF00E676), Color(0xFF2196F3))),
                            CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Wifi,
                        contentDescription = "P2P Seeder",
                        tint = Color.Black,
                        modifier = Modifier.size(16.dp)
                    )
                }
                Text(
                    text = video.authorName,
                    color = Color.White,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
                // Peer status badge
                Box(
                    modifier = Modifier
                        .background(Color(0xFF00E676).copy(alpha = 0.2f), RoundedCornerShape(4.dp))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "NODE ACTIVE",
                        color = Color(0xFF00E676),
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Description / Tags
            Text(
                text = video.description,
                color = Color(0xFFE2E8F0),
                fontSize = 13.sp,
                lineHeight = 18.sp,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )

            // Dynamic P2P Telemetry details
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF1E293B).copy(alpha = 0.4f), RoundedCornerShape(8.dp))
                    .padding(horizontal = 10.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = null,
                        tint = Color(0xFF60A5FA),
                        modifier = Modifier.size(14.dp)
                    )
                    Text(
                        text = "${video.peerCount} Seeders",
                        color = Color(0xFF93C5FD),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.SdStorage,
                        contentDescription = null,
                        tint = Color(0xFF34D399),
                        modifier = Modifier.size(14.dp)
                    )
                    Text(
                        text = "${video.sizeMb} MB Cached",
                        color = Color(0xFF6EE7B7),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = Color(0xFFA7F3D0),
                        modifier = Modifier.size(14.dp)
                    )
                    Text(
                        text = video.safeGemmaStatus,
                        color = Color(0xFFA7F3D0),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }

        // TikTok Right Action Sidebar
        Column(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .navigationBarsPadding()
                .padding(end = 12.dp, bottom = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Seeder Avatar Circle with follow "+" button
            Box(contentAlignment = Alignment.BottomCenter) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .border(1.5.dp, Color.White, CircleShape)
                        .background(Color(0xFF2D3748), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Person,
                        contentDescription = "User avatar",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Box(
                    modifier = Modifier
                        .offset(y = 8.dp)
                        .size(18.dp)
                        .background(Color(0xFF00E676), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Follow peer node",
                        tint = Color.Black,
                        modifier = Modifier.size(12.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Like Action Item
            val likeScale by animateFloatAsState(
                targetValue = if (isLiked) 1.25f else 1f,
                animationSpec = spring(dampingRatio = 0.5f)
            )
            SidebarActionItem(
                icon = if (isLiked) Icons.Default.Favorite else Icons.Outlined.FavoriteBorder,
                contentDescription = "Like video",
                count = localLikesCount.toString(),
                tintColor = if (isLiked) Color(0xFFFF4081) else Color.White,
                onClick = {
                    isLiked = !isLiked
                    if (isLiked) localLikesCount++ else localLikesCount--
                },
                modifier = Modifier
                    .scale(likeScale)
                    .testTag("like_button_${video.id}")
            )

            // Comment Action Item
            SidebarActionItem(
                icon = Icons.Outlined.Comment,
                contentDescription = "Comment list",
                count = video.commentsCount.toString(),
                onClick = { /* Simulated Comments UI */ },
                modifier = Modifier.testTag("comment_button_${video.id}")
            )

            // P2P Stats Action Button
            SidebarActionItem(
                icon = Icons.Outlined.Hub,
                contentDescription = "P2P Node Telemetry",
                count = "Node",
                tintColor = Color(0xFF00E676),
                onClick = onOpenP2PSettings,
                modifier = Modifier.testTag("p2p_details_button_${video.id}")
            )

            // Mute Action Button
            SidebarActionItem(
                icon = if (isMuted) Icons.Default.VolumeOff else Icons.Default.VolumeUp,
                contentDescription = "Mute Toggle",
                count = if (isMuted) "Muted" else "Sound",
                onClick = { isMuted = !isMuted },
                modifier = Modifier.testTag("mute_button_${video.id}")
            )
        }
    }
}

@Composable
fun SidebarActionItem(
    icon: ImageVector,
    contentDescription: String,
    count: String,
    modifier: Modifier = Modifier,
    tintColor: Color = Color.White,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            )
            .padding(4.dp)
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .background(Color.Black.copy(alpha = 0.5f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = contentDescription,
                tint = tintColor,
                modifier = Modifier.size(26.dp)
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = count,
            color = Color.White,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}
