package com.brunocasado.randomemojianduserrepos

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import com.brunocasado.randomemojianduserrepos.core.BaseActivity
import com.brunocasado.randomemojianduserrepos.databinding.ActivityMainBinding
import com.brunocasado.randomemojianduserrepos.emojilist.EmojiListActivity
import com.brunocasado.randomemojianduserrepos.googlerepo.RepoListActivity
import com.brunocasado.randomemojianduserrepos.useravatar.UserAvatarListActivity
import com.bumptech.glide.Glide

class MainActivity :
    BaseActivity<MainActivityViewModel, ActivityMainBinding>(R.layout.activity_main) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBinding()
        initViewModel()
    }

    private fun initBinding() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        binding.searchUserNameButton.setOnClickListener {
            viewModel.userLogin = binding.searchUserNameInput.text.toString()
            viewModel.getUser()
        }
    }

    private fun initViewModel() {
        initViewModelActions()
        observeEmojis()
    }

    private fun initViewModelActions() {
        viewModel.showNetworkConnectionError = {
            showNetworkError()
        }
        viewModel.showPersistenceError = {
            showPersistenceError()
        }
        viewModel.showServerError = {
            showServerError()
        }
        viewModel.showSuccessMessage = {
            showToast(getString(R.string.success_message))
        }
        viewModel.showLoadEmojiIntoImageViewError = {
            showToast(getString(R.string.random_emoji_error_message))
        }
        viewModel.loadUrlIntoImageView = { emoji ->
            loadImageInto(emoji.url, binding.randomEmojiImageView)
            setContentDescriptionOnImageView(emoji.name, binding.randomEmojiImageView)
        }
        viewModel.openEmojiListActivityAction = {
            openEmojiListActivity()
        }
        viewModel.displayAvatarOnEmojiImageHolder = { userAvatar ->
            loadImageInto(userAvatar, binding.randomEmojiImageView)
        }
        viewModel.openAvatarListActivity = {
            openAvatarListActivity()
        }
        viewModel.openRepoListActivity = {
            openRepoListActivity()
        }
    }

    private fun openAvatarListActivity() {
        startActivity(Intent(this, UserAvatarListActivity::class.java))
    }

    private fun openRepoListActivity() {
        startActivity(Intent(this, RepoListActivity::class.java))
    }

    private fun loadImageInto(url: String, imageView: ImageView) {
        Glide.with(this).load(url).into(imageView)
    }

    private fun setContentDescriptionOnImageView(contentDescription: String, imageView: ImageView) {
        imageView.contentDescription = contentDescription
    }

    private fun openEmojiListActivity() {
        val intent = Intent(this, EmojiListActivity::class.java)
        startActivity(intent)
    }

    private fun observeEmojis() {
        viewModel.emojis.observe(this, {
            if (it.isNullOrEmpty()) {
                binding.getEmojiButton.text = getText(R.string.get_emoji_button)
            } else {
                binding.getEmojiButton.text = getText(R.string.get_random_emoji_button)
                binding.getEmojiButton.setOnClickListener {
                    viewModel.showRandomEmoji()
                }
            }
        })
    }

}
