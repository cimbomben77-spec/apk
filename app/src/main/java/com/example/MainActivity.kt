package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.P2PStatsPanel
import com.example.ui.components.VideoFeedItem
import com.example.ui.theme.MyApplicationTheme
import com.example.video.SAMPLE_VIDEOS

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme(darkTheme = true) { // Immersive dark mode is ideal for full-screen video feeds
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    containerColor = Color.Black
                ) { innerPadding ->
                    // Full bleed content, we will pass bottom/status bars insets internally where appropriate
                    Box(modifier = Modifier.fillMaxSize()) {
                        VideoFeedScreen()
                    }
                }
            }
        }
    }
}

@Composable
fun VideoFeedScreen() {
    val pagerState = rememberPagerState(pageCount = { SAMPLE_VIDEOS.size })
    var showP2PSettings by remember { mutableStateOf(false) }
    var selectedBottomTab by remember { mutableStateOf("Home") }

    Box(modifier = Modifier.fillMaxSize().background(Color.Black)) {
        // Full screen Vertical Video Pager
        VerticalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize().testTag("video_vertical_pager")
        ) { page ->
            val video = SAMPLE_VIDEOS[page]
            // Only play the video if it is currently selected (active) in the vertical pager
            val isPageActive = pagerState.currentPage == page

            VideoFeedItem(
                video = video,
                isPageActive = isPageActive,
                onOpenP2PSettings = { showP2PSettings = true }
            )
        }

        // TikTok style Home / Following top switcher tabs
        Row(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .statusBarsPadding()
                .padding(top = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "P2P Network",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { }
                )
                Spacer(modifier = Modifier.height(4.dp))
                Box(
                    modifier = Modifier
                        .size(width = 30.dp, height = 3.dp)
                        .background(Color(0xFF00E676), CircleShape)
                )
            }
            Text(
                text = "Nearby Peers",
                color = Color.White.copy(alpha = 0.6f),
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.clickable { }
            )
        }

        // TikTok-style Bottom Navigation Bar
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .background(Color.Black.copy(alpha = 0.85f))
                .navigationBarsPadding()
        ) {
            Divider(color = Color(0xFF1E293B), thickness = 0.5.dp)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                BottomNavItem(
                    title = "Home",
                    iconSelected = Icons.Filled.Home,
                    iconUnselected = Icons.Outlined.Home,
                    isSelected = selectedBottomTab == "Home",
                    onClick = { selectedBottomTab = "Home" },
                    modifier = Modifier.testTag("nav_home_button")
                )
                BottomNavItem(
                    title = "Discover",
                    iconSelected = Icons.Filled.Search,
                    iconUnselected = Icons.Outlined.Search,
                    isSelected = selectedBottomTab == "Discover",
                    onClick = { selectedBottomTab = "Discover" },
                    modifier = Modifier.testTag("nav_discover_button")
                )
                
                // Central TikTok Upload Button with custom styled cyan/magenta borders
                Box(
                    modifier = Modifier
                        .size(width = 46.dp, height = 30.dp)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) {
                            selectedBottomTab = "Create"
                        }
                        .testTag("nav_create_button"),
                    contentAlignment = Alignment.Center
                ) {
                    // Simulated neon P2P distribute/upload button
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .border(2.dp, Color(0xFF00E676), RoundedCornerShape(8.dp))
                            .background(Color.White, RoundedCornerShape(8.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "P2P Upload Video",
                            tint = Color.Black,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                BottomNavItem(
                    title = "Inbox",
                    iconSelected = Icons.Filled.Mail,
                    iconUnselected = Icons.Outlined.Mail,
                    isSelected = selectedBottomTab == "Inbox",
                    onClick = { selectedBottomTab = "Inbox" },
                    modifier = Modifier.testTag("nav_inbox_button")
                )
                BottomNavItem(
                    title = "Node",
                    iconSelected = Icons.Filled.Settings,
                    iconUnselected = Icons.Outlined.Settings,
                    isSelected = showP2PSettings,
                    onClick = { showP2PSettings = true },
                    modifier = Modifier.testTag("nav_node_settings_button")
                )
            }
        }

        // Animated Slid-up Custom P2P Settings Control Sheet
        AnimatedVisibility(
            visible = showP2PSettings,
            enter = slideInVertically(
                initialOffsetY = { height -> height },
                animationSpec = spring(dampingRatio = 0.85f)
            ) + fadeIn(),
            exit = slideOutVertically(
                targetOffsetY = { height -> height },
                animationSpec = spring(dampingRatio = 0.85f)
            ) + fadeOut(),
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f))
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {
                        showP2PSettings = false
                    },
                contentAlignment = Alignment.BottomCenter
            ) {
                P2PStatsPanel(
                    onDismiss = { showP2PSettings = false },
                    modifier = Modifier.clickable(enabled = false) { } // prevent dismissing panel when clicking on the content
                )
            }
        }
    }
}

@Composable
fun BottomNavItem(
    title: String,
    iconSelected: ImageVector,
    iconUnselected: ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            )
            .padding(8.dp)
            .width(60.dp)
    ) {
        Icon(
            imageVector = if (isSelected) iconSelected else iconUnselected,
            contentDescription = title,
            tint = if (isSelected) Color(0xFF00E676) else Color.White.copy(alpha = 0.6f),
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = title,
            color = if (isSelected) Color.White else Color.White.copy(alpha = 0.6f),
            fontSize = 10.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
        )
    }
}
