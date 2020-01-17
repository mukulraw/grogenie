package codes.tuton.grocery.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import codes.tuton.grocery.Interface.TotalAmountInterface;
import codes.tuton.grocery.R;
import codes.tuton.grocery.model.CategoryInfo;
import codes.tuton.grocery.model.ProductCalculationModel;
import codes.tuton.grocery.model.ProductInfo;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {
    private List<CategoryInfo> categoryInfoList;
    private Context context;
    private static int currentPosition = 0;
    private List<ProductInfo> productInfos = new ArrayList<>();
    private TotalAmountInterface totalAmountInterface;

    public CategoryAdapter(List<CategoryInfo> categoryInfoList, Context context, TotalAmountInterface totalAmountInterface) {
        this.categoryInfoList = categoryInfoList;
        this.context = context;
        this.totalAmountInterface = totalAmountInterface;
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder {
        public RecyclerView recyclerView;
        public ImageView imageView;
        public TextView mDiscount, mTitle, mAddAll;
        public Button expandButton;

        public CategoryViewHolder(@NonNull View i) {
            super(i);
            recyclerView = i.findViewById(R.id.recyclerViewSunCategory);
            imageView = i.findViewById(R.id.categoryImage);
            mDiscount = i.findViewById(R.id.categoryDiscount);
            mTitle = i.findViewById(R.id.categoryTitle);
            expandButton = i.findViewById(R.id.categoryViewLess);
            mAddAll = i.findViewById(R.id.categoryAddAll);
        }
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_category_item, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final CategoryViewHolder i, final int position) {
        CategoryInfo categoryInfo = categoryInfoList.get(position);
        final ProductCalculationModel productCalculationModel = new ProductCalculationModel();
        String imageUrl = "https://kaushiktejani.github.io/image/" + categoryInfo.getImage() + ".png";
//        Glide.with(context).load(imageUrl).into(i.imageView);
        i.mTitle.setText(categoryInfo.getCategoryName());

        productInfos.addAll(Arrays.asList(categoryInfo.getProduct_info()));
        i.recyclerView.setLayoutManager(new LinearLayoutManager(context));
        i.recyclerView.setAdapter(new SubProductInfoAdtaper(productInfos, totalAmountInterface, context, i.mDiscount));

        i.expandButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (productCalculationModel.getItemCount() == 0) {
                    productCalculationModel.setItemCount(productCalculationModel.getItemCount() + 1);
                    i.recyclerView.setVisibility(View.GONE);
                } else if (productCalculationModel.getItemCount() % 2 == 0 || productCalculationModel.getItemCount() == 2) {
                    productCalculationModel.setItemCount(productCalculationModel.getItemCount() + 1);
                    i.recyclerView.setVisibility(View.VISIBLE);
                } else {
                    productCalculationModel.setItemCount(productCalculationModel.getItemCount() + 1);
                    i.recyclerView.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return categoryInfoList.size();
    }

}
