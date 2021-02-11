package com.brunocasado.randomemojianduserrepos.core

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.brunocasado.randomemojianduserrepos.R
import dagger.android.AndroidInjection
import java.lang.reflect.ParameterizedType
import javax.inject.Inject

abstract class BaseActivity<T : ViewModel, V : ViewDataBinding>(
    private val layoutId: Int
) : AppCompatActivity() {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    protected lateinit var viewModel: T
    protected lateinit var binding: V

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setupViewModel()
        setupDataBinding()
    }

    private fun setupViewModel() {
        val clazz =
            (this.javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0] as Class<T>
        viewModel = ViewModelProvider(this, viewModelFactory)[clazz]
    }

    private fun setupDataBinding() {
        binding = DataBindingUtil.setContentView(this, layoutId)
    }

    protected fun showNetworkError() {
        showToast(getString(R.string.network_connection_error_message))
    }

    protected fun showPersistenceError() {
        showToast(getString(R.string.persistence_error_message))
    }

    protected fun showServerError() {
        showToast(getString(R.string.server_error_message))
    }

    protected fun showToast(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show()
    }
}
