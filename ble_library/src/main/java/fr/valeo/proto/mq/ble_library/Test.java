package fr.valeo.proto.mq.ble_library;

import android.content.Context;
import android.widget.Toast;

public class Test
{
    private String str = "bonjour";

    public void toast_test(Context c) {
        Toast.makeText(c, str, Toast.LENGTH_SHORT).show();
    }
}
