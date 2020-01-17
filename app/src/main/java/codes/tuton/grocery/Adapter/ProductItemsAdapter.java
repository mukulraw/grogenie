package codes.tuton.grocery.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import codes.tuton.grocery.Interface.TotalAmountInterface;
import codes.tuton.grocery.R;
import codes.tuton.grocery.model.ProductCalculationModel;
import codes.tuton.grocery.model.ProductInfo;

public class ProductItemsAdapter extends RecyclerView.Adapter<ProductItemsAdapter.ProductViewHolder> {
    List<ProductInfo> productInfoArrayList;
    private TotalAmountInterface totalAmountInterface;
    Context context;

    public ProductItemsAdapter(List<ProductInfo> productInfoArrayList, TotalAmountInterface totalAmountInterface, Context context) {
        this.productInfoArrayList = productInfoArrayList;
        this.totalAmountInterface = totalAmountInterface;
        this.context = context;
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView title, disountPrice, price, discount, itemCount;
        public ImageButton addButton, removeButton;

        public ProductViewHolder(@NonNull View i) {
            super(i);
            imageView = i.findViewById(R.id.imageProductCard);
            title = i.findViewById(R.id.titleProductInfo);
            disountPrice = i.findViewById(R.id.productAmountWithDiscount);
            price = i.findViewById(R.id.productAmountTotal);
            discount = i.findViewById(R.id.productDiscount);
            itemCount = i.findViewById(R.id.itemCoutnButton);
            addButton = i.findViewById(R.id.itemPlusButton);
            removeButton = i.findViewById(R.id.itemMinusButton);
        }
    }


    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View i = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item, parent, false);
        return new ProductViewHolder(i);
    }

    @Override
    public void onBindViewHolder(@NonNull final ProductViewHolder i, int position) {

        final ProductCalculationModel productCalculationModel = new ProductCalculationModel();
        final ProductInfo productInfo = productInfoArrayList.get(position);
        final int productTotalPrice = Integer.valueOf(productInfo.getSellingPrice());
        int discount = Integer.valueOf(productInfo.getDiscount());
        final int finalPrice = productTotalPrice - ((productTotalPrice * discount) / 100);
        final int quntyty = Integer.valueOf(productInfo.getQnt());
        String imageUrl = context.getResources().getString(R.string.serverUrl)+"/image/" + productInfo.getImageName() + ".png";

        i.title.setText(productInfo.getPname());
        i.disountPrice.setText(String.valueOf(finalPrice));
        i.price.setText(String.valueOf(productTotalPrice));
        i.discount.setText(discount + "% OFF");

        Glide.with(context).load(imageUrl).into(i.imageView);

        i.addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (productCalculationModel.getItemCount() == 0 && productCalculationModel.getItemCount() <= quntyty) {
                    i.removeButton.setVisibility(View.VISIBLE);
                    productCalculationModel.setItemCount(productCalculationModel.getItemCount() + 1);
                    i.itemCount.setText(String.valueOf(productCalculationModel.getItemCount()));

                    ProductCalculationModel.totalAmount += Float.valueOf(finalPrice);
                    ProductCalculationModel.totalItem += 1;
                    ProductCalculationModel.totalSaved += Float.valueOf(productTotalPrice - finalPrice);
                } else if (productCalculationModel.getItemCount() < quntyty) {
                    productCalculationModel.setItemCount(productCalculationModel.getItemCount() + 1);
                    i.itemCount.setText(String.valueOf(productCalculationModel.getItemCount()));
                    ProductCalculationModel.totalAmount += Float.valueOf(finalPrice);
                    ProductCalculationModel.totalItem += 1;
                    ProductCalculationModel.totalSaved += Float.valueOf(productTotalPrice - finalPrice);
                }
                totalAmountInterface.totalCheckoutData("₹" + ProductCalculationModel.totalAmount, " X " + ProductCalculationModel.totalItem + " item", "₹" + ProductCalculationModel.totalSaved);
            }
        });

        i.removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (productCalculationModel.getItemCount() == 1) {
                    productCalculationModel.setItemCount(0);
                    i.removeButton.setVisibility(View.GONE);
                    i.itemCount.setText("Add");
                    ProductCalculationModel.totalAmount -= Float.valueOf(finalPrice);
                    ProductCalculationModel.totalItem -= 1;
                    ProductCalculationModel.totalSaved -= Float.valueOf(productTotalPrice - finalPrice);
                } else if (productCalculationModel.getItemCount() > 0) {
                    productCalculationModel.setItemCount(productCalculationModel.getItemCount() - 1);
                    i.itemCount.setText(String.valueOf(productCalculationModel.getItemCount()));

                    ProductCalculationModel.totalAmount -= Float.valueOf(finalPrice);
                    ProductCalculationModel.totalItem -= 1;
                    ProductCalculationModel.totalSaved -= Float.valueOf(productTotalPrice - finalPrice);
                }

                totalAmountInterface.totalCheckoutData("₹" + ProductCalculationModel.totalAmount, " X " + ProductCalculationModel.totalItem + " item", "₹" + ProductCalculationModel.totalSaved);
            }
        });
    }

    @Override
    public int getItemCount() {
        return productInfoArrayList.size();
    }


}
