package com.brunocasado.randomemojianduserrepos

import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModel
import javax.inject.Inject

class MainActivityViewModel @Inject constructor() : ViewModel() {

    fun getEmoji() = View.OnClickListener {
        Log.d("Teste", "Clicou")
    }
}