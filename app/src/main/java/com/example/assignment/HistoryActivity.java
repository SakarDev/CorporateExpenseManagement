package com.example.assignment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity {
    private PieChart pieChart;
    String email;
    String user_id;
    DBHelper db;
    ArrayList array_list;
    float totalExpense;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        db = new DBHelper(this);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            email = extras.getString("email");
            user_id = extras.getString("user_id");
            array_list = db.getCategoryAndTotalPrice(user_id);
            Toast.makeText(getApplicationContext(), "user_id: "+user_id, Toast.LENGTH_SHORT).show();
            Toast.makeText(getApplicationContext(), "arrList.size:: "+array_list.size(), Toast.LENGTH_SHORT).show();
            Cursor cursor = db.getTotalUserExpense(user_id);
            if(cursor.getCount() != 0){
                totalExpense = Float.parseFloat(cursor.getString(0));
            }
        }

        pieChart = findViewById(R.id.activity_main_piechart);
        setupPieChart();
        loadPieChartData();

    }

    private void setupPieChart() {
        pieChart.setDrawHoleEnabled(true);
        pieChart.setUsePercentValues(true);
        pieChart.setEntryLabelTextSize(12);
        pieChart.setEntryLabelColor(Color.BLACK);
        pieChart.setCenterText("Spending by Category");
        pieChart.setCenterTextSize(24);
        pieChart.getDescription().setEnabled(false);

        Legend l = pieChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setEnabled(true);
    }

    private void loadPieChartData() {

        ArrayList<PieEntry> entries = new ArrayList<>();


        for (int i = 0; i <array_list.size(); i++) {
            String item = array_list.get(i).toString();
            String[] parts = item.split(", ");
            String category = parts[0].trim();
            String categoryPrice = parts[1].trim();

            float fraction = Float.parseFloat(categoryPrice)/totalExpense;
            entries.add(new PieEntry( fraction, category));

        }

        ArrayList<Integer> colors = new ArrayList<>();
        for (int color: ColorTemplate.MATERIAL_COLORS) {
            colors.add(color);
        }

        for (int color: ColorTemplate.VORDIPLOM_COLORS) {
            colors.add(color);
        }

        PieDataSet dataSet = new PieDataSet(entries, "Expense Category");
        dataSet.setColors(colors);

        PieData data = new PieData(dataSet);
        data.setDrawValues(true);
        data.setValueFormatter(new PercentFormatter(pieChart));
        data.setValueTextSize(12f);
        data.setValueTextColor(Color.BLACK);

        pieChart.setData(data);
        pieChart.invalidate();

        pieChart.animateY(1400, Easing.EaseInOutQuad);
    }


}