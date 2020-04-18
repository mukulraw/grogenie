package app.grocegenie.grocery;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import app.grocegenie.grocery.ordersPOJO.Active;
import app.grocegenie.grocery.ordersPOJO.Prod;
import app.grocegenie.grocery.ordersPOJO.ordersBean;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class OrderDetails extends AppCompatActivity {

    TextView amount , saving , genie , orderid , delcharges , deldate , status , grand , date;
    EditText name, email, flat, landmark, city, area, mobile;

    RecyclerView orders;

    ProgressBar progress;
    ImageButton back;
    TextView title;

    String id , tit;
    ProductAdapter adapter;
    GridLayoutManager manager;
    List<Prod> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);

        id = getIntent().getStringExtra("id");
        tit = getIntent().getStringExtra("tit");

        list = new ArrayList<>();

        amount = findViewById(R.id.textView3);
        saving = findViewById(R.id.textView5);
        genie = findViewById(R.id.textView7);
        orderid = findViewById(R.id.textView9);
        grand = findViewById(R.id.textView11);
        delcharges = findViewById(R.id.textView34);
        deldate = findViewById(R.id.textView8);
        status = findViewById(R.id.textView37);
        date = findViewById(R.id.textView47);

        name = findViewById(R.id.editText6);
        email = findViewById(R.id.editText7);
        flat = findViewById(R.id.editText3);
        landmark = findViewById(R.id.editText4);
        city = findViewById(R.id.autoCompleteTextView);
        area = findViewById(R.id.autoCompleteTextView2);
        mobile = findViewById(R.id.editText5);

        orders = findViewById(R.id.grid);
        progress = findViewById(R.id.progressBar2);
        back = findViewById(R.id.imageButton);
        title = findViewById(R.id.textView);

        title.setText(tit);

        adapter = new ProductAdapter(this , list);
        manager = new GridLayoutManager(this , 1);

        orders.setAdapter(adapter);
        orders.setLayoutManager(manager);

        progress.setVisibility(View.VISIBLE);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Bean b = (Bean) getApplicationContext();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(b.baseurl)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        AllApiIneterface cr = retrofit.create(AllApiIneterface.class);
        Call<ordersBean> call = cr.getOrderDetails(id);

        call.enqueue(new Callback<ordersBean>() {
            @Override
            public void onResponse(Call<ordersBean> call, Response<ordersBean> response) {

                Active item = response.body().getActive().get(0);

                amount.setText(item.getAmount());
                float d = Float.parseFloat(item.getDelivery());

                if (d > 0)
                {
                    delcharges.setText("₹" + item.getDelivery());
                }
                else
                {
                    delcharges.setText("Free");
                }

                orderid.setText("GG" + String.format("%04d" , Integer.parseInt(item.getId())));

                deldate.setText(item.getDeliveryTime());
                grand.setText("₹" + item.getGrand());

                date.setText(item.getCreated());

                saving.setText(item.getSaving());
                genie.setText(item.getGeniecash());
                status.setText(item.getStatus());

                name.setText(item.getName());
                email.setText(item.getEmail());
                flat.setText(item.getFlat());
                landmark.setText(item.getLandmark());
                city.setText(item.getCity());
                area.setText(item.getArea());
                mobile.setText(item.getMobile());

                adapter.setData(item.getProds());

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


                progress.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(Call<ordersBean> call, Throwable t) {
                progress.setVisibility(View.GONE);
            }
        });


    }

    class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {
        Context context;
        List<Prod> list = new ArrayList<>();

        ProductAdapter(Context context, List<Prod> list) {
            this.context = context;
            this.list = list;
        }

        void setData(List<Prod> list) {
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
            final Prod item = list.get(position);


            String imageUrl = context.getResources().getString(R.string.serverUrl) + "/admin/upload/" + item.getImageName();

            holder.name.setText(item.getPname());
            DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).resetViewBeforeLoading(false).build();
            ImageLoader loader = ImageLoader.getInstance();
            loader.displayImage(imageUrl, holder.image, options);

            holder.discount.setVisibility(View.INVISIBLE);
            holder.addButton.setVisibility(View.GONE);
            holder.removeButton.setVisibility(View.GONE);
            holder.itemCount.setVisibility(View.GONE);
            holder.sprice.setVisibility(View.GONE);

            holder.dprice.setText("₹" + item.getAmount() +" X " + item.getQuantity());


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
