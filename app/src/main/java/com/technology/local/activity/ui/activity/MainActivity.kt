package com.technology.local.activity.ui.activity

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import com.technology.local.domain.provider.ble.BLEProvider

class MainActivity : AppCompatActivity() {

    override fun onResume() {
        super.onResume()
        checkGeoPermission()
        turnOnBLERequest()
    }

    private fun checkGeoPermission() {
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
            || checkSelfPermission(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
            != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_BACKGROUND_LOCATION
                ),
                GEO_PERMISSION_REQUEST_CODE
            )
        }
    }

    private fun turnOnBLERequest() {
        val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
        startActivityForResult(enableBtIntent, GEO_PERMISSION_REQUEST_CODE)
    }

    override fun startActivityForResult(intent: Intent?, requestCode: Int) {
        BLEProvider().initBLE()
        super.startActivityForResult(intent, requestCode)
    }

    private companion object {
        const val GEO_PERMISSION_REQUEST_CODE = 1
    }
}