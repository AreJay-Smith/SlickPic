package com.sweetbytesdev.slickpiclib.Utility

import android.animation.AnimatorListenerAdapter
import android.app.Activity
import android.content.Context
import android.database.Cursor
import android.os.Build
import android.os.Environment
import android.support.v7.app.AppCompatActivity
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.view.ViewPropertyAnimator
import android.view.WindowManager
import com.sweetbytesdev.slickpiclib.R
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

object Utility {

    var HEIGHT: Int = 0
    var WIDTH:Int = 0

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

    fun showScrollbar(mScrollbar: View, context: Context): ViewPropertyAnimator {
        val transX = context.resources.getDimensionPixelSize(R.dimen.fastscroll_scrollbar_padding_end).toFloat()
        mScrollbar.translationX = transX
        mScrollbar.visibility = View.VISIBLE
        return mScrollbar.animate().translationX(0f).alpha(1f)
                .setDuration(Constants.sScrollbarAnimDuration.toLong())
                .setListener(object : AnimatorListenerAdapter() {

                    // adapter required for new alpha value to stick
                })
    }

    fun cancelAnimation(animator: ViewPropertyAnimator?) {
        animator?.cancel()
    }

    fun getValueInRange(min: Int, max: Int, value: Int): Int {
        val minimum = Math.max(min, value)
        return Math.min(minimum, max)
    }

    fun getScreenSize(activity: Activity) {
        val displayMetrics = DisplayMetrics()
        activity.windowManager.defaultDisplay.getMetrics(displayMetrics)
        HEIGHT = displayMetrics.heightPixels
        WIDTH = displayMetrics.widthPixels
    }

    fun getCursor(context: Context): Cursor? {
        return context.contentResolver.query(Constants.URI, Constants.PROJECTION, null, null, Constants.ORDERBY)
    }

    fun getDateDifference(context: Context, calendar: Calendar): String {
        val d = calendar.time
        val lastMonth = Calendar.getInstance()
        val lastWeek = Calendar.getInstance()
        val recent = Calendar.getInstance()
        lastMonth.add(Calendar.DAY_OF_MONTH, -Calendar.DAY_OF_MONTH)
        lastWeek.add(Calendar.DAY_OF_MONTH, -7)
        recent.add(Calendar.DAY_OF_MONTH, -2)
        return if (calendar.before(lastMonth)) {
            SimpleDateFormat("MMMM").format(d)
        } else if (calendar.after(lastMonth) && calendar.before(lastWeek)) {
            context.resources.getString(R.string.last_month)
        } else if (calendar.after(lastWeek) && calendar.before(recent)) {
            context.resources.getString(R.string.last_week)
        } else {
            context.resources.getString(R.string.recent)
        }
    }

    fun isViewVisible(view: View?): Boolean {
        return view != null && view.visibility == View.VISIBLE
    }

    fun convertDpToPixel(dp: Float, context: Context): Float {
        val resources = context.resources
        val metrics = resources.displayMetrics
        return dp * (metrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
    }

    fun convertPixelsToDp(px: Float, context: Context): Float {
        val resources = context.resources
        val metrics = resources.displayMetrics
        return px / (metrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
    }

    fun writeImage(jpeg: ByteArray): File {
        val dir = File(Environment.getExternalStorageDirectory(), "/DCIM/Camera")
        if (!dir.exists())
            dir.mkdir()
        val photo = File(dir, "IMG_" + SimpleDateFormat("yyyyMMdd_HHmmSS", Locale.ENGLISH).format(Date()) + ".jpg")
        if (photo.exists()) {
            photo.delete()
        }

        try {
            val fos = FileOutputStream(photo.path)
            fos.write(jpeg)
            fos.close()
        } catch (e: Exception) {
            Log.e("PictureDemo", "Exception in photoCallback", e)
        }

        return photo
    }
}