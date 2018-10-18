package com.sweetbytesdev.slickpiclib.Interfaces

import com.sweetbytesdev.slickpiclib.SlickPic.GalleryPickerFragment

interface FastScrollStateChangeListener {
    /**
     * Called when fast scrolling begins
     */
    abstract fun onFastScrollStart(fastScroller: GalleryPickerFragment)

    /**
     * Called when fast scrolling ends
     */
    abstract fun onFastScrollStop(fastScroller: GalleryPickerFragment)
}