package com.expensemaster.Activities;

import android.animation.ValueAnimator;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.EmbossMaskFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.androidplot.pie.PieChart;
import com.androidplot.pie.PieRenderer;
import com.androidplot.pie.Segment;
import com.androidplot.pie.SegmentFormatter;
import com.androidplot.util.PixelUtils;
import com.androidplot.xy.XYPlot;
import com.expensemaster.Activities.R;
import com.expensemaster.Bean.Category;
import com.expensemaster.Bean.Expense;
import com.expensemaster.DAO.SQLiteDAO;
import com.expensemaster.Management.ManagementBean;
import com.expensemaster.Supporting.ExpenseRecyclerViewAdapter;

import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class ChartsActivity extends AppCompatActivity {

    Button btnPieChart, btnBarChart,btnXYchart;
    public PieChart pie;
    private XYPlot plot;
    private XYPlot Xyplot;

    private Segment s1;
    private Segment s2;
    private Segment s3;

    private Segment s4;
    Button btnSearch;
    EditText txtFrom, txtTo;
    private List<Expense> expenseList = new ArrayList<>();
    List<Category> categoryList;
    int[] categoryColor;
    private SQLiteDAO daoImpl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charts);

        pie = (PieChart) findViewById(R.id.mySimplePieChart);
        daoImpl = new ManagementBean().getDAOFactory(getApplicationContext());
        categoryColor = getApplicationContext().getResources().getIntArray(R.array.rainbow);
        categoryList=daoImpl.getCategorywiseExpense();
        final SimpleDateFormat formatter = new SimpleDateFormat("dd-MMMM-yyyy");
        final DateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        txtFrom = (EditText) findViewById(R.id.txt_from);
        txtTo = (EditText) findViewById(R.id.txt_to);
        btnSearch = (Button) findViewById(R.id.btn_search);

        piechartdisplay();



        txtFrom.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {
                    final Calendar c = Calendar.getInstance();
                    DatePickerDialog dpd = new DatePickerDialog(ChartsActivity.this,
                            new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                    txtFrom.setText(dayOfMonth + "-" + (new DateFormatSymbols().getShortMonths()[monthOfYear]) + "-" + year);
                                }
                            }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
                    dpd.show();
                }
                return false;
            }
        });

        txtTo.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {
                    final Calendar c = Calendar.getInstance();
                    DatePickerDialog dpd = new DatePickerDialog(ChartsActivity.this,
                            new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                    txtTo.setText(dayOfMonth + "-" + (new DateFormatSymbols().getShortMonths()[monthOfYear]) + "-" + year);
                                }
                            }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
                    dpd.show();
                }
                return false;
            }
        });
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fromDate = null, toDate = null;
                categoryList = null;
                try {
                    fromDate = df.format(formatter.parse(txtFrom.getText().toString()));
                    toDate = df.format(formatter.parse(txtTo.getText().toString()));
                    System.out.println(fromDate + "=" + toDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                //expenseList = daoImpl.searchTransactions(fromDate, toDate);
                categoryList = daoImpl.getExpense(fromDate, toDate);
                System.out.println("444444444444444444444444444444444: "+categoryList.size());
                pie.clear();
                piechartdisplay();
                pie.redraw();
                setupIntroAnimation();
                System.out.println("from and to date:" + fromDate + toDate);


            }
        });

    }

    private void piechartdisplay() {
        final float padding = PixelUtils.dpToPix(30);

        pie.getPie().setPadding(padding, padding, padding, padding);
        EmbossMaskFilter emf = new EmbossMaskFilter(
                new float[]{1, 1, 1}, 0.4f, 10, 8.2f);

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


    @Override
    public void onStart() {
        super.onStart();
        setupIntroAnimation();
    }

   /* protected void updateDonutText() {
        donutSizeTextView.setText(donutSizeSeekBar.getProgress() + "%");
    }*/


    protected void setupIntroAnimation() {
        final PieRenderer renderer = pie.getRenderer(PieRenderer.class);
        // start with a zero degrees pie:

        renderer.setExtentDegs(0);
        // animate a scale value from a starting val of 0 to a final value of 1:
        ValueAnimator animator = ValueAnimator.ofFloat(0, 1);

        // use an animation pattern that begins and ends slowly:
        animator.setInterpolator(new AccelerateDecelerateInterpolator());

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float scale = valueAnimator.getAnimatedFraction();
//                scalingSeries1.setScale(scale);
//                scalingSeries2.setScale(scale);
                renderer.setExtentDegs(360 * scale);
                pie.redraw();
            }
        });

        animator.setDuration(1500);
        animator.start();



    }



}
