package com.sweetbytesdev.slickpiclib.SlickPic


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.PagerAdapter
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

import com.sweetbytesdev.slickpiclib.R
import java.util.ArrayList

class CameraPickerHostFragment : Fragment() {

    private lateinit var mViewPager: ViewPager
    private lateinit var mTabLayout: TabLayout

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_camera_picker_host, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mTabLayout = view.findViewById<TabLayout>(R.id.tab_layout)
        mViewPager = view.findViewById<ViewPager>(R.id.view_pager)
        activity?.supportFragmentManager
        setUpViewPager()
    }

    private fun setUpViewPager() {
        var adapter = ViewPagerAdapter(fragmentManager!!)
        adapter.addFragment(CameraPreviewFragment.getInstance(), "Camera")
        adapter.addFragment(GalleryPickerFragment.getInstance(), "Gallery")
        mViewPager.adapter = adapter
        mTabLayout.setupWithViewPager(mViewPager)
    }

    class ViewPagerAdapter : FragmentPagerAdapter {

        private val mFragmentList = ArrayList<Fragment>()
        private val mFragmentTitleList = ArrayList<String>()

        constructor(manager: FragmentManager): super(manager) {}

        override fun getItem(position: Int): Fragment {
            return mFragmentList[position]
        }

        override fun getCount(): Int {
            return mFragmentList.size
        }

        fun addFragment(fragment: Fragment, title: String) {
            mFragmentList.add(fragment)
            mFragmentTitleList.add(title)
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return mFragmentTitleList[position]
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        Toast.makeText(activity, "Destroying Host frag", Toast.LENGTH_SHORT).show()
    }
}
