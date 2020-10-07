package fr.valeo.proto.mq.ble_library;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProviders;

import butterknife.BindView;
import butterknife.OnClick;


public class BLE_lib
{
    public static final String TAG = "testapp";
    private static final int REQUEST_ACCESS_COARSE_LOCATION = 1022; // random number
    private ScannerViewModel mScannerViewModel;
    private BlinkyViewModel viewModel;

    private int NB_DATA_OUT = 5;
    private int[] buffer_out = new int[NB_DATA_OUT];
    private int[] buffer_in;

    private String ble_name;
    private int nb_data;

    public BLE_lib(String mble_name, int mnb_data)
    {
        ble_name = mble_name;
        nb_data = mnb_data;
    }
}
