package com.brunocasado.randomemojianduserrepos.googlerepo

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.brunocasado.randomemojianduserrepos.R
import com.brunocasado.randomemojianduserrepos.core.BaseActivity
import com.brunocasado.randomemojianduserrepos.databinding.ActivityRepoListBinding
import com.brunocasado.randomemojianduserrepos.db.RepoDao

class RepoListActivity :
    BaseActivity<RepoListViewModel, ActivityRepoListBinding>(R.layout.activity_repo_list) {

    private lateinit var listAdapter: RepoListAdapter

    private val lastVisibleItemPosition: Int
        get() = (binding.repoRecyclerView.layoutManager as LinearLayoutManager).findLastCompletelyVisibleItemPosition()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBinding()
        initViewModel()
    }

    private fun initBinding() {
        binding.lifecycleOwner = this
        listAdapter = RepoListAdapter()
        binding.repoRecyclerView.adapter = listAdapter
        binding.repoRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                val totalItemCount = recyclerView.layoutManager?.itemCount
                if ((totalItemCount == lastVisibleItemPosition + 1) &&
                    (viewModel.dataLoadingError != RepoFailure.LastPageReached) &&
                    (viewModel.repos.value?.size ?: 0 >= RepoDao.PAGE_SIZE) &&
                    !viewModel.isLoading
                ) {
                    viewModel.loadNextPage()
                }
            }
        })
    }

    private fun initViewModel() {
        viewModel.repos.observe(this, {
            listAdapter.submitList(it)
        })
    }
}