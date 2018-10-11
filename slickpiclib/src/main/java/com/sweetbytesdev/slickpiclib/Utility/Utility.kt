package com.sweetbytesdev.slickpiclib.Utility

import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.WindowManager

fun setupStatusBarHidden(appCompatActivity: AppCompatActivity) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
        val w = appCompatActivity.window
        w.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
        w.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
    }
}

fun showStatusBar(appCompatActivity: AppCompatActivity) {
    synchronized(appCompatActivity) {
        val w = appCompatActivity.window
        val decorView = w.decorView
        // Show Status Bar.
        val uiOptions = View.SYSTEM_UI_FLAG_VISIBLE
        decorView.systemUiVisibility = uiOptions

    }
}

fun hideStatusBar(appCompatActivity: AppCompatActivity) {
    synchronized(appCompatActivity) {
        val w = appCompatActivity.window
        val decorView = w.decorView
        // Hide Status Bar.
        val uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN
        decorView.systemUiVisibility = uiOptions
    }
}