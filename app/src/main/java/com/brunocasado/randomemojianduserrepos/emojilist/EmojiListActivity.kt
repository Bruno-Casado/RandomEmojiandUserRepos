package com.brunocasado.randomemojianduserrepos.emojilist

import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.GridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.brunocasado.randomemojianduserrepos.MainActivityViewModel
import com.brunocasado.randomemojianduserrepos.R
import com.brunocasado.randomemojianduserrepos.core.AndroidUtils
import com.brunocasado.randomemojianduserrepos.core.BaseActivity
import com.brunocasado.randomemojianduserrepos.databinding.ActivityEmojiListBinding

class EmojiListActivity :
    BaseActivity<MainActivityViewModel, ActivityEmojiListBinding>(R.layout.activity_emoji_list),
    SwipeRefreshLayout.OnRefreshListener {
    private lateinit var listAdapter: EmojiListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBinding()
        initViewModel()
    }

    private fun initBinding() {
        binding.emojiRecyclerView.layoutManager =
            GridLayoutManager(this, AndroidUtils().calculateNumberOfColumns(this, EMOJI_SIZE))
        listAdapter = EmojiListAdapter {
            viewModel.removeEmojiFromList(it)
        }
        binding.emojiRecyclerView.adapter = listAdapter
        binding.swipeRefreshLayout.setOnRefreshListener(this)
    }

    private fun initViewModel() {
        viewModel.emojis.observe(this, {
            listAdapter.submitList(it)
        })
        viewModel.isLoading.observe(this, {
            binding.swipeRefreshLayout.isRefreshing = it
        })
        viewModel.showSuccessMessage = {
            Log.d("EmojiListActivity", "success")
        }
    }

    companion object {
        private const val EMOJI_SIZE = 128
    }

    override fun onRefresh() {
        viewModel.getEmoji()
    }
}