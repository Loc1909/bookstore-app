package com.example.bookstore_app.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bookstore_app.R;
import com.example.bookstore_app.adapters.SimpleAdapter;
import com.example.bookstore_app.adapters.TopProductAdapter;
import com.example.bookstore_app.database.dao.StatisticDAO;
import com.example.bookstore_app.models.stats.*;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.*;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

public class StatsActivity extends AppCompatActivity {

    TextView txtRevenue, txtOrders;

    BarChart barChart;

    HorizontalBarChart chartTopProducts;
    PieChart chartCategory;

    HorizontalBarChart chartUser;

    StatisticDAO dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        // Text
        txtRevenue = findViewById(R.id.txtRevenue);
        txtOrders = findViewById(R.id.txtOrders);

        // ListView

        // Chart
        chartTopProducts = findViewById(R.id.chartTopProducts);
        chartUser = findViewById(R.id.chartUser);
        chartCategory = findViewById(R.id.chartCategory);
        barChart = findViewById(R.id.barChartRevenue);

        dao = new StatisticDAO(this);

        loadData();
        loadTopProductChart();
        loadUserChart();
        loadCategoryChart();
    }

    private void loadData() {

        // 💰 Doanh thu
        double revenue = dao.getTotalRevenue();
        txtRevenue.setText("💰 " + revenue + " VND");

        // 📦 Số đơn (tạm tính theo user)
        txtOrders.setText("📦 " + dao.getUserStats().size() + " đơn");


        // 📊 Chart
        loadChart();
    }

    private void loadChart() {

        List<RevenueStat> list = dao.getRevenueByMonth();

        if (list == null || list.isEmpty()) return;

        ArrayList<BarEntry> entries = new ArrayList<>();
        ArrayList<String> labels = new ArrayList<>();

        int index = 0;

        for (RevenueStat r : list) {
            entries.add(new BarEntry(index, (float) r.getRevenue()));
            labels.add(r.getMonth());
            index++;
        }

        BarDataSet dataSet = new BarDataSet(entries, "Doanh thu");

        // 🎨 màu đẹp
        dataSet.setColor(Color.parseColor("#FF9800"));
        dataSet.setValueTextSize(12f);

        BarData data = new BarData(dataSet);
        barChart.setData(data);

        // X axis
        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
        xAxis.setGranularity(1f);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        // tối ưu UI chart
        barChart.getAxisRight().setEnabled(false);
        barChart.getDescription().setEnabled(false);
        barChart.setFitBars(true);
        barChart.setScaleEnabled(false);

        barChart.animateY(1000);
        barChart.invalidate();
    }
    private void loadTopProductChart() {
        List<TopProduct> list = dao.getTopProducts();

        ArrayList<BarEntry> entries = new ArrayList<>();
        ArrayList<String> labels = new ArrayList<>();

        for (int i = 0; i < list.size(); i++) {
            entries.add(new BarEntry(i, list.get(i).getTotalSold()));
            labels.add(list.get(i).getBookName());
        }

        BarDataSet dataSet = new BarDataSet(entries, "Top sản phẩm");
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);

        BarData data = new BarData(dataSet);
        chartTopProducts.setData(data);

        XAxis xAxis = chartTopProducts.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        chartTopProducts.getDescription().setEnabled(false);
        chartTopProducts.animateY(1000);
    }


    private void loadUserChart() {
        List<UserStat> list = dao.getUserStats();

        ArrayList<BarEntry> entries = new ArrayList<>();
        ArrayList<String> labels = new ArrayList<>();

        for (int i = 0; i < list.size(); i++) {
            entries.add(new BarEntry(i, (float) list.get(i).getTotalSpent()));
            labels.add(list.get(i).getUsername());
        }

        BarDataSet dataSet = new BarDataSet(entries, "Top khách hàng");
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        dataSet.setValueTextSize(12f);

        BarData data = new BarData(dataSet);
        chartUser.setData(data);

        // trục X
        XAxis xAxis = chartUser.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
        xAxis.setGranularity(1f);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        // UI đẹp hơn
        chartUser.getAxisRight().setEnabled(false);
        chartUser.getDescription().setEnabled(false);
        chartUser.setFitBars(true);
        chartUser.setScaleEnabled(false);

        chartUser.animateY(1000);
        chartUser.invalidate();
    }

    private void loadCategoryChart() {
        List<CategoryStat> list = dao.getCategoryStats();

        ArrayList<PieEntry> entries = new ArrayList<>();

        for (CategoryStat c : list) {
            entries.add(new PieEntry(c.getTotalSold(), c.getCategoryName()));
        }

        PieDataSet dataSet = new PieDataSet(entries, "Danh mục");
        dataSet.setColors(ColorTemplate.PASTEL_COLORS);

        PieData data = new PieData(dataSet);
        chartCategory.setData(data);

        chartCategory.setUsePercentValues(true);
        chartCategory.getDescription().setEnabled(false);
        chartCategory.animateY(1000);
    }
}