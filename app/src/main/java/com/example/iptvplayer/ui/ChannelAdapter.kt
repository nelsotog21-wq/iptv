package com.example.iptvplayer.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.iptvplayer.databinding.ItemChannelBinding
import com.example.iptvplayer.model.IptvChannel

class ChannelAdapter(
    private val onItemClick: (IptvChannel) -> Unit
) : ListAdapter<IptvChannel, ChannelAdapter.ChannelViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChannelViewHolder {
        val binding = ItemChannelBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ChannelViewHolder(binding, onItemClick)
    }

    override fun onBindViewHolder(holder: ChannelViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ChannelViewHolder(
        private val binding: ItemChannelBinding,
        private val onItemClick: (IptvChannel) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: IptvChannel) {
            binding.channelName.text = item.name
            binding.channelUrl.text = item.streamUrl
            binding.root.setOnClickListener { onItemClick(item) }
        }
    }

    private companion object {
        val DiffCallback = object : DiffUtil.ItemCallback<IptvChannel>() {
            override fun areItemsTheSame(oldItem: IptvChannel, newItem: IptvChannel): Boolean {
                return oldItem.streamUrl == newItem.streamUrl
            }

            override fun areContentsTheSame(oldItem: IptvChannel, newItem: IptvChannel): Boolean {
                return oldItem == newItem
            }
        }
    }
}
