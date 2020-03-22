package app.tuton.grocery;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.chaos.view.PinView;
import com.github.javiersantos.appupdater.AppUpdater;
import com.github.javiersantos.appupdater.enums.Display;
import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.phone.SmsRetrieverClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.stfalcon.smsverifycatcher.OnSmsCatchListener;
import com.stfalcon.smsverifycatcher.SmsVerifyCatcher;
import com.treebo.internetavailabilitychecker.InternetAvailabilityChecker;
import com.treebo.internetavailabilitychecker.InternetConnectivityListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import app.tuton.grocery.offlineCartPOJO.offlineCartBean;
import app.tuton.grocery.productListPOJO.ProductInfo;
import app.tuton.grocery.productListPOJO.productListBean;
import me.relex.circleindicator.CircleIndicator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class HomeActivity extends AppCompatActivity implements
        SMSReceiver.OTPReceiveListener, InternetConnectivityListener {

    RecyclerView grid;
    ProgressBar progress;
    GridLayoutManager manager;
    List<productListBean> list;
    ProductAdapter adapter;
    TextView totalFinalMount, totalSavedAmount, totalItemTextView;
    TextInputEditText search;
    Button checkout , collapse;
    LinearLayout bottom;
    TextView count , number;
    ImageButton cart , menu;
    DrawerLayout drawer;
    CircleIndicator indicator;
    ViewPager pager;
TabLayout tabs;
    TextView opencart , logout , orders , support , share , kirana , loved , smess;
    //SmsVerifyCatcher smsVerifyCatcher;

    PinView oottpp;
    private SMSReceiver smsReceiver;
    boolean kir = true;

    InternetAvailabilityChecker mInternetAvailabilityChecker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        InternetAvailabilityChecker.init(this);

        AppUpdater appUpdater = new AppUpdater(this);
        appUpdater.setDisplay(Display.NOTIFICATION);
        appUpdater.start();

        /*smsVerifyCatcher = new SmsVerifyCatcher(this, new OnSmsCatchListener<String>() {
            @Override
            public void onSmsCatch(String message) {

                try {
                    Log.d("message" , message);

                    String ot = message.substring(27,33);
                    //String code = parseCode(message);//Parse verification code
                    oottpp.setText(ot);
                }catch (Exception e)
                {
                    e.printStackTrace();
                }

                //set code in edit text
                //then you can send verification code to server
            }
        });*/



        list = new ArrayList<>();
        grid = findViewById(R.id.grid);
        progress = findViewById(R.id.progressBar);
        collapse = findViewById(R.id.button7);
        tabs = findViewById(R.id.linearLayout);
        pager = findViewById(R.id.viewPager);
        smess = findViewById(R.id.smess);
        loved = findViewById(R.id.loved);
        indicator = findViewById(R.id.textView12);
        totalFinalMount = findViewById(R.id.totalAmountTextView);
        totalSavedAmount = findViewById(R.id.savedAmountTextView);
        totalItemTextView = findViewById(R.id.totalItemTextView);
        search = findViewById(R.id.search);
        checkout = findViewById(R.id.checkOutButton);
        bottom = findViewById(R.id.linearLayout2);
        count = findViewById(R.id.textView19);
        cart = findViewById(R.id.imageButton2);
        menu = findViewById(R.id.imageButton);
        drawer = findViewById(R.id.drawer);
        opencart = findViewById(R.id.cart);
        number = findViewById(R.id.number);
        logout = findViewById(R.id.logout);
        orders = findViewById(R.id.orders);
        share = findViewById(R.id.share);
        support = findViewById(R.id.support);
        kirana = findViewById(R.id.kirana);


        adapter = new ProductAdapter(this , list);
        manager = new GridLayoutManager(this , 1);

        grid.setAdapter(adapter);
        grid.setLayoutManager(manager);

        startSMSListener();

        tabs.addTab(tabs.newTab().setText(getPageTitle1()));
        tabs.addTab(tabs.newTab().setText(getPageTitle2()));
        mInternetAvailabilityChecker = InternetAvailabilityChecker.getInstance();
        mInternetAvailabilityChecker.addInternetConnectivityListener(this);

        number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (SharePreferenceUtils.getInstance().getString("userId").length() == 0)
                {
                    drawer.closeDrawer(GravityCompat.START);

                    login();

                }

            }
        });



        kirana.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                drawer.closeDrawer(GravityCompat.START);
                tabs.getTabAt(0).select();

            }
        });

        loved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                drawer.closeDrawer(GravityCompat.START);
                tabs.getTabAt(1).select();

            }
        });


        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            FirebaseInstanceId.getInstance().deleteInstanceId();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();

                SharePreferenceUtils.getInstance().deletePref();
                Intent intent = new Intent(HomeActivity.this , Splash.class);
                startActivity(intent);
                finishAffinity();

            }
        });

        /*search.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if (!hasFocus)
                {
                    search.setText("");
                }

            }
        });*/

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

                            if (response.body().size() > 0)
                            {
                                smess.setVisibility(View.GONE);
                            }
                            else
                            {
                                smess.setVisibility(View.VISIBLE);
                            }

                            List<Boolean> coll = new ArrayList<>();

                            for (int i = 0; i < response.body().size(); i++) {
                                coll.add(false);
                            }

                            adapter.setData(response.body() , coll);

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


                    if (kir)
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

                                List<Boolean> coll = new ArrayList<>();

                                for (int i = 0; i < response.body().size(); i++) {
                                    coll.add(false);
                                }

                                list = response.body();
                                adapter.setData(list , coll);
                                smess.setVisibility(View.GONE);
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
                        progress.setVisibility(View.VISIBLE);

                        Bean b = (Bean) getApplicationContext();

                        Retrofit retrofit = new Retrofit.Builder()
                                .baseUrl(b.baseurl)
                                .addConverterFactory(ScalarsConverterFactory.create())
                                .addConverterFactory(GsonConverterFactory.create())
                                .build();

                        AllApiIneterface cr = retrofit.create(AllApiIneterface.class);

                        Call<List<productListBean>> call = cr.loved();
                        call.enqueue(new Callback<List<productListBean>>() {
                            @Override
                            public void onResponse(Call<List<productListBean>> call, Response<List<productListBean>> response) {

                                List<Boolean> coll = new ArrayList<>();

                                for (int i = 0; i < response.body().size(); i++) {
                                    coll.add(false);
                                }

                                list = response.body();
                                adapter.setData(list , coll);
                                smess.setVisibility(View.GONE);
                                progress.setVisibility(View.GONE);

                            }

                            @Override
                            public void onFailure(Call<List<productListBean>> call, Throwable t) {
                                t.printStackTrace();
                                progress.setVisibility(View.GONE);
                            }
                        });
                    }



                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                if (tab.getPosition() == 0)
                {
                    kir = true;
                    loaddata();
                }
                else
                {
                    kir = false;
                    loaddata2();
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (SharePreferenceUtils.getInstance().getString("userId").length() > 0)
                {
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
                else
                {
                    Toast.makeText(HomeActivity.this, "Please login to continue", Toast.LENGTH_SHORT).show();

                    login();

                }




            }
        });

        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (SharePreferenceUtils.getInstance().getString("userId").length() > 0)
                {
                    if (offlineCartBean.getTotalItem() > 0)
                    {
                        Intent intent = new Intent(HomeActivity.this , Checkout.class);
                        startActivity(intent);
                    }
                    else
                    {
                        Toast.makeText(HomeActivity.this, "Cart is Empty", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(HomeActivity.this, "Please login to continue", Toast.LENGTH_SHORT).show();
                    drawer.closeDrawer(GravityCompat.START);

                    login();

                }




                //Toast.makeText(HomeActivity.this, json, Toast.LENGTH_LONG).show();



            }
        });

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String appPackageName = getPackageName();
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "Download GroceGenie from: https://play.google.com/store/apps/details?id=" + appPackageName);
                sendIntent.setType("text/plain");
                startActivity(sendIntent);

            }
        });

        opencart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (SharePreferenceUtils.getInstance().getString("userId").length() > 0)
                {
                    if (offlineCartBean.getTotalItem() > 0)
                    {
                        Intent intent = new Intent(HomeActivity.this , Checkout.class);
                        startActivity(intent);
                        drawer.closeDrawer(GravityCompat.START);
                    }
                    else
                    {
                        Toast.makeText(HomeActivity.this, "Cart is Empty", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(HomeActivity.this, "Please login to continue", Toast.LENGTH_SHORT).show();
                    drawer.closeDrawer(GravityCompat.START);

                    login();

                }




            }
        });

        orders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (SharePreferenceUtils.getInstance().getString("userId").length() > 0)
                {
                    Intent intent = new Intent(HomeActivity.this , Orders.class);
                    startActivity(intent);
                    drawer.closeDrawer(GravityCompat.START);
                }
                else
                {
                    Toast.makeText(HomeActivity.this, "Please login to continue", Toast.LENGTH_SHORT).show();
                    drawer.closeDrawer(GravityCompat.START);

                    login();

                }




            }
        });


        support.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (SharePreferenceUtils.getInstance().getString("userId").length() > 0)
                {
                    Intent intent = new Intent(HomeActivity.this , Support.class);
                    startActivity(intent);
                    drawer.closeDrawer(GravityCompat.START);
                }
                else
                {
                    Toast.makeText(HomeActivity.this, "Please login to continue", Toast.LENGTH_SHORT).show();
                    drawer.closeDrawer(GravityCompat.START);

                    login();

                }




            }
        });



        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(drawer.isDrawerOpen(GravityCompat.START))
                {
                    drawer.closeDrawer(GravityCompat.START);
                }
                else
                {
                    drawer.openDrawer(GravityCompat.START);
                }


            }
        });


        collapse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                adapter.collapseall();

            }
        });


        progress.setVisibility(View.VISIBLE);

        Bean b = (Bean) getApplicationContext();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(b.baseurl)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        AllApiIneterface cr = retrofit.create(AllApiIneterface.class);

        Call<List<bannerBean>> call = cr.getBanners();

        call.enqueue(new Callback<List<bannerBean>>() {
            @Override
            public void onResponse(Call<List<bannerBean>> call, Response<List<bannerBean>> response) {

                BannerAdapter adapter = new BannerAdapter(getSupportFragmentManager() , FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT , response.body());

                pager.setAdapter(adapter);
                indicator.setViewPager(pager);


                progress.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(Call<List<bannerBean>> call, Throwable t) {
                progress.setVisibility(View.GONE);
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();

        search.setText("");

        if (SharePreferenceUtils.getInstance().getString("userId").length() > 0)
        {
            number.setText(SharePreferenceUtils.getInstance().getString("phone"));
        }
        else
        {
            number.setText("Login");
        }

        loaddata();
        updateCart();

        /*Tovuti.from(this).monitor(new Monitor.ConnectivityListener(){
            @Override
            public void onConnectivityChanged(int connectionType, boolean isConnected, boolean isFast){
                // TODO: Handle the connection...
                if (isConnected)
                {
                    loaddata();
                    updateCart();
                }
                else
                {
                    Toast.makeText(HomeActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                }

            }
        });*/


    }

    /*@Override
    protected void onStop(){
        //Tovuti.from(this).stop();
        super.onStop();
        smsVerifyCatcher.onStop();
    }*/

    void loaddata2()
    {
        progress.setVisibility(View.VISIBLE);

        Bean b = (Bean) getApplicationContext();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(b.baseurl)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        AllApiIneterface cr = retrofit.create(AllApiIneterface.class);

        Call<List<productListBean>> call = cr.loved();
        call.enqueue(new Callback<List<productListBean>>() {
            @Override
            public void onResponse(Call<List<productListBean>> call, Response<List<productListBean>> response) {

                List<Boolean> coll = new ArrayList<>();

                for (int i = 0; i < response.body().size(); i++) {
                    coll.add(false);
                }

                list = response.body();
                adapter.setData(list , coll);

                progress.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(Call<List<productListBean>> call, Throwable t) {
                t.printStackTrace();
                progress.setVisibility(View.GONE);
            }
        });
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

                List<Boolean> coll = new ArrayList<>();

                for (int i = 0; i < response.body().size(); i++) {
                    coll.add(false);
                }

                list = response.body();
                adapter.setData(list , coll);
                smess.setVisibility(View.GONE);
                progress.setVisibility(View.GONE);

                collapse.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(Call<List<productListBean>> call, Throwable t) {
                t.printStackTrace();
                progress.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onOTPReceived(String message) {
        //showToast("OTP Received: " + message);
Log.d("otp" , message);
        String ot = message.substring(43 , 49);
        oottpp.setText(ot);

        if (smsReceiver != null) {
            unregisterReceiver(smsReceiver);
            smsReceiver = null;
        }
    }

    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onOTPTimeOut() {
        //showToast("OTP Time out");
    }

    @Override
    public void onOTPReceivedError(String error) {
        //showToast(error);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mInternetAvailabilityChecker.removeInternetConnectivityChangeListener(this);

        if (smsReceiver != null) {
            unregisterReceiver(smsReceiver);
        }
    }

    private void startSMSListener() {
        try {
            smsReceiver = new SMSReceiver();
            smsReceiver.setOTPListener(this);

            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(SmsRetriever.SMS_RETRIEVED_ACTION);
            this.registerReceiver(smsReceiver, intentFilter);

            SmsRetrieverClient client = SmsRetriever.getClient(this);

            Task<Void> task = client.startSmsRetriever();
            task.addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    // API successfully started
                }
            });

            task.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    // Fail to start API
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onInternetConnectivityChanged(boolean isConnected) {

        if (isConnected)
        {
            loaddata();
            updateCart();
        }
        else
        {
            Toast.makeText(HomeActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }

    }


    class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder>
    {
        Context context;
        List<productListBean> list = new ArrayList<>();
        List<Boolean> coll = new ArrayList<>();

        ProductAdapter(Context context, List<productListBean> list)
        {
            this.context = context;
            this.list = list;
        }

        void setData(List<productListBean> list , List<Boolean> coll)
        {
            this.list = list;
            this.coll = coll;
            notifyDataSetChanged();
        }

        void collapseall()
        {
            for (int i = 0; i < coll.size(); i++) {

                coll.set(i , false);

            }
            notifyDataSetChanged();
            checkCollapse();
        }

        List<Boolean> getColl()
        {
            return coll;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.product_list_model , parent , false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
            holder.setIsRecyclable(false);
            final productListBean item = list.get(position);


            String imageUrl = context.getResources().getString(R.string.serverUrl) + "/admin/upload/" + item.getImageName();

            holder.name.setText(item.getPname());
            DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).resetViewBeforeLoading(false).build();
            ImageLoader loader = ImageLoader.getInstance();
            loader.displayImage(imageUrl, holder.image, options);



            final float productTotalPrice = Float.parseFloat(item.getUnitPriceKg());
            final float finalPrice = Float.parseFloat(item.getSellingPrice());
            float da = productTotalPrice - finalPrice;



            holder.dprice.setText("₹" + finalPrice);
            holder.sprice.setText(Html.fromHtml("₹<strike>" + productTotalPrice + "</strike>"));

            if (item.getProductInfo().size() > 0) {

                holder.dprice.setVisibility(View.GONE);
                holder.sprice.setVisibility(View.GONE);
                holder.expand.setVisibility(View.VISIBLE);
                //holder.grid.setVisibility(View.VISIBLE);
                holder.total.setVisibility(View.VISIBLE);
                holder.addLayout.setVisibility(View.GONE);

                if (item.getOffer().equals("no"))
                {

                    holder.offertext.setVisibility(View.GONE);

                    float dis = 0;

                    for (int i = 0 ; i < item.getProductInfo().size() ; i++)
                    {
                        final float productTotalPrice2 = Float.parseFloat(item.getProductInfo().get(i).getUnitPriceKg());
                        final float finalPrice2 = Float.parseFloat(item.getProductInfo().get(i).getSellingPrice());
                        float da2 = productTotalPrice2 - finalPrice2;
                        float discount2 = (da2 / productTotalPrice2) * 100;

                        if (dis <= discount2) {
                            dis = discount2;
                            Log.d("dduucc" , String.valueOf(dis));
                            //this.textViewDiscount.setText("UP TO " + this.discount + "% OFF");
                            holder.discount.setText("UP TO " + (int)dis + "% OFF");
                            if (dis > 0)
                            {
                                holder.discount.setVisibility(View.VISIBLE);
                            }
                            else
                            {
                                holder.discount.setVisibility(View.GONE);
                            }
                        }

                    }
                }
                else
                {
                    holder.offertext.setVisibility(View.VISIBLE);
                    holder.discount.setText("OFFER");
                }




                SublistAdapter adapter1 = new SublistAdapter(context, item.getProductInfo(), holder.discount);
                GridLayoutManager manager = new GridLayoutManager(context, 1);
                holder.grid.setAdapter(adapter1);
                holder.grid.setLayoutManager(manager);


            } else {

                float discount = (da / productTotalPrice) * 100;

                holder.dprice.setVisibility(View.VISIBLE);
                holder.sprice.setVisibility(View.VISIBLE);
                holder.expand.setVisibility(View.GONE);
                holder.grid.setVisibility(View.GONE);
                holder.total.setVisibility(View.GONE);
                holder.addLayout.setVisibility(View.VISIBLE);

                if ((int)discount < 1)
                {
                    holder.discount.setVisibility(View.INVISIBLE);
                }
                else
                {
                    holder.discount.setVisibility(View.VISIBLE);
                }

                holder.discount.setText((int)discount + "% OFF");

            }

            holder.total.setText(item.getProductInfo().size() + " Items");

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (item.getProductInfo().size() > 0) {

                        if (holder.grid.getVisibility() == View.VISIBLE) {
                            holder.grid.setVisibility(View.GONE);
                            coll.set(position , false);
                            holder.expand.setText("VIEW MORE");
                            checkCollapse();
                        } else {
                            holder.grid.setVisibility(View.VISIBLE);
                            coll.set(position , true);
                            holder.expand.setText("VIEW LESS");
                            checkCollapse();
                        }
                    }

                }
            });

            holder.expand.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (holder.grid.getVisibility() == View.VISIBLE) {
                        holder.grid.setVisibility(View.GONE);
                        coll.set(position , false);
                        holder.expand.setText("VIEW MORE");
                        checkCollapse();
                    } else {
                        holder.grid.setVisibility(View.VISIBLE);
                        coll.set(position , true);
                        holder.expand.setText("VIEW LESS");
                        checkCollapse();
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

            holder.itemCount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (holder.itemCount.getText().toString().equals("Add"))
                    {
                        offlineCartBean.cartitems.add(item.getPid());
                        offlineCartBean.totalAmount += Float.valueOf(finalPrice);
                        offlineCartBean.totalItem += 1;
                        offlineCartBean.totalSaved += Float.valueOf(productTotalPrice - finalPrice);
                        notifyDataSetChanged();

                        updateCart();
                    }

                }
            });

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
            TextView sprice , dprice , name , discount , itemCount , total , offertext;
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
                total = itemView.findViewById(R.id.total);
                grid = itemView.findViewById(R.id.grid);
                addLayout = itemView.findViewById(R.id.linearLayout3);
                addButton = itemView.findViewById(R.id.itemPlusButton);
                removeButton = itemView.findViewById(R.id.itemMinusButton);
                itemCount = itemView.findViewById(R.id.itemCoutnButton);
                offertext = itemView.findViewById(R.id.textView40);

            }
        }
    }

    class SublistAdapter extends RecyclerView.Adapter<SublistAdapter.ViewHolder>
    {
        Context context;
        List<ProductInfo> list = new ArrayList<>();
        private TextView textViewDiscount;
        private float discount1 = 0;

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
        public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
            holder.setIsRecyclable(false);
            final ProductInfo item = list.get(position);



            String imageUrl = context.getResources().getString(R.string.serverUrl)+"/admin/upload/" + item.getImageName();

            holder.name.setText(item.getPname());
            DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).resetViewBeforeLoading(false).build();
            ImageLoader loader = ImageLoader.getInstance();
            loader.displayImage(imageUrl , holder.image , options);


            final float finalPrice;

            final float productTotalPrice = Float.parseFloat(item.getUnitPriceKg());
            if (item.getOffer().equals("no"))
            {
                finalPrice = Float.parseFloat(item.getSellingPrice());
            }
            else
            {
                finalPrice = 1;
            }


            float da = productTotalPrice - finalPrice;
            float discount = (da / productTotalPrice) * 100;






            holder.dprice.setText("₹" + finalPrice);
            holder.sprice.setText(Html.fromHtml("₹<strike>" + productTotalPrice + "</strike>"));

            if ((int)discount < 1)
            {
                holder.discount.setVisibility(View.INVISIBLE);
            }
            else
            {
                holder.discount.setVisibility(View.VISIBLE);
            }



            if (item.getOffer().equals("no"))
            {
                if (this.discount1 <= discount) {
                    this.discount1 = discount;
                    Log.d("dduucc" , String.valueOf(this.discount1));
                    //this.textViewDiscount.setText("UP TO " + this.discount + "% OFF");
                    this.textViewDiscount.setText("UP TO " + (int)this.discount1 + "% OFF");
                }

                holder.discount.setText((int)discount + "% OFF");
            }
            else
            {
                this.textViewDiscount.setText("OFFER");
                holder.discount.setText("OFFER");
            }

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


            holder.itemCount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (holder.itemCount.getText().toString().equals("Add"))
                    {
                        offlineCartBean.cartitems.add(item.getPid());
                        offlineCartBean.totalAmount += Float.valueOf(finalPrice);
                        offlineCartBean.totalItem += 1;
                        offlineCartBean.totalSaved += Float.valueOf(productTotalPrice - finalPrice);
                        notifyDataSetChanged();

                        updateCart();
                    }

                }
            });

            holder.addButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    if (item.getOffer().equals("no"))
                    {
                        offlineCartBean.cartitems.add(item.getPid());
                        offlineCartBean.totalAmount += Float.valueOf(finalPrice);
                        offlineCartBean.totalItem += 1;
                        offlineCartBean.totalSaved += Float.valueOf(productTotalPrice - finalPrice);
                        notifyDataSetChanged();

                        updateCart();
                    }
                    else
                    {

                        if (offlineCartBean.cartitems.contains(item.getPid()))
                        {
                            Toast.makeText(context, "You can only add 1 quantity per item", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            offlineCartBean.cartitems.add(item.getPid());
                            offlineCartBean.totalAmount += Float.valueOf(finalPrice);
                            offlineCartBean.totalItem += 1;
                            offlineCartBean.totalSaved += Float.valueOf(productTotalPrice - finalPrice);
                            notifyDataSetChanged();

                            updateCart();
                        }

                    }




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

    /*@Override
    protected void onStart() {
        super.onStart();
        smsVerifyCatcher.onStart();
    }*/

    /**
     * need for Android 6 real time permissions
     */
    /*@Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        smsVerifyCatcher.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }*/

    void login()
    {
        final Dialog dialog = new Dialog(HomeActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.otp_dialog);
        dialog.setCancelable(true);
        dialog.show();

        final EditText mob = dialog.findViewById(R.id.editTextMobile);
        final EditText reffer = dialog.findViewById(R.id.reffer);
        final TextView resend = dialog.findViewById(R.id.resend);
        final PinView pin = dialog.findViewById(R.id.pinView);
        oottpp = pin;
        Button verify = dialog.findViewById(R.id.buttonContinue);
        final ProgressBar progressBar = dialog.findViewById(R.id.progress);

        pin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.length() == 6)
                {
                    progressBar.setVisibility(View.VISIBLE);

                    Bean b = (Bean) getApplicationContext();

                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(b.baseurl)
                            .addConverterFactory(ScalarsConverterFactory.create())
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();

                    AllApiIneterface cr = retrofit.create(AllApiIneterface.class);

                    Call<loginBean> call = cr.verify(SharePreferenceUtils.getInstance().getString("phone") , s.toString());

                    call.enqueue(new Callback<loginBean>() {
                        @Override
                        public void onResponse(Call<loginBean> call, Response<loginBean> response) {

                            if (response.body().getStatus().equals("1"))
                            {
                                SharePreferenceUtils.getInstance().saveString("userId" , response.body().getUserId());
                                SharePreferenceUtils.getInstance().saveString("phone" , response.body().getPhone());
                                SharePreferenceUtils.getInstance().saveString("rewards" , response.body().getRewards());
                                Toast.makeText(HomeActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                                dialog.dismiss();

                                if (offlineCartBean.getTotalItem() > 0)
                                {
                                    Intent intent = new Intent(HomeActivity.this , Checkout.class);
                                    startActivity(intent);
                                }
                                else
                                {
                                    Intent intent = new Intent(HomeActivity.this , HomeActivity.class);
                                    startActivity(intent);
                                    finishAffinity();
                                }

                            }
                            Toast.makeText(HomeActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();


                            progressBar.setVisibility(View.GONE);

                        }

                        @Override
                        public void onFailure(Call<loginBean> call, Throwable t) {
                            progressBar.setVisibility(View.GONE);
                        }
                    });
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (resend.getText().toString().equals("RESEND"))
                {
                    progressBar.setVisibility(View.VISIBLE);

                    Bean b = (Bean) getApplicationContext();

                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(b.baseurl)
                            .addConverterFactory(ScalarsConverterFactory.create())
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();

                    AllApiIneterface cr = retrofit.create(AllApiIneterface.class);

                    Call<loginBean> call = cr.login(mob.getText().toString() , SharePreferenceUtils.getInstance().getString("token") , reffer.getText().toString());

                    call.enqueue(new Callback<loginBean>() {
                        @Override
                        public void onResponse(Call<loginBean> call, Response<loginBean> response) {

                            resend.setVisibility(View.VISIBLE);

                            new CountDownTimer(60000, 1000) {

                                public void onTick(long millisUntilFinished) {
                                    resend.setText("RESEND OTP in " + millisUntilFinished / 1000);
                                }

                                public void onFinish() {
                                    resend.setText("RESEND");
                                }
                            }.start();

                            SharePreferenceUtils.getInstance().saveString("phone" , response.body().getPhone());
                            Log.d("message" , response.body().getMessage());
                            Toast.makeText(HomeActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                            progressBar.setVisibility(View.GONE);

                        }

                        @Override
                        public void onFailure(Call<loginBean> call, Throwable t) {
                            progressBar.setVisibility(View.GONE);
                        }
                    });

                }


            }
        });


        mob.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.length() == 10)
                {

                    progressBar.setVisibility(View.VISIBLE);

                    Bean b = (Bean) getApplicationContext();

                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(b.baseurl)
                            .addConverterFactory(ScalarsConverterFactory.create())
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();

                    AllApiIneterface cr = retrofit.create(AllApiIneterface.class);

                    Call<loginBean> call = cr.login(s.toString() , SharePreferenceUtils.getInstance().getString("token") , reffer.getText().toString());

                    call.enqueue(new Callback<loginBean>() {
                        @Override
                        public void onResponse(Call<loginBean> call, Response<loginBean> response) {

                            resend.setVisibility(View.VISIBLE);

                            new CountDownTimer(60000, 1000) {

                                public void onTick(long millisUntilFinished) {
                                    resend.setText("RESEND OTP in " + millisUntilFinished / 1000);
                                }

                                public void onFinish() {
                                    resend.setText("RESEND");
                                }
                            }.start();

                            SharePreferenceUtils.getInstance().saveString("phone" , response.body().getPhone());
                            Log.d("message" , response.body().getMessage());
                            Toast.makeText(HomeActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                            progressBar.setVisibility(View.GONE);

                        }

                        @Override
                        public void onFailure(Call<loginBean> call, Throwable t) {
                            progressBar.setVisibility(View.GONE);
                        }
                    });


                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String p = pin.getText().toString();

                if (SharePreferenceUtils.getInstance().getString("phone").length() > 0)
                {

                    if (p.length() == 6)
                    {


                        progressBar.setVisibility(View.VISIBLE);

                        Bean b = (Bean) getApplicationContext();

                        Retrofit retrofit = new Retrofit.Builder()
                                .baseUrl(b.baseurl)
                                .addConverterFactory(ScalarsConverterFactory.create())
                                .addConverterFactory(GsonConverterFactory.create())
                                .build();

                        AllApiIneterface cr = retrofit.create(AllApiIneterface.class);

                        Call<loginBean> call = cr.verify(SharePreferenceUtils.getInstance().getString("phone") , p );

                        call.enqueue(new Callback<loginBean>() {
                            @Override
                            public void onResponse(Call<loginBean> call, Response<loginBean> response) {

                                if (response.body().getStatus().equals("1"))
                                {
                                    SharePreferenceUtils.getInstance().saveString("userId" , response.body().getUserId());
                                    SharePreferenceUtils.getInstance().saveString("phone" , response.body().getPhone());
                                    SharePreferenceUtils.getInstance().saveString("rewards" , response.body().getRewards());
                                    Toast.makeText(HomeActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                                    dialog.dismiss();

                                    if (offlineCartBean.getTotalItem() > 0)
                                    {
                                        Intent intent = new Intent(HomeActivity.this , Checkout.class);
                                        startActivity(intent);
                                    }
                                    else
                                    {
                                        Intent intent = new Intent(HomeActivity.this , HomeActivity.class);
                                        startActivity(intent);
                                        finishAffinity();
                                    }



                                }
                                Toast.makeText(HomeActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();


                                progressBar.setVisibility(View.GONE);

                            }

                            @Override
                            public void onFailure(Call<loginBean> call, Throwable t) {
                                progressBar.setVisibility(View.GONE);
                            }
                        });


                    }
                    else
                    {
                        Toast.makeText(HomeActivity.this, "Invalid OTP", Toast.LENGTH_SHORT).show();
                    }

                }
                else
                {
                    Toast.makeText(HomeActivity.this, "Please enter your phone number", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    class BannerAdapter extends FragmentStatePagerAdapter
    {

        List<bannerBean> list = new ArrayList<>();

        BannerAdapter(@NonNull FragmentManager fm, int behavior, List<bannerBean> list) {
            super(fm, behavior);
            this.list = list;
        }


        @NonNull
        @Override
        public Fragment getItem(int position) {

            String url = list.get(position).getBanner();

            page frag = new page();
            Bundle b = new Bundle();
            b.putString("url" , url);
            frag.setArguments(b);
            return frag;
        }

        @Override
        public int getCount() {
            return list.size();
        }
    }

    public static class page extends Fragment
    {
        ImageView image;
        String url;

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.page , container , false);

            url = getArguments().getString("url");

            image = view.findViewById(R.id.imageView3);

            DisplayImageOptions options = new DisplayImageOptions.Builder().cacheOnDisk(true).cacheInMemory(true).resetViewBeforeLoading(false).build();

            ImageLoader loader = ImageLoader.getInstance();
            loader.displayImage(getResources().getString(R.string.serverUrl) + "/admin/upload/" + url , image , options);

            return view;
        }
    }


    CharSequence getPageTitle1()
    {
        Drawable image = getResources().getDrawable(R.drawable.ic_groceries);
        image.setBounds(0, 0, image.getIntrinsicWidth(), image.getIntrinsicHeight());
        // Replace blank spaces with image icon
        SpannableString sb = new SpannableString("   Kirana List");
        ImageSpan imageSpan = new ImageSpan(image, ImageSpan.ALIGN_CENTER);
        sb.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return sb;
    }

    CharSequence getPageTitle2()
    {
        Drawable image = getResources().getDrawable(R.drawable.ic_favorite);
        image.setBounds(0, 0, image.getIntrinsicWidth(), image.getIntrinsicHeight());
        // Replace blank spaces with image icon
        SpannableString sb = new SpannableString("   Most Loved Items");
        ImageSpan imageSpan = new ImageSpan(image, ImageSpan.ALIGN_CENTER);
        sb.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return sb;
    }

    void checkCollapse()
    {
        List<Boolean> coll = adapter.getColl();

        if (coll.contains(true))
        {
            collapse.setVisibility(View.VISIBLE);
        }
        else
        {
            collapse.setVisibility(View.GONE);
        }

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View view = getCurrentFocus();
            if (view != null && view instanceof EditText) {
                Rect r = new Rect();
                view.getGlobalVisibleRect(r);
                int rawX = (int)ev.getRawX();
                int rawY = (int)ev.getRawY();
                if (!r.contains(rawX, rawY)) {
                    view.clearFocus();
                }
            }
        }
        return super.dispatchTouchEvent(ev);
    }

}
