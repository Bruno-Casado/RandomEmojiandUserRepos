package com.brunocasado.randomemojianduserrepos.useravatar

import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import com.brunocasado.randomemojianduserrepos.R
import com.brunocasado.randomemojianduserrepos.core.AndroidUtils
import com.brunocasado.randomemojianduserrepos.core.BaseActivity
import com.brunocasado.randomemojianduserrepos.databinding.ActivityUserAvatarListBinding

class UserAvatarListActivity :
    BaseActivity<UserAvatarListViewModel, ActivityUserAvatarListBinding>(R.layout.activity_user_avatar_list) {

    private lateinit var listAdapter: UserAvatarListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBinding()
        initViewModel()
    }

    private fun initBinding() {
        binding.avatarRecyclerView.layoutManager =
            GridLayoutManager(this, AndroidUtils().calculateNumberOfColumns(this, AVATAR_SIZE))
        listAdapter = UserAvatarListAdapter {
            viewModel.deleteUser(it)
        }
        binding.avatarRecyclerView.adapter = listAdapter
    }

    private fun initViewModel() {
        viewModel.users.observe(this, {
            listAdapter.submitList(it)
        })
        viewModel.showDeleteUserSuccess = {
            showToast("Delete success")
        }
        viewModel.showNetworkConnectionError = {
            showNetworkError()
        }
        viewModel.showPersistenceError = {
            showPersistenceError()
        }
        viewModel.showServerError = {
            showServerError()
        }
    }

    companion object {
        private const val AVATAR_SIZE = 128
    }
}