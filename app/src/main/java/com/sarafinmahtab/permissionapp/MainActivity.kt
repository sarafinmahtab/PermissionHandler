package com.sarafinmahtab.permissionapp

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.sarafinmahtab.permissionmanager.PermissionManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    companion object {
        private const val REQUEST_PERMISSION = 102
    }

    private var useDialogCheck = false

    private val permissionMap = HashMap<String, Array<String>>()

    private lateinit var permissionManager: PermissionManager
    private lateinit var selectedPermissions: Array<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        permissionManager = PermissionManager(this)

        permissionMap["CAMERA,STORAGE,CONTACT,LOCATION"] = arrayOf(
            android.Manifest.permission.CAMERA,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.READ_CONTACTS,
            android.Manifest.permission.ACCESS_FINE_LOCATION
        )
        permissionMap["CAMERA"] = arrayOf(
            android.Manifest.permission.CAMERA
        )
        permissionMap["CAMERA,LOCATION"] = arrayOf(
            android.Manifest.permission.CAMERA,
            android.Manifest.permission.ACCESS_FINE_LOCATION
        )
        permissionMap["STORAGE,CONTACT"] = arrayOf(
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.READ_CONTACTS
        )

        useDialog.isChecked = useDialogCheck

        useDialog.setOnCheckedChangeListener { _, b ->
            useDialogCheck = b
        }

        permissionSpinner.adapter = spinnerAdapter(permissionMap.keys.toList())
        permissionSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                val permission = permissionMap.keys.toList()[p2]

                permissionMap[permission]?.let {
                    selectedPermissions = it
                }
            }
        }

        permissionClick.setOnClickListener {
            if (useDialogCheck) {
                permissionManager.setPermissionExplanationDialog(
                    title = "Permission Need",
                    message = "Please accept permission"
                )
            }

            permissionManager.checkForPermission(
                REQUEST_PERMISSION,
                selectedPermissions
            )
        }

        permissionCheckClick.setOnClickListener {

            updatePermissionStatus(
                "Camera Permission -> ${permissionManager.hasPermission(android.Manifest.permission.CAMERA)}\n" +
                        "Storage Permission -> ${permissionManager.hasPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)}\n" +
                        "Location Permission -> ${permissionManager.hasPermission(android.Manifest.permission.ACCESS_FINE_LOCATION)}\n" +
                        "Read Contacts Permission -> ${permissionManager.hasPermission(android.Manifest.permission.READ_CONTACTS)}\n"
            )
        }

        permissionCheckClick.performClick()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == REQUEST_PERMISSION) {
            if (permissionManager.allPermissionGranted(grantResults)) {
                updatePermissionStatus("Granted Permissions")
            } else {
                updatePermissionStatus("Cancelled Permissions")
            }
        }
    }

    private fun updatePermissionStatus(status: String) {
        permissionStatus.text = status
    }
}


fun <T> Context.spinnerAdapter(list: List<T>): ArrayAdapter<T> {
    val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, list)
    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
    return adapter
}
