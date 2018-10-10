package com.sweetbytesdev.slickpiclib.SlickPic


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.sweetbytesdev.slickpiclib.R
import com.camerakit.CameraKitView
import android.support.annotation.NonNull
import android.widget.Button


/**
 * A simple [Fragment] subclass.
 *
 */
class CameraPreviewFragment : Fragment() {

    private var cameraKitView: CameraKitView? = null

    companion object {

        fun getInstance() = CameraPreviewFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_camera_preview, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        cameraKitView = view.findViewById<CameraKitView>(R.id.camera)
        view.findViewById<Button>(R.id.test_button).setOnClickListener {

        }
    }

    override fun onResume() {
        super.onResume()
        cameraKitView?.onResume()
    }

    override fun onPause() {
        super.onPause()
        cameraKitView?.onPause()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        cameraKitView?.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}
