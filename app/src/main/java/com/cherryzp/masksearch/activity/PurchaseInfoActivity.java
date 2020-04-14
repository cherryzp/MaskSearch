package com.cherryzp.masksearch.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.cherryzp.masksearch.R;

public class PurchaseInfoActivity extends AppCompatActivity {

    TextView call1TextView;
    TextView call2TextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_info);

        call1TextView = findViewById(R.id.call_1_tv);
        call2TextView = findViewById(R.id.call_2_tv);

        call2TextView.setOnClickListener(callListener);
        call1TextView.setOnClickListener(callListener);

    }

    View.OnClickListener callListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent;
            switch (view.getId()) {
                case R.id.call_1_tv:
                    intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:15771255"));
                    startActivity(intent);
                    break;

                case R.id.call_2_tv:
                    intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:110"));
                    startActivity(intent);
                    break;

            }
        }
    };
}
