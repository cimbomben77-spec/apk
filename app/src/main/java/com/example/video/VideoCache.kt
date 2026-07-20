package com.example.video

import android.content.Context
import androidx.annotation.OptIn
import androidx.media3.common.util.UnstableApi
import androidx.media3.database.StandaloneDatabaseProvider
import androidx.media3.datasource.cache.LeastRecentlyUsedCacheEvictor
import androidx.media3.datasource.cache.SimpleCache
import java.io.File

@OptIn(UnstableApi::class)
object VideoCache {
    private var simpleCache: SimpleCache? = null
    private val lock = Any()

    fun getInstance(context: Context): SimpleCache {
        return simpleCache ?: synchronized(lock) {
            simpleCache ?: createCache(context).also { simpleCache = it }
        }
    }

    private fun createCache(context: Context): SimpleCache {
        val cacheDirectory = File(context.cacheDir, "p2p_video_cache")
        val evictor = LeastRecentlyUsedCacheEvictor(100 * 1024 * 1024) // 100MB max cache size
        val databaseProvider = StandaloneDatabaseProvider(context)
        return SimpleCache(cacheDirectory, evictor, databaseProvider)
    }

    /**
     * Helper to get the current cache directory size in bytes.
     */
    fun getCacheSize(context: Context): Long {
        val cacheDirectory = File(context.cacheDir, "p2p_video_cache")
        return getFolderSize(cacheDirectory)
    }

    private fun getFolderSize(file: File): Long {
        if (!file.exists()) return 0L
        if (file.isFile) return file.length()
        var size = 0L
        val files = file.listFiles()
        if (files != null) {
            for (f in files) {
                size += getFolderSize(f)
            }
        }
        return size
    }

    /**
     * Clears the cache safely.
     */
    fun clearCache(context: Context) {
        synchronized(lock) {
            try {
                simpleCache?.release()
                simpleCache = null
                val cacheDirectory = File(context.cacheDir, "p2p_video_cache")
                cacheDirectory.deleteRecursively()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
