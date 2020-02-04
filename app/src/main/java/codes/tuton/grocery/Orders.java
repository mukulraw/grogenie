package codes.tuton.grocery;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.badoualy.stepperindicator.StepperIndicator;

import java.net.ContentHandler;

public class Orders extends AppCompatActivity {

    ImageButton back;
    RecyclerView active , previous;
    ActiveAdapter adapter;


  /*  StepperIndicator indicator;
*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);

        back = findViewById(R.id.imageButton);
        active = findViewById(R.id.active);
        previous = findViewById(R.id.previous);
        adapter = new ActiveAdapter(this);
        GridLayoutManager manager = new GridLayoutManager(this , 1);

        active.setAdapter(adapter);
        active.setLayoutManager(manager);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();

            }
        });


    }

    class ActiveAdapter extends RecyclerView.Adapter<ActiveAdapter.ViewHolder>
    {

        Context context;

        public ActiveAdapter(Context context)
        {
            this.context = context;
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

            String[] labels = {
                    "Accepted",
                    "In transit",
                    "Delivered"
            };


            holder.indicator.setStepCount(3);
            holder.indicator.setLabels(labels);
            holder.indicator.setCurrentStep(2);

        }

        @Override
        public int getItemCount() {
            return 10;
        }

        class ViewHolder extends RecyclerView.ViewHolder
        {

            StepperIndicator indicator;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);

                indicator = itemView.findViewById(R.id.textView31);

            }
        }
    }

}
