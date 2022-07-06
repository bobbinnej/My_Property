package com.moringaschool.myproperty.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.moringaschool.myproperty.R;
import com.moringaschool.myproperty.databinding.ActivityPropertiesBinding;
import com.moringaschool.myproperty.ui.adapters.PropertyRecAdapter;
import com.moringaschool.myproperty.ui.api.ApiCalls;
import com.moringaschool.myproperty.ui.api.RetrofitClient;
import com.moringaschool.myproperty.ui.models.Property;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PropertiesActivity extends AppCompatActivity {
    ActivityPropertiesBinding properBind;
    List<Property> allProperties;
    PropertyRecAdapter adapter;
    Call<List<Property>> call1;
    String managerName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        properBind = ActivityPropertiesBinding.inflate(getLayoutInflater());
        setContentView(properBind.getRoot());

        allProperties = new ArrayList<>();
        ApiCalls calls = RetrofitClient.getClient();
//        call1 = calls.getProperties();

        Intent intent = getIntent();
        managerName = intent.getStringExtra("managerName");
        call1 = calls.getManagerProperties("charles");

        call1.enqueue(new Callback<List<Property>>() {
            @Override
            public void onResponse(Call<List<Property>> call, Response<List<Property>> response) {
                allProperties = response.body();
                Toast.makeText(PropertiesActivity.this, managerName, Toast.LENGTH_SHORT).show();
                adapter = new PropertyRecAdapter(allProperties, PropertiesActivity.this);
                properBind.recView.setAdapter(adapter);
                properBind.recView.setLayoutManager(new LinearLayoutManager(PropertiesActivity.this));
                properBind.recView.setHasFixedSize(true);
            }

            @Override
            public void onFailure(Call<List<Property>> call, Throwable t) {
                String error = t.getMessage();
                Toast.makeText(PropertiesActivity.this, error, Toast.LENGTH_SHORT).show();
            }
        });








    }
}