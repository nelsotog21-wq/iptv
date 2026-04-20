package com.example.iptvplayer

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.iptvplayer.data.M3uParser
import com.example.iptvplayer.databinding.ActivityMainBinding
import com.example.iptvplayer.model.IptvChannel
import com.example.iptvplayer.ui.ChannelAdapter
import java.net.URL
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var player: ExoPlayer? = null
    private val channelAdapter = ChannelAdapter(::playChannel)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupPlayer()
        setupList()
        setupActions()

        binding.playlistInput.setText(DEFAULT_PLAYLIST_URL)
    }

    private fun setupPlayer() {
        player = ExoPlayer.Builder(this).build().also { exoPlayer ->
            binding.playerView.player = exoPlayer
        }
    }

    private fun setupList() {
        binding.channelList.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = channelAdapter
        }
    }

    private fun setupActions() {
        binding.loadButton.setOnClickListener {
            val playlistUrl = binding.playlistInput.text.toString().trim()
            if (playlistUrl.isEmpty()) {
                Toast.makeText(this, R.string.empty_playlist_error, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            fetchPlaylist(playlistUrl)
        }
    }

    private fun fetchPlaylist(url: String) {
        binding.loadButton.isEnabled = false
        lifecycleScope.launch {
            runCatching {
                withContext(Dispatchers.IO) {
                    URL(url).readText()
                }
            }.onSuccess { content ->
                val channels = M3uParser.parse(content)
                if (channels.isEmpty()) {
                    Toast.makeText(this@MainActivity, R.string.empty_channels_error, Toast.LENGTH_SHORT)
                        .show()
                }
                channelAdapter.submitList(channels)
            }.onFailure {
                Toast.makeText(this@MainActivity, R.string.playlist_load_error, Toast.LENGTH_SHORT)
                    .show()
            }

            binding.loadButton.isEnabled = true
        }
    }

    private fun playChannel(channel: IptvChannel) {
        val mediaItem = MediaItem.fromUri(channel.streamUrl)
        player?.apply {
            setMediaItem(mediaItem)
            prepare()
            playWhenReady = true
        }
        Toast.makeText(this, getString(R.string.now_playing, channel.name), Toast.LENGTH_SHORT).show()
    }

    override fun onStop() {
        super.onStop()
        player?.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.playerView.player = null
        player?.release()
        player = null
    }

    companion object {
        private const val DEFAULT_PLAYLIST_URL =
            "https://iptv-org.github.io/iptv/countries/us.m3u"
    }
}
