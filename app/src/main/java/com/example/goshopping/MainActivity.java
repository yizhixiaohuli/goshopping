package com.example.goshopping;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Jia ha;
    private ExpandableListView el;
    private String url = "http://www.zhaoapi.cn/product/getCarts";
    private CheckBox ck_all;
    private Madapter ma;
    private TextView tv_allprice;
    private Button bt_pay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();


    }

    private void initView() {
        ha = (Jia) findViewById(R.id.ha);

        el = (ExpandableListView) findViewById(R.id.el);
        el.setGroupIndicator(null);
        getData();
        ck_all = (CheckBox) findViewById(R.id.ck_all);
        ck_all.setOnClickListener(this);
        tv_allprice = (TextView) findViewById(R.id.tv_allprice);
        tv_allprice.setOnClickListener(this);
        bt_pay = (Button) findViewById(R.id.bt_pay);
        bt_pay.setOnClickListener(this);
    }

    private void getData() {
        HashMap<String, String> map = new HashMap<>();
        map.put("uid", "71");
        OkUtils.getInstance().doPost(url, map, new OkUtils.OkHttpCallBack() {
            @Override
            public void success(String s) {
                User user = new Gson().fromJson(s, User.class);
                if ("0".equals(user.getCode())) {
                    List<User.DataBean> data = user.getData();
                    ma = new Madapter(data);
                    el.setAdapter(ma);
                    ma.setOnClickListener(new Madapter.OnClickListener() {
                        @Override
                        public void ischeckShop(int groupPosition) {
                            boolean currentShopChecked = ma.isCurrentShopChecked(groupPosition);
                            ma.changeShopChecked(groupPosition, !currentShopChecked);
                            ma.notifyDataSetChanged();
                            refreshAll();
                        }

                        @Override
                        public void ischeckGoods(int groupPosition, int childPosition) {
                            ma.changeGoodsChecked(groupPosition, childPosition);
                            ma.notifyDataSetChanged();
                            refreshAll();
                        }

                        @Override
                        public void ischeckNum(int groupPosition, int childPosition, int number) {
                            ma.changeGoodsNum(groupPosition,childPosition,number);
                            ma.notifyDataSetChanged();
                            refreshAll();
                        }
                    });
                    for (int i = 0; i < data.size(); i++) {
                        el.expandGroup(i);

                    }
                }

            }

            @Override
            public void failed(Exception e) {
                Toast.makeText(MainActivity.this, "没网络了", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ck_all:
                //选中商品
                boolean currenGoodsChecked = ma.isCurrenGoodsChecked();
                //改变状态
                ma.changeAll(!currenGoodsChecked);
                //刷新
                ma.notifyDataSetChanged();
                refreshAll();
                break;
        }

    }

    public void refreshAll(){
        //全选按钮
        boolean currenGoodsChecked = ma.isCurrenGoodsChecked();
        ck_all.setChecked(currenGoodsChecked);
        //价格
        float goodscheckedPrice = ma.isGoodscheckedPrice();
        tv_allprice.setText("总计"+goodscheckedPrice);
        //数量
        int goodscheckedNum = ma.isGoodscheckedNum();
        bt_pay.setText("结算"+goodscheckedNum);
    }
}
