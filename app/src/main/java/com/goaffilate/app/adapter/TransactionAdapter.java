package com.goaffilate.app.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.goaffilate.app.R;
import com.goaffilate.app.model.TransactionModel;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.MyViewHolder> {

    private List<TransactionModel> modelList;
    private Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name,amount,status;
        public CircleImageView image;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.username);
            amount=view.findViewById(R.id.amount);
            status=view.findViewById(R.id.status);
            image = (CircleImageView) view.findViewById(R.id.iv);
        }
    }

    public TransactionAdapter(List<TransactionModel> modelList) {
        this.modelList = modelList;
    }

    @Override
    public TransactionAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.transaction, parent, false);

        context = parent.getContext();

        return new TransactionAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(TransactionAdapter.MyViewHolder holder, int position) {

        final TransactionModel mList = modelList.get(position);

              holder.name.setText(mList.getUser_name());
              holder.amount.setText("-₹"+" "+mList.getRedeem_points());

              if (mList.getStatus().contains("0")){
                  holder.status.setText("Pending");
              }
              else {
                  holder.status.setText("Approved");

              }


    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

}
