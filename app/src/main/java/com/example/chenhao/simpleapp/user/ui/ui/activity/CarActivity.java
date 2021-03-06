package com.example.chenhao.simpleapp.user.ui.ui.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.chenhao.simpleapp.R;
import com.example.chenhao.simpleapp.app.BaseData;
import com.example.chenhao.simpleapp.base.SuperBaseActivity;
import com.example.chenhao.simpleapp.base.UserInfoBean;
import com.example.chenhao.simpleapp.bean.Car;
import com.example.chenhao.simpleapp.db.CarTableTableDBopenhelerService;
import com.example.chenhao.simpleapp.db.UserTableDBopenhelerService;

import java.util.ArrayList;
import java.util.List;

/**
 * The type Car activity.
 * 用户的小车界面
 */
public class CarActivity extends SuperBaseActivity {

    private UserTableDBopenhelerService instance;
    private String[] split;
    private CarTableTableDBopenhelerService instanceCar;
    /**
     * The M cars.
     */
    List<Car> mCars = new ArrayList<>();

    private ListView mListView;
    /**
     * The M adapter.
     */
    CarActivityListViewAdapter1 mAdapter;
    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car);
        initDatas();
        initViews();
        if (mAdapter.getCount() <= 0) {
            showMsgDialog("用户暂无小车，请添加小车！");
        }
    }

    private void initViews() {
        mListView = findView(R.id.mListView);
        mListView.setAdapter(mAdapter = new CarActivityListViewAdapter1(mCars));
        findView(R.id.add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                startActivity(new Intent(CarActivity.this, CarInfoActivity.class)
                        .putExtra("key", mCars.get(i).getId()));
            }
        });

    }

    private void initDatas() {
        instance = UserTableDBopenhelerService.getInstance(this);
        UserInfoBean userInfoBean = instance.findUserInfoBean(BaseData.mUserInfoBean.getUserName(), BaseData.mUserInfoBean.getPassword());
        instanceCar = CarTableTableDBopenhelerService.getInstance(this);
        String carId = userInfoBean.getCarId();
        Log.e("233", "initDatas: " + carId);
        try {

            if (carId != null) {
                split = carId.split(",");
                mCars.clear();
                for (int i = 0; i < split.length; i++) {
                    if (!TextUtils.isEmpty(split[i])) {
                        Car car = instanceCar.findCar(Integer.valueOf(split[i]));
                        mCars.add(car);
                    } else {
                        getSharedPreferences("data", MODE_PRIVATE).edit().clear().commit();
                    }
                }

            } else {
                split = new String[0];
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    /**
     * Update.
     */
    public void update() {
        initDatas();
        mAdapter.notifyDataSetChanged();
    }

    /**
     * Show dialog.
     */
    public void showDialog() {
        ListView listView = new ListView(this);
        listView.setAdapter(new CarActivityListViewAdapter2(instanceCar.findAllCar()));
        dialog = new Dialog(this);
        dialog.setTitle("小车选择");
        dialog.setContentView(listView);
        dialog.show();

    }

    /**
     * Dissmiss dialog.
     */
    public void dissmissDialog() {
        if (dialog.isShowing())
            dialog.dismiss();


    }


    @Override
    public String getToolbarTitle() {
        return "我的小车";
    }

    /**
     * The type Car activity list view adapter 1.
     */
    class CarActivityListViewAdapter1 extends BaseAdapter {
        private List<Car> mCars;

        /**
         * Instantiates a new Car activity list view adapter 1.
         *
         * @param mCars the m cars
         */
        public CarActivityListViewAdapter1(List<Car> mCars) {
            this.mCars = mCars;
        }

        @Override
        public int getCount() {
            return mCars.size();
        }

        @Override
        public Car getItem(int i) {
            return mCars.get(i);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {
            if (view == null) view = getLayoutInflater().inflate(R.layout.item_car_activity, null);
            TextView t1 = (TextView) view.findViewById(R.id.item_t1);
            TextView t2 = (TextView) view.findViewById(R.id.item_t2);
            TextView t3 = (TextView) view.findViewById(R.id.item_t3);
            TextView t4 = (TextView) view.findViewById(R.id.item_t4);

            final Car item = getItem(i);
            t1.setText("闽A" + item.getId());
            t2.setText("车架号:" + item.getCarId());
            t3.setText("发动机号:" + item.getUserId());
            t4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String[] temp = new String[split.length - 1];
                    int num = 0;
                    for (int j = 0; j < split.length; j++) {
                        if (!split[j].equals(item.getId() + "")) {
                            if (!TextUtils.isEmpty(split[j]))
                                temp[num++] = split[j];
                        }
                    }
                    split = temp;
                    instance.updateCarId(BaseData.mUserInfoBean.getId(), splitAndString(split));
                    update();

                }
            });
            return view;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            startActivity(new Intent(CarActivity.this, UserHomeActivity.class));
            finish();
        }
        return true;
//        return super.onOptionsItemSelected(item);
    }

    /**
     * The type Car activity list view adapter 2.
     */
    class CarActivityListViewAdapter2 extends BaseAdapter {
        private List<Car> mCars;

        /**
         * Instantiates a new Car activity list view adapter 2.
         *
         * @param mCars the m cars
         */
        public CarActivityListViewAdapter2(List<Car> mCars) {
            this.mCars = mCars;
        }

        @Override
        public int getCount() {
            return mCars.size();
        }

        @Override
        public Car getItem(int i) {
            return mCars.get(i);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {
            if (view == null) view = getLayoutInflater().inflate(R.layout.item_car_activity, null);
            TextView t1 = (TextView) view.findViewById(R.id.item_t1);
            TextView t2 = (TextView) view.findViewById(R.id.item_t2);
            TextView t3 = (TextView) view.findViewById(R.id.item_t3);
            TextView t4 = (TextView) view.findViewById(R.id.item_t4);

            final Car item = getItem(i);
            t1.setText("闽A" + item.getId());
            t2.setText("车架号:" + item.getCarId());
            t3.setText("发动机号:" + item.getUserId());
            t4.setText("添加");
            t4.setBackgroundResource(R.drawable.ic_background_blue);
            t4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    boolean isAdd = true;
                    String[] temp = new String[split.length + 1];
                    int num = 0;
                    for (int j = 0; j < split.length; j++) {
                        temp[num++] = split[j];
                        if ((item.getId() + "").equals(split[j])) {
                            isAdd = false;
                        }
                    }
                    if (!isAdd) {
                        showMsgDialog("不能添加重复的小车！");
                        return;
                    }
                    temp[num] = item.getId() + "";

                    split = temp;
                    instance.updateCarId(BaseData.mUserInfoBean.getId(), splitAndString(split));
                    update();
                    dissmissDialog();

                }
            });
            return view;
        }
    }
}
