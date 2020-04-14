package com.cherryzp.masksearch.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.cherryzp.masksearch.R;

public class MaskPolicyActivity extends AppCompatActivity {

    ImageView image1View;
    ImageView image2View;
    ImageView image3View;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mask_policy);

        image1View = findViewById(R.id.image_1_iv);
        image2View = findViewById(R.id.image_2_iv);
        image3View = findViewById(R.id.image_3_iv);

        Glide.with(this).load(R.drawable.mask_policy_1).into(image1View);
        Glide.with(this).load(R.drawable.mask_policy_2).into(image2View);
        Glide.with(this).load(R.drawable.mask_policy_3).into(image3View);
    }
}
