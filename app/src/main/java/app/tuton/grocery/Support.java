package app.tuton.grocery;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class Support extends AppCompatActivity {

    ImageButton back;
    EditText message;
    Button send , sendnew;
    ProgressBar progress;
    LinearLayout hide;
    TextView ticket;
    CardView tic;
    ImageButton email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_support);

        back = findViewById(R.id.imageButton);
        message = findViewById(R.id.etMessage);
        send = findViewById(R.id.btnSend);
        progress = findViewById(R.id.progressBar2);
        tic = findViewById(R.id.card3);
        hide = findViewById(R.id.mess);
        ticket = findViewById(R.id.tvTickete);
        sendnew = findViewById(R.id.btnSendNewMsg);
        email = findViewById(R.id.btnEmail);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String m = message.getText().toString();

                if (m.length() > 0)
                {

                    progress.setVisibility(View.VISIBLE);

                    Bean b = (Bean) getApplicationContext();

                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(b.baseurl)
                            .addConverterFactory(ScalarsConverterFactory.create())
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();

                    AllApiIneterface cr = retrofit.create(AllApiIneterface.class);

                    Call<addMessageBean> call = cr.addFeedback(
                            SharePreferenceUtils.getInstance().getString("userId"),
                            m
                    );

                    call.enqueue(new Callback<addMessageBean>() {
                        @Override
                        public void onResponse(Call<addMessageBean> call, Response<addMessageBean> response) {

                            if (response.body().getStatus().equals("1"))
                            {

                                message.setText("");

                                ticket.setText(response.body().getMessage());
                                tic.setVisibility(View.VISIBLE);
                                hide.setVisibility(View.GONE);

                            }
                            else
                            {
                                Toast.makeText(Support.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            }

                            progress.setVisibility(View.GONE);
                        }

                        @Override
                        public void onFailure(Call<addMessageBean> call, Throwable t) {
                            progress.setVisibility(View.GONE);
                        }
                    });

                }
                else
                {
                    Toast.makeText(Support.this, "Please enter a message", Toast.LENGTH_SHORT).show();
                }

            }
        });

        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto", "support@grocegenie.com", null));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "");
                startActivity(Intent.createChooser(emailIntent, null));

            }
        });

        sendnew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                tic.setVisibility(View.GONE);
                hide.setVisibility(View.VISIBLE);

            }
        });


    }
}
