package com.example.pruebasplash;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class Menu extends AppCompatActivity {
    ListView menuList;
    int labels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        menuList = (ListView) findViewById(android.R.id.list);
        ArrayAdapter<String> adapt = new ArrayAdapter<String>(this, R.layout.menu_item, getResources().getStringArray(R.array.menu));
        menuList.setAdapter(adapt);

        menuList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View itemClicked, int position, long id) {
                TextView textView = (TextView) itemClicked;
                String strText = textView.getText().toString();

                if (strText.equalsIgnoreCase(getResources().getStringArray(R.array.menu)[0])) {
                // Launch the Game Activity
                    startActivity(new Intent(Menu.this, _2048_Pantalla.class));
                } else if (strText.equalsIgnoreCase(getResources().getStringArray(R.array.menu)[1])) {
                // Launch the Help Activity
                    startActivity(new Intent(Menu.this, _Peg_Pantalla.class));
                } else if (strText.equalsIgnoreCase(getResources().getStringArray(R.array.menu)[2])) {
                // Launch the Settings Activity
                    startActivity(new Intent(Menu.this, _2048_Pantalla.class));
                } else if (strText.equalsIgnoreCase(getResources().getStringArray(R.array.menu)[3])) {
                    // Launch the Scores Activity
                    startActivity(new Intent(Menu.this, ModificacionCuenta.class));
                }
            }
        });
    }
}