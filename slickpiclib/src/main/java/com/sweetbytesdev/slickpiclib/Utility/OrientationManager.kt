package com.sweetbytesdev.slickpiclib.Utility

import android.content.Context
import android.view.OrientationEventListener
import com.sweetbytesdev.slickpiclib.Interfaces.OnOrientationChangeListener


class OrientationManager : OrientationEventListener {

    enum class ScreenOrientation {
        REVERSED_LANDSCAPE, LANDSCAPE, PORTRAIT, REVERSED_PORTRAIT
    }

    var screenOrientation: ScreenOrientation? = null
    var listener: OnOrientationChangeListener? = null

    constructor(context: Context, rate: Int, listener: OnOrientationChangeListener): super(context, rate) {
        this.listener = listener
    }

    constructor(context: Context, rate: Int): super(context, rate) {

    }

    constructor(context: Context): super(context) {

    }

    override fun onOrientationChanged(orientation: Int) {
        if (orientation == -1) {
            return
        }
        val newOrientation: ScreenOrientation
        if (orientation >= 60 && orientation <= 140) {
            newOrientation = ScreenOrientation.REVERSED_LANDSCAPE
        } else if (orientation >= 140 && orientation <= 220) {
            newOrientation = ScreenOrientation.REVERSED_PORTRAIT
        } else if (orientation >= 220 && orientation <= 300) {
            newOrientation = ScreenOrientation.LANDSCAPE
        } else {
            newOrientation = ScreenOrientation.PORTRAIT
        }
        if (newOrientation != screenOrientation) {
            screenOrientation = newOrientation
            if (listener != null) {
                listener?.onOrientationChangeListener(newOrientation)
            }
        }
    }
}