package com.brunocasado.randomemojianduserrepos

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import javax.inject.Inject

class MainActivity : AppCompatActivity(), HasActivityInjector {
    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Activity>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun activityInjector(): AndroidInjector<Activity> = dispatchingAndroidInjector
}