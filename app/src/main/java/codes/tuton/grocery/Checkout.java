package codes.tuton.grocery;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.tv.TvContract;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.androidstudy.networkmanager.Monitor;
import com.androidstudy.networkmanager.Tovuti;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import codes.tuton.grocery.cartPOJO.Cart;
import codes.tuton.grocery.cartPOJO.cartBean;
import codes.tuton.grocery.checkPromoPOJO.checkPromoBean;
import codes.tuton.grocery.offlineCartPOJO.offlineCartBean;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class Checkout extends AppCompatActivity {

    TextView amount, saving, total, applied;
    CheckBox geniecash;
    RadioGroup delivery;
    Spinner free, express;
    EditText promo;
    Button apply;

    EditText name, email, flat, landmark, city, area, mobile;
    Button add;

    RecyclerView orders;

    ProgressBar progress;
    ImageButton back;
    TextView grand;

    float tamount = 0, gtotal = 0;
    boolean fdel = true;
    float pvalue = 0;
    String pid = "";
    float dvalue = 0;
    ProductAdapter adapter;
    GridLayoutManager manager;
    List<Cart> list;

    ConstraintLayout checkout;

    boolean address = false;

    String json = "[]";

    float genie = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        list = new ArrayList<>();

        amount = findViewById(R.id.textView3);
        saving = findViewById(R.id.textView5);
        total = findViewById(R.id.textView11);
        geniecash = findViewById(R.id.textView7);
        delivery = findViewById(R.id.radioGroup);
        free = findViewById(R.id.free);
        express = findViewById(R.id.express);
        promo = findViewById(R.id.editText2);
        apply = findViewById(R.id.button);
        applied = findViewById(R.id.textView15);


        name = findViewById(R.id.editText6);
        email = findViewById(R.id.editText7);
        flat = findViewById(R.id.editText3);
        landmark = findViewById(R.id.editText4);
        city = findViewById(R.id.autoCompleteTextView);
        area = findViewById(R.id.autoCompleteTextView2);
        mobile = findViewById(R.id.editText5);
        add = findViewById(R.id.button2);

        orders = findViewById(R.id.grid);
        progress = findViewById(R.id.progressBar2);
        back = findViewById(R.id.imageButton);
        grand = findViewById(R.id.grand);
        checkout = findViewById(R.id.textView16);


        adapter = new ProductAdapter(this, list);
        manager = new GridLayoutManager(this, 1);

        orders.setAdapter(adapter);
        orders.setLayoutManager(manager);


        List<String> fr = new ArrayList<>();
        List<String> ex = new ArrayList<>();

        fr.add("8-10PM");

        ex.add("90 MIN");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.simple_spinner_item, fr);

        free.setAdapter(adapter);


        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this,
                R.layout.simple_spinner_item, ex);
        express.setAdapter(adapter2);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();

            }
        });

        delivery.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                int iidd = group.getCheckedRadioButtonId();
                if (iidd == R.id.f) {
                    fdel = true;
                    updateSummary();
                } else {
                    fdel = false;
                    updateSummary();
                }

            }
        });

        geniecash.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    genie = 10;
                } else {
                    genie = 0;
                }

            }
        });






        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String pc = promo.getText().toString();

                if (pc.length() > 0) {

                    apply.setEnabled(false);
                    apply.setClickable(false);

                    promo.setEnabled(false);
                    promo.setClickable(false);

                    progress.setVisibility(View.VISIBLE);

                    Bean b = (Bean) getApplicationContext();

                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(b.baseurl)
                            .addConverterFactory(ScalarsConverterFactory.create())
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();

                    AllApiIneterface cr = retrofit.create(AllApiIneterface.class);

                    Call<checkPromoBean> call = cr.checkPromoCode(pc, SharePreferenceUtils.getInstance().getString("userId"));

                    call.enqueue(new Callback<checkPromoBean>() {
                        @Override
                        public void onResponse(Call<checkPromoBean> call, Response<checkPromoBean> response) {

                            if (response.body().getStatus().equals("1")) {

                                float dis = Float.parseFloat(response.body().getData().getDiscount());

                                pvalue = (dis / 100) * tamount;

                                pid = response.body().getData().getPid();

                                applied.setText("PROMO Code applied worth ₹" + pvalue);
                                applied.setVisibility(View.VISIBLE);

                                updateSummary();

                                Toast.makeText(Checkout.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                            } else {

                                pvalue = 0;
                                pid = "";

                                Toast.makeText(Checkout.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                apply.setEnabled(true);
                                apply.setClickable(true);
                                applied.setVisibility(View.GONE);
                                promo.setEnabled(true);
                                promo.setClickable(true);
                            }

                            progress.setVisibility(View.GONE);

                        }

                        @Override
                        public void onFailure(Call<checkPromoBean> call, Throwable t) {
                            pvalue = 0;
                            pid = "";
                            progress.setVisibility(View.GONE);
                            apply.setEnabled(true);
                            apply.setClickable(true);
                            applied.setVisibility(View.GONE);
                            promo.setEnabled(true);
                            promo.setClickable(true);
                        }
                    });

                } else {
                    Toast.makeText(Checkout.this, "Invalid PROMO code", Toast.LENGTH_SHORT).show();
                }

            }
        });


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String te = add.getText().toString();

                if (te.equals("ADD")) {

                    String n = name.getText().toString();
                    String e = email.getText().toString();
                    String f = flat.getText().toString();
                    String l = landmark.getText().toString();
                    String c = city.getText().toString();
                    String a = area.getText().toString();
                    String m = mobile.getText().toString();


                    if (n.length() > 0) {
                        if (e.length() > 0) {


                            if (f.length() > 0) {
                                if (l.length() > 0) {
                                    if (c.length() > 0) {
                                        if (a.length() > 0) {


                                                progress.setVisibility(View.VISIBLE);

                                                Bean b = (Bean) getApplicationContext();

                                                Retrofit retrofit = new Retrofit.Builder()
                                                        .baseUrl(b.baseurl)
                                                        .addConverterFactory(ScalarsConverterFactory.create())
                                                        .addConverterFactory(GsonConverterFactory.create())
                                                        .build();

                                                AllApiIneterface cr = retrofit.create(AllApiIneterface.class);

                                                Call<addMessageBean> call = cr.addAddress(
                                                        SharePreferenceUtils.getInstance().getString("userId"),
                                                        n,
                                                        e,
                                                        f,
                                                        l,
                                                        c,
                                                        a,
                                                        m
                                                );

                                                call.enqueue(new Callback<addMessageBean>() {
                                                    @Override
                                                    public void onResponse(Call<addMessageBean> call, Response<addMessageBean> response) {

                                                        loadCart();

                                                        progress.setVisibility(View.GONE);

                                                    }

                                                    @Override
                                                    public void onFailure(Call<addMessageBean> call, Throwable t) {
                                                        progress.setVisibility(View.GONE);
                                                    }
                                                });


                                        } else {
                                            Toast.makeText(Checkout.this, "Invalid Area", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        Toast.makeText(Checkout.this, "Invalid City", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(Checkout.this, "Invalid Landmark and Locality", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(Checkout.this, "Invalid Flat no./ Floor/ Building Name", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(Checkout.this, "Invalid Email", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(Checkout.this, "Invalid Name", Toast.LENGTH_SHORT).show();
                    }


                } else if (te.equals("CHANGE")) {
                    name.setEnabled(true);
                    email.setEnabled(true);
                    flat.setEnabled(true);
                    landmark.setEnabled(true);
                    //city.setEnabled(true);
                    //area.setEnabled(true);
                    mobile.setEnabled(true);

                    name.setClickable(true);
                    email.setClickable(true);
                    flat.setClickable(true);
                    landmark.setClickable(true);
                    city.setClickable(true);
                    area.setClickable(true);
                    mobile.setClickable(true);

                    add.setText("UPDATE");
                } else {


                    String n = name.getText().toString();
                    String e = email.getText().toString();
                    String f = flat.getText().toString();
                    String l = landmark.getText().toString();
                    String c = city.getText().toString();
                    String a = area.getText().toString();
                    String m = mobile.getText().toString();



                    if (n.length() > 0) {
                        if (e.length() > 0) {


                            if (f.length() > 0) {
                                if (l.length() > 0) {
                                    if (c.length() > 0) {
                                        if (a.length() > 0) {


                                                progress.setVisibility(View.VISIBLE);

                                                Bean b = (Bean) getApplicationContext();

                                                Retrofit retrofit = new Retrofit.Builder()
                                                        .baseUrl(b.baseurl)
                                                        .addConverterFactory(ScalarsConverterFactory.create())
                                                        .addConverterFactory(GsonConverterFactory.create())
                                                        .build();

                                                AllApiIneterface cr = retrofit.create(AllApiIneterface.class);

                                                Call<addMessageBean> call = cr.updateAddress(
                                                        SharePreferenceUtils.getInstance().getString("userId"),
                                                        n,
                                                        e,
                                                        f,
                                                        l,
                                                        c,
                                                        a,
                                                        m
                                                );

                                                call.enqueue(new Callback<addMessageBean>() {
                                                    @Override
                                                    public void onResponse(Call<addMessageBean> call, Response<addMessageBean> response) {

                                                        loadCart();

                                                        progress.setVisibility(View.GONE);

                                                    }

                                                    @Override
                                                    public void onFailure(Call<addMessageBean> call, Throwable t) {
                                                        progress.setVisibility(View.GONE);
                                                    }
                                                });


                                        } else {
                                            Toast.makeText(Checkout.this, "Invalid Area", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        Toast.makeText(Checkout.this, "Invalid City", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(Checkout.this, "Invalid Landmark and Locality", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(Checkout.this, "Invalid Flat no./ Floor/ Building Name", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(Checkout.this, "Invalid Email", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(Checkout.this, "Invalid Name", Toast.LENGTH_SHORT).show();
                    }

                }

            }
        });

        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {





                if (address) {


                    progress.setVisibility(View.VISIBLE);

                    Bean b = (Bean) getApplicationContext();

                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(b.baseurl)
                            .addConverterFactory(ScalarsConverterFactory.create())
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();

                    AllApiIneterface cr = retrofit.create(AllApiIneterface.class);

                    Call<addMessageBean> call = cr.checkout(
                            SharePreferenceUtils.getInstance().getString("userId"),
                            json,
                            String.valueOf(tamount),
                            String.valueOf(offlineCartBean.getTotalSaved()),
                            String.valueOf(genie),
                            String.valueOf(dvalue),
                            "",
                            pid,
                            String.valueOf(pvalue),
                            String.valueOf(gtotal),
                            name.getText().toString(),
                            email.getText().toString(),
                            flat.getText().toString(),
                            landmark.getText().toString(),
                            city.getText().toString(),
                            area.getText().toString(),
                            mobile.getText().toString()
                    );

                    call.enqueue(new Callback<addMessageBean>() {
                        @Override
                        public void onResponse(Call<addMessageBean> call, Response<addMessageBean> response) {

                            if (response.body().getStatus().equals("1")) {

                                offlineCartBean.totalAmount = 0;
                                offlineCartBean.totalItem = 0;
                                offlineCartBean.totalSaved = 0;
                                offlineCartBean.cartitems.clear();

                                Toast.makeText(Checkout.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                                final Dialog dialog = new Dialog(Checkout.this);
                                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                dialog.setCancelable(true);
                                dialog.setContentView(R.layout.final_popup);
                                dialog.show();

                                Button ok = dialog.findViewById(R.id.button3);
                                Button cs = dialog.findViewById(R.id.button4);

                                ok.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialog.dismiss();
                                        finish();
                                    }
                                });

                                cs.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialog.dismiss();
                                        finish();
                                    }
                                });

                                dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                                    @Override
                                    public void onCancel(DialogInterface dialog) {
                                        finish();
                                    }
                                });

                            } else {
                                Toast.makeText(Checkout.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            }

                            progress.setVisibility(View.GONE);

                        }

                        @Override
                        public void onFailure(Call<addMessageBean> call, Throwable t) {
                            progress.setVisibility(View.GONE);
                        }
                    });


                } else {
                    Toast.makeText(Checkout.this, "Please Add an Address", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        Tovuti.from(this).monitor(new Monitor.ConnectivityListener(){
            @Override
            public void onConnectivityChanged(int connectionType, boolean isConnected, boolean isFast){
                // TODO: Handle the connection...
                if (isConnected)
                {
                    loadCart();

                    updateSummary();
                }
                else
                {
                    Toast.makeText(Checkout.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    void loadCart() {

        List<String> clist = offlineCartBean.cartitems;

        List<String> nlist = new ArrayList<>(clist);

        final Set<String> sslist = new LinkedHashSet<>(nlist);
        nlist = new ArrayList<>(sslist);

        List<cartRequestPOJO> reqlist = new ArrayList<>();

        for (int i = 0; i < nlist.size(); i++) {

            cartRequestPOJO item = new cartRequestPOJO();
            item.setPid(nlist.get(i));
            item.setQuantity(String.valueOf(offlineCartBean.getCount(nlist.get(i))));
            reqlist.add(item);

        }

        Gson gson = new Gson();
        json = gson.toJson(reqlist);

        Log.d("reqlist", json);


        progress.setVisibility(View.VISIBLE);

        Bean b = (Bean) getApplicationContext();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(b.baseurl)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        AllApiIneterface cr = retrofit.create(AllApiIneterface.class);

        Call<cartBean> call = cr.getCartData(json, SharePreferenceUtils.getInstance().getString("userId"));

        call.enqueue(new Callback<cartBean>() {
            @Override
            public void onResponse(Call<cartBean> call, Response<cartBean> response) {

                if (response.body().getCart().size() > 0) {
                    list = response.body().getCart();
                    adapter.setData(list);

                    if (response.body().getAddress().size() > 0) {
                        address = true;

                        name.setText(response.body().getAddress().get(0).getName());
                        email.setText(response.body().getAddress().get(0).getEmail());
                        flat.setText(response.body().getAddress().get(0).getFlat());
                        landmark.setText(response.body().getAddress().get(0).getLandmark());
                        city.setText(response.body().getAddress().get(0).getCity());
                        area.setText(response.body().getAddress().get(0).getArea());
                        mobile.setText(response.body().getAddress().get(0).getMobile());

                        name.setEnabled(false);
                        email.setEnabled(false);
                        flat.setEnabled(false);
                        landmark.setEnabled(false);
                        city.setEnabled(false);
                        area.setEnabled(false);
                        mobile.setEnabled(false);

                        name.setClickable(false);
                        email.setClickable(false);
                        flat.setClickable(false);
                        landmark.setClickable(false);
                        city.setClickable(false);
                        area.setClickable(false);
                        mobile.setClickable(false);

                        add.setText("CHANGE");

                    } else {
                        address = false;

                        name.setEnabled(true);
                        email.setEnabled(true);
                        flat.setEnabled(true);
                        landmark.setEnabled(true);
                        //city.setEnabled(true);
                        //area.setEnabled(true);
                        mobile.setEnabled(true);

                        name.setClickable(true);
                        email.setClickable(true);
                        flat.setClickable(true);
                        landmark.setClickable(true);
                        //city.setClickable(true);
                        //area.setClickable(true);
                        mobile.setClickable(true);

                        add.setText("ADD");

                    }

                } else {
                    Toast.makeText(Checkout.this, "Cart is Empty", Toast.LENGTH_SHORT).show();
                    finish();
                }


                progress.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(Call<cartBean> call, Throwable t) {
                progress.setVisibility(View.GONE);
            }
        });

    }

    void updateSummary() {
        tamount = offlineCartBean.getTotalAmount();

        amount.setText("₹" + offlineCartBean.getTotalAmount());
        //totalItemTextView.setText(" X " + offlineCartBean.getTotalItem() + " item");
        saving.setText("₹" + offlineCartBean.getTotalSaved());

        gtotal = tamount;
        if (!fdel) {
            dvalue = 10;
            gtotal = gtotal + 10;
        } else {
            dvalue = 0;
        }
        gtotal = gtotal - pvalue;

        total.setText("₹" + gtotal);
        grand.setText("₹" + gtotal);

    }


    class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {
        Context context;
        List<Cart> list = new ArrayList<>();

        ProductAdapter(Context context, List<Cart> list) {
            this.context = context;
            this.list = list;
        }

        void setData(List<Cart> list) {
            this.list = list;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.sub_list_model, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
            holder.setIsRecyclable(false);
            final Cart item = list.get(position);


            String imageUrl = context.getResources().getString(R.string.serverUrl) + "/image/" + item.getImageName();

            holder.name.setText(item.getPname());
            DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).resetViewBeforeLoading(false).build();
            ImageLoader loader = ImageLoader.getInstance();
            loader.displayImage(imageUrl, holder.image, options);

            final int productTotalPrice = Integer.valueOf(item.getSellingPrice());
            int discount = Integer.valueOf(item.getDiscount());
            final int finalPrice = productTotalPrice - ((productTotalPrice * discount) / 100);

            holder.dprice.setText("₹" + finalPrice);
            holder.sprice.setText(Html.fromHtml("₹<strike>" + productTotalPrice + "</strike>"));

            holder.discount.setVisibility(View.INVISIBLE);


            if (offlineCartBean.cartitems.contains(item.getPid())) {
                int c = offlineCartBean.getCount(item.getPid());
                holder.addButton.setVisibility(View.VISIBLE);
                holder.removeButton.setVisibility(View.VISIBLE);
                holder.itemCount.setText(String.valueOf(c));
            } else {
                holder.addButton.setVisibility(View.VISIBLE);
                holder.removeButton.setVisibility(View.GONE);
                holder.itemCount.setText("Add");

                loadCart();

            }

            holder.addButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    offlineCartBean.cartitems.add(item.getPid());
                    offlineCartBean.totalAmount += Float.valueOf(finalPrice);
                    offlineCartBean.totalItem += 1;
                    offlineCartBean.totalSaved += Float.valueOf(productTotalPrice - finalPrice);
                    notifyDataSetChanged();

                    updateSummary();

                }
            });

            holder.removeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    offlineCartBean.cartitems.remove(item.getPid());
                    offlineCartBean.totalAmount -= Float.valueOf(finalPrice);
                    offlineCartBean.totalItem -= 1;
                    offlineCartBean.totalSaved -= Float.valueOf(productTotalPrice - finalPrice);
                    notifyDataSetChanged();

                    updateSummary();

                }
            });

        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            ImageView image;
            TextView sprice, dprice, name, discount, itemCount;
            ImageButton addButton, removeButton;

            ViewHolder(@NonNull View itemView) {
                super(itemView);

                image = itemView.findViewById(R.id.imageProductCard);
                sprice = itemView.findViewById(R.id.productAmountTotal);
                dprice = itemView.findViewById(R.id.productAmountWithDiscount);
                name = itemView.findViewById(R.id.titleProductInfo);
                discount = itemView.findViewById(R.id.productDiscount);
                addButton = itemView.findViewById(R.id.itemPlusButton);
                removeButton = itemView.findViewById(R.id.itemMinusButton);
                itemCount = itemView.findViewById(R.id.itemCoutnButton);

            }
        }
    }

    @Override
    protected void onStop(){
        Tovuti.from(this).stop();
        super.onStop();
    }

}
