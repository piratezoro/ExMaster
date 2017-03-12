package com.expensemaster.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.expensemaster.Activities.R;

public class ChartsActivity extends AppCompatActivity {

    Button btnPieChart, btnBarChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charts);

        btnPieChart = (Button) findViewById(R.id.btn_pie_chart);
        btnBarChart = (Button) findViewById(R.id.btn_bar_chart);

        btnPieChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), DisplayGraphActivity.class);
                intent.putExtra("chart_type", "pie");
                startActivity(intent);
            }
        });

        btnBarChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), DisplayGraphActivity.class);
                intent.putExtra("chart_type", "bar");
                startActivity(intent);
            }
        });
    }
}
