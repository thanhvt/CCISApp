package com.es.ccisapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.activeandroid.query.Select;
import com.es.adapter.TaxInvoiceAdapter;
import com.es.model.Bill_TaxInvoice;
import com.es.model.Bill_TaxInvoiceModel;
import com.es.model.Mobile_Adjust_DB;
import com.es.model.SoGCS_User_DB;
import com.es.network.CCISDataService;
import com.es.network.RetrofitInstance;
import com.es.utils.CustomCallBack;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Response;

public class CCISFragment extends Fragment {
    public static final String EXTRA_DATA = "DATA_CONTENT";
    Bill_TaxInvoice taxInvoice;
    private String content;
    private static final String TAG = CCISFragment.class.getSimpleName() + " msg: ";
    TaxInvoiceAdapter taxInvoiceAdapter;
    CCISDataService apiService;
    RecyclerView recyclerView;
    List<Bill_TaxInvoice> lstTaxInvoiceData;
    @BindView(R.id.empty_view)
    TextView txtEmpty;
    @BindView(R.id.txtSoKH)
    TextView txtSoKH;
    //    @BindView(R.id.txtTienThu)
//    TextView txtTienThu;
    @BindView(R.id.txtTienCoVAT)
    TextView txtTienCoVAT;
    @BindView(R.id.spnSo)
    Spinner spnSo;
    List<String> listStrSo;
    List<SoGCS_User_DB> info;

    private void retrieveExtras() {
        if (getArguments() != null) {
            content = getArguments().getString(EXTRA_DATA);
        }
    }

    public CCISFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        retrieveExtras();

        listStrSo = new ArrayList<>();
        info = new Select().all().from(SoGCS_User_DB.class).execute();
        SoGCS_User_DB x = new SoGCS_User_DB(0, "", 0, "", "Lọc theo sổ", 0);
        listStrSo.add("Lọc theo sổ");
        for (SoGCS_User_DB item : info) {
            listStrSo.add(item.getBookCode() + ":" + item.getBookName());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, listStrSo);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        spnSo.setAdapter(adapter);

        spnSo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i > 0) {
                    List<Bill_TaxInvoice> lstTmp = new ArrayList<Bill_TaxInvoice>();
                    for (Bill_TaxInvoice bill : lstTaxInvoiceData) {
                        for (SoGCS_User_DB item : info) {
                            if (item.getBookCode().equals(spnSo.getSelectedItem().toString().split(":")[0])) {
                                if (bill.getFigureBookId() == item.getFigureBookId()) {
                                    lstTmp.add(bill);
                                }
                            }
                        }
                    }
                    Toasty.info(getActivity(), "Lọc ra được " + lstTmp.size() + " khách hàng", Toasty.LENGTH_LONG, true).show();
                    taxInvoiceAdapter = new TaxInvoiceAdapter(lstTmp, R.layout.list_taxinvoice, getContext());
                    recyclerView.setAdapter(taxInvoiceAdapter);
                    taxInvoiceAdapter.notifyDataSetChanged();
                } else {
                    taxInvoiceAdapter = new TaxInvoiceAdapter(lstTaxInvoiceData, R.layout.list_taxinvoice, getContext());
                    recyclerView.setAdapter(taxInvoiceAdapter);
                    taxInvoiceAdapter.notifyDataSetChanged();

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_ccis, container, false);
        ButterKnife.bind(this, rootView);
        lstTaxInvoiceData = new ArrayList<>();
        txtSoKH.setText("Đã thu: ");
