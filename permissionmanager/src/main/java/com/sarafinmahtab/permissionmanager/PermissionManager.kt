package com.sarafinmahtab.permissionmanager

import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import androidx.core.content.ContextCompat.checkSelfPermission

/**
 * Created by mahtab on 10/29/2019.
 */
class PermissionManager(private val activity: Activity) {

    private var message: String? = null
    private var title: String? = null

    @DrawableRes
    private var drawableId: Int? = null

    private var showPermissionDialog = false
    private var permissionDialog: AlertDialog? = null

    private var permissionList: Array<String> = arrayOf()

    private var requestCode = 0

    /**
     * First need to set the permission, other it will throw exceptions
     *
     * @param permissions
     *
     */
    fun setPermissions(permissions: Array<String>) {
        this.permissionList = permissions
    }

    /**
     * Show Permission explanations dialog
     *
     * @param title
     * @param message
     * @param drawableId
     *
     */
    fun setPermissionExplanationDialog(
        title: String,
        message: String,
        @DrawableRes drawableId: Int? = null
    ) {
        this.title = title
        this.message = message
        this.drawableId = drawableId

        showPermissionDialog = true

        val builder = AlertDialog.Builder(activity)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(android.R.string.ok) { dialog, _ ->
                dialog.dismiss()
                requestForPermission()
            }

        if (drawableId != null) {
            builder.setIcon(drawableId)
        }

        permissionDialog = builder.create()
    }

    /**
     * Check and Request permissions
     *
     * @param permissions should not be empty
     * @param requestCode must send request code for each permission check
     */
    fun checkForPermission(requestCode: Int, permissions: Array<String> = permissionList) {

        if (permissions.isNotEmpty()) {
            setPermissions(permissions)
        }

        if (permissionList.isEmpty()) {
            throw RuntimeException("Please setup permissions first. Permissions should not be empty")
        }

        this.requestCode = requestCode

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (showPermissionDialog) {
                ifCheckSelfPermissionNotGranted()
            } else {
                requestForPermission()
            }
        } else {
            requestForPermission()
        }
    }

    /**
     * Request for permission `access private`
     */
    private fun requestForPermission() {

        if (permissionList.isEmpty()) {
            throw RuntimeException("Please setup permissions first. Permissions should not be empty")
        }

        requestPermissions(
            activity, permissionList, requestCode
        )
    }

    private fun ifCheckSelfPermissionNotGranted() {

        if (permissionList.isEmpty()) {
            throw RuntimeException("Please setup permissions first. Permissions should not be empty")
        }

        for (permission in permissionList) {

            if (!hasPermission(permission)) {

                if (shouldShowRequestPermissionRationale(activity, permission)) {
                    /** Request permission will be accepted from dialog */
                    showPermissionExplanationDialog()
                } else {
                    /** First Time No need to show dialog */
                    requestForPermission()
                    break
                }
            }
        }
    }

    /**
     * Check if permission accepted already
     *
     * @param permission
     */
    fun hasPermission(permission: String): Boolean {
        return checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED
    }

    /**
     * Check if all permissions accepted already
     *
     * @param permissions
     */
    fun hasPermission(permissions: Array<String>): Boolean {
        for (permission in permissions) {
            if (!hasPermission(permission)) {
                return false
            }
        }
        return true
    }

    /**
     * Show permission dialog
     */
    private fun showPermissionExplanationDialog() {
        if (permissionDialog?.isShowing == false) {
            permissionDialog?.show()
        }
    }

    /**
     * Check if all permission are granted
     *
     * @param grantResults
     */
    fun allPermissionGranted(grantResults: IntArray): Boolean {
        if (grantResults.isNotEmpty()) {

            for (grantResult in grantResults) {
                if (grantResult != PackageManager.PERMISSION_GRANTED) {
                    return false
                }
            }
            return true
        }
        return false
    }
}
