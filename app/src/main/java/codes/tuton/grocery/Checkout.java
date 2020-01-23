package codes.tuton.grocery;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
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

import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import codes.tuton.grocery.cartPOJO.Cart;
import codes.tuton.grocery.cartPOJO.cartBean;
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

    EditText flat, landmark, city, area, mobile;
    Button add;

    RecyclerView orders;

    ProgressBar progress;
    ImageButton back;
    TextView grand;

    float tamount = 0, gtotal = 0;
    boolean fdel = true;
    float pvalue = 0;

    String android_id;
    ProductAdapter adapter;
    GridLayoutManager manager;
    List<Cart> list;

    RelativeLayout checkout;

    boolean address = false;

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

        android_id = Settings.Secure.getString(getContentResolver(),
                Settings.Secure.ANDROID_ID);

        adapter = new ProductAdapter(this, list);
        manager = new GridLayoutManager(this, 1);

        orders.setAdapter(adapter);
        orders.setLayoutManager(manager);


        List<String> fr = new ArrayList<>();
        List<String> ex = new ArrayList<>();

        fr.add("8PM-10PM");

        ex.add("90 MIN");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, fr);

        free.setAdapter(adapter);


        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, ex);
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

        loadCart();

        updateSummary();

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String te = add.getText().toString();

                if (te.equals("ADD")) {

                    String f = flat.getText().toString();
                    String l = landmark.getText().toString();
                    String c = city.getText().toString();
                    String a = area.getText().toString();
                    String m = mobile.getText().toString();

                    if (f.length() > 0) {
                        if (l.length() > 0) {
                            if (c.length() > 0) {
                                if (a.length() > 0) {
                                    if (m.length() == 10) {


                                        progress.setVisibility(View.VISIBLE);

                                        Bean b = (Bean) getApplicationContext();

                                        Retrofit retrofit = new Retrofit.Builder()
                                                .baseUrl(b.baseurl)
                                                .addConverterFactory(ScalarsConverterFactory.create())
                                                .addConverterFactory(GsonConverterFactory.create())
                                                .build();

                                        AllApiIneterface cr = retrofit.create(AllApiIneterface.class);

                                        Call<addMessageBean> call = cr.addAddress(
                                                android_id,
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
                                        Toast.makeText(Checkout.this, "Invalid Mobile Number", Toast.LENGTH_SHORT).show();
                                    }
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

                } else if (te.equals("CHANGE")) {
                    flat.setEnabled(true);
                    landmark.setEnabled(true);
                    city.setEnabled(true);
                    area.setEnabled(true);
                    mobile.setEnabled(true);

                    flat.setClickable(true);
                    landmark.setClickable(true);
                    city.setClickable(true);
                    area.setClickable(true);
                    mobile.setClickable(true);

                    add.setText("UPDATE");
                } else {

                    String f = flat.getText().toString();
                    String l = landmark.getText().toString();
                    String c = city.getText().toString();
                    String a = area.getText().toString();
                    String m = mobile.getText().toString();

                    if (f.length() > 0) {
                        if (l.length() > 0) {
                            if (c.length() > 0) {
                                if (a.length() > 0) {
                                    if (m.length() == 10) {


                                        progress.setVisibility(View.VISIBLE);

                                        Bean b = (Bean) getApplicationContext();

                                        Retrofit retrofit = new Retrofit.Builder()
                                                .baseUrl(b.baseurl)
                                                .addConverterFactory(ScalarsConverterFactory.create())
                                                .addConverterFactory(GsonConverterFactory.create())
                                                .build();

                                        AllApiIneterface cr = retrofit.create(AllApiIneterface.class);

                                        Call<addMessageBean> call = cr.updateAddress(
                                                android_id,
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
                                        Toast.makeText(Checkout.this, "Invalid Mobile Number", Toast.LENGTH_SHORT).show();
                                    }
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

                }

            }
        });

        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (address) {

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
                        }
                    });

                    cs.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            finish();
                        }
                    });

                } else {
                    Toast.makeText(Checkout.this, "Please Add an Address", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    void loadCart() {

        List<String> clist = offlineCartBean.cartitems;

        List<String> nlist = new ArrayList<>(clist);

        final Set<String> sslist = new HashSet<>(nlist);
        nlist = new ArrayList<>(sslist);

        List<cartRequestPOJO> reqlist = new ArrayList<>();

        for (int i = 0; i < nlist.size(); i++) {

            cartRequestPOJO item = new cartRequestPOJO();
            item.setPid(nlist.get(i));
            item.setQuantity(String.valueOf(offlineCartBean.getCount(nlist.get(i))));
            reqlist.add(item);

        }

        Gson gson = new Gson();
        String json = gson.toJson(reqlist);

        Log.d("reqlist", json);


        progress.setVisibility(View.VISIBLE);

        Bean b = (Bean) getApplicationContext();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(b.baseurl)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        AllApiIneterface cr = retrofit.create(AllApiIneterface.class);

        Call<cartBean> call = cr.getCartData(json, android_id);

        call.enqueue(new Callback<cartBean>() {
            @Override
            public void onResponse(Call<cartBean> call, Response<cartBean> response) {

                if (response.body().getCart().size() > 0) {
                    list = response.body().getCart();
                    adapter.setData(list);

                    if (response.body().getAddress().size() > 0) {
                        address = true;

                        flat.setText(response.body().getAddress().get(0).getFlat());
                        landmark.setText(response.body().getAddress().get(0).getLandmark());
                        city.setText(response.body().getAddress().get(0).getCity());
                        area.setText(response.body().getAddress().get(0).getArea());
                        mobile.setText(response.body().getAddress().get(0).getMobile());

                        flat.setEnabled(false);
                        landmark.setEnabled(false);
                        city.setEnabled(false);
                        area.setEnabled(false);
                        mobile.setEnabled(false);

                        flat.setClickable(false);
                        landmark.setClickable(false);
                        city.setClickable(false);
                        area.setClickable(false);
                        mobile.setClickable(false);

                        add.setText("CHANGE");

                    } else {
                        address = false;

                        flat.setEnabled(true);
                        landmark.setEnabled(true);
                        city.setEnabled(true);
                        area.setEnabled(true);
                        mobile.setEnabled(true);

                        flat.setClickable(true);
                        landmark.setClickable(true);
                        city.setClickable(true);
                        area.setClickable(true);
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
            gtotal = gtotal + 10;
        }
        gtotal = gtotal + pvalue;

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

}
