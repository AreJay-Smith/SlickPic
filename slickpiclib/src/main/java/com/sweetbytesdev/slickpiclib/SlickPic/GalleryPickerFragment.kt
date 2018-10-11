package com.sweetbytesdev.slickpiclib.SlickPic


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

import com.sweetbytesdev.slickpiclib.R

/**
 * A simple [Fragment] subclass.
 *
 */
class GalleryPickerFragment : Fragment() {

    companion object {
        fun getInstance() = GalleryPickerFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_gallery_picker, container, false)
    }

    override fun onDestroy() {
        super.onDestroy()
        Toast.makeText(activity, "Destroying Gallery frag", Toast.LENGTH_SHORT).show()
    }
}
