package app.grocegenie.grocery;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.badoualy.stepperindicator.StepperIndicator;

import java.util.ArrayList;
import java.util.List;

import app.grocegenie.grocery.ordersPOJO.Active;
import app.grocegenie.grocery.ordersPOJO.Previou;
import app.grocegenie.grocery.ordersPOJO.ordersBean;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class Orders extends AppCompatActivity {

    ImageButton back;
    RecyclerView active , previous;
    ActiveAdapter adapter;
    PreviousAdapter adapter2;
    List<Active> alist;
    List<Previou> plist;
    ProgressBar progress;
  /*  StepperIndicator indicator;
*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);

        alist = new ArrayList<>();
        plist = new ArrayList<>();

        progress = findViewById(R.id.progressBar2);
        back = findViewById(R.id.imageButton);
        active = findViewById(R.id.active);
        previous = findViewById(R.id.previous);
        adapter = new ActiveAdapter(this , alist);
        adapter2 = new PreviousAdapter(this , plist);
        GridLayoutManager manager = new GridLayoutManager(this , 1);
        GridLayoutManager manager2 = new GridLayoutManager(this , 1);

        active.setAdapter(adapter);
        active.setLayoutManager(manager);

        previous.setAdapter(adapter2);
        previous.setLayoutManager(manager2);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Orders.this , HomeActivity.class);
                startActivity(intent);
                finishAffinity();

            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();

        progress.setVisibility(View.VISIBLE);

        Bean b = (Bean) getApplicationContext();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(b.baseurl)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        AllApiIneterface cr = retrofit.create(AllApiIneterface.class);
        Call<ordersBean> call = cr.getOrders(SharePreferenceUtils.getInstance().getString("userId"));
        call.enqueue(new Callback<ordersBean>() {
            @Override
            public void onResponse(Call<ordersBean> call, Response<ordersBean> response) {

                adapter.setData(response.body().getActive());
                adapter2.setData(response.body().getPrevious());

                progress.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(Call<ordersBean> call, Throwable t) {
                progress.setVisibility(View.GONE);
            }
        });

    }

    class ActiveAdapter extends RecyclerView.Adapter<ActiveAdapter.ViewHolder>
    {

        Context context;
        List<Active> alist = new ArrayList<>();

        ActiveAdapter(Context context, List<Active> alist)
        {
            this.context = context;
            this.alist = alist;
        }


        void setData(List<Active> alist)
        {
            this.alist = alist;
            notifyDataSetChanged();
        }


        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.active_list_model , parent , false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

            final Active item = alist.get(position);

            holder.total.setText(String.valueOf(item.getProds().size()));


            try {
                float d = Float.parseFloat(item.getDelivery());

                if (d > 0)
                {
                    holder.delcharges.setText("₹" + item.getDelivery());
                }
                else
                {
                    holder.delcharges.setText("Free");
                }

            }catch (Exception e)
            {
                e.printStackTrace();
                holder.delcharges.setText("Free");
            }




            holder.orderid.setText("GG" + String.format("%04d" , Integer.parseInt(item.getId())));

            holder.deldate.setText(item.getDeliveryTime());
            holder.grand.setText("₹" + item.getGrand());

            String[] labels = {
                    "Placed",
                    "Accepted",
                    "In transit",
                    "Delivered"
            };

            holder.date.setText(item.getCreated());

            holder.indicator.setStepCount(4);
            holder.indicator.setLabels(labels);

            if (item.getStatus().equals("accepted"))
            {
                holder.indicator.setCurrentStep(2);
            }
            else if (item.getStatus().equals("intransit"))
            {
                holder.indicator.setCurrentStep(3);
            }
            else if(item.getStatus().equals("placed"))
            {
                holder.indicator.setCurrentStep(1);
            }
            else
            {
                holder.indicator.setCurrentStep(4);
            }


            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(context , OrderDetails.class);
                    intent.putExtra("id" , item.getId());
                    intent.putExtra("tit" , "GG" + String.format("%04d" , Integer.parseInt(item.getId())));
                    startActivity(intent);

                }
            });

            holder.help.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(context , Support.class);
                    startActivity(intent);

                }
            });

            holder.cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(context);

                    builder.setTitle("Cancel Order");
                    builder.setMessage("Are you sure you want to cancel this order?");

                    builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            // Do nothing but close the dialog
                            progress.setVisibility(View.VISIBLE);

                            dialog.dismiss();

                            Bean b = (Bean) getApplicationContext();

                            Retrofit retrofit = new Retrofit.Builder()
                                    .baseUrl(b.baseurl)
                                    .addConverterFactory(ScalarsConverterFactory.create())
                                    .addConverterFactory(GsonConverterFactory.create())
                                    .build();

                            AllApiIneterface cr = retrofit.create(AllApiIneterface.class);

                            Call<addMessageBean> call = cr.cancelOrder(
                                    item.getId()
                            );

                            call.enqueue(new Callback<addMessageBean>() {
                                @Override
                                public void onResponse(Call<addMessageBean> call, Response<addMessageBean> response) {

                                    onResume();

                                }

                                @Override
                                public void onFailure(Call<addMessageBean> call, Throwable t) {

                                }
                            });


                        }
                    });

                    builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            // Do nothing
                            dialog.dismiss();
                        }
                    });

                    AlertDialog alert = builder.create();
                    alert.show();

                }
            });



        }

        @Override
        public int getItemCount() {
            return alist.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder
        {
            TextView total , delcharges , orderid , deldate , grand , date;
            Button cancel , help;
            StepperIndicator indicator;

            ViewHolder(@NonNull View itemView) {
                super(itemView);

                indicator = itemView.findViewById(R.id.textView31);
                total = itemView.findViewById(R.id.textView24);
                delcharges = itemView.findViewById(R.id.textView25);
                orderid = itemView.findViewById(R.id.textView26);
                deldate = itemView.findViewById(R.id.textView27);
                grand = itemView.findViewById(R.id.textView30);
                cancel = itemView.findViewById(R.id.button5);
                help = itemView.findViewById(R.id.button6);
                date = itemView.findViewById(R.id.textView43);


            }
        }
    }


    class PreviousAdapter extends RecyclerView.Adapter<PreviousAdapter.ViewHolder>
    {

        Context context;
        List<Previou> plist = new ArrayList<>();

        PreviousAdapter(Context context, List<Previou> plist)
        {
            this.context = context;
            this.plist = plist;
        }


        void setData(List<Previou> plist)
        {
            this.plist = plist;
            notifyDataSetChanged();
        }


        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.previous_list_model , parent , false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

            final Previou item = plist.get(position);

            holder.total.setText(String.valueOf(item.getProds().size()));
            try {
                float d = Float.parseFloat(item.getDelivery());

                if (d > 0)
                {
                    holder.delcharges.setText("₹" + item.getDelivery());
                }
                else
                {
                    holder.delcharges.setText("Free");
                }

            }catch (Exception e)
            {
                e.printStackTrace();
                holder.delcharges.setText("Free");
            }

            holder.date.setText(item.getCreated());

            holder.orderid.setText("GG" + String.format("%04d" , Integer.parseInt(item.getId())));

            holder.deldate.setText(item.getDeliveryTime());
            holder.grand.setText("₹" + item.getGrand());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(context , OrderDetails.class);
                    intent.putExtra("id" , item.getId());
                    intent.putExtra("tit" , "GG" + String.format("%04d" , Integer.parseInt(item.getId())));
                    startActivity(intent);

                }
            });

            holder.status.setText(item.getStatus());

            holder.help.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(context , Support.class);
                    startActivity(intent);

                }
            });

        }

        @Override
        public int getItemCount() {
            return plist.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder
        {
            TextView total , delcharges , orderid , deldate , grand , status , date;
            Button cancel , help;


            ViewHolder(@NonNull View itemView) {
                super(itemView);

                total = itemView.findViewById(R.id.textView24);
                delcharges = itemView.findViewById(R.id.textView25);
                orderid = itemView.findViewById(R.id.textView26);
                deldate = itemView.findViewById(R.id.textView23);
                grand = itemView.findViewById(R.id.textView30);
                cancel = itemView.findViewById(R.id.button5);
                help = itemView.findViewById(R.id.button6);
                status = itemView.findViewById(R.id.textView27);
                date = itemView.findViewById(R.id.textView45);


            }
        }
    }

}
