package com.moringaschool.myproperty.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.moringaschool.myproperty.R;
import com.moringaschool.myproperty.adapters.DefectRecAdapter;
import com.moringaschool.myproperty.databinding.ActivityPropertiesBinding;
import com.moringaschool.myproperty.adapters.PropertyRecAdapter;
import com.moringaschool.myproperty.api.ApiCalls;
import com.moringaschool.myproperty.api.RetrofitClient;
import com.moringaschool.myproperty.fragments.PropertyDialogFragment;
import com.moringaschool.myproperty.fragments.TenantDialogFragment;
import com.moringaschool.myproperty.models.Constants;
import com.moringaschool.myproperty.models.Defect;
import com.moringaschool.myproperty.models.Property;
import com.moringaschool.myproperty.models.PropertyManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PropertiesActivity extends AppCompatActivity implements View.OnClickListener {
    ActivityPropertiesBinding properBind;
    List<Property> allProperties;
    PropertyRecAdapter adapter;
    Call<List<Property>> call1;
    Call<Property> call2;
    SharedPreferences pref;
    DefectRecAdapter adp;
    DatabaseReference ref;
    List<Defect> allDefects;
    PropertyManager manager;
    BottomSheetDialog dialog;
    ApiCalls calls;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        properBind = ActivityPropertiesBinding.inflate(getLayoutInflater());
        setContentView(properBind.getRoot());


        ref = FirebaseDatabase.getInstance().getReference();
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        dialog = new BottomSheetDialog(this);
        createDialog();

        properBind.managerName.setText("Hey, "+ pref.getString(Constants.NAME, "Charles"));


//        ref.child(Constants.PROPERTY_MANAGERS).child(pref.getString(Constants.EMAIL,"")).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                manager = snapshot.getValue(PropertyManager.class);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError t) {
//                String error = t.getMessage();
//                Toast.makeText(PropertiesActivity.this, error, Toast.LENGTH_SHORT).show();
//            }
//        });


        properBind.add.setOnClickListener(this);
        properBind.add2.setOnClickListener(this);
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

//        manager = (PropertyManager) getIntent().getSerializableExtra("manager");


        allProperties = new ArrayList<>();
        allDefects = new ArrayList<>();
        calls = RetrofitClient.getClient();

        call1 = calls.getManagerProperties(pref.getString(Constants.NAME, "Charles"));



        ref.child("defects").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot != null){
                    for (DataSnapshot singleDefect: snapshot.getChildren()){
                        Defect defect = singleDefect.getValue(Defect.class);
                        allDefects.add(defect);
                    }
                }

                adp = new DefectRecAdapter(allDefects, PropertiesActivity.this);
                properBind.recView.setAdapter(adp);
                properBind.recView.setLayoutManager(new LinearLayoutManager(PropertiesActivity.this));
                properBind.recView.setHasFixedSize(true);
                adp.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError t) {
                String error = t.getMessage();
                Toast.makeText(PropertiesActivity.this, error, Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void createDialog() {
        View v = getLayoutInflater().inflate(R.layout.add_property_dialog, null, false);
        TextInputLayout name = v.findViewById(R.id.name);
        TextInputLayout location = v.findViewById(R.id.location);
        MaterialButton submit = v.findViewById(R.id.submit);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String propertyName = name.getEditText().getText().toString().trim();
                String propertyLocation = location.getEditText().getText().toString().trim();

                Property property = new Property(propertyName, pref.getString(Constants.NAME, ""), propertyLocation);
                call2 = calls.addProperty(property);

                call2.clone().enqueue(new Callback<Property>() {
                    @Override
                    public void onResponse(Call<Property> call, Response<Property> response) {
                        if (response.isSuccessful()){
                            Toast.makeText(PropertiesActivity.this, "Property successfully added", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    }

                    @Override
                    public void onFailure(Call<Property> call, Throwable t) {
                        String error = t.getMessage();
                        Toast.makeText(PropertiesActivity.this, error, Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });


        dialog.setContentView(v);

    }

    @Override
    public void onClick(View v) {
        if (v == properBind.add){
//            FragmentManager fm = getSupportFragmentManager();
//            PropertyDialogFragment fragment = new PropertyDialogFragment();
//
//            fragment.show(fm, "Property Dialog");

            dialog.show();

        }else
        if (v == properBind.add2){
            Intent intent = new Intent(PropertiesActivity.this, DefectPostActivity.class);
            startActivity(intent);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        call1.clone().enqueue(new Callback<List<Property>>() {
            @Override
            public void onResponse(Call<List<Property>> call, Response<List<Property>> response) {
                allProperties = response.body();
                adapter = new PropertyRecAdapter(allProperties, PropertiesActivity.this);
                properBind.myRec.setAdapter(adapter);
                properBind.myRec.setLayoutManager(new LinearLayoutManager(PropertiesActivity.this, RecyclerView.HORIZONTAL, false));
                properBind.myRec.setHasFixedSize(true);
            }

            @Override
            public void onFailure(Call<List<Property>> call, Throwable t) {
                String error = t.getMessage();
                Toast.makeText(PropertiesActivity.this, error, Toast.LENGTH_SHORT).show();
            }
        });
    }
}