//        txtTienThu.setText("Tiền thu: ");
        txtTienCoVAT.setText("Tổng tiền: ");
        recyclerView = (RecyclerView) rootView.findViewById(R.id.movies_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        List<Bill_TaxInvoiceModel> lstDB = new Select().all().from(Bill_TaxInvoiceModel.class).execute();
        if (lstDB.size() == 0) {
            recyclerView.setVisibility(View.GONE);
            txtEmpty.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            txtEmpty.setVisibility(View.GONE);
            int stt = 0;
            for (Bill_TaxInvoiceModel b : lstDB) {
                b.setChecked(false);
                Bill_TaxInvoice x = new Bill_TaxInvoice(b.getTaxCode(), b.getCustomerCode(),
                        b.getBankName(), b.getMonth(), b.getSerialNumber(), b.getYear(), b.getCustomerId(), b.getDepartmentId(), "1",
                        b.getTaxInvoiceAddress(), b.getTaxInvoiceId(), b.getIdDevice(), b.getContractId(), b.getFigureBookId(), b.getSerialCode(),
                        b.getCustomerName(), b.getCustomerCode_Pay(), b.getSubTotal(), b.getAddress_Pay(), b.getBankAccount(), b.getVAT(),
                        b.getTaxRatio(), b.getCustomerId_Pay(), b.getBillType(), b.getCustomerName_Pay(), b.getTotal(), b.isChecked(),
                        b.isThuOffline(), stt, b.getAmount(), b.getServiceTypeId(), b.getServiceName(), b.getINDEX_THU(), b.getKIEU(), b.getPriceId());
                stt++;
                x.setSTT(stt);
                lstTaxInvoiceData.add(x);
            }
            taxInvoiceAdapter = new TaxInvoiceAdapter(lstTaxInvoiceData, R.layout.list_taxinvoice, getContext());
            recyclerView.setAdapter(taxInvoiceAdapter);
            taxInvoiceAdapter.notifyDataSetChanged();

            try {
                int daThu = 0;
                long tienThu = 0L;
                long tongTien = 0L;
                for (Bill_TaxInvoice bill : lstTaxInvoiceData) {
                    if (bill.isThuOffline() > 0) {
                        daThu++;
                        tienThu += Long.parseLong(bill.getSubTotal().substring(0, bill.getSubTotal().indexOf(".")));
//                        tongTien += Long.parseLong(bill.getTotal().substring(0, bill.getTotal().indexOf(".")));
                        tongTien += bill.getTotal().indexOf(".") != -1 ? Long.parseLong(bill.getTotal().substring(0, bill.getTotal().indexOf("."))) : Long.parseLong(bill.getTotal());
                    }
                }
                txtSoKH.setText("Đã thu: " + daThu + "/" + lstTaxInvoiceData.size() + " KH");
//                txtTienThu.setText("Tiền thu: " + formatNumber(tienThu) + " VNĐ");
                txtTienCoVAT.setText("Tổng tiền: " + formatNumber(tongTien) + " VNĐ");
            } catch (Exception e) {

            }

        }

        apiService =
                RetrofitInstance.getRetrofitInstance(getContext()).create(CCISDataService.class);

//        recyclerView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });

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

//    public void getDataTaxInvoice(int trangThai) {
//        SharedPreferences pref = getActivity().getSharedPreferences("LOGIN", 0);
//        int strUserID = pref.getInt("USERID", -1);
//        Call<List<Bill_TaxInvoice>> call = apiService.getBill_TaxInvoice(trangThai, strUserID);
//        Log.wtf("URL Called", call.request().url() + "");
//        call.enqueue(new CustomCallBack<List<Bill_TaxInvoice>>(getActivity()) {
//            @Override
//            public void onResponse(Call<List<Bill_TaxInvoice>> call, Response<List<Bill_TaxInvoice>> response) {
//                try {
//                    if (response != null && response.body() != null) {
//                        lstTaxInvoiceData = response.body();
//                        if (lstTaxInvoiceData.size() > 0) {
//                            int stt = 0;
//                            for (Bill_TaxInvoice b : lstTaxInvoiceData) {
//                                b.setChecked(false);
//                                stt++;
//                                b.setSTT(stt);
//                            }
//                            Log.e(TAG, "Bill_TaxInvoice[0] received: " + lstTaxInvoiceData.get(0).toString());
//                            taxInvoiceAdapter = new TaxInvoiceAdapter(lstTaxInvoiceData, R.layout.list_taxinvoice, getContext());
//                            recyclerView.setAdapter(taxInvoiceAdapter);
//                            taxInvoiceAdapter.notifyDataSetChanged();
//                            recyclerView.setVisibility(View.VISIBLE);
//                            txtEmpty.setVisibility(View.GONE);
//                            int daThu = 0;
//                            long tienThu = 0L;
//                            long tongTien = 0L;
//                            for (Bill_TaxInvoice bill : lstTaxInvoiceData) {
//                                if (bill.isThuOffline()) {
//                                    daThu++;
//                                    tienThu += Long.parseLong(bill.getSubTotal().substring(0, bill.getSubTotal().indexOf(".")));
//                                    tongTien += Long.parseLong(bill.getTotal().substring(0, bill.getTotal().indexOf(".")));
//                                }
//                            }
//                            txtSoKH.setText("Đã thu: " + daThu + "/" + lstTaxInvoiceData.size() + " KH");
////                            txtTienThu.setText("Tiền thu: " + formatNumber(tienThu) + " VNĐ");
//                            txtTienCoVAT.setText("Tổng tiền: " + formatNumber(tongTien) + " VNĐ");
//                        }
//                    } else {
//                        Log.e(TAG, response.message());
//                        Toasty.error(getActivity(), "Không có dữ liệu hoặc gặp lỗi trong quá trình lấy dữ liệu !", Toasty.LENGTH_LONG, true).show();
//                    }
//                } catch (Exception e) {
//                    Log.e(TAG, e.getMessage());
//                } finally {
//                    this.mProgressDialog.dismiss();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<List<Bill_TaxInvoice>> call, Throwable t) {
//                // Log error here since request failed
//                if (t.getMessage().contains("Expected BEGIN_ARRAY")) {
//                    Toasty.error(getActivity(), "Không có dữ liệu. Đề nghị kiểm tra lại !", Toasty.LENGTH_LONG, true).show();
//                } else
//                    Toasty.error(getActivity(), "Xảy ra lỗi quá trình lấy dữ liệu !", Toasty.LENGTH_LONG, true).show();
//                Log.e(TAG, t.toString());
//                this.mProgressDialog.dismiss();
//            }
//        });
//    }

    @Override
    public void onResume() {
        super.onResume();

        lstTaxInvoiceData.clear();  //Reset before update adapter to avoid duplication of list
        List<Bill_TaxInvoiceModel> lstDB = new Select().all().from(Bill_TaxInvoiceModel.class).execute();
        if (lstDB.size() == 0) {
            recyclerView.setVisibility(View.GONE);
            txtEmpty.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            txtEmpty.setVisibility(View.GONE);
            int stt = 0;
            for (Bill_TaxInvoiceModel b : lstDB) {
                b.setChecked(false);
                stt++;

                List<Mobile_Adjust_DB> lstDieuChinh = new Select().all().from(Mobile_Adjust_DB.class).where("TYPE != '3' and CustomerID = ?", b.getCustomerId()).execute();
                if (lstDieuChinh != null && lstDieuChinh.size() > 0) {
                    Mobile_Adjust_DB m = lstDieuChinh.get(lstDieuChinh.size() - 1);
                    b.setCustomerName(m.getCustomerName());
                    b.setAddress_Pay(m.getCustomerAdd());
                    b.setAmount(Double.parseDouble(m.getAmout()));
                    b.setSubTotal(m.getPrice());
//                    String vat = taxInvoice.getTaxRatio();
//                    Double dSub = Double.parseDouble(lstDieuChinh.get(lstDieuChinh.size() - 1).getPrice());
//                    Double dVat = dSub * Double.parseDouble(vat) / 100;
//                    Double dTotal = dSub + dVat;
//                    txtVAT.setText(formatNumber(Math.round(dVat)) + " VNĐ");
//                    txtSubTotal.setText(formatNumber(Math.round(dSub)) + " VNĐ (đã điều chỉnh)");
//                    txtTotal.setText(formatNumber(Math.round(dTotal)) + " VNĐ");
                }

                lstTaxInvoiceData.add(new Bill_TaxInvoice(b.getTaxCode(), b.getCustomerCode(),
                        b.getBankName(), b.getMonth(), b.getSerialNumber(), b.getYear(), b.getCustomerId(), b.getDepartmentId(), "1",
                        b.getTaxInvoiceAddress(), b.getTaxInvoiceId(), b.getIdDevice(), b.getContractId(), b.getFigureBookId(), b.getSerialCode(),
                        b.getCustomerName(), b.getCustomerCode_Pay(), b.getSubTotal(), b.getAddress_Pay(), b.getBankAccount(), b.getVAT(),
                        b.getTaxRatio(), b.getCustomerId_Pay(), b.getBillType(), b.getCustomerName_Pay(), b.getTotal(), b.isChecked(),
                        b.isThuOffline(), stt, b.getAmount(), b.getServiceTypeId(), b.getServiceName(), b.getINDEX_THU(), b.getKIEU(), b.getPriceId()));
            }


            if (spnSo.getSelectedItemPosition() > 0) {
                List<Bill_TaxInvoice> lstTmp = new ArrayList<Bill_TaxInvoice>();
                for (Bill_TaxInvoice bill : lstTaxInvoiceData) {
                    for (SoGCS_User_DB item : info) {
                        if (item.getBookCode().equals(spnSo.getSelectedItem().toString().split(":")[0])) {
                            if (bill.getFigureBookId() == item.getFigureBookId()) {
                                lstTmp.add(bill);
                            }
                        }
                    }
                }
                taxInvoiceAdapter = new TaxInvoiceAdapter(lstTmp, R.layout.list_taxinvoice, getContext());
                recyclerView.setAdapter(taxInvoiceAdapter);
                taxInvoiceAdapter.notifyDataSetChanged();
            } else {
                taxInvoiceAdapter = new TaxInvoiceAdapter(lstTaxInvoiceData, R.layout.list_taxinvoice, getContext());
                recyclerView.setAdapter(taxInvoiceAdapter);
                taxInvoiceAdapter.notifyDataSetChanged();
            }
            if (lstTaxInvoiceData.size() > 0 && searchView != null && searchView.getQuery() != null && !searchView.getQuery().toString().isEmpty()
                    && taxInvoiceAdapter != null && taxInvoiceAdapter.getLstTaxInvoice() != null && taxInvoiceAdapter.getItemCount() > 0
            ) {
                taxInvoiceAdapter.getFilter().filter(searchView.getQuery().toString());
                taxInvoiceAdapter.notifyDataSetChanged();
            }

            try {
                int daThu = 0;
                long tienThu = 0L;
                long tongTien = 0L;
                for (Bill_TaxInvoice bill : lstTaxInvoiceData) {
                    if (bill.isThuOffline() > 0) {
                        daThu++;
                        tienThu += Long.parseLong(bill.getSubTotal().substring(0, bill.getSubTotal().indexOf(".")));
                        tongTien += bill.getTotal().indexOf(".") != -1 ? Long.parseLong(bill.getTotal().substring(0, bill.getTotal().indexOf("."))) : Long.parseLong(bill.getTotal());
                    }
                }
                txtSoKH.setText("Đã thu: " + daThu + "/" + lstTaxInvoiceData.size() + " KH");
//                txtTienThu.setText("Tiền thu: " + formatNumber(tienThu) + " VNĐ");
                txtTienCoVAT.setText("Tổng tiền: " + formatNumber(tongTien) + " VNĐ");
            } catch (Exception e) {

            }

        }
    }

    private SearchView searchView;
    private MenuItem searchMenuItem;

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // TODO Add your menu entries here
        //inflater.inflate(R.menu.menu_log, menu);
        //super.onCreateOptionsMenu(menu, inflater);
//        getActivity().getMenuInflater().inflate(R.menu.menu_scrolling, menu);
        searchMenuItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        ImageView mCloseButton = (ImageView) searchView.findViewById(android.support.v7.appcompat.R.id.search_close_btn);

        searchView.setQueryHint("Tìm kiếm theo tên, địa chỉ, vv...");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if (!s.isEmpty() && taxInvoiceAdapter != null && taxInvoiceAdapter.getFilter() != null) {
                    taxInvoiceAdapter.getFilter().filter(s);
                }
                return false;
            }
        });
        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    searchMenuItem.collapseActionView();
                    searchView.setQuery("", false);
                    searchView.setIconified(true);
                }
            }
        });
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                taxInvoiceAdapter.getFilter().filter("");
                return false;
            }
        });
        searchView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b && searchView.getQuery().length() == 0) {
                    taxInvoiceAdapter.getFilter().filter("");
                }
            }
        });
        mCloseButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //Clear the query
                searchMenuItem.collapseActionView();
                searchView.setQuery("", false);
                taxInvoiceAdapter.getFilter().filter("");
