package com.es.ccisapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.activeandroid.query.Select;
import com.es.model.Mobile_Adjust_DB;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@OnListFragmentInteractionListener}
 * interface.
 */
public class NewCustomerFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
//    private OnListFragmentInteractionListener mListener;
RecyclerView recyclerView;
    List<Mobile_Adjust_DB> lstMobile = new ArrayList<>();
    MyNewCustomerRecyclerViewAdapter taxInvoiceAdapter;
    @BindView(R.id.txtSoKH)
    TextView txtSoKH;
    @BindView(R.id.txtTienThu)
    TextView txtTienThu;
    @BindView(R.id.txtTienCoVAT)
    TextView txtTienCoVAT;
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public NewCustomerFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static NewCustomerFragment newInstance(int columnCount) {
        NewCustomerFragment fragment = new NewCustomerFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }

        lstMobile = new Select().all().from(Mobile_Adjust_DB.class).where("Type = '3'").execute();
        Log.e("NEW KH", lstMobile.size() + "");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_newcustomer_list, container, false);
        ButterKnife.bind(this, rootView);
        // Set the adapter
//        if (view instanceof RecyclerView) {
//            Context context = view.getContext();
//            RecyclerView recyclerView = (RecyclerView) view;
//            if (mColumnCount <= 1) {
//                recyclerView.setLayoutManager(new LinearLayoutManager(context));
//            } else {
//                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
//            }
//            recyclerView.setAdapter(new MyNewCustomerRecyclerViewAdapter(lstMobile)); // mListener
//        }
        recyclerView = (RecyclerView) rootView.findViewById(R.id.movies_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        taxInvoiceAdapter = new MyNewCustomerRecyclerViewAdapter(lstMobile);
        recyclerView.setAdapter(taxInvoiceAdapter);
        taxInvoiceAdapter.notifyDataSetChanged();
        try {
            int daThu = 0;
            long tienThu = 0L;
            long tongTien = 0L;
            for (Mobile_Adjust_DB bill : lstMobile) {
                daThu++;
                tienThu += new BigDecimal(bill.getSubTotal()).longValue(); // Long.parseLong(bill.getSubTotal().substring(0, bill.getSubTotal().indexOf(".")));
                tongTien += new BigDecimal(bill.getTotal()).longValue(); // Long.parseLong(bill.getTotal().substring(0, bill.getTotal().indexOf(".")));
            }
            txtSoKH.setText("Đã thu: " + daThu + "/" + lstMobile.size() + " KH");
            txtTienThu.setText("Tiền thu: " + formatNumber(tienThu) + " VNĐ");
            txtTienCoVAT.setText("Tổng tiền: " + formatNumber(tongTien) + " VNĐ");
        } catch (Exception e) {

        }

        return rootView;
    }

    public String formatNumber(long number) {
        if (number < 1000) {
            return String.valueOf(number);
        }
        try {
            NumberFormat formatter = new DecimalFormat("###,###");
            String resp = formatter.format(number);
            resp = resp.replaceAll(",", ".");
            return resp;
        } catch (Exception e) {
            return "";
        }
    }


//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnListFragmentInteractionListener) {
//            mListener = (OnListFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnListFragmentInteractionListener");
//        }
//    }
//
//    @Override
//    public void onDetach() {
//        super.onDetach();
//        mListener = null;
//    }
//
//    /**
//     * This interface must be implemented by activities that contain this
//     * fragment to allow an interaction in this fragment to be communicated
//     * to the activity and potentially other fragments contained in that
//     * activity.
//     * <p/>
//     * See the Android Training lesson <a href=
//     * "http://developer.android.com/training/basics/fragments/communicating.html"
//     * >Communicating with Other Fragments</a> for more information.
//     */
//    public interface OnListFragmentInteractionListener {
//        // TODO: Update argument type and name
//        void onListFragmentInteraction(Mobile_Adjust_DB item);
//    }
}
