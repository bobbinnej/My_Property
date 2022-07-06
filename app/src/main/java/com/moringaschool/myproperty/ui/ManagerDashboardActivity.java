package com.moringaschool.myproperty.ui;

import androidx.appcompat.app.AppCompatActivity;
import com.moringaschool.myproperty.R;
import com.moringaschool.myproperty.databinding.ActivityMainBinding;
import com.moringaschool.myproperty.databinding.ActivityManagerDashboardBinding;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class ManagerDashboardActivity extends AppCompatActivity {
    ActivityManagerDashboardBinding mainBind;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBind = ActivityManagerDashboardBinding.inflate(getLayoutInflater());
        setContentView(mainBind.getRoot());

        mainBind.btnMaintenance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ManagerDashboardActivity.this, DefectPostActivity.class);
                startActivity(intent);
            }
        });

        mainBind.btnGetDefects.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ManagerDashboardActivity.this, DefectActivity.class);
                startActivity(intent);
            }
        });
    }
}