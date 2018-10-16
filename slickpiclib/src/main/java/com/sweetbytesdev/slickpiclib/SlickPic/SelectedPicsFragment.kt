package com.sweetbytesdev.slickpiclib.SlickPic


import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.sweetbytesdev.slickpiclib.Adapters.GalleryPickerAdapter
import com.sweetbytesdev.slickpiclib.Adapters.SelectedPicsAdapter
import com.sweetbytesdev.slickpiclib.Interfaces.OnSelectionListener
import com.sweetbytesdev.slickpiclib.Models.Img

import com.sweetbytesdev.slickpiclib.R
import com.sweetbytesdev.slickpiclib.Utility.Utility

class SelectedPicsFragment : Fragment() {

    lateinit var mVm: SlickPicViewModel
    lateinit var mRecyclerView: RecyclerView
    lateinit var mAdapter: SelectedPicsAdapter

    companion object {
        fun getInstance() = SelectedPicsFragment()

        var TOPBAR_HEIGHT: Float = 0f
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_selected_pics, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mVm = ViewModelProviders.of(activity!!).get(SlickPicViewModel::class.java)

        mRecyclerView = view.findViewById(R.id.recyclerView)
        mAdapter = SelectedPicsAdapter(activity!!)
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
        mVm.mSelectionList.observe(activity!!, onUpdatedList)
        mAdapter.updateImageList(mVm.mSelectionList.value!!)
        TOPBAR_HEIGHT = Utility.convertDpToPixel(56f, activity!!)
    }

    var onUpdatedList = Observer<MutableList<Img>> { updatedList ->
        mAdapter.updateImageList(updatedList!!) }

    private val onSelectionListener = object : OnSelectionListener {
        override fun OnClick(img: Img, view: View, position: Int) {
            Toast.makeText(activity, "short click", Toast.LENGTH_SHORT).show()
            if (mVm.mSelectionList.value!!.contains(img)) {
                mVm.mSelectionList.value!!.remove(img)
                mVm.mSelectionList.postValue(mVm.mSelectionList.value!!)
//                mAdapter.select(false, position)
            } else {
                mVm.mSelectionList.value!!.add(img)
                mVm.mSelectionList.postValue(mVm.mSelectionList.value!!)
//                mAdapter.select(true, position)
            }
            mAdapter.notifyDataSetChanged()
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
