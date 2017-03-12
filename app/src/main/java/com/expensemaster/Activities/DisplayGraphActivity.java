package com.expensemaster.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.EmbossMaskFilter;
import android.util.Log;
import android.view.View;

import com.androidplot.pie.PieChart;
import com.androidplot.pie.Segment;
import com.androidplot.pie.SegmentFormatter;
import com.androidplot.util.PixelUtils;
import com.androidplot.xy.BarFormatter;
import com.androidplot.xy.BarRenderer;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYGraphWidget;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYSeries;
import com.expensemaster.Bean.Category;
import com.expensemaster.DAO.SQLiteDAO;
import com.expensemaster.Management.ManagementBean;

import java.text.FieldPosition;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DisplayGraphActivity extends AppCompatActivity {

    public PieChart pie;
    private XYPlot plot;

    private Segment s1;
    private Segment s2;
    private Segment s3;

    private Segment s4;

    int[] categoryColor;
    private SQLiteDAO daoImpl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_graph);

        pie = (PieChart) findViewById(R.id.mySimplePieChart);
        plot = (XYPlot) findViewById(R.id.plot);
        daoImpl = new ManagementBean().getDAOFactory(getApplicationContext());
        categoryColor = getApplicationContext().getResources().getIntArray(R.array.rainbow);

        Intent intent = getIntent();
        String chartType = intent.getStringExtra("chart_type");
        System.out.println(chartType);
        if(chartType.equals("pie")){
            setTitle("Pie Chart");
            piechartdisplay();
            plot.setVisibility(View.INVISIBLE);
        }
        if(chartType.equals("bar")){
            setTitle("Bar Chart");
            barchartdisplay();
        }


    }

    private void barchartdisplay() {

        List<Category> categorylist=daoImpl.getCategorywiseExpense();

        final String categoryname[]= new String[categorylist.size()];
        Number value[]= new Number[categorylist.size()];
        // XYSeries wins = new SimpleXYSeries(SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, "wins", 3, 4, 5, 3);
        //List<category> categorylist=daoImpl.CalculateSumCategory();


        for(int i=0;i<categorylist.size();i++)
        {
            Category element= categorylist.get(i);
            categoryname[i]=element.getCategory();
            System.out.println("category name"+categoryname[i]+categorylist.size());

            value[i]=element.getAmount();
            System.out.println("Amount value"+value[i]);


        }
        XYSeries trail = new SimpleXYSeries(Arrays.asList(value),SimpleXYSeries.ArrayFormat.Y_VALS_ONLY,"Bar graph");
        //


        BarFormatter winsFormatter = new BarFormatter(Color.GREEN, Color.BLACK);
        plot.addSeries(trail, winsFormatter);
        BarRenderer renderer = plot.getRenderer(BarRenderer.class);
        // renderer.setBarOrientation(BarRenderer.BarOrientation.OVERLAID);
        renderer.setBarGroupWidth(BarRenderer.BarGroupWidthMode.FIXED_WIDTH, PixelUtils.dpToPix(25));
        plot.getInnerLimits().setMinY(0);
        // plot.setDomainBoundaries(-1, trail.size(), BoundaryMode.FIXED);
        plot.getGraph().getLineLabelStyle(XYGraphWidget.Edge.BOTTOM).
                setFormat(new NumberFormat() {
                    @Override
                    public StringBuffer format(double value, StringBuffer buffer,
                                               FieldPosition field) {
                        //String[] category={"Travel","Entertainment","Food","porn"};
                        int parsedInt = Math.round(Float.parseFloat(Double.toString(value)));
                        Log.d("test", parsedInt + " " + buffer + " " + field);
                        String labelString = categoryname[parsedInt];
                        buffer.append(labelString);
                        /*return new StringBuffer(DateFormatSymbols.getInstance()
                                .getShortMonths()[month] + " '0" + year);*/
                        return buffer;
                    }

                    @Override
                    public StringBuffer format(long value, StringBuffer buffer,
                                               FieldPosition field) {
                        throw new UnsupportedOperationException("Not yet implemented.");
                    }

                    @Override
                    public Number parse(String string, ParsePosition position) {
                        throw new UnsupportedOperationException("Not yet implemented.");
                    }
                });
    }

    private void piechartdisplay() {
        final float padding = PixelUtils.dpToPix(30);
        pie.getPie().setPadding(padding, padding, padding, padding);
        EmbossMaskFilter emf = new EmbossMaskFilter(
                new float[]{1, 1, 1}, 0.4f, 10, 8.2f);
        List<Category> categoryList=daoImpl.getCategorywiseExpense();
        System.out.println("Category1111111111111111111111111111"+categoryList.size());


        List<Segment> piesegmentList=new ArrayList<>();
        List<SegmentFormatter> piesegmentFormatterList=new ArrayList<>();
        //int[] colorse = {12,65536,256,16777216,91};
        for (Category element : categoryList) {
            // 1 - can call methods of element
            Segment segments= new Segment(element.getCategory(),element.getAmount());
            //SegmentFormatter sf1 = new SegmentFormatter(this, R.xml.pie_segment_formatter1);
            piesegmentList.add(segments);
            System.out.println("Category"+element.getCategory()+"Amount"+element.getAmount());
            //  count++;
            // ...
        }
        for(int i=0;i<categoryList.size();i++)
        {
            SegmentFormatter formatter=new SegmentFormatter(categoryColor[i]);
            formatter.getLabelPaint().setShadowLayer(3, 0, 0, Color.BLACK);
            formatter.getFillPaint().setMaskFilter(emf);
            piesegmentFormatterList.add(formatter);
        }

        for(int j=0;j<piesegmentFormatterList.size();j++)
        {
            pie.addSegment(piesegmentList.get(j),piesegmentFormatterList.get(j));
        }
        pie.getBorderPaint().setColor(Color.TRANSPARENT);
        pie.getBackgroundPaint().setColor(Color.TRANSPARENT);
    }
}
