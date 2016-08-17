package com.kongtech.plutocon.template.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kongtech.plutocon.template.distance.R;

public class AttrItemView extends LinearLayout {

    private TextView tvAttrName;
    private TextView tvAttrValue;
    private ImageView kongLogo;
    private ImageView ivIndicator;

    private boolean isChangeable;
    private int attrValueColor;

    private String attrName;
    private String attrValue;

    public AttrItemView(Context context) {
        super(context);
        init();
    }

    public AttrItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
        initAttrs(context, attrs);
    }

    public AttrItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
        initAttrs(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public AttrItemView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
        initAttrs(context, attrs, defStyleAttr, defStyleRes);
    }

    private void init() {
        inflate(getContext(), R.layout.item_attribute, this);

        tvAttrName = (TextView) findViewById(R.id.tvAttrName);
        tvAttrValue = (TextView) findViewById(R.id.tvAttrValue);
        kongLogo = (ImageView) findViewById(R.id.ivLogo);
        ivIndicator = (ImageView) findViewById(R.id.ivIndicator);

        kongLogo.setVisibility(View.GONE);
        setChangeable(false);
    }

    private void setAttr(TypedArray typedArray) {
        attrValueColor = typedArray.getColor(R.styleable.AttrItem_attrValueColor, 0x99ffffff);
        tvAttrValue.setTextColor(attrValueColor);

        setAttrName(typedArray.getString(R.styleable.AttrItem_attrName));
        setValue(typedArray.getString(R.styleable.AttrItem_attrValue));
        setChangeable(typedArray.getBoolean(R.styleable.AttrItem_isChangeable, false));


        if (typedArray.getBoolean(R.styleable.AttrItem_isLogo, false)) {
            setVisibleLogo();
        }
        typedArray.recycle();
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.AttrItem);
        setAttr(typedArray);
    }

    private void initAttrs(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.AttrItem, defStyleAttr, 0);
        setAttr(typedArray);
    }

    private void initAttrs(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.AttrItem, defStyleAttr, defStyleRes);
        setAttr(typedArray);
    }


    public String getAttrName() {
        return attrName;
    }

    public void setAttrName(String attrName) {
        this.attrName = attrName;
        tvAttrName.setText(attrName);
    }

    public String getValue() {
        return attrValue;
    }

    public void setAttrValueColor(int color){
        this.attrValueColor = color;
        tvAttrValue.setTextColor(color);
    }

    public void setValue(String attrValue) {
        this.attrValue = attrValue;
        tvAttrValue.setText(attrValue);
    }

    public boolean isChangeable() {
        return isChangeable;
    }

    public void setChangeable(boolean changeable) {

        this.isChangeable = changeable;

        if (isChangeable) {
            setClickable(true);
            tvAttrValue.setTextColor(0xff000000);
            ivIndicator.setVisibility(View.VISIBLE);
        } else {
            setClickable(false);
            tvAttrValue.setTextColor(0xff9b9b9b);
            ivIndicator.setVisibility(View.GONE);
        }

        invalidate();
    }

    public void setVisibleLogo() {
        kongLogo.setVisibility(View.VISIBLE);
        ivIndicator.setVisibility(View.GONE);
        tvAttrValue.setVisibility(View.GONE);

        invalidate();
    }

    public void setNameColor(int color) {
        tvAttrName.setTextColor(color);
    }

    public void setValueTextSize(float sp) {
        tvAttrValue.setTextSize(sp);
    }
}
