package codes.tuton.grocery;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import codes.tuton.grocery.offlineCartPOJO.offlineCartBean;

public class Checkout extends AppCompatActivity {

    TextView amount , saving , total , applied;
    CheckBox geniecash;
    RadioGroup delivery;
    Spinner free , express;
    EditText promo;
    Button apply;

    EditText flat , landmark , city , area , mobile;
    Button add;

    RecyclerView orders;

    ProgressBar progress;
    ImageButton back;

    float tamount = 0 , gtotal = 0;
    boolean fdel = true;
    float pvalue = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        amount = findViewById(R.id.textView3);
        saving = findViewById(R.id.textView5);
        total = findViewById(R.id.textView11);
        geniecash = findViewById(R.id.textView7);
        delivery = findViewById(R.id.radioGroup);
        free = findViewById(R.id.free);
        express = findViewById(R.id.express);
        promo = findViewById(R.id.editText2);
        apply = findViewById(R.id.button);
        applied = findViewById(R.id.textView15);

        flat = findViewById(R.id.editText3);
        landmark = findViewById(R.id.editText4);
        city = findViewById(R.id.autoCompleteTextView);
        area = findViewById(R.id.autoCompleteTextView2);
        mobile = findViewById(R.id.editText5);
        add = findViewById(R.id.button2);

        orders = findViewById(R.id.grid);
        progress = findViewById(R.id.progressBar2);
        back = findViewById(R.id.imageButton);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();

            }
        });

        delivery.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                int iidd = group.getCheckedRadioButtonId();
                if (iidd == R.id.f)
                {
                    fdel = true;
                    updateSummary();
                }
                else
                {
                    fdel = false;
                    updateSummary();
                }

            }
        });

        updateSummary();



    }

    void updateSummary()
    {
        tamount = offlineCartBean.getTotalAmount();

        amount.setText("₹" + offlineCartBean.getTotalAmount());
        //totalItemTextView.setText(" X " + offlineCartBean.getTotalItem() + " item");
        saving.setText("₹" + offlineCartBean.getTotalSaved());

        gtotal = tamount;
        if (!fdel)
        {
            gtotal = gtotal + 10;
        }
        gtotal = gtotal + pvalue;

        total.setText("₹" + gtotal);

    }

}
