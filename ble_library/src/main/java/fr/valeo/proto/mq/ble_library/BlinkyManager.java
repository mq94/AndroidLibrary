/*
 * Copyright (c) 2015, Nordic Semiconductor
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 *
 * 3. Neither the name of the copyright holder nor the names of its contributors may be used to endorse or promote products derived from this
 * software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE
 * USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package fr.valeo.proto.mq.ble_library;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.Context;

import androidx.annotation.NonNull;

import java.util.Deque;
import java.util.LinkedList;
import java.util.UUID;

import no.nordicsemi.android.ble.BleManager;
import no.nordicsemi.android.ble.Request;
import no.nordicsemi.android.log.LogContract;

public class BlinkyManager extends BleManager<BlinkyManagerCallbacks> {
	/** Nordic Blinky Service UUID. */
//	public final static UUID LBS_UUID_SERVICE = UUID.fromString("00001523-1212-efde-1523-785feabcd123");
	public final static UUID LBS_UUID_SERVICE = UUID.fromString("a7bb1500-eef2-4a8e-80d4-13a83c8cf46f");
//	 Button char
//	private final static UUID LBS_UUID_BUTTON_CHAR = UUID.fromString("00001524-1212-efde-1523-785feabcd123");
	private final static UUID LBS_UUID_BUTTON_CHAR = UUID.fromString("a7bb1501-eef2-4a8e-80d4-13a83c8cf46f");   // Recevoir BLE
	/** LED characteristic UUID. */
//	private final static UUID LBS_UUID_LED_CHAR = UUID.fromString("00001525-1212-efde-1523-785feabcd123");
	private final static UUID LBS_UUID_LED_CHAR = UUID.fromString("a7bb1501-eef2-4a8e-80d4-13a83c8cf46f");    // Emettre BLE
	/** LED characteristic UUID. */
	//	private final static UUID LBS_UUID_LED_CHAR = UUID.fromString("00001525-1212-efde-1523-785feabcd123");
	private final static UUID LBS_UUID_SECU_CHAR = UUID.fromString("a7bb1503-eef2-4a8e-80d4-13a83c8cf46f");    // Securité BLE
	/** LED characteristic UUID. */
