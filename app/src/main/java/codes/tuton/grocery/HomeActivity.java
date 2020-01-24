package codes.tuton.grocery;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import codes.tuton.grocery.offlineCartPOJO.offlineCartBean;
import codes.tuton.grocery.productListPOJO.ProductInfo;
import codes.tuton.grocery.productListPOJO.productListBean;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class HomeActivity extends AppCompatActivity {

    RecyclerView grid;
    ProgressBar progress;
    GridLayoutManager manager;
    List<productListBean> list;
    ProductAdapter adapter;
    TextView totalFinalMount, totalSavedAmount, totalItemTextView;
    EditText search;
    Button checkout;
    LinearLayout bottom;
    TextView count;
    ImageButton cart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        list = new ArrayList<>();
        grid = findViewById(R.id.grid);
        progress = findViewById(R.id.progressBar);
        totalFinalMount = findViewById(R.id.totalAmountTextView);
        totalSavedAmount = findViewById(R.id.savedAmountTextView);
        totalItemTextView = findViewById(R.id.totalItemTextView);
        search = findViewById(R.id.editText);
        checkout = findViewById(R.id.checkOutButton);
        bottom = findViewById(R.id.linearLayout2);
        count = findViewById(R.id.textView19);
        cart = findViewById(R.id.imageButton2);


        adapter = new ProductAdapter(this , list);
        manager = new GridLayoutManager(this , 1);

        grid.setAdapter(adapter);
        grid.setLayoutManager(manager);



        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.length() > 0)
                {
                    progress.setVisibility(View.VISIBLE);

                    Bean b = (Bean) getApplicationContext();

                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(b.baseurl)
                            .addConverterFactory(ScalarsConverterFactory.create())
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();

                    AllApiIneterface cr = retrofit.create(AllApiIneterface.class);

                    Call<List<productListBean>> call = cr.search(s.toString());
                    call.enqueue(new Callback<List<productListBean>>() {
                        @Override
                        public void onResponse(Call<List<productListBean>> call, Response<List<productListBean>> response) {

                            adapter.setData(response.body());

                            progress.setVisibility(View.GONE);

                        }

                        @Override
                        public void onFailure(Call<List<productListBean>> call, Throwable t) {
                            t.printStackTrace();
                            progress.setVisibility(View.GONE);
                        }
                    });
                }
                else
                {
                    adapter.setData(list);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                List<String> clist = offlineCartBean.cartitems;

                List<String> nlist = new ArrayList<>(clist);

                Set<String> sslist = new HashSet<>(nlist);
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

                Log.d("reqlist" , json);

                //Toast.makeText(HomeActivity.this, json, Toast.LENGTH_LONG).show();

                Intent intent = new Intent(HomeActivity.this , Checkout.class);
                startActivity(intent);

            }
        });

        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                if (offlineCartBean.getTotalItem() > 0)
                {
                    Intent intent = new Intent(HomeActivity.this , Checkout.class);
                    startActivity(intent);
                }
                else
                {
                    Toast.makeText(HomeActivity.this, "Cart is Empty", Toast.LENGTH_SHORT).show();
                }

                //Toast.makeText(HomeActivity.this, json, Toast.LENGTH_LONG).show();



            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        loaddata();
        updateCart();
    }

    void loaddata()
    {
        progress.setVisibility(View.VISIBLE);

        Bean b = (Bean) getApplicationContext();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(b.baseurl)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        AllApiIneterface cr = retrofit.create(AllApiIneterface.class);

        Call<List<productListBean>> call = cr.CategoryAllData();
        call.enqueue(new Callback<List<productListBean>>() {
            @Override
            public void onResponse(Call<List<productListBean>> call, Response<List<productListBean>> response) {

                list = response.body();
                adapter.setData(list);

                progress.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(Call<List<productListBean>> call, Throwable t) {
                t.printStackTrace();
                progress.setVisibility(View.GONE);
            }
        });
    }

    class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder>
    {
        Context context;
        List<productListBean> list = new ArrayList<>();

        ProductAdapter(Context context, List<productListBean> list)
        {
            this.context = context;
            this.list = list;
        }

        void setData(List<productListBean> list)
        {
            this.list = list;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.product_list_model , parent , false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
            holder.setIsRecyclable(false);
            final productListBean item = list.get(position);



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

            if (item.getProductInfo().size() > 0) {

                holder.dprice.setVisibility(View.GONE);
                holder.sprice.setVisibility(View.GONE);
                holder.expand.setVisibility(View.VISIBLE);
                holder.grid.setVisibility(View.VISIBLE);
                holder.addLayout.setVisibility(View.GONE);

                SublistAdapter adapter1 = new SublistAdapter(context, item.getProductInfo(), holder.discount);
                GridLayoutManager manager = new GridLayoutManager(context, 1);
                holder.grid.setAdapter(adapter1);
                holder.grid.setLayoutManager(manager);


            } else {
                holder.dprice.setVisibility(View.VISIBLE);
                holder.sprice.setVisibility(View.VISIBLE);
                holder.expand.setVisibility(View.GONE);
                holder.grid.setVisibility(View.GONE);
                holder.addLayout.setVisibility(View.VISIBLE);

                holder.discount.setText(discount + "% OFF");

            }


            holder.expand.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (holder.grid.getVisibility() == View.VISIBLE) {
                        holder.grid.setVisibility(View.GONE);
                        holder.expand.setText("VIEW MORE");
                    } else {
                        holder.grid.setVisibility(View.VISIBLE);
                        holder.expand.setText("VIEW LESS");
                    }

                }
            });

            if (offlineCartBean.cartitems.contains(item.getPid()))
            {
                int c = offlineCartBean.getCount(item.getPid());
                holder.addButton.setVisibility(View.VISIBLE);
                holder.removeButton.setVisibility(View.VISIBLE);
                holder.itemCount.setText(String.valueOf(c));
            }
            else
            {
                holder.addButton.setVisibility(View.VISIBLE);
                holder.removeButton.setVisibility(View.GONE);
                holder.itemCount.setText("Add");
            }

            holder.addButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    offlineCartBean.cartitems.add(item.getPid());
                    offlineCartBean.totalAmount += Float.valueOf(finalPrice);
                    offlineCartBean.totalItem += 1;
                    offlineCartBean.totalSaved += Float.valueOf(productTotalPrice - finalPrice);
                    notifyDataSetChanged();

                    updateCart();

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

                    updateCart();

                }
            });

        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder
        {
            Button expand;
            ImageView image;
            TextView sprice , dprice , name , discount , itemCount;
            RecyclerView grid;
            LinearLayout addLayout;
            ImageButton addButton, removeButton;

            ViewHolder(@NonNull View itemView) {
                super(itemView);

                image = itemView.findViewById(R.id.imageProductCard);
                sprice = itemView.findViewById(R.id.productAmountTotal);
                dprice = itemView.findViewById(R.id.productAmountWithDiscount);
                name = itemView.findViewById(R.id.titleProductInfo);
                discount = itemView.findViewById(R.id.productDiscount);
                expand = itemView.findViewById(R.id.categoryViewLess);
                grid = itemView.findViewById(R.id.grid);
                addLayout = itemView.findViewById(R.id.linearLayout3);
                addButton = itemView.findViewById(R.id.itemPlusButton);
                removeButton = itemView.findViewById(R.id.itemMinusButton);
                itemCount = itemView.findViewById(R.id.itemCoutnButton);

            }
        }
    }

    class SublistAdapter extends RecyclerView.Adapter<SublistAdapter.ViewHolder>
    {
        Context context;
        List<ProductInfo> list = new ArrayList<>();
        private TextView textViewDiscount;
        private int discount1 = 0;

        SublistAdapter(Context context, List<ProductInfo> list , TextView textViewDiscount)
        {
            this.context = context;
            this.list = list;
            this.textViewDiscount = textViewDiscount;
        }


        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.sub_list_model , parent , false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.setIsRecyclable(false);
            final ProductInfo item = list.get(position);



            String imageUrl = context.getResources().getString(R.string.serverUrl)+"/image/" + item.getImageName();

            holder.name.setText(item.getPname());
            DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).resetViewBeforeLoading(false).build();
            ImageLoader loader = ImageLoader.getInstance();
            loader.displayImage(imageUrl , holder.image , options);




            final int productTotalPrice = Integer.valueOf(item.getSellingPrice());
            int discount = Integer.valueOf(item.getDiscount());
            final int finalPrice = productTotalPrice - ((productTotalPrice * discount) / 100);

            if (this.discount1 <= discount) {
                this.discount1 = discount;
                Log.d("dduucc" , String.valueOf(this.discount1));
                //this.textViewDiscount.setText("UP TO " + this.discount + "% OFF");
                this.textViewDiscount.setText("UP TO " + this.discount1 + "% OFF");
            }

            holder.dprice.setText("₹" + finalPrice);
            holder.sprice.setText(Html.fromHtml("₹<strike>" + productTotalPrice + "</strike>"));
            holder.discount.setText(discount + "% OFF");

            if (offlineCartBean.cartitems.contains(item.getPid()))
            {
                int c = offlineCartBean.getCount(item.getPid());
                holder.addButton.setVisibility(View.VISIBLE);
                holder.removeButton.setVisibility(View.VISIBLE);
                holder.itemCount.setText(String.valueOf(c));
            }
            else
            {
                holder.addButton.setVisibility(View.VISIBLE);
                holder.removeButton.setVisibility(View.GONE);
                holder.itemCount.setText("Add");
            }

            holder.addButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    offlineCartBean.cartitems.add(item.getPid());
                    offlineCartBean.totalAmount += Float.valueOf(finalPrice);
                    offlineCartBean.totalItem += 1;
                    offlineCartBean.totalSaved += Float.valueOf(productTotalPrice - finalPrice);
                    notifyDataSetChanged();

                    updateCart();

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

                    updateCart();

                }
            });

        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder
        {
            ImageView image;
            TextView sprice , dprice , name , discount , itemCount;
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

    void updateCart()
    {

        if (offlineCartBean.getTotalItem() > 0)
        {
            totalFinalMount.setText("₹" + offlineCartBean.getTotalAmount());
            totalItemTextView.setText(" X " + offlineCartBean.getTotalItem() + " item");
            totalSavedAmount.setText("₹" + offlineCartBean.getTotalSaved());
            count.setText(String.valueOf(offlineCartBean.getTotalItem()));

            bottom.setVisibility(View.VISIBLE);

        }
        else
        {
            totalFinalMount.setText("₹" + offlineCartBean.getTotalAmount());
            totalItemTextView.setText(" X " + offlineCartBean.getTotalItem() + " item");
            count.setText(String.valueOf(offlineCartBean.getTotalItem()));
            totalSavedAmount.setText("₹" + offlineCartBean.getTotalSaved());
            bottom.setVisibility(View.GONE);
        }



    }

}
