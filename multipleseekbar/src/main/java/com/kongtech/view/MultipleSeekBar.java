package com.kongtech.view;


import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kongtech.multipleseekbar.R;

import org.w3c.dom.Text;

public class MultipleSeekBar extends FrameLayout implements View.OnTouchListener {

    private static final String TAG = "MultipleSeekBar";

    private TextView tvMaxValue;
    private TextView tvMinValue;


    private int viewWidth;
    private int viewHeight;
    private int containerHeight;

    private int min;
    private int max;

    public MultipleSeekBar(Context context) {
        super(context);
        init(context);
    }

    public MultipleSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MultipleSeekBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void init(Context context) {
        View view = inflate(context, R.layout.view_multiple_seekbar, this);

        view.findViewById(R.id.container_min).setOnTouchListener(this);
        view.findViewById(R.id.container_max).setOnTouchListener(this);

        tvMaxValue = (TextView) view.findViewById(R.id.tvMaxValue);
        tvMinValue = (TextView) view.findViewById(R.id.tvMinValue);

        min = 45;
        max = 70;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        viewWidth = w;
        viewHeight = h;

        containerHeight = findViewById(R.id.container_min).getMeasuredHeight();

        setMinValue(min);
        setMaxValue(max);
    }

    private float startY;
    private float startTranslationY;

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //Log.d(TAG, "ACTION_DOWN");
                startY = event.getRawY();
                startTranslationY = v.getTranslationY();
                return true;
            case MotionEvent.ACTION_MOVE:
                float deltaY = event.getRawY() - startY;
                int value = getValueFromPosition(deltaY) + getValueFromPosition(startTranslationY);
               // Log.d(TAG, "ACTION_MOVE deltaY: " + deltaY + ", value: " + value);
                if (0 <= value && value <= 100) {
                    if (v.getId() == R.id.container_min) setMinValue(value);
                    else setMaxValue(value);
                }
                return true;
        }

        return false;
    }

    public int getMaxValue(){
        return max;
    }

    public int getMinValue(){
        return min;
    }

    public void setIndicator(int value){
        View v = findViewById(R.id.indicator);
        v.setVisibility(View.VISIBLE);
        v.setTranslationY(getPositionFromValue(value));
    }

    public void setMaxValue(int value) {
        if (!validateRange(min, value)) return;
        max = value;
        tvMaxValue.setText("-" + max + "dBm");
        setValue(findViewById(R.id.container_max), max);
        invalidateRange();
    }

    public void setMinValue(int value) {
        if (!validateRange(value, max)) return;
        min = value;
        tvMinValue.setText("-" + min + "dBm");
        setValue(findViewById(R.id.container_min), min);
        invalidateRange();
    }

    private void setValue(View view, int value) {
        view.setTranslationY(getPositionFromValue(value));
    }

    private void invalidateRange() {

        float level1 = (float) min / 100.0f;
        float level2 = (float) (max - min) / 100.0f;
        float level3 = 1.0f - level1 - level2;

        //Log.d(TAG, "invalidateRange() level: " + level1 + ", " + level2 + ", " + level3);
        setWeight(findViewById(R.id.tvLevel1), level1);
        setWeight(findViewById(R.id.tvLevel2), level2);
        setWeight(findViewById(R.id.tvLevel3), level3);

        setWeight(findViewById(R.id.progressLevel1), level1);
        setWeight(findViewById(R.id.progressLevel2), level2);
        setWeight(findViewById(R.id.progressLevel3), level3);
    }

    private void setWeight(View v, float weight) {
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) v.getLayoutParams();
        params.weight = weight;
        v.setLayoutParams(params);
    }

    private boolean validateRange(int min, int max) {
        if (min < 10 | max > 90 | max - min < 10) return false;
        return true;
    }

    private int getValueFromPosition(float deltaY) {
        float totalHeight = viewHeight - containerHeight;
        return (int) (deltaY * 100 / totalHeight);
    }

    private float getPositionFromValue(int value) {
        float totalHeight = viewHeight - containerHeight;
        return (float) value * totalHeight / 100;
    }
}
