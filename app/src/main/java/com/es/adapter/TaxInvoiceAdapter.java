package com.es.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.es.ccisapp.HomeNavActivity;
import com.es.ccisapp.R;
import com.es.model.Bill_TaxInvoice;

import java.util.List;

public class TaxInvoiceAdapter extends RecyclerView.Adapter<TaxInvoiceAdapter.TaxInvoiceHolder> {

    private List<Bill_TaxInvoice> lstTaxInvoice;
    private int rowLayout;
    private Context context;

    public class TaxInvoiceHolder extends RecyclerView.ViewHolder {
        LinearLayout moviesLayout;
        TextView movieTitle;
        TextView data;
        TextView movieDescription;

        public TaxInvoiceHolder(View v) {
            super(v);
            moviesLayout = (LinearLayout) v.findViewById(R.id.movies_layout);
            movieTitle = (TextView) v.findViewById(R.id.title);
            data = (TextView) v.findViewById(R.id.subtitle);
            movieDescription = (TextView) v.findViewById(R.id.description);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context context = v.getContext();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("DATA", "123");
                    bundle.putString("IMAGE", lstTaxInvoice.get(getAdapterPosition()).getBankName());
                    bundle.putSerializable("TAX", lstTaxInvoice.get(getAdapterPosition()));
                    Intent intent = new Intent(context, HomeNavActivity.class);
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                }
            });
        }
    }

    public TaxInvoiceAdapter(List<Bill_TaxInvoice> movies, int rowLayout, Context context) {
        this.lstTaxInvoice = movies;
        this.rowLayout = rowLayout;
        this.context = context;
    }

    @Override
    public TaxInvoiceAdapter.TaxInvoiceHolder onCreateViewHolder(ViewGroup parent,
                                                            int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new TaxInvoiceHolder(view);
    }


    @Override
    public void onBindViewHolder(TaxInvoiceHolder holder, final int position) {
        holder.movieTitle.setText(lstTaxInvoice.get(position).getCustomerCode() + " - " + lstTaxInvoice.get(position).getCustomerName());
        holder.data.setText("Đơn giá: " + lstTaxInvoice.get(position).getSubTotal() + " VAT: " + lstTaxInvoice.get(position).getVAT());
        holder.movieDescription.setText(lstTaxInvoice.get(position).getTaxInvoiceAddress());

    }

    @Override
    public int getItemCount() {
        return lstTaxInvoice.size();
    }
}
