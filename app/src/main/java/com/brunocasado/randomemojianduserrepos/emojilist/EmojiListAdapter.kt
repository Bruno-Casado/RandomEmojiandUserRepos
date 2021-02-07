package com.brunocasado.randomemojianduserrepos.emojilist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.brunocasado.randomemojianduserrepos.databinding.EmojiItemBinding
import com.brunocasado.randomemojianduserrepos.datasource.entity.Emoji
import com.bumptech.glide.Glide

class EmojiListAdapter(
    private val removeEmoji: (Emoji) -> Unit
) : ListAdapter<Emoji, EmojiListAdapter.ViewHolder>(EmojiDiffCallback()) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)

        holder.bind(item, removeEmoji)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(val binding: EmojiItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Emoji, removeEmoji: (Emoji) -> Unit) {
            Glide.with(binding.emojiImageView).load(item.url).into(binding.emojiImageView)

            binding.emojiImageView.setOnClickListener {
                removeEmoji(item)
            }
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = EmojiItemBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}

class EmojiDiffCallback : DiffUtil.ItemCallback<Emoji>() {
    override fun areItemsTheSame(oldItem: Emoji, newItem: Emoji): Boolean {
        return oldItem.name == newItem.name
    }

    override fun areContentsTheSame(oldItem: Emoji, newItem: Emoji): Boolean {
        return oldItem == newItem
    }
}