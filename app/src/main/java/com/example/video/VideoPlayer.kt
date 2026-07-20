package com.example.video

import android.content.Context
import androidx.annotation.OptIn
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.datasource.cache.CacheDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView

@OptIn(UnstableApi::class)
@Composable
fun VideoPlayer(
    videoUrl: String,
    isPlaying: Boolean,
    modifier: Modifier = Modifier,
    isMuted: Boolean = false,
    onPlayerStateChanged: ((playbackState: Int) -> Unit)? = null
) {
    val context = LocalContext.current

    val exoPlayer = remember {
        val cache = VideoCache.getInstance(context)
        val httpDataSourceFactory = DefaultHttpDataSource.Factory()
            .setAllowCrossProtocolRedirects(true)
        
        val cacheDataSourceFactory = CacheDataSource.Factory()
            .setCache(cache)
            .setUpstreamDataSourceFactory(httpDataSourceFactory)
            .setFlags(CacheDataSource.FLAG_IGNORE_CACHE_ON_ERROR)

        val mediaSourceFactory = DefaultMediaSourceFactory(context)
            .setDataSourceFactory(cacheDataSourceFactory)

        ExoPlayer.Builder(context)
            .setMediaSourceFactory(mediaSourceFactory)
            .build().apply {
                repeatMode = Player.REPEAT_MODE_ONE
                playWhenReady = isPlaying
                volume = if (isMuted) 0f else 1f
            }
    }

    // Set listener for buffering and other states
    DisposableEffect(exoPlayer) {
        val listener = object : Player.Listener {
            override fun onPlaybackStateChanged(playbackState: Int) {
                onPlayerStateChanged?.invoke(playbackState)
            }
        }
        exoPlayer.addListener(listener)
        onDispose {
            exoPlayer.removeListener(listener)
        }
    }

    // Handle play/pause updates
    LaunchedEffect(isPlaying) {
        exoPlayer.playWhenReady = isPlaying
    }

    // Handle mute state updates
    LaunchedEffect(isMuted) {
        exoPlayer.volume = if (isMuted) 0f else 1f
    }

    // Handle URL updates
    LaunchedEffect(videoUrl) {
        exoPlayer.stop()
        exoPlayer.clearMediaItems()
        exoPlayer.setMediaItem(MediaItem.fromUri(videoUrl))
        exoPlayer.prepare()
        exoPlayer.playWhenReady = isPlaying
    }

    // Clean up ExoPlayer when this Composable leaves the composition
    DisposableEffect(Unit) {
        onDispose {
            exoPlayer.release()
        }
    }

    AndroidView(
        factory = { ctx ->
            PlayerView(ctx).apply {
                player = exoPlayer
                useController = false // Custom TikTok gestures and overlay
                resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM
            }
        },
        modifier = modifier
    )
}
