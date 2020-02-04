package codes.tuton.grocery;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

public class OrderDetails extends AppCompatActivity {

    TextView amount , saving , genie , orderid , delcharges , deldate , status , grand;
    EditText name, email, flat, landmark, city, area, mobile;

    RecyclerView orders;

    ProgressBar progress;
    ImageButton back;
    TextView title;

    String id , tit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);

        id = getIntent().getStringExtra("id");
        tit = getIntent().getStringExtra("tit");

        amount = findViewById(R.id.textView3);
        saving = findViewById(R.id.textView5);
        genie = findViewById(R.id.textView7);
        orderid = findViewById(R.id.textView9);
        grand = findViewById(R.id.textView11);
        delcharges = findViewById(R.id.textView34);
        deldate = findViewById(R.id.textView8);
        status = findViewById(R.id.textView37);

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

    }
}
