package com.example.goshopping;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * date:2018/11/20
 * author:别的小朋友(别的小朋友)
 * function:
 */
public class Jia extends LinearLayout implements View.OnClickListener {
    private int number=0;
    private TextView mJian;
    private TextView mJia;
    private TextView mshu;

    public Jia(Context context) {
        this(context, null);
    }

    public Jia(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Jia(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        View view = View.inflate(context, R.layout.jj, this);
        mJian = view.findViewById(R.id.tv_jian);
        mJia = view.findViewById(R.id.tv_jia);
        mshu = view.findViewById(R.id.tv_shu);
        mJia.setOnClickListener(this);
        mJian.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_jian:
              if (number>1){
                  number--;
                  mshu.setText(number+"");
                  if (mAbcd!=null){
                      mAbcd.hao(number);
                  }
              }

                break;
            case R.id.tv_jia:
                number++;
                mshu.setText(number+"");
                if (mAbcd!=null){
                    mAbcd.hao(number);
                }
                break;
        }

    }


    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
        mshu.setText(number+"");
    }

    public interface Abcd{
        void  hao(int number);
    }
    public Abcd mAbcd;

    public void setAbcd(Abcd abcd) {
        mAbcd = abcd;
    }
}
