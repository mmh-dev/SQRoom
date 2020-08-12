package com.example.carsqlite;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    FloatingActionButton addBtn;
    ExampleAdapter adapter;
    DbHandler handler = new DbHandler(this);
    List<Car> carList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        carList = handler.getAllCars();
//        handler.deleteAllCars();
        addBtn = findViewById(R.id.addBtn);

        buildRecyclerView();

        adapter.setOnItemClickListener(new ExampleAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                editCar(position);
            }

            @Override
            public void onDeleteClick(int position) {
                handler.deleteCar(carList.get(position));
                carList.remove(position);
                Toast.makeText(MainActivity.this, "Car has been deleted!", Toast.LENGTH_SHORT).show();
                adapter.notifyItemRemoved(position);
            }
        });

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNewCar();
            }
        });
    }

    private void editCar(final int position) {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Edit Car");
        View layout = LayoutInflater.from(this).inflate(R.layout.layout_add, null);

        final EditText carName = layout.findViewById(R.id.e_carModel);
        final EditText carPrice = layout.findViewById(R.id.e_CarPrice);
        carName.setText(carList.get(position).getModel());
        carPrice.setText(carList.get(position).getPrice());
        dialog.setView(layout);
        dialog.setPositiveButton("UPDATE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Car c = new Car();
                c.setModel(carName.getText().toString());
                c.setPrice(carPrice.getText().toString());
                carList.set(position,c);
                handler.updateCar(c);
                Toast.makeText(MainActivity.this, "Car has been updated!", Toast.LENGTH_SHORT).show();
                adapter.notifyItemChanged(position);
            }
        });

        dialog.setNegativeButton("DELETE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                handler.deleteCar(carList.get(position));
                carList.remove(position);
                Toast.makeText(MainActivity.this, "Car has been deleted!", Toast.LENGTH_SHORT).show();
                adapter.notifyItemRemoved(position);
            }
        });
        dialog.show();
    }

    private void addNewCar() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Add Car");
        View layout = LayoutInflater.from(this).inflate(R.layout.layout_add, null);

        final EditText carName = layout.findViewById(R.id.e_carModel);
        final EditText carPrice = layout.findViewById(R.id.e_CarPrice);
        dialog.setView(layout);
        dialog.setPositiveButton("SAVE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (TextUtils.isEmpty(carName.getText().toString()) && TextUtils.isEmpty(carPrice.getText().toString())){
                    Toast.makeText(MainActivity.this, "Please enter car model and price!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(carName.getText().toString())){
                    Toast.makeText(MainActivity.this, "Please enter car model!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(carPrice.getText().toString())){
                    Toast.makeText(MainActivity.this, "Please enter car price!", Toast.LENGTH_SHORT).show();
                    return;
                }

                Car c = new Car(carName.getText().toString(), carPrice.getText().toString());
                long id = handler.addCar(c);
                Car car  = handler.getCar(id);

                if (car != null){
                    carList.add(0,car);
                    adapter.notifyItemInserted(0);
                }
                Toast.makeText(MainActivity.this, "New Car is added!", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        dialog.show();
    }

    private void buildRecyclerView() {
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ExampleAdapter(carList);
        recyclerView.setAdapter(adapter);
    }
}
