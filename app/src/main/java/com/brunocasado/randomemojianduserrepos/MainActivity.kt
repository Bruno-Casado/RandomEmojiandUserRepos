package com.brunocasado.randomemojianduserrepos

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.brunocasado.randomemojianduserrepos.databinding.ActivityMainBinding
import com.brunocasado.randomemojianduserrepos.emojilist.EmojiListActivity
import com.bumptech.glide.Glide
import dagger.android.AndroidInjection
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val mainActivityViewModel: MainActivityViewModel by viewModels {
        viewModelFactory
    }

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        initBinding()
        initViewModel()
    }

    private fun initBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.viewModel = mainActivityViewModel
        binding.lifecycleOwner = this
        binding.searchUserNameButton.setOnClickListener {
            mainActivityViewModel.userLogin = binding.searchUserNameInput.text.toString()
            mainActivityViewModel.getUser()
        }
    }

    private fun initViewModel() {
        initViewModelActions()
        observeEmojis()
    }

    private fun initViewModelActions() {
        mainActivityViewModel.showNetworkConnectionError = {
            showToast(getString(R.string.network_connection_error_message))
        }
        mainActivityViewModel.showPersistenceError = {
            showToast(getString(R.string.persistence_error_message))
        }
        mainActivityViewModel.showServerError = {
            showToast(getString(R.string.server_error_message))
        }
        mainActivityViewModel.showSuccessMessage = {
            showToast(getString(R.string.success_message))
        }
        mainActivityViewModel.showLoadEmojiIntoImageViewError = {
            showToast(getString(R.string.random_emoji_error_message))
        }
        mainActivityViewModel.loadUrlIntoImageView = { emoji ->
            loadImageInto(emoji.url, binding.randomEmojiImageView)
            setContentDescriptionOnImageView(emoji.name, binding.randomEmojiImageView)
        }
        mainActivityViewModel.openEmojiListActivityAction = {
            openEmojiListActivity()
        }
        mainActivityViewModel.displayAvatarOnEmojiImageHolder = { userAvatar ->
            loadImageInto(userAvatar, binding.randomEmojiImageView)
        }
    }

    private fun showToast(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show()
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
        mainActivityViewModel.emojis.observe(this, {
            if (it.isNullOrEmpty()) {
                binding.getEmojiButton.text = getText(R.string.get_emoji_button)
            } else {
                binding.getEmojiButton.text = getText(R.string.get_random_emoji_button)
                binding.getEmojiButton.setOnClickListener {
                    mainActivityViewModel.showRandomEmoji()
                }
            }
        })
    }

}
