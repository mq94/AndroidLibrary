package fr.valeo.proto.mq.testlibrary;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import fr.valeo.proto.mq.ble_library.Test;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Test test = new Test();
        test.toast_test(getApplicationContext());
    }
}
