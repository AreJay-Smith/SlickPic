package com.sweetbytesdev.slickpiclib.Utility

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import com.sweetbytesdev.slickpiclib.Interfaces.WorkFinish
import java.util.ArrayList

object PermUtil {

    val REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 1990

    @RequiresApi(api = Build.VERSION_CODES.M)
    private fun addPermission(permissionsList: MutableList<String>, permission: String, ac: Activity): Boolean {
        if (ac.checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(permission)
            // Check for Rationale Option
            return ac.shouldShowRequestPermissionRationale(permission)
        }
        return true
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    fun checkForCamara_WritePermissions(activity: Activity, workFinish: WorkFinish) {
        val permissionsNeeded = ArrayList<String>()
        val permissionsList = ArrayList<String>()
        if (!addPermission(permissionsList, Manifest.permission.CAMERA, activity))
            permissionsNeeded.add("CAMERA")
        if (!addPermission(permissionsList, Manifest.permission.WRITE_EXTERNAL_STORAGE, activity))
            permissionsNeeded.add("WRITE_EXTERNAL_STORAGE")
        if (permissionsList.size > 0) {
            val array = arrayOfNulls<String>(permissionsList.size)
            activity.requestPermissions(permissionsList.toArray<String>(array),
                    REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS)
        } else {
            workFinish.onWorkFinish(true)
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    fun checkForCamara_WritePermissions(fragment: Fragment, workFinish: WorkFinish) {
        val permissionsNeeded = ArrayList<String>()
        val permissionsList = ArrayList<String>()
        if (!addPermission(permissionsList, Manifest.permission.CAMERA, fragment.activity!!))
            permissionsNeeded.add("CAMERA")
        if (!addPermission(permissionsList, Manifest.permission.WRITE_EXTERNAL_STORAGE, fragment.activity!!))
            permissionsNeeded.add("WRITE_EXTERNAL_STORAGE")
        if (permissionsList.size > 0) {
            val array = arrayOfNulls<String>(permissionsList.size)
            fragment.requestPermissions(permissionsList.toArray(array),
                    REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS)
        } else {
            workFinish.onWorkFinish(true)
        }
    }
}