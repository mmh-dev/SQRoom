package com.example.carsqlite;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ExampleAdapter  extends RecyclerView.Adapter<ExampleAdapter.Holder> {

    List<Car> carList;
    OnItemClickListener listener;

    public void setOnItemClickListener (OnItemClickListener listener){
        this.listener = listener;
    }

    public ExampleAdapter(List<Car> carList) {
        this.carList = carList;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.example_item, parent, false);
        return new Holder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        holder.model.setText(carList.get(position).getModel());
        holder.price.setText(carList.get(position).getPrice());
    }

    @Override
    public int getItemCount() {
        return carList.size();
    }

    public static class Holder extends RecyclerView.ViewHolder {
        TextView model, price;
        ImageView deleteBtn;

        public Holder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            model = itemView.findViewById(R.id.car_model);
            price = itemView.findViewById(R.id.car_price);
            deleteBtn = itemView.findViewById(R.id.deleteBtn);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    listener.onItemClick(position);
                }
            });

            deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onDeleteClick(getAdapterPosition());
                }
            });
        }
    }

    public interface OnItemClickListener{
        void onItemClick(int position);
        void onDeleteClick(int position);
    }
}
