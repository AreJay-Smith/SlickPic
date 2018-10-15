package com.sweetbytesdev.slickpiclib.Interfaces

import android.view.View
import com.sweetbytesdev.slickpiclib.Models.Img

interface OnSelectionListener {

    abstract fun OnClick(Img: Img, view: View, position: Int)

    abstract fun OnLongClick(img: Img, view: View, position: Int)
}