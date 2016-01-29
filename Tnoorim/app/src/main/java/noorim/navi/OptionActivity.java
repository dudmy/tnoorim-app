package noorim.navi;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;

import noorim.tnoorim.R;

/**
 * Created by YuJin on 2015-03-18.
 *
 * 사용자 설정 액티비티
 */
public class OptionActivity extends ActionBarActivity {

    private com.rey.material.widget.Switch swOption1, swOption2;

    BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navi_option);

        swOption1 = (com.rey.material.widget.Switch)findViewById(R.id.sw_option1);
        swOption2 = (com.rey.material.widget.Switch)findViewById(R.id.sw_option2);

        SharedPreferences prefs = getSharedPreferences("Key", 0);

        if(prefs.getBoolean("option1", false)){
            swOption1.setChecked(true);
        } else {
            swOption1.setChecked(false);
        }

        swOption1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences prefs = getSharedPreferences("Key", 0);
                SharedPreferences.Editor editor = prefs.edit();

                if(swOption1.isChecked()) {
                    swOption1.setChecked(true);
                    editor.putBoolean("option1", true);
                    editor.commit();

                    if (!bluetoothAdapter.isEnabled()) {
                        Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                        startActivity(enableBtIntent);
                    }
                }
                else if (!swOption1.isChecked()) {
                    swOption1.setChecked(false);
                    editor.putBoolean("option1", false);
                    editor.commit();
                }


            }
        });

        swOption2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences prefs = getSharedPreferences("PrefName", MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();

                if(swOption2.isChecked()) {
                    editor.putBoolean("is_option2", true);
                    editor.commit();
                }
                else {
                    editor.putBoolean("is_option2", false);
                    editor.commit();
                }
            }
        });
    }

}
