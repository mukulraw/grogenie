package codes.tuton.grocery;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

import codes.tuton.grocery.productListPOJO.ProductInfo;

public class HomeActivity extends AppCompatActivity {

    RecyclerView grid;
    ProgressBar progress;
    GridLayoutManager manager;
    List<ProductInfo> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        list = new ArrayList<>();
        grid = findViewById(R.id.grid);
        progress = findViewById(R.id.progressBar);

    }

    class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder>
    {
        Context context;
        List<ProductInfo> list = new ArrayList<>();

        public ProductAdapter(Context context , List<ProductInfo> list)
        {
            this.context = context;
            this.list = list;
        }

        public void setData(List<ProductInfo> list)
        {
            this.list = list;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return null;
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder
        {

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
            }
        }
    }

}
