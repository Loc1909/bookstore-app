package com.example.bookstore_app.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bookstore_app.R;
import com.example.bookstore_app.database.dao.StatisticDAO;
import com.example.bookstore_app.models.stats.*;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.*;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class StatsActivity extends AppCompatActivity {

    // ── KPI ────────────────────────────────────────────────────────
    TextView txtRevenue, txtOrders, txtNewCustomers, txtAvgOrder;

    // ── Charts ─────────────────────────────────────────────────────
    BarChart           barChartRevenue;
    HorizontalBarChart chartTopProducts;
    HorizontalBarChart chartUser;
    PieChart           chartCategory;

    // ── DAO ────────────────────────────────────────────────────────
    StatisticDAO dao;

    // ── Design tokens ──────────────────────────────────────────────
    private static final int COLOR_BLUE        = Color.parseColor("#378ADD");
    private static final int COLOR_AMBER       = Color.parseColor("#BA7517");
    private static final int COLOR_PURPLE      = Color.parseColor("#534AB7");
    private static final int COLOR_PURPLE_MID  = Color.parseColor("#7F77DD");
    private static final int COLOR_PURPLE_LIGHT= Color.parseColor("#AFA9EC");
    private static final int COLOR_PURPLE_PALE = Color.parseColor("#CECBF6");
    private static final int COLOR_PURPLE_GHOST= Color.parseColor("#EEEDFE");
    private static final int COLOR_TEAL        = Color.parseColor("#0F6E56");
    private static final int COLOR_TEAL_MID    = Color.parseColor("#1D9E75");
    private static final int COLOR_TEAL_LIGHT  = Color.parseColor("#5DCAA5");
    private static final int COLOR_TEAL_PALE   = Color.parseColor("#9FE1CB");
    private static final int COLOR_TEAL_GHOST  = Color.parseColor("#E1F5EE");
    private static final int COLOR_GRAY        = Color.parseColor("#888780");
    private static final int COLOR_TEXT        = Color.parseColor("#2C2C2A");
    private static final int COLOR_GRID        = Color.parseColor("#22888780");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        bindViews();
        dao = new StatisticDAO(this);

        loadKpi();
        loadRevenueChart();
        loadTopProductChart();
        loadUserChart();
        loadCategoryChart();
    }

    private void bindViews() {
        txtRevenue      = findViewById(R.id.txtRevenue);
        txtOrders       = findViewById(R.id.txtOrders);
        txtNewCustomers = findViewById(R.id.txtNewCustomers);
        txtAvgOrder     = findViewById(R.id.txtAvgOrder);

        barChartRevenue  = findViewById(R.id.barChartRevenue);
        chartTopProducts = findViewById(R.id.chartTopProducts);
        chartUser        = findViewById(R.id.chartUser);
        chartCategory    = findViewById(R.id.chartCategory);
    }

    private void loadKpi() {
        double revenue   = dao.getTotalRevenue();
        int    orders    = dao.getTotalOrders();
        int    customers = dao.getTotalCustomers();
        double avgOrder  = orders > 0 ? revenue / orders : 0;

        txtRevenue.setText(formatVnd(revenue));
        txtOrders.setText(String.valueOf(orders));
        txtNewCustomers.setText(String.valueOf(customers));
        txtAvgOrder.setText(formatVndShort(avgOrder));
    }

    private void loadRevenueChart() {
        List<RevenueStat> list = dao.getRevenueByMonth();
        if (list == null || list.isEmpty()) return;

        ArrayList<BarEntry> entries = new ArrayList<>();
        ArrayList<String>   labels  = new ArrayList<>();

        for (int i = 0; i < list.size(); i++) {
            entries.add(new BarEntry(i, (float) list.get(i).getRevenue()));
            String raw = list.get(i).getMonth();
            try {
                int m = Integer.parseInt(raw.substring(5));
                labels.add("T" + m);
            } catch (Exception e) {
                labels.add(raw);
            }
        }

        BarDataSet dataSet = new BarDataSet(entries, "Doanh thu");
        dataSet.setColor(COLOR_BLUE);
        dataSet.setDrawValues(false);

        BarData data = new BarData(dataSet);
        data.setBarWidth(0.6f);

        styleBarChart(barChartRevenue, labels);
        barChartRevenue.setData(data);
        barChartRevenue.getLegend().setEnabled(false);
        barChartRevenue.animateY(900);
        barChartRevenue.invalidate();
    }

    private void loadTopProductChart() {
        List<TopProduct> list = dao.getTopProducts();
        if (list == null || list.isEmpty()) return;

        int[] purpleRamp = { COLOR_PURPLE, COLOR_PURPLE_MID, COLOR_PURPLE_LIGHT, COLOR_PURPLE_PALE, COLOR_PURPLE_GHOST };

        ArrayList<BarEntry> entries = new ArrayList<>();
        ArrayList<String>   labels  = new ArrayList<>();

        for (int i = 0; i < list.size(); i++) {
            entries.add(new BarEntry(i, list.get(i).getTotalSold()));
            labels.add(formatLabel(list.get(i).getBookName(), 14));
        }

        BarDataSet dataSet = new BarDataSet(entries, "");
        dataSet.setColors(purpleRamp);
        dataSet.setValueTextColor(COLOR_TEXT);
        dataSet.setValueTextSize(10f);
        dataSet.setValueFormatter(new IntValueFormatter());

        BarData data = new BarData(dataSet);
        data.setBarWidth(0.5f);

        styleHorizontalBarChart(chartTopProducts, labels);
        chartTopProducts.setData(data);
        // Tăng giới hạn trục Y để tránh số bị mất ở mép phải
        chartTopProducts.getAxisLeft().setAxisMaximum(data.getYMax() * 1.15f);
        chartTopProducts.animateX(800);
        chartTopProducts.invalidate();
    }

    private void loadUserChart() {
        List<UserStat> list = dao.getUserStats();
        if (list == null || list.isEmpty()) return;

        int[] tealRamp = { COLOR_TEAL, COLOR_TEAL_MID, COLOR_TEAL_LIGHT, COLOR_TEAL_PALE, COLOR_TEAL_GHOST };

        ArrayList<BarEntry> entries = new ArrayList<>();
        ArrayList<String>   labels  = new ArrayList<>();

        for (int i = 0; i < list.size(); i++) {
            entries.add(new BarEntry(i, (float) list.get(i).getTotalSpent()));
            labels.add(formatLabel(list.get(i).getUsername(), 12));
        }

        BarDataSet dataSet = new BarDataSet(entries, "");
        dataSet.setColors(tealRamp);
        dataSet.setValueTextColor(COLOR_TEXT);
        dataSet.setValueTextSize(10f);
        dataSet.setValueFormatter(new VndShortFormatter());

        BarData data = new BarData(dataSet);
        data.setBarWidth(0.5f);

        styleHorizontalBarChart(chartUser, labels);
        chartUser.setData(data);
        // Tăng giới hạn trục Y để số doanh thu không bị đè vào mép
        chartUser.getAxisLeft().setAxisMaximum(data.getYMax() * 1.25f);
        chartUser.animateX(800);
        chartUser.invalidate();
    }

    private void loadCategoryChart() {
        List<CategoryStat> list = dao.getCategoryStats();
        if (list == null || list.isEmpty()) return;

        int[] palette = { COLOR_BLUE, COLOR_TEAL_MID, COLOR_AMBER, COLOR_PURPLE_MID, Color.parseColor("#D85A30") };

        ArrayList<PieEntry> entries = new ArrayList<>();
        for (CategoryStat c : list) {
            entries.add(new PieEntry(c.getTotalSold(), c.getCategoryName()));
        }

        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setColors(palette);
        dataSet.setSliceSpace(2f);
        dataSet.setSelectionShift(5f);
        dataSet.setValueTextSize(11f);
        dataSet.setValueTextColor(Color.WHITE);

        PieData data = new PieData(dataSet);
        chartCategory.setData(data);
        chartCategory.setUsePercentValues(true);
        chartCategory.setHoleRadius(52f);
        chartCategory.setTransparentCircleRadius(57f);
        chartCategory.setDrawEntryLabels(false);
        chartCategory.getDescription().setEnabled(false);

        Legend legend = chartCategory.getLegend();
        legend.setEnabled(true);
        legend.setWordWrapEnabled(true);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        chartCategory.animateY(900);
        chartCategory.invalidate();
    }

    // ── Helpers ────────────────────────────────────────────────────

    private void styleBarChart(BarChart chart, List<String> labels) {
        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
        xAxis.setGranularity(1f);
        xAxis.setDrawGridLines(false);
        xAxis.setTextColor(COLOR_GRAY);
        xAxis.setAxisLineColor(COLOR_GRID);

        YAxis left = chart.getAxisLeft();
        left.setGridColor(COLOR_GRID);
        left.setTextColor(COLOR_GRAY);
        left.setAxisLineColor(Color.TRANSPARENT);
        left.setValueFormatter(new VndShortFormatter());

        chart.getAxisRight().setEnabled(false);
        chart.getDescription().setEnabled(false);
        chart.setScaleEnabled(false);
    }

    private void styleHorizontalBarChart(HorizontalBarChart chart, List<String> labels) {
        XAxis xAxis = chart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM); // Ở biểu đồ ngang, BOTTOM nghĩa là bên trái
        xAxis.setGranularity(1f);
        xAxis.setLabelCount(labels.size());
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(false);
        xAxis.setTextColor(COLOR_TEXT);
        xAxis.setTextSize(10f);

        YAxis left = chart.getAxisLeft();
        left.setDrawLabels(false); // Ẩn số liệu chia vạch ở dưới để tránh rối
        left.setDrawGridLines(false);
        left.setDrawAxisLine(false);
        left.setAxisMinimum(0f);

        chart.getAxisRight().setEnabled(false);
        chart.getDescription().setEnabled(false);
        chart.getLegend().setEnabled(false);
        chart.setScaleEnabled(false);
        chart.setFitBars(true);

        // Tăng khoảng trống bên trái (cho tên) và phải (cho giá trị)
        chart.setExtraLeftOffset(15f);
        chart.setExtraRightOffset(35f);
    }

    private String formatLabel(String text, int max) {
        if (text == null) return "";
        return text.length() > max ? text.substring(0, max - 2) + "…" : text;
    }

    private String formatVnd(double value) {
        NumberFormat nf = NumberFormat.getInstance(new Locale("vi", "VN"));
        return nf.format((long) value) + " đ";
    }

    private String formatVndShort(double value) {
        if (value >= 1_000_000) return String.format(Locale.US, "%.1fM đ", value / 1_000_000);
        if (value >= 1_000)     return String.format(Locale.US, "%.0fK đ", value / 1_000);
        return (long) value + " đ";
    }

    private static class IntValueFormatter extends ValueFormatter {
        @Override
        public String getFormattedValue(float value) {
            return String.valueOf((int) value);
        }
    }

    private class VndShortFormatter extends ValueFormatter {
        @Override
        public String getFormattedValue(float value) {
            return formatVndShort(value);
        }
    }
}