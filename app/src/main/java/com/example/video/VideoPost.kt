package com.example.video

data class VideoPost(
    val id: String,
    val title: String,
    val url: String,
    val authorName: String,
    val description: String,
    val likesCount: Int,
    val commentsCount: Int,
    val sharesCount: Int,
    val peerCount: Int, // Number of active peer nodes caching/seeding this video
    val sizeMb: Double, // Video file size in Megabytes
    val safeGemmaStatus: String // On-device Gemma AI Moderation Tag (e.g., "Clean / Safe", "Family Friendly")
)

/**
 * High-quality public sample videos from Google Storage buckets.
 * These are fast, reliable MP4 links that demonstrate Media3 SimpleCache beautifully.
 */
val SAMPLE_VIDEOS = listOf(
    VideoPost(
        id = "1",
        title = "Big Buck Bunny",
        url = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
        authorName = "@peer_alpha_92",
        description = "A friendly rabbit's journey through a magical forest. Decentralized Peer Node #102. Caching enabled.",
        likesCount = 1420,
        commentsCount = 89,
        sharesCount = 54,
        peerCount = 8,
        sizeMb = 12.4,
        safeGemmaStatus = "Clean / Verified"
    ),
    VideoPost(
        id = "2",
        title = "Elephants Dream",
        url = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4",
        authorName = "@node_omega_04",
        description = "First open-source computer-generated short movie. Distributed via secure Onion network.",
        likesCount = 985,
        commentsCount = 42,
        sharesCount = 19,
        peerCount = 5,
        sizeMb = 15.1,
        safeGemmaStatus = "Clean / Verified"
    ),
    VideoPost(
        id = "3",
        title = "For Bigger Blazes",
        url = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4",
        authorName = "@swarm_delta_57",
        description = "Chromecast fire presentation. Fully offline playback from local node cache.",
        likesCount = 540,
        commentsCount = 21,
        sharesCount = 12,
        peerCount = 12,
        sizeMb = 4.2,
        safeGemmaStatus = "Clean / Verified"
    ),
    VideoPost(
        id = "4",
        title = "For Bigger Escapes",
        url = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerEscapes.mp4",
        authorName = "@nexus_sigma_88",
        description = "Breathtaking landscapes cached across 3 geographic zones. Multi-source P2P downloading active.",
        likesCount = 2130,
        commentsCount = 156,
        sharesCount = 92,
        peerCount = 18,
        sizeMb = 8.7,
        safeGemmaStatus = "Clean / Verified"
    ),
    VideoPost(
        id = "5",
        title = "Subaru Outback On Street",
        url = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/SubaruOutbackOnStreetAndDirt.mp4",
        authorName = "@car_peer_07",
        description = "Driving demonstration on street and dirt. High-definition playback. On-device Gemma model scans complete.",
        likesCount = 320,
        commentsCount = 15,
        sharesCount = 6,
        peerCount = 3,
        sizeMb = 9.8,
        safeGemmaStatus = "Clean / Verified"
    )
)
