package com.example.chat_app

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class ChatApp: Application() {

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context

        lateinit var sharedPreferences: SharedPreferences

    }

    override fun onCreate() {
        super.onCreate();
        //通过这个方法得到程序级别的Context
        context = applicationContext
        sharedPreferences = getSharedPreferences("username",0)
    }

}