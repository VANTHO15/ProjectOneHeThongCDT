package com.nguyenvantho.vanthosmarthome.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nguyenvantho.vanthosmarthome.R;

public class Fragment1 extends Fragment implements CompoundButton.OnCheckedChangeListener {
    TextView txtQuat1,txtCua,txtQuat2,txtNhietDo,txtKhiGa,txtDoAm;
    ToggleButton tgQuat1,tgCua,tgQuat2,tgDenChinh,tgDenHoc,tgDenNgu;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.layout_fragment1,container,false);

        txtQuat1= view.<TextView>findViewById(R.id.txtquat1);
        txtQuat2 = view.<TextView>findViewById(R.id.txtQuat2);
        txtNhietDo = view.<TextView>findViewById(R.id.txtNhietDo);
        txtCua = view.<TextView>findViewById(R.id.txtCua);
        txtKhiGa = view.<TextView>findViewById(R.id.txtKhiGa);
        txtDoAm= view.<TextView>findViewById(R.id.txtDoAm);

        tgQuat1= view.<ToggleButton>findViewById(R.id.tgquat1);
        tgCua= view.<ToggleButton>findViewById(R.id.tgCua);
        tgQuat2= view.<ToggleButton>findViewById(R.id.tgQuat2);
        tgDenChinh= view.<ToggleButton>findViewById(R.id.tgDenChinh);
        tgDenHoc= view.<ToggleButton>findViewById(R.id.tgDenHoc);
        tgDenNgu= view.<ToggleButton>findViewById(R.id.tgDenNgu);



        tgQuat1.setOnCheckedChangeListener(this);
        tgCua.setOnCheckedChangeListener(this);
        tgQuat2.setOnCheckedChangeListener(this);
        tgDenChinh.setOnCheckedChangeListener(this);
        tgDenHoc.setOnCheckedChangeListener(this);
        tgDenNgu.setOnCheckedChangeListener(this);

        ///// ?????c nhi???t ????? v?? kh?? ga v?? ????? ???m
        ReadDataToFirebase();

        return view;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId())
        {
            case R.id.tgquat1:
            {
                if(isChecked)
                {
                    WriteDataToFirebase("Quat1" ,  "ON");
                    Toast.makeText(getActivity(),"Qu???t 1 B???t !",Toast.LENGTH_SHORT).show();
                    txtQuat1.setText("Qu???t 1 B???t");
                }
                else
                {
                    WriteDataToFirebase("Quat1" ,  "OFF");
                    Toast.makeText(getActivity(),"Qu???t 1 T???t !",Toast.LENGTH_SHORT).show();
                    txtQuat1.setText("Qu???t 1 T???t");
                }
                break;
            }
            case R.id.tgCua:
            {
                if(isChecked)
                {
                    WriteDataToFirebase("Cua" ,  "ON");
                    Toast.makeText(getActivity(),"M??? C???a !",Toast.LENGTH_SHORT).show();
                    txtCua.setText("C???a M???    ");
                }
                else
                {
                    WriteDataToFirebase("Cua" ,  "OFF");
                    Toast.makeText(getActivity(),"????ng C???a !",Toast.LENGTH_SHORT).show();
                    txtCua.setText("C???a ????ng");
                }
                break;
            }
            case R.id.tgQuat2:
            {
                if(isChecked)
                {
                    WriteDataToFirebase("Quat2" ,  "ON");
                    Toast.makeText(getActivity(),"Qu???t 2 M???",Toast.LENGTH_SHORT).show();
                    txtQuat2.setText("Qu???t 2 B???t");
                }
                else
                {
                    WriteDataToFirebase("Quat2" ,  "OFF");
                    Toast.makeText(getActivity(),"Qu???t 2 T???t !",Toast.LENGTH_SHORT).show();
                    txtQuat2.setText("Qu???t 2 T???t");
                }
                break;
            }
            case R.id.tgDenChinh:
            {
                if(isChecked)
                {
                    WriteDataToFirebase("DenChinh" ,  "ON");
                    Toast.makeText(getActivity(),"????n Ch??nh B???t !",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    WriteDataToFirebase("DenChinh" ,  "OFF");
                    Toast.makeText(getActivity(),"????n Ch??nh T???t !",Toast.LENGTH_SHORT).show();
                }
                break;
            }
            case R.id.tgDenNgu:
            {
                if(isChecked)
                {
                    WriteDataToFirebase("DenNgu" ,  "ON");
                    Toast.makeText(getActivity(),"????n Ph??ng Ng??? B???t !",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    WriteDataToFirebase("DenNgu" ,  "OFF");
                    Toast.makeText(getActivity(),"????n Ph??ng Ng??? T???t !",Toast.LENGTH_SHORT).show();
                }
                break;
            }
            case R.id.tgDenHoc:
            {
                if(isChecked)
                {
                    WriteDataToFirebase("DenHoc" ,  "ON");
                    Toast.makeText(getActivity(),"????n H???c B???t !",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    WriteDataToFirebase("DenHoc" ,  "OFF");
                    Toast.makeText(getActivity(),"????n H???c T???t !",Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
    }
    void WriteDataToFirebase(String ??uongdan, String Data)
    {
        DatabaseReference mDatabase;
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child(??uongdan).setValue(Data);
    }
    void ReadDataToFirebase()
    {
        DatabaseReference NhietDoData,KhiGaData,DoAData,CuaData,Quat1Data,Quat2Data,DenNguData, DenHocData,DenChinhData;

        ///////////////////////Quat1Data
        Quat1Data = FirebaseDatabase.getInstance().getReference("Quat1");
        Quat1Data.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String dulieu= (String) snapshot.getValue();

                    if(dulieu.equals("ON"))
                    {
                        tgQuat1.setChecked(true);
                    }
                    else
                    {
                        tgQuat1.setChecked(false);
                    }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        ///////////////////////Quat2Data
        Quat2Data = FirebaseDatabase.getInstance().getReference("Quat2");
        Quat2Data.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String dulieu= (String) snapshot.getValue();

                    if(dulieu.equals("ON"))
                    {
                        tgQuat2.setChecked(true);
                    }
                    else
                    {
                        tgQuat2.setChecked(false);
                    }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        ///////////////////////DenChinhData
        DenChinhData = FirebaseDatabase.getInstance().getReference("DenChinh");
        DenChinhData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String dulieu= (String) snapshot.getValue();
                    if(dulieu.equals("ON"))
                    {
                        tgDenChinh.setChecked(true);
                    }
                    else
                    {
                        tgDenChinh.setChecked(false);
                    }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        ///////////////////////DenHoc
        DenHocData = FirebaseDatabase.getInstance().getReference("DenHoc");
        DenHocData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String dulieu= (String) snapshot.getValue();

                    if(dulieu.equals("ON"))
                    {
                        tgDenHoc.setChecked(true);
                    }
                    else
                    {
                        tgDenHoc.setChecked(false);
                    }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        ///////////////////////DenNgu
        DenHocData = FirebaseDatabase.getInstance().getReference("DenNgu");
        DenHocData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String dulieu= (String) snapshot.getValue();

                if(dulieu.equals("ON"))
                {
                    tgDenNgu.setChecked(true);
                }
                else
                {
                    tgDenNgu.setChecked(false);
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        ///////////////////////CuaData
        CuaData = FirebaseDatabase.getInstance().getReference("Cua");
        CuaData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String dulieu= (String) snapshot.getValue();

                    if(dulieu.equals("ON"))
                    {
                        txtCua.setText("C???a M???    ");
                        tgCua.setChecked(true);
                    }
                    else
                    {
                        txtCua.setText("C???a ????ng");
                        tgCua.setChecked(false);
                    }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        ///////////////////////Nhi???t ?????
        NhietDoData = FirebaseDatabase.getInstance().getReference("NhietDo");
        NhietDoData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String dulieu= (String) snapshot.getValue();
                txtNhietDo.setText(dulieu+" C");

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        /////////////////////////// kh?? ga
        KhiGaData = FirebaseDatabase.getInstance().getReference("KhiGa");
        KhiGaData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String dulieu= (String) snapshot.getValue();
                txtKhiGa.setText(dulieu+" %");
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        /////////////////////////// ????? ???M
        KhiGaData = FirebaseDatabase.getInstance().getReference("DoAm");
        KhiGaData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String dulieu= (String) snapshot.getValue();
                txtDoAm.setText(dulieu+" %");
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


}
