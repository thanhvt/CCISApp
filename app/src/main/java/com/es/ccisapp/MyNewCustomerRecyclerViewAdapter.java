package com.es.ccisapp;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.es.model.Mobile_Adjust_DB;

import java.util.List;

//import com.es.ccisapp.NewCustomerFragment.OnListFragmentInteractionListener;

///**
// * {@link RecyclerView.Adapter} that can display a {@link Mobile_Adjust_DB} and makes a call to the
// * specified {@link OnListFragmentInteractionListener}.
// * TODO: Replace the implementation with code for your data type.
// */
public class MyNewCustomerRecyclerViewAdapter extends RecyclerView.Adapter<MyNewCustomerRecyclerViewAdapter.ViewHolder> {

    private final List<Mobile_Adjust_DB> lstTaxInvoice;
//    private final OnListFragmentInteractionListener mListener;

    public MyNewCustomerRecyclerViewAdapter(List<Mobile_Adjust_DB> items) { // , OnListFragmentInteractionListener listener
        lstTaxInvoice = items;
//        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_newcustomer, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
//        holder.mItem = lstTaxInvoice.get(position);
//        holder.mIdView.setText(lstTaxInvoice.get(position).getCustomerName());
//        holder.mContentView.setText(lstTaxInvoice.get(position).getTotal());

//        holder.mView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (null != mListener) {
//                    // Notify the active callbacks interface (the activity, if the
//                    // fragment is attached to one) that an item has been selected.
//                    mListener.onListFragmentInteraction(holder.mItem);
//                }
//            }
//        });
        holder.movieTitle.setText("MÃ KH MỚI" + " - " + lstTaxInvoice.get(position).getCustomerName());
        holder.data.setText("Tổng tiền: " + lstTaxInvoice.get(position).getTotal() + " VAT: " + lstTaxInvoice.get(position).getTax());
        holder.movieDescription.setText(lstTaxInvoice.get(position).getCustomerAdd());
//        holder.isChecked.setChecked(lstTaxInvoice.get(position).isChecked());
//        holder.txtOffline.setVisibility(lstTaxInvoice.get(position).isThuOffline() ? View.VISIBLE : View.INVISIBLE);
//        holder.isChecked.setTag(lstTaxInvoice.get(position));
//        holder.isChecked.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                CheckBox cb = (CheckBox) v;
//                Bill_TaxInvoice contact = (Bill_TaxInvoice) cb.getTag();
//
//                contact.setChecked(cb.isChecked());
//                lstTaxInvoice.get(position).setChecked(cb.isChecked());
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return lstTaxInvoice.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout moviesLayout;
        TextView movieTitle;
        TextView data;
        TextView movieDescription;
        CheckBox isChecked;
        TextView txtOffline;

        public ViewHolder(View v) {
            super(v);
            moviesLayout = (LinearLayout) v.findViewById(R.id.movies_layout);
            movieTitle = (TextView) v.findViewById(R.id.title);
            data = (TextView) v.findViewById(R.id.subtitle);
            movieDescription = (TextView) v.findViewById(R.id.description);
//            txtOffline = (TextView) v.findViewById(R.id.txtThu);
//            isChecked = (CheckBox) v.findViewById(R.id.checkBox);

//            v.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Context context = v.getContext();
//                    Bundle bundle = new Bundle();
//                    bundle.putSerializable("DATA", "123");
//                    bundle.putString("IMAGE", lstTaxInvoice.get(getAdapterPosition()).getBankName());
//                    bundle.putSerializable("TAX", lstTaxInvoice.get(getAdapterPosition()));
//                    HomeNavActivity.lstTaxInvoice = lstTaxInvoice;
//                    Intent intent = new Intent(context, HomeNavActivity.class);
//                    intent.putExtras(bundle);
//                    context.startActivity(intent);
        }
//            });
//        }
    }
}
