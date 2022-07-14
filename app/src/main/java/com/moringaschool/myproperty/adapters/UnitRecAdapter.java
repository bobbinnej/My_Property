package com.moringaschool.myproperty.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.moringaschool.myproperty.R;
import com.moringaschool.myproperty.api.ApiCalls;
import com.moringaschool.myproperty.api.RetrofitClient;
import com.moringaschool.myproperty.models.Tenant;
import com.moringaschool.myproperty.models.Unit;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UnitRecAdapter extends RecyclerView.Adapter<UnitRecAdapter.myHolder> {
    List<Unit> allUnits;
    Context cont;

    public UnitRecAdapter(List<Unit> allUnits, Context cont) {
        this.allUnits = allUnits;
        this.cont = cont;
    }

    @NonNull
    @Override
    public myHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View v = LayoutInflater.from(cont).inflate(R.layout.activity_unit, parent, false);
        return new myHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull myHolder holder, int position) {
        holder.setData(allUnits.get(position));

    }

    @Override
    public int getItemCount() {

        return allUnits.size();
    }

    public class myHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView name, tenantName1, tenantPhone2, unitRooms;
        ImageView add, remove;
        BottomSheetDialog dialog;
        Unit unit;
        Call<Tenant> call;
        ApiCalls calls;


        public myHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.quest_type);
            tenantName1 = itemView.findViewById(R.id.quest_ans);
            tenantPhone2 = itemView.findViewById(R.id.quest_mode);
            unitRooms = itemView.findViewById(R.id.location);
            remove = itemView.findViewById(R.id.remove);
            add = itemView.findViewById(R.id.add);


            dialog = new BottomSheetDialog(cont);
            createDialog();
            add.setOnClickListener(this);
            remove.setOnClickListener(this);
            dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        }

        private void createDialog() {
            View v = LayoutInflater.from(cont).inflate(R.layout.add_tenant, null, false);


            calls = RetrofitClient.getClient();

            TextInputLayout name = v.findViewById(R.id.tenantUsername);
            TextInputLayout phone = v.findViewById(R.id.editTextPhone);
            TextInputLayout email = v.findViewById(R.id.editTextEmail);
            TextInputLayout id = v.findViewById(R.id.tenantId);
            MaterialButton btn = v.findViewById(R.id.addTenant);

            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String tenantName = name.getEditText().getText().toString().trim();
                    String tenantPhone = phone.getEditText().getText().toString().trim();
                    String tenantEmail = email.getEditText().getText().toString().trim();
                    String tenantId = id.getEditText().getText().toString().trim();

                    Tenant tenant = new Tenant(tenantName, tenantEmail,tenantPhone,tenantId,unit.getProperty_name(),unit.getUnit_name());
                    call = calls.addTenant(tenant);
                    call.clone().enqueue(new Callback<Tenant>() {
                        @Override
                        public void onResponse(Call<Tenant> call, Response<Tenant> response) {
                            if (response.isSuccessful()){
                                tenantName1.setText(tenantName);
                                tenantPhone2.setText(tenantPhone);
                                Toast.makeText(cont, "Tenant successfully onboarded", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        }

                        @Override
                        public void onFailure(Call<Tenant> call, Throwable t) {
                            String error = t.getMessage();
                            Toast.makeText(cont, error, Toast.LENGTH_SHORT).show();

                        }
                    });

                }
            });

            dialog.setContentView(v);
        }

        public void setData(Unit unit){
            name.setText(unit.getUnit_name());
            tenantName1.setText("Vacant");
            unitRooms.setText("This unit contains "+unit.getUnit_rooms() + " rooms");
            this.unit = unit;
        }

        @Override
        public void onClick(View v) {
            if (v == add){
                dialog.show();
            }
        }
    }
}
