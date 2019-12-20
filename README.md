### Permission Manager
-----------

Permission Manager module simply process the given permissions and lets you decide what to do after the permissions are granted or denied.

#### Dependency
-----------

##### Gradle

Include the library in your ``build.gradle``

```
dependencies {
    implementation 'com.sarafinmahtab:permissionmanager:1.0'
}
```

##### maven

```
<dependency>
	<groupId>com.sarafinmahtab</groupId>
	<artifactId>permissionmanager</artifactId>
	<version>1.0</version>
	<type>pom</type>
</dependency>
```

##### ivy

```
<dependency org="com.sarafinmahtab" name="permissionmanager" rev="1.0">
	<artifact name="permissionmanager" ext="pom"></artifact>
</dependency>
```


#### Implementation

##### Initialization

Initialize Permission Manager in your activity
```
var permissionManager = PermissionManager(this)
```

##### Get Permission

You need to send a `requestcode` and your `list of permissions` or `single permission`.

Then PermissionManager will do the rest of the part. And send the response `onRequestPermissionsResult`

```
permissionManager.checkForPermission(
    REQUEST_PERMISSION,
    arrayOf(
            android.Manifest.permission.CAMERA,
            android.Manifest.permission.ACCESS_FINE_LOCATION
        )
)
```

##### Check Permission

Check if your selected permissions is already granted
```
permissionManager.hasPermission(
    arrayOf(
            android.Manifest.permission.CAMERA,
            android.Manifest.permission.ACCESS_FINE_LOCATION
        )
)
```

##### Permission Request Result

Decide what to do with the permission request result

```
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

```

##### Set Custom Dialog

Set Custom Dialog for Permission Request Explanation
```
permissionManager.setPermissionExplanationDialog(
    title = "Permission Need",
    message = "Please accept permission",
    drawableId = null
)
```
