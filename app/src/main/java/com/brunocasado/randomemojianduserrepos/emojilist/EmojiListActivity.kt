package com.brunocasado.randomemojianduserrepos.emojilist

import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.brunocasado.randomemojianduserrepos.MainActivityViewModel
import com.brunocasado.randomemojianduserrepos.R
import com.brunocasado.randomemojianduserrepos.databinding.ActivityEmojiListBinding
import dagger.android.AndroidInjection
import javax.inject.Inject

class EmojiListActivity : AppCompatActivity(), SwipeRefreshLayout.OnRefreshListener {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val emojiListViewModel: MainActivityViewModel by viewModels {
        viewModelFactory
    }
    private lateinit var listAdapter: EmojiListAdapter

    private lateinit var binding: ActivityEmojiListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        initBinding()
        initViewModel()
    }

    private fun initBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_emoji_list)
        binding.emojiRecyclerView.layoutManager = GridLayoutManager(this, calculateNoOfColumns())
        listAdapter = EmojiListAdapter {
            emojiListViewModel.removeEmojiFromList(it)
        }
        binding.emojiRecyclerView.adapter = listAdapter
        binding.swipeRefreshLayout.setOnRefreshListener(this)
    }

    private fun initViewModel() {
        emojiListViewModel.emojis.observe(this, {
            listAdapter.submitList(it)
        })
        emojiListViewModel.isLoading.observe(this, {
            binding.swipeRefreshLayout.isRefreshing = it
        })
        emojiListViewModel.showSuccessMessage = {
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
        emojiListViewModel.getEmoji()
    }
}