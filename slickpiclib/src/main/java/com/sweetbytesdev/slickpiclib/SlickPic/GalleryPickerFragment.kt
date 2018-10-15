package com.sweetbytesdev.slickpiclib.SlickPic


import android.arch.lifecycle.ViewModelProviders
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.sweetbytesdev.slickpiclib.Adapters.GalleryPickerAdapter
import com.sweetbytesdev.slickpiclib.Interfaces.OnSelectionListener
import com.sweetbytesdev.slickpiclib.Models.Img
import com.sweetbytesdev.slickpiclib.R
import com.sweetbytesdev.slickpiclib.Utility.Utility
import java.util.*

/**
 * A simple [Fragment] subclass.
 *
 */
class GalleryPickerFragment : Fragment() {

    lateinit var mVm: SlickPicViewModel
    lateinit var mRecyclerView: RecyclerView
    lateinit var mAdapter: GalleryPickerAdapter

    companion object {
        fun getInstance() = GalleryPickerFragment()

        var TOPBAR_HEIGHT: Float = 0f
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_gallery_picker, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mVm = ViewModelProviders.of(activity!!).get(SlickPicViewModel::class.java)

        mRecyclerView = view.findViewById(R.id.recyclerView)
        mAdapter = GalleryPickerAdapter(activity!!)
        mAdapter.addOnSelectionListener(onSelectionListener)
        var gridLayoutManager = GridLayoutManager(activity, GalleryPickerAdapter.SPAN_COUNT)
        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if (mAdapter.getItemViewType(position) == GalleryPickerAdapter.HEADER) {
                    GalleryPickerAdapter.SPAN_COUNT
                } else 1
            }
        }
        mRecyclerView.layoutManager = gridLayoutManager
        mRecyclerView.adapter = mAdapter
        getImagesFromDeviceGallery()

        TOPBAR_HEIGHT = Utility.convertDpToPixel(56f, activity!!)
    }

    fun getImagesFromDeviceGallery() {
        val cursor = Utility.getCursor(activity!!)
        val INSTANTLIST = ArrayList<Img>()
        var header = ""
        var limit = 100
        if (cursor?.count!! < 100) {
            limit = cursor.count
        }
        val date = cursor.getColumnIndex(MediaStore.Images.Media.DATE_TAKEN)
        val data = cursor.getColumnIndex(MediaStore.Images.Media.DATA)
        val contentUrl = cursor.getColumnIndex(MediaStore.Images.Media._ID)
        var calendar: Calendar

        try {
            for (i in 0 until limit) {
                cursor.moveToNext()
                val path = Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "" + cursor.getInt(contentUrl))
                calendar = Calendar.getInstance()
                calendar.timeInMillis = cursor.getLong(date)
                val dateDifference = Utility.getDateDifference(activity!!, calendar)
                if (!header.equals("" + dateDifference, ignoreCase = true)) {
                    header = "" + dateDifference
                    INSTANTLIST.add(Img("" + dateDifference, "", "", false, "", -1))
                }
                INSTANTLIST.add(Img("" + header, "" + path, cursor.getString(data), false, "", -1))
            }
        } finally {
            cursor.close()
        }

        mAdapter.updateImageList(INSTANTLIST)
    }

    private val onSelectionListener = object : OnSelectionListener {
        override fun OnClick(img: Img, view: View, position: Int) {
            Toast.makeText(activity, "short click", Toast.LENGTH_SHORT).show()
            if (mVm.mSelectionList.contains(img)) {
                mVm.mSelectionList.remove(img)
                mAdapter.select(false, position)
            } else {
                mVm.mSelectionList.add(img)
                mAdapter.select(true, position)
            }
        }

        override fun OnLongClick(img: Img, view: View, position: Int) {
            Toast.makeText(activity, "long click", Toast.LENGTH_SHORT).show()
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        Toast.makeText(activity, "Destroying Gallery frag", Toast.LENGTH_SHORT).show()
    }
}
