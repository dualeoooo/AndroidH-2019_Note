package com.example.myapplication;

import android.app.Dialog;
import android.database.Cursor;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Database database;
    ListView lvCongViec;
    ArrayList<CongViec> arrayCongViec;
    CongViecAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lvCongViec = (ListView) findViewById(R.id.listviewCongViec);
        arrayCongViec = new ArrayList<>();

        adapter = new CongViecAdapter(this,R.layout.dong_cong_viec, arrayCongViec);
        lvCongViec.setAdapter(adapter);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        //tao DB ghi chú
        database = new Database(this, "ghichu.sqlite",null,1);
        //tạo bảng công việc
        database.QueryData("CREATE TABLE IF NOT EXISTS CongViec(Id INTERGER PRIMARY KEY AUTOINCREMENT, TEN CV(200))");
        //insert data
            //database.QueryData("INSERT INTO CongViec VALUES(null, 'Làm bài Project')");
        GetDataCongViec();
    }
    private void GetDataCongViec(){
        //select data
        Cursor dataCongViec = database.GetData("SELECT * FROM CongViec");
        arrayCongViec.clear();
        while (dataCongViec.moveToNext()){
            String ten = dataCongViec.getString(1);
            int id = dataCongViec.getInt(0);
            arrayCongViec.add(new CongViec(id,ten));
        }
        adapter.notifyDataSetChanged();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.them_congviec,menu);
        return super.onCreateOptionsMenu(menu);



    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId()==R.id.menuAdd){
            DialogThem();
        }
        return super.onOptionsItemSelected(item);
    }
    private void DialogThem(){
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_them_cong_viec);

        final EditText edtTen = dialog.findViewById(R.id.editTextTenCV);
        Button btnThem = (Button) dialog.findViewById(R.id.buttonThem);
        Button btnHuy = (Button) dialog.findViewById(R.id.buttonHuy);
        //them cv
        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tencv = edtTen.getText().toString();
                if (tencv.equals("")){
                    Toast.makeText(MainActivity.this,"Nhập lại",Toast.LENGTH_SHORT).show();
                }else{
                    database.QueryData("INSERT INTO CongViec VALUES(null, '"+ tencv+"')");
                    Toast.makeText(MainActivity.this,"Đã thêm",Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    GetDataCongViec();
                }
            }
        });
        //huy them cv
        btnHuy.setOnClickListener(new View.OnClickListener(){
        @Override
            public void onClick(View view){
            dialog.dismiss();
        }
        });

        dialog.show();
    }
}

