package com.example.goshopping;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * date:2018/11/20
 * author:别的小朋友(别的小朋友)
 * function:
 */
public class Madapter extends BaseExpandableListAdapter {
    List<User.DataBean> data;

    public Madapter(List<User.DataBean> data) {
        this.data = data;

    }

    @Override
    public int getGroupCount() {
        return data == null ? 0 : data.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return data.get(groupPosition).getList() == null ? 0 : data.get(groupPosition).getList().size();
    }


    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupHolder groupHolder = null;
        if (convertView == null) {
            groupHolder = new GroupHolder();
            convertView = View.inflate(parent.getContext(), R.layout.group_item, null);
            groupHolder.tv_group = convertView.findViewById(R.id.tv_group);
            groupHolder.ck_group = convertView.findViewById(R.id.ck_group);
            convertView.setTag(groupHolder);
        } else {
            groupHolder = (GroupHolder) convertView.getTag();
        }
        groupHolder.tv_group.setText(data.get(groupPosition).getSellerName());
        //商家选中
        boolean currentShopChecked = isCurrentShopChecked(groupPosition);
        groupHolder.ck_group.setChecked(currentShopChecked);
        groupHolder.ck_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnClickListener != null) {
                    mOnClickListener.ischeckShop(groupPosition);
                }
            }
        });
        return convertView;
    }

    class GroupHolder {
        TextView tv_group;
        CheckBox ck_group;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildHoler childHoler = null;
        if (childHoler == null) {
            childHoler = new ChildHoler();
            convertView = View.inflate(parent.getContext(), R.layout.child_item, null);

            childHoler.tv_price = convertView.findViewById(R.id.tv_price);
            childHoler.tv_title = convertView.findViewById(R.id.tv_title);
            childHoler.ig_tu = convertView.findViewById(R.id.ig_tu);
            childHoler.ck_chlid = convertView.findViewById(R.id.ck_child);
            childHoler.ha=convertView.findViewById(R.id.ha);
            convertView.setTag(childHoler);
        } else {
            childHoler = (ChildHoler) convertView.getTag();
        }


        childHoler.tv_title.setText(data.get(groupPosition).getList().get(childPosition).getTitle());

        childHoler.tv_price.setText(data.get(groupPosition).getList().get(childPosition).getPrice() + "");
        String images[] = data.get(groupPosition).getList().get(childPosition).getImages().split("!");
        ImageLoader.getInstance().displayImage(images[0], childHoler.ig_tu);
        //商品是否选中
        childHoler.ck_chlid.setChecked(data.get(groupPosition).getList().get(childPosition).getSelected() == 1);
        childHoler.ck_chlid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnClickListener != null) {
                    mOnClickListener.ischeckGoods(groupPosition, childPosition);
                }

            }
        });
        //商品数量
        childHoler.ha.setNumber(data.get(groupPosition).getList().get(childPosition).getNum());
        childHoler.ha.setAbcd(new Jia.Abcd() {
            @Override
            public void hao(int number) {
                if (mOnClickListener!=null){
                    mOnClickListener.ischeckNum(groupPosition,childPosition,number);
                }
            }
        });

        return convertView;
    }

    class ChildHoler {
        ImageView ig_tu;
        TextView tv_title;
        TextView tv_price;
        CheckBox ck_chlid;
        Jia ha;

    }

    //商家是否全选中状态
    public boolean isCurrentShopChecked(int groupPosition) {
        User.DataBean dataBean = data.get(groupPosition);
        List<User.DataBean.ListBean> list = dataBean.getList();
        for (User.DataBean.ListBean listBean : list) {
            if (listBean.getSelected() == 0) {
                return false;
            }
        }
        return true;
    }

    //全选按钮   所有商品是否被选中
    public boolean isCurrenGoodsChecked() {
        for (int i = 0; i < data.size(); i++) {
            User.DataBean dataBean = data.get(i);
            List<User.DataBean.ListBean> list = dataBean.getList();
            for (int j = 0; j < list.size(); j++) {
                if (list.get(j).getSelected() == 0) {
                    return false;
                }
            }
        }
        return true;
    }

    //商品数量 只计算选中的
    public int isGoodscheckedNum(){
       int  allnum=0;
        for (int i=0;i<data.size();i++){
            User.DataBean dataBean = data.get(i);
            List<User.DataBean.ListBean> list = dataBean.getList();
            for (int j=0;j<list.size();j++){
                if (list.get(j).getSelected()==1){
                    int num=list.get(j).getNum();
                    allnum+=num;
                }
            }
        }
        return allnum;
    }

    //商品价格 只计算选中的
    public float isGoodscheckedPrice(){
        float allprice=0;
        for (int i=0;i<data.size();i++){
            User.DataBean dataBean = data.get(i);
            List<User.DataBean.ListBean> list = dataBean.getList();
            for (int j=0;j<list.size();j++){
                if (list.get(j).getSelected()==1){
                    float price=list.get(j).getPrice();
                    int num=list.get(j).getNum();
                    allprice+=num*price;
                }

            }
        }
        return allprice;
    }

    //商家状态

    public void changeShopChecked(int groupPosition, boolean isChecked) {
        User.DataBean dataBean = data.get(groupPosition);
        List<User.DataBean.ListBean> list = dataBean.getList();
        for (int i = 0; i < list.size(); i++) {
            User.DataBean.ListBean listBean = list.get(i);
            listBean.setSelected(isChecked ? 1 : 0);
        }
    }

    //改变商品状态
    public void changeGoodsChecked(int groupPosition, int childPosition) {
        User.DataBean dataBean = data.get(groupPosition);
        List<User.DataBean.ListBean> list = dataBean.getList();
        User.DataBean.ListBean listBean = list.get(childPosition);
        listBean.setSelected(listBean.getSelected() == 0 ? 1 : 0);

    }
    //所有商品的状态
  public void changeAll(boolean isChecked){
        for (int i=0;i<data.size();i++){
            User.DataBean dataBean = data.get(i);
            List<User.DataBean.ListBean> list = dataBean.getList();
            for (int j=0;j<list.size();j++){
                list.get(j).setSelected(isChecked?1:0);
            }
        }
  }


  //加减器的数量
    public void changeGoodsNum(int groupPosition,int childPosition,int number){
        User.DataBean.ListBean listBean = data.get(groupPosition).getList().get(childPosition);
        listBean.setNum(number);
    }






























































    //接口回调
    public interface OnClickListener {
        //商家
        void ischeckShop(int groupPosition);

        //商品
        void ischeckGoods(int groupPosition, int childPosition);
        //加减器
        void ischeckNum(int groupPosition, int childPosition,int number);
    }

    public OnClickListener mOnClickListener;

    public void setOnClickListener(OnClickListener onClickListener) {
        mOnClickListener = onClickListener;
    }


    //必须实现 但是不用写
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return null;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return null;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}
