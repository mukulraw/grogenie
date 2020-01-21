package codes.tuton.grocery;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

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
    offlineCartBean cartBean;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        cartBean = new offlineCartBean();

        list = new ArrayList<>();
        grid = findViewById(R.id.grid);
        progress = findViewById(R.id.progressBar);
        totalFinalMount = findViewById(R.id.totalAmountTextView);
        totalSavedAmount = findViewById(R.id.savedAmountTextView);
        totalItemTextView = findViewById(R.id.totalItemTextView);


        adapter = new ProductAdapter(this , list);
        manager = new GridLayoutManager(this , 1);

        grid.setAdapter(adapter);
        grid.setLayoutManager(manager);

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
            holder.sprice.setText("₹" + productTotalPrice);

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
                int c = cartBean.getCount(item.getPid());
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
            holder.sprice.setText("₹" + productTotalPrice);
            holder.discount.setText(discount + "% OFF");

            if (offlineCartBean.cartitems.contains(item.getPid()))
            {
                int c = cartBean.getCount(item.getPid());
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

        totalFinalMount.setText("₹" + cartBean.getTotalAmount());
        totalItemTextView.setText(" X " + cartBean.getTotalItem() + " item");
        totalSavedAmount.setText("₹" + cartBean.getTotalSaved());

    }

}
