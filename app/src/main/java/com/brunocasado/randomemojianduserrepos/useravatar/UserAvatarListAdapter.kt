package com.brunocasado.randomemojianduserrepos.useravatar

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.brunocasado.randomemojianduserrepos.databinding.EmojiItemBinding
import com.brunocasado.randomemojianduserrepos.datasource.entity.User
import com.bumptech.glide.Glide

class UserAvatarListAdapter(
    private val removeUserAvatar: (User) -> Unit
) : ListAdapter<User, UserAvatarListAdapter.ViewHolder>(UserDiffCallback()) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)

        holder.bind(item, removeUserAvatar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(private val binding: EmojiItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: User, removeUserAvatar: (User) -> Unit) {
            Glide.with(binding.emojiImageView).load(item.avatarUrl).into(binding.emojiImageView)

            binding.emojiImageView.setOnClickListener {
                removeUserAvatar(item)
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

class UserDiffCallback : DiffUtil.ItemCallback<User>() {
    override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
        return oldItem.login == newItem.login
    }

    override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
        return oldItem == newItem
    }
}