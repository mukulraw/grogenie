package codes.tuton.grocery;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import codes.tuton.grocery.Adapter.CategoryAdapter;
import codes.tuton.grocery.Adapter.ProductItemsAdapter;
import codes.tuton.grocery.Interface.TotalAmountInterface;
import codes.tuton.grocery.model.CategoryInfo;
import codes.tuton.grocery.model.ProductInfo;

public class MainActivity extends AppCompatActivity {
    private final String TAG = "Demo";
    private Toolbar toolbar;
    private EditText serchEditText;
    private TextView toolbarTitle, totalFinalMount, totalSavedAmount, totalItemTextView;
    private String jsonProductInfo;
    private List<ProductInfo> productInfoList = new ArrayList<>(), productInfoListFilter = new ArrayList<>();
    private List<CategoryInfo> categoryInfoList = new ArrayList<>(), categoryInfoListFilter = new ArrayList<>();
    private RecyclerView recyclerViewProductInfo, recyclerViewCategoryInfo;
    private RecyclerView.LayoutManager layoutManager, layoutManagerCate;
    private RecyclerView.Adapter adapterProductInfo, adapterCategoryInf;
    private List<CategoryInfo> categoryInfosNewData = new ArrayList<>();
    private List<ProductInfo> productInfosNewData = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Bind View
        bindView();

        //SetToolBar
        setSupportActionBar(toolbar);

        setRecyclerViewProductInfo();

        serchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });

        RequestQueue queue = Volley.newRequestQueue(this);
        //Test Api https://api.myjson.com/bins/hjlum
        StringRequest stringRequest = new StringRequest(Request.Method.GET, getResources().getString(R.string.serverUrl) + "/ProductData.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        Log.e(TAG, "Json object of Product info = " + response);
                        Gson gson = new Gson();
                        ProductInfo[] productInfos = gson.fromJson(response, ProductInfo[].class);
                        productInfoList.addAll(Arrays.asList(productInfos));
                        productInfoListFilter.addAll(productInfoList);
                        adapterProductInfo.notifyDataSetChanged();
                        Log.i(TAG, "Data store into ArrayList");

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                Log.e(TAG, error.toString());
            }
        });
        queue.add(stringRequest);

        //test api https://api.myjson.com/bins/ius5i
        StringRequest stringRequestProductData = new StringRequest(Request.Method.GET, getResources().getString(R.string.serverUrl) + "/CategoryAllData.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        Log.e(TAG, "Json object of Product info = " + response);
                        Gson gson = new Gson();
                        CategoryInfo[] categoryInfos = gson.fromJson(response, CategoryInfo[].class);
                        categoryInfoList.addAll(Arrays.asList(categoryInfos));
                        categoryInfoListFilter.addAll(categoryInfoList);
                        adapterCategoryInf.notifyDataSetChanged();
                        Log.i(TAG, "Data store into ArrayList");

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                Log.e(TAG, error.toString());
            }
        });
        queue.add(stringRequestProductData);

    }

    void setRecyclerViewProductInfo() {
        recyclerViewProductInfo = findViewById(R.id.recyclerViewProductItemMain);
        layoutManager = new LinearLayoutManager(this);
        adapterProductInfo = new ProductItemsAdapter(productInfoList, new TotalAmountInterface() {
            @Override
            public void totalCheckoutData(String amount, String totalItem, String saved) {
                totalFinalMount.setText(amount);
                totalItemTextView.setText(totalItem);
                totalSavedAmount.setText(saved);
            }
        }, this);

        recyclerViewProductInfo.setLayoutManager(layoutManager);
        recyclerViewProductInfo.setAdapter(adapterProductInfo);


        recyclerViewCategoryInfo = findViewById(R.id.recyclerViewCategoruMain);
        layoutManagerCate = new LinearLayoutManager(this);
        recyclerViewCategoryInfo.setLayoutManager(layoutManagerCate);
        recyclerViewCategoryInfo.setHasFixedSize(true);
        adapterCategoryInf = new CategoryAdapter(categoryInfoList, this, new TotalAmountInterface() {
            @Override
            public void totalCheckoutData(String amount, String totalItem, String saved) {
                totalFinalMount.setText(amount);
                totalItemTextView.setText(totalItem);
                totalSavedAmount.setText(saved);
            }
        });
        recyclerViewCategoryInfo.setAdapter(adapterCategoryInf);
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar_top, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.shoppingCart) {
            Toast.makeText(this, "Shopping Cart Selected.", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }*/


    void filter(String text) {
        productInfosNewData.clear();
        for (ProductInfo productInfo : productInfoListFilter) {
            if (productInfo.getPname().contains(text)) {
                productInfosNewData.add(productInfo);
            }
        }

        categoryInfosNewData.clear();
        for (CategoryInfo categoryInfo : categoryInfoListFilter) {
            if (categoryInfo.getCategoryName().contains(text)) {
                categoryInfosNewData.add(categoryInfo);
            }
        }

        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(this);
        Intent intent = new Intent("Data_Reset");
        localBroadcastManager.sendBroadcast(intent);

        productInfoList.clear();
        productInfoList.addAll(productInfosNewData);
        categoryInfoList.clear();
        categoryInfoList.addAll(categoryInfosNewData);
        adapterProductInfo.notifyDataSetChanged();
        adapterCategoryInf.notifyDataSetChanged();
    }

    void bindView() {
        toolbar = findViewById(R.id.toolbar);
        toolbarTitle = findViewById(R.id.toolbarTitle);

        totalFinalMount = findViewById(R.id.totalAmountTextView);
        totalSavedAmount = findViewById(R.id.savedAmountTextView);
        totalItemTextView = findViewById(R.id.totalItemTextView);

        serchEditText = findViewById(R.id.serchEditText);
    }
}
