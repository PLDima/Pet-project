package com.technology.local.domain.provider.ble

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.bluetooth.le.ScanSettings
import android.os.Handler
import android.os.Looper
import android.util.Log

class BLEProvider {

    private val bluetoothLeAdapter = BluetoothAdapter.getDefaultAdapter()
    private var scanning = false
    private val list = mutableListOf<DeviceInfo>()

    fun initBLE() {
        if (!bluetoothLeAdapter.isEnabled) {
            throw IllegalArgumentException()
        }
        scanLeDevice()
    }

    private fun scanLeDevice() {
        if (!scanning) {
            Handler(Looper.getMainLooper()).postDelayed({
                scanning = false
                bluetoothLeAdapter.bluetoothLeScanner.stopScan(leScanCallback)
            }, SCAN_PERIOD)
            scanning = true
            bluetoothLeAdapter.bluetoothLeScanner.startScan(
                null,
                ScanSettings.Builder()
                    .setScanMode(ScanSettings.SCAN_MODE_BALANCED)
                    .setCallbackType(ScanSettings.CALLBACK_TYPE_ALL_MATCHES)
                    .setMatchMode(ScanSettings.MATCH_MODE_AGGRESSIVE)
                    .setNumOfMatches(ScanSettings.MATCH_NUM_ONE_ADVERTISEMENT)
                    .setReportDelay(0L)
                    .build(), leScanCallback
            )
        } else {
            scanning = false
            bluetoothLeAdapter.bluetoothLeScanner.stopScan(leScanCallback)
        }
    }

    private val leScanCallback: ScanCallback = object : ScanCallback() {

        override fun onScanResult(callbackType: Int, result: ScanResult) {
            super.onScanResult(callbackType, result)
            if (result.device.name != null && !checkSame(result.device)) {
                val device = DeviceInfo(result.device.name, result.device.address)
                showBLELog("${device.name} , ${device.address}")
                list.add(device)
            }
        }

        override fun onBatchScanResults(results: MutableList<ScanResult>?) {
            super.onBatchScanResults(results)
        }
    }

    private fun checkSame(BLEDevice: BluetoothDevice): Boolean {
        for (device in list) {
            showBLELog("${device.name} , ${device.address}")
            if (device.name == BLEDevice.name && device.address == BLEDevice.address) {
                return true
            }
        }
        return false
    }

    private fun showBLELog(message: String) {
        Log.e("BLE Logs", message)
    }

    private companion object {
        const val SCAN_PERIOD: Long = 10000
    }
}