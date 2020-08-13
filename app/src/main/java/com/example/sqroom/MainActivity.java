package com.example.sqroom;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
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
    CarDB database;
    List<Car> carList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        database = Room.databaseBuilder(getApplicationContext(),
                CarDB.class, "CarDB")
                .allowMainThreadQueries()
                .build();

        new getAllCarsAsyncTask().execute();

        addBtn = findViewById(R.id.addBtn);

        buildRecyclerView();

        adapter.setOnItemClickListener(new ExampleAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                editCar(position);
            }

            @Override
            public void onDeleteClick(int position) {
                Car c = carList.get(position);

                new deleteCarAsyncTask().execute(c);

                carList.remove(position);
                Toast.makeText(MainActivity.this, "Car has been deleted!", Toast.LENGTH_SHORT).show();
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

                new editCarAsyncTask().execute(c);

                Toast.makeText(MainActivity.this, "Car has been updated!", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.setNegativeButton("DELETE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Car c = carList.get(position);

                new deleteCarAsyncTask().execute(c);

                carList.remove(position);
                Toast.makeText(MainActivity.this, "Car has been deleted!", Toast.LENGTH_SHORT).show();
            }
        });
        dialog.show();
    }

    private void addNewCar() {

        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Add Car");
        View layout = LayoutInflater.from(this).inflate(R.layout.layout_add, null);

        final EditText carModel = layout.findViewById(R.id.e_carModel);
        final EditText carPrice = layout.findViewById(R.id.e_CarPrice);
        dialog.setView(layout);
        dialog.setPositiveButton("SAVE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (TextUtils.isEmpty(carModel.getText().toString()) && TextUtils.isEmpty(carPrice.getText().toString())){
                    Toast.makeText(MainActivity.this, "Please enter car model and price!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(carModel.getText().toString())){
                    Toast.makeText(MainActivity.this, "Please enter car model!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(carPrice.getText().toString())){
                    Toast.makeText(MainActivity.this, "Please enter car price!", Toast.LENGTH_SHORT).show();
                    return;
                }

                Car c = new Car(0,carModel.getText().toString(), carPrice.getText().toString());

                new addNewCarAsyncTask().execute(c);

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

    public void buildRecyclerView(){
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ExampleAdapter(carList);
        recyclerView.setAdapter(adapter);
    }

    private  class getAllCarsAsyncTask extends AsyncTask <Void, Void , Void>{

        @Override
        protected Void doInBackground(Void... voids) {

            // делает работу асинхронно на вторичном потоке
            carList. addAll(database.getCarDao().getAllCars());
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            // метод срабатывает как только doInBackground заканчивает свою работу
            super.onPostExecute(aVoid);
            adapter.notifyDataSetChanged();
        }
    }

    private  class addNewCarAsyncTask extends AsyncTask <Car, Void , Void>{

        @Override
        protected Void doInBackground(Car... cars) {

            long id =  database.getCarDao().addCar(cars[0]);
            Car car  = database.getCarDao().getCar(id);
            if (car != null){
                carList.add(0,car);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            adapter.notifyDataSetChanged();
        }
    }

    private  class editCarAsyncTask extends AsyncTask <Car, Void , Void>{

        @Override
        protected Void doInBackground(Car... cars) {
            database.getCarDao().updateCar(cars[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            adapter.notifyDataSetChanged();
        }
    }

    private  class deleteCarAsyncTask extends AsyncTask <Car, Void , Void>{

        @Override
        protected Void doInBackground(Car... cars) {
            database.getCarDao().deleteCar(cars[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            adapter.notifyDataSetChanged();
        }
    }
}