//	private final static UUID LBS_UUID_LED_CHAR = UUID.fromString("00001525-1212-efde-1523-785feabcd123");
	private final static UUID LBS_UUID_SECU_WRITE_CHAR = UUID.fromString("a7bb1503-eef2-4a8e-80d4-13a83c8cf46f");    // Securité BLE Write result

	private BluetoothGattCharacteristic mBufferCharacteristic, mButtonCharacteristic, mSecuCharacteristic, mSecuWriteCharacteristic;

	private int[] __private_key = new int[16];
	private int[] __public_key = new int[16];

	public BlinkyManager(final Context context) {
		super(context);
	}

	@NonNull
	@Override
	protected BleManagerGattCallback getGattCallback() {
		return mGattCallback;
	}

	@Override
	protected boolean shouldAutoConnect() {
		// If you want to connect to the device using autoConnect flag = true, return true here.
		// Read the documentation of this method.
		return super.shouldAutoConnect();
	}

	/**
	 * BluetoothGatt callbacks for connection/disconnection, service discovery, receiving indication, etc
	 */
	private final BleManagerGattCallback mGattCallback = new BleManagerGattCallback() {

		@Override
		protected Deque<Request> initGatt(final BluetoothGatt gatt) {
			final LinkedList<Request> requests = new LinkedList<>();
			requests.push(Request.newReadRequest(mBufferCharacteristic));
			requests.push(Request.newReadRequest(mButtonCharacteristic));
			requests.push(Request.newEnableNotificationsRequest(mButtonCharacteristic));
			requests.push(Request.newReadRequest(mSecuCharacteristic));
			requests.push(Request.newEnableNotificationsRequest(mSecuCharacteristic));
			requests.push(Request.newReadRequest(mSecuWriteCharacteristic));

			__private_key[0] = 6627;
			__private_key[1] = 6855;
			__private_key[2] = 3424;
			__private_key[3] = 7121;
			__private_key[4] = 6054;
			__private_key[5] = 9187;
			__private_key[6] = 2013;
			__private_key[7] = 4725;
			__private_key[8] = 625;
			__private_key[9] = 132;
			__private_key[10] = 9827;
			__private_key[11] = 5467;
			__private_key[12] = 3827;
			__private_key[13] = 8992;
			__private_key[14] = 65;
			__private_key[15] = 1087;

			return requests;
		}

		@Override
		public boolean isRequiredServiceSupported(final BluetoothGatt gatt) {
			final BluetoothGattService service = gatt.getService(LBS_UUID_SERVICE);
			if (service != null) {
				mBufferCharacteristic = service.getCharacteristic(LBS_UUID_LED_CHAR);
				mButtonCharacteristic = service.getCharacteristic(LBS_UUID_BUTTON_CHAR);
				mSecuCharacteristic = service.getCharacteristic(LBS_UUID_SECU_CHAR);
				mSecuWriteCharacteristic = service.getCharacteristic(LBS_UUID_SECU_WRITE_CHAR);
			}

			boolean writeRequest = false;
			if (mBufferCharacteristic != null) {
				final int rxProperties = mBufferCharacteristic.getProperties();
				writeRequest = (rxProperties & BluetoothGattCharacteristic.PROPERTY_WRITE) > 0;
			}

			boolean writeRequestSecu = false;
			if (mSecuWriteCharacteristic != null) {
				final int rxProperties = mSecuWriteCharacteristic.getProperties();
				writeRequestSecu = (rxProperties & BluetoothGattCharacteristic.PROPERTY_WRITE) > 0;
			}

			return mButtonCharacteristic != null && mBufferCharacteristic != null && writeRequest && writeRequestSecu && mSecuCharacteristic != null && mSecuWriteCharacteristic != null;
		}

		@Override
		protected void onDeviceDisconnected() {
			mButtonCharacteristic = null;
			mBufferCharacteristic = null;
			mSecuCharacteristic = null;
			mSecuWriteCharacteristic = null;
		}

		@Override
		protected void onCharacteristicRead(final BluetoothGatt gatt, final BluetoothGattCharacteristic characteristic) {
			final byte[] data = characteristic.getValue();

			if (characteristic == mBufferCharacteristic) {
			} else {
				mCallbacks.onDataReceived(data);
			}

			if (characteristic == mSecuWriteCharacteristic) {
			} else {
				mCallbacks.onDataReceived(data);
			}
		}

		@Override
		public void onCharacteristicWrite(final BluetoothGatt gatt, final BluetoothGattCharacteristic characteristic) {
			// This method is only called for LED characteristic
			final int data = characteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT8, 0);
			final byte ledOn = (byte) data;
			log(LogContract.Log.Level.APPLICATION, "LED " + ledOn);
			mCallbacks.onDataSent(ledOn);
		}

		@Override
		public void onCharacteristicNotified(final BluetoothGatt gatt, final BluetoothGattCharacteristic characteristic) {
			// This method is only called for Button characteristic
			final byte[] data = characteristic.getValue();

			if (characteristic == mSecuWriteCharacteristic)
            {
//                for (int i=0 ; i<data.length ; i++)
//                    Log.d("lala", "NOTIF data " + data[i]);
//                Log.d("lala", "----------");

                if (data[1] == 16)
                {
                    for (int i=0 ; i<16 ; i++) {
                        __public_key[i] = data[i+2];
                    }

//                    for (int i=0 ; i<__public_key.length ; i++)
//                        Log.d("lala", "NOTIF Key " + Integer.toHexString(__public_key[i]));
//                    Log.d("lala", "----------");

                    int result = fu_public_key_decode();
//                    Log.d("lala", "----RESULLTTT ------ " + Integer.toHexString(result));

                    send_secu(result);
                }
            }
			else
            {
                mCallbacks.onDataReceived(data);
            }
		}
	};

	private int fu_public_key_decode()
	{
        int result = 0;
        for (int i = 0 ; i < 16 ; i++) {
        	result += ((__public_key[i] & 0xFF) * __private_key[i]);
        }
        return result;
	}

	private void send_secu(int result)
	{
		// Are we connected?
		if (mSecuWriteCharacteristic == null)
			return;

		int nb_data = 4;
		int total_data = nb_data + 2;
		byte id_scenario = 0x01;   // 0x30 : buffer // 0x40 : send 1 scenario
		final byte[] command = new byte[total_data];

		// Fill buffer with header
		command[0] = id_scenario;
		command[1] = (byte) nb_data;
		command[2] = (byte) (result & 0xFF);
		command[3] = (byte) ((result >> 8) & 0xFF);
		command[4] = (byte) ((result >> 16) & 0xFF);
		command[5] = (byte) ((result >> 24) & 0xFF);

		// Log
//		for (int i=0 ; i<total_data ; i++) {
//			Log.d("lala", "SEND TO BLE -- " + i + " " + Integer.toHexString(command[i]) + " " + command[i]);
//		}
		// Send
		writeCharacteristic(mSecuWriteCharacteristic, command);
	}

	public void send(int nb_data, int[] buffer)
	{
		// Are we connected?
		if (mBufferCharacteristic == null)
			return;

		int total_data = nb_data + 2;
		byte id_scenario = 0x30;   // 0x30 : buffer // 0x40 : send 1 scenario
		final byte[] command = new byte[total_data];

		// Fill buffer with header
		command[0] = id_scenario;
		command[1] = (byte) nb_data;
		for (int i=2, j=0 ; i<total_data ; i++, j++)
		{
			command[i] = (byte) buffer[j];
		}

		// Log
//		Log.d(MainActivity.TAG, "SEND TO BLE -- total data " + total_data);
//		for (int i=0 ; i<total_data ; i++) {
//			Log.d(MainActivity.TAG, "SEND TO BLE -- " + i + " " + command[i]);
//		}

		// Send
		writeCharacteristic(mBufferCharacteristic, command);
	}
}
