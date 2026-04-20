package com.example.iptvplayer.data

import com.example.iptvplayer.model.IptvChannel

object M3uParser {

    fun parse(content: String): List<IptvChannel> {
        val lines = content.lines().map { it.trim() }
        val channels = mutableListOf<IptvChannel>()
        var pendingName: String? = null

        lines.forEach { line ->
            when {
                line.startsWith("#EXTINF", ignoreCase = true) -> {
                    pendingName = line.substringAfterLast(',').ifBlank { "Canal" }
                }

                line.isNotEmpty() && !line.startsWith("#") -> {
                    val name = pendingName ?: line
                    channels += IptvChannel(name = name, streamUrl = line)
                    pendingName = null
                }
            }
        }

        return channels
    }
}
