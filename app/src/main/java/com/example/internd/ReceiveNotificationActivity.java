package com.example.internd;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ReceiveNotificationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recieve_notification);

        TextView categotyTv = findViewById(R.id.category);
        TextView brandTv = findViewById(R.id.brand);

        if (getIntent().hasExtra("title")){
            String category = getIntent().getStringExtra("title");
            String brand = getIntent().getStringExtra("body");
            System.out.println(category+"  "+brand);
            categotyTv.setText(category);
            brandTv.setText(brand);
        }
    }
}
