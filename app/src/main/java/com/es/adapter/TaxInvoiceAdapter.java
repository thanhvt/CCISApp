package com.es.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.es.ccisapp.HomeNavActivity;
import com.es.ccisapp.R;
import com.es.model.Bill_TaxInvoice;

import java.util.ArrayList;
import java.util.List;

public class TaxInvoiceAdapter extends RecyclerView.Adapter<TaxInvoiceAdapter.TaxInvoiceHolder> implements Filterable {

    private List<Bill_TaxInvoice> lstTaxInvoice, filterList;
    private int rowLayout;
    private Context context;
    private ContactFilter filter;
    public class TaxInvoiceHolder extends RecyclerView.ViewHolder {
        LinearLayout moviesLayout;
        TextView movieTitle;
        TextView data;
        TextView movieDescription;
        CheckBox isChecked;
        TextView txtOffline;

        public TaxInvoiceHolder(View v) {
            super(v);
            moviesLayout = (LinearLayout) v.findViewById(R.id.movies_layout);
            movieTitle = (TextView) v.findViewById(R.id.title);
            data = (TextView) v.findViewById(R.id.subtitle);
            txtOffline = (TextView) v.findViewById(R.id.txtThu);
            movieDescription = (TextView) v.findViewById(R.id.description);
            isChecked = (CheckBox) v.findViewById(R.id.checkBox);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context context = v.getContext();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("DATA", "123");
                    bundle.putString("IMAGE", lstTaxInvoice.get(getAdapterPosition()).getBankName());
                    bundle.putSerializable("TAX", lstTaxInvoice.get(getAdapterPosition()));
//                    bundle.putSerializable("ALL", (Serializable) lstTaxInvoice);
                    HomeNavActivity.lstTaxInvoice = lstTaxInvoice;
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
        filterList = new ArrayList<>();
        this.filterList.addAll(movies);
    }

    @Override
    public TaxInvoiceAdapter.TaxInvoiceHolder onCreateViewHolder(ViewGroup parent,
                                                            int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new TaxInvoiceHolder(view);
    }

    @Override
    public void onBindViewHolder(TaxInvoiceHolder holder, final int position) {
        holder.movieTitle.setText(lstTaxInvoice.get(position).getSTT() + ". " + lstTaxInvoice.get(position).getCustomerCode() + " - " + lstTaxInvoice.get(position).getCustomerName());
        holder.data.setText("Đơn giá: " + lstTaxInvoice.get(position).getSubTotal() + " VAT: " + lstTaxInvoice.get(position).getVAT());
        holder.movieDescription.setText(lstTaxInvoice.get(position).getTaxInvoiceAddress());
        holder.isChecked.setChecked(lstTaxInvoice.get(position).isChecked());
        holder.txtOffline.setVisibility(lstTaxInvoice.get(position).isThuOffline() ? View.VISIBLE : View.INVISIBLE);
        holder.isChecked.setTag(lstTaxInvoice.get(position));
        holder.isChecked.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                CheckBox cb = (CheckBox) v;
                Bill_TaxInvoice contact = (Bill_TaxInvoice) cb.getTag();

                contact.setChecked(cb.isChecked());
                lstTaxInvoice.get(position).setChecked(cb.isChecked());

            }
        });
    }

    public List<Bill_TaxInvoice> getLstTaxInvoice() {
        return lstTaxInvoice;
    }

    @Override
    public int getItemCount() {
        return lstTaxInvoice.size();
    }


    @Override
    public Filter getFilter() {
        if (filter == null)
            filter = new ContactFilter();
        return filter;
    }

//    @Override
//    public void notifyDataSetChanged() {
//        super.notifyDataSetChanged();
//    }

    private class ContactFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            String data = constraint.toString().toLowerCase();
            FilterResults results = new FilterResults();
            if (data.length() > 0 && filterList.size() > 0) {
                List<Bill_TaxInvoice> filteredList = new ArrayList<>(filterList);
                List<Bill_TaxInvoice> nList = new ArrayList<>();
                int count = filteredList.size();
                for (int i = 0; i < count; i++) {
                    Bill_TaxInvoice item = filteredList.get(i);
                    String name = item.getCustomerName().toLowerCase();
                    String strCase = item.getAddress_Pay().toLowerCase();
                    String dchi = item.getTaxInvoiceAddress().toLowerCase();
                    if (name.equalsIgnoreCase(data) || name.contains(data) || strCase.equalsIgnoreCase(data) || strCase.contains(data) || dchi.equalsIgnoreCase(data) || dchi.contains(data))
                        nList.add(item);
                }
                results.count = nList.size();
                results.values = nList;
            } else {
                List<Bill_TaxInvoice> list = new ArrayList<>(filterList);
                results.count = list.size();
                results.values = list;
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            lstTaxInvoice = (ArrayList<Bill_TaxInvoice>) results.values;
//            clear();
//            for (int i = 0; i < lstTaxInvoice.size(); i++) {
//                Bill_TaxInvoice item = (Bill_TaxInvoice) lstTaxInvoice.get(i);
//                add(item);
//                notifyDataSetChanged();
//            }
            notifyDataSetChanged();
        }
    }

}