//                mSearchView.setQuery("",false);
//                // below lines were all tried and do not work
//                **mSearchView.setQueryHint(" Search here...");**
//                **mSearchView.setIconified(false);**
//                **mSearchView.onActionViewCollapsed);**
//                **MenuItemCompat.expandActionView(searchitem);**
//                adapter.clear();
//                adapter.addAll(allList);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_thutien) {
            if (taxInvoiceAdapter != null) {
                boolean isChon = false;
                List<Bill_TaxInvoice> stList = taxInvoiceAdapter.getLstTaxInvoice();
                for (Bill_TaxInvoice b : stList) {
                    if (b.isChecked()) {
                        isChon = true;
                        break;
                    }
                }
                if (!isChon) {
                    Toasty.warning(getActivity(), "Yêu cầu chọn ít nhất 1 KH để thực hiện !", Toasty.LENGTH_LONG, true).show();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle(R.string.app_name);
                    builder.setMessage("Anh/Chị muốn thu tiền offline hay online ?");
                    builder.setIcon(R.drawable.logo);
                    builder.setPositiveButton("Thu offline", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                            List<Bill_TaxInvoice> stList = taxInvoiceAdapter.getLstTaxInvoice();
                            for (final Bill_TaxInvoice b : stList) {
                                if (b.isChecked()) {
                                    Bill_TaxInvoiceModel billTaxInvoiceModel = new Select().from(Bill_TaxInvoiceModel.class).where("TaxInvoiceId = ?", b.getTaxInvoiceId()).executeSingle();
                                    if (billTaxInvoiceModel != null) {
                                        billTaxInvoiceModel.setThuOffline(1);
                                        billTaxInvoiceModel.save();
                                        Log.e(TAG + " update ", "1");
                                    } else {
                                        Bill_TaxInvoiceModel c = new Bill_TaxInvoiceModel(b.getTaxCode(), b.getCustomerCode(),
                                                b.getBankName(), b.getMonth(), b.getSerialNumber(), b.getYear(), b.getCustomerId(), b.getDepartmentId(),
                                                b.getTaxInvoiceAddress(), b.getTaxInvoiceId(), b.getIdDevice(), b.getContractId(), b.getFigureBookId(), b.getSerialCode(),
                                                b.getCustomerName(), b.getCustomerCode_Pay(), b.getSubTotal(), b.getAddress_Pay(), b.getBankAccount(), b.getVAT(),
                                                b.getTaxRatio(), b.getCustomerId_Pay(), b.getBillType(), b.getCustomerName_Pay(), b.getTotal(), b.isChecked(),
                                                1, b.getAmount(), b.getServiceTypeId(), b.getServiceName(), b.getINDEX_THU(), b.getKIEU(), b.getPriceId());
                                        c.save();
                                        Log.e(TAG + " insert ", "2");
                                    }
                                    b.setThuOffline(1);
                                    taxInvoiceAdapter.notifyDataSetChanged();
                                    Toasty.success(getActivity(), "Thu tiền offline khách hàng " + b.getCustomerName() + " thành công !", Toasty.LENGTH_LONG, true).show();
                                }
                            }

                            int daThu = 0;
                            long tienThu = 0L;
                            long tongTien = 0L;
                            for (Bill_TaxInvoice bill : stList) {
                                if (bill.isThuOffline() > 0) {
                                    daThu++;
                                    tienThu += Long.parseLong(bill.getSubTotal().substring(0, bill.getSubTotal().indexOf(".")));
//                                    tongTien += Long.parseLong(bill.getTotal().substring(0, bill.getTotal().indexOf(".")));
                                    tongTien += bill.getTotal().indexOf(".") != -1 ? Long.parseLong(bill.getTotal().substring(0, bill.getTotal().indexOf("."))) : Long.parseLong(bill.getTotal());
                                }
                            }
                            txtSoKH.setText("Đã thu: " + daThu + "/" + lstTaxInvoiceData.size() + " KH");
//                            txtTienThu.setText("Tiền thu: " + formatNumber(tienThu) + " VNĐ");
                            txtTienCoVAT.setText("Tổng tiền: " + formatNumber(tongTien) + " VNĐ");
                        }
                    });
                    builder.setNegativeButton("Thu online", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();

                            final List<Bill_TaxInvoice> stList = taxInvoiceAdapter.getLstTaxInvoice();
                            for (final Bill_TaxInvoice b : stList) {
                                if (b.isChecked()) {
                                    Log.e(TAG, b.toString());
                                    Call<Integer> call = apiService.ThuTien((b.getTaxInvoiceId()));
                                    call.enqueue(new CustomCallBack<Integer>(getActivity()) {
                                        @Override
                                        public void onResponse(Call<Integer> call, Response<Integer> response) {
                                            this.mProgressDialog.dismiss();
                                            Integer movies = response.body();
                                            Log.d(TAG, "movies: " + movies);
                                            if (movies == 1) {
//                                                List<Bill_TaxInvoiceModel> info = new Delete().from(Bill_TaxInvoiceModel.class).where("TaxInvoiceId = ?", b.getTaxInvoiceId()).execute();
//                                                new Delete().from(Bill_TaxInvoiceDetail_DB.class).where("TaxInvoiceId = ?", b.getTaxInvoiceId()).execute();
                                                Bill_TaxInvoiceModel billTaxInvoiceModel = new Select().from(Bill_TaxInvoiceModel.class).where("TaxInvoiceId = ?", b.getTaxInvoiceId()).executeSingle();
                                                billTaxInvoiceModel.setThuOffline(2);
                                                billTaxInvoiceModel.save();
                                                b.setThuOffline(2);
                                                taxInvoiceAdapter.notifyDataSetChanged();
                                                Toasty.success(getActivity(), "Thu tiền khách hàng " + b.getCustomerName() + " thành công !", Toasty.LENGTH_LONG, true).show();

                                                int daThu = 0;
                                                long tienThu = 0L;
                                                long tongTien = 0L;
                                                for (Bill_TaxInvoice bill : stList) {
                                                    if (bill.isThuOffline() > 0) {
                                                        daThu++;
                                                        tienThu += Long.parseLong(bill.getSubTotal().substring(0, bill.getSubTotal().indexOf(".")));
//                                                        tongTien += Long.parseLong(bill.getTotal().substring(0, bill.getTotal().indexOf(".")));
                                                        tongTien += bill.getTotal().indexOf(".") != -1 ? Long.parseLong(bill.getTotal().substring(0, bill.getTotal().indexOf("."))) : Long.parseLong(bill.getTotal());
                                                    }
                                                }
                                                txtSoKH.setText("Đã thu: " + daThu + "/" + lstTaxInvoiceData.size() + " KH");
//                                              txtTienThu.setText("Tiền thu: " + formatNumber(tienThu) + " VNĐ");
                                                txtTienCoVAT.setText("Tổng tiền: " + formatNumber(tongTien) + " VNĐ");

                                            } else {
                                                Toasty.error(getActivity(), "Thu tiền khách hàng " + b.getCustomerName() + " không thành công. Đề nghị kiểm tra lại dữ liệu !", Toasty.LENGTH_LONG, true).show();
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<Integer> call, Throwable t) {
                                            this.mProgressDialog.dismiss();
                                            // Log error here since request failed
                                            Log.e(TAG, t.toString());
                                        }
                                    });
                                }
                            }

                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                }


            }
            return true;
        }
        if (id == R.id.action_daochon) {
            if (taxInvoiceAdapter != null) {
                String strID = "";
                List<Bill_TaxInvoice> stList = taxInvoiceAdapter.getLstTaxInvoice();
                for (Bill_TaxInvoice b : stList) {
                    if (b.isChecked()) {
                        b.setChecked(false);
                    } else
                        b.setChecked(true);
                }
                taxInvoiceAdapter.notifyDataSetChanged();
            }
            return true;
        }
//        if (id == R.id.action_all) {
//            if (!Utils.isOnline(getContext())) {
//                Toast.makeText(getActivity(), "Yêu cầu kết nối mạng để thực hiện !", Toast.LENGTH_LONG).show();
//            } else {
//                getDataTaxInvoice(-1);
//            }
//        }
//        if (id == R.id.action_dathu) {
//            if (!Utils.isOnline(getContext())) {
//                Toast.makeText(getActivity(), "Yêu cầu kết nối mạng để thực hiện !", Toast.LENGTH_LONG).show();
//            } else {
//                getDataTaxInvoice(1);
//            }
//        }
//        if (id == R.id.action_chuathu) {
//            if (!Utils.isOnline(getContext())) {
//                Toast.makeText(getActivity(), "Yêu cầu kết nối mạng để thực hiện !", Toast.LENGTH_LONG).show();
//            } else {
//                getDataTaxInvoice(0);
//            }
//        }

        return super.onOptionsItemSelected(item);
    }
}
