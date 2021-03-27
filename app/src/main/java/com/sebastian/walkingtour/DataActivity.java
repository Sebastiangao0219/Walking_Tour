package com.sebastian.walkingtour;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.squareup.picasso.Picasso;

public class DataActivity extends AppCompatActivity {
    private Typeface myCustomFont;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);

        setupHomeIndicator(getSupportActionBar());

        myCustomFont = Typeface.createFromAsset(getAssets(), "fonts/Acme-Regular.ttf");

        ConstraintLayout layout = findViewById(R.id.layout);
        TextView nameText = findViewById(R.id.buildingName);
        ImageView imageView = findViewById(R.id.buildingImage);
        TextView addressText = findViewById(R.id.buildingAddress);
        TextView descriptionText = findViewById(R.id.buldingDescription);
        descriptionText.setMovementMethod(new ScrollingMovementMethod());

        nameText.setTypeface(myCustomFont);
        addressText.setTypeface(myCustomFont);
        descriptionText.setTypeface(myCustomFont);

        FenceData fd = (FenceData) getIntent().getSerializableExtra("DATA");

        if (fd != null) {
            layout.setBackgroundColor(Color.parseColor(fd.getFenceColor()));
            nameText.setText(fd.getId());
            Picasso.get().load(fd.getImageUrl())
                    .resize(395, 296)
                    .error(R.drawable.logo)
                    .into(imageView);
            addressText.setText(fd.getAddress());
            descriptionText.setText(fd.getDescription());
        }
    }

    void setupHomeIndicator(ActionBar actionBar) {
        if (actionBar != null) {
            setTitle("");
            // Comment out the below line to show the default home indicator
            actionBar.setHomeAsUpIndicator(R.drawable.home_image);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }
}
