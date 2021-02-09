package com.brunocasado.randomemojianduserrepos.emojilist

import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.brunocasado.randomemojianduserrepos.MainActivityViewModel
import com.brunocasado.randomemojianduserrepos.R
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
        binding = DataBindingUtil.setContentView(this, R.layout.activity_emoji_list)
        binding.emojiRecyclerView.layoutManager = GridLayoutManager(this, calculateNoOfColumns())
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

    private fun calculateNoOfColumns(): Int {
        val displayMetrics: DisplayMetrics = resources.displayMetrics
        val screenWidthDp = displayMetrics.widthPixels / displayMetrics.density
        return (screenWidthDp / EMOJI_SIZE).toInt()
    }

    companion object {
        private const val EMOJI_SIZE = 128
    }

    override fun onRefresh() {
        viewModel.getEmoji()
    }
}