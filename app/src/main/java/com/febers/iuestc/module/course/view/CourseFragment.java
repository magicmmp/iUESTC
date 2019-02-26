/*
 * Created by Febers 2018.
 * Copyright (c). All rights reserved.
 *
 * Last Modified 18-9-4 下午6:24
 *
 */

package com.febers.iuestc.module.course.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.febers.iuestc.R;
import com.febers.iuestc.MyApplication;
import com.febers.iuestc.base.BaseCode;
import com.febers.iuestc.base.BaseEvent;
import com.febers.iuestc.base.BaseFragment;
import com.febers.iuestc.entity.BeanCourse;
import com.febers.iuestc.module.course.presenter.CourseContract;
import com.febers.iuestc.module.course.presenter.CoursePresenterImpl;
import com.febers.iuestc.module.login.view.LoginActivity;
import com.febers.iuestc.util.CustomSPUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CourseFragment extends BaseFragment implements CourseContract.View {

    private static final String TAG = "CourseFragment";

    private CourseContract.Presenter mPresenter = new CoursePresenterImpl(this);

    private List<Button> buttonList = new ArrayList<>();
    private List<Integer> weeks = new ArrayList<>();
    private OptionsPickerView mPickerView;
    private TextView tvNowWeek;
    private ImageView ivNull;
    private int nowWeek;

    @Override
    protected int setContentView() {
        return R.layout.fragment_course;
    }

    @Override
    protected int setMenu() {
        return R.menu.course_menu;
    }

    @Override
    public void dataRequest(Boolean isRefresh) {
        if (isRefresh) {
            if (!MyApplication.checkNetConnecting()) {
                onError("当前网络不可用");
                return;
            }
            showProgressDialog();
        }
        mPresenter.courseRequest(isRefresh);
    }

    @Override
    protected void initView() {
        Toolbar toolbar = findViewById(R.id.tb_course);
        toolbar.setTitle("");
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        toolbar.inflateMenu(R.menu.course_menu);    //防止activity销毁之后menu消失
        initPicker();
        tvNowWeek = findViewById(R.id.tv_course_title);
        ivNull = findViewById(R.id.iv_null_course);
        setTitle(0);
    }

    @Override
    public void showUnderCourse(BaseEvent<List<BeanCourse>> event) {
        dismissProgressDialog();
        getActivity().runOnUiThread( () -> {
            if (event.getCode() == BaseCode.ERROR) {
                onError("刷新课表出错,请尝试再次获取");
                return;
            }
            if (event.getCode() == BaseCode.CLEAR) {
                for (int i = 0; i < buttonList.size(); i++) {
                    buttonList.get(i).setVisibility(View.GONE);
                }
                ivNull.setVisibility(View.VISIBLE);
                return;
            }
            for (int i = 0; i < buttonList.size(); i++) {
                buttonList.get(i).setVisibility(View.INVISIBLE);
            }
            if (event.getCode() == BaseCode.UPDATE) {
                ivNull.setVisibility(View.GONE);
                onError("刷新课表成功");
            }
            CourseViewHelper helper = new CourseViewHelper(_mActivity);
            helper.create(event.getDate(), nowWeek, buttonList);
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_course_choose_week:
                if (!isLogin()) {
                    break;
                }
                mPickerView.show();
                break;
            case R.id.item_course_refresh:
                if (!isLogin()) {
                    break;
                }
                dataRequest(true);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        buttonList.clear();
        if (!isLogin()) {
            showUnderCourse(new BaseEvent<>(BaseCode.CLEAR, new ArrayList<>()));
            return;
        }
        Boolean firstGet = CustomSPUtil.getInstance().get(mContext
                .getString(R.string.sp_course_first_get), true);
        if (firstGet) {
            if (!MyApplication.checkNetConnecting()) {
                return;
            }
            dataRequest(true);
            CustomSPUtil.getInstance().put(mContext
                    .getString(R.string.sp_course_first_get), false);
            return;
        }
        dataRequest(false);
    }

    @Override
    public void statusToFail() {
        startActivity(new Intent(getActivity(), LoginActivity.class));
    }

    /**
     * 选择器初始化
     * 默认共有20周
     */
    private void initPicker() {
        for (int i = 1; i < 21; i++) {
            weeks.add(i);
        }
        mPickerView = new OptionsPickerBuilder(getContext(), (options1, options2, options3, v) -> {
            Toast.makeText(getContext(), "当前周数已设置为第" + (options1+1) + "周", Toast.LENGTH_SHORT).show();
            CustomSPUtil.getInstance().put(mContext.getString(R.string.sp_now_week), (options1+1));
            CustomSPUtil.getInstance().put("set_week", true);
            setTitle(options1+1);
            dataRequest(false);
        })
                .setTitleText("选择当前周数")
                .setOutSideCancelable(false)
                .setCyclic(true, false, false)
                //.setCancelColor(AttrUtil.getColor(getContext(), R.color.colorAccent))
                .setBgColor(getResources().getColor(R.color.lightgray))
                .build();
        mPickerView.setPicker(weeks);
    }

    private void setTitle(int week) {
        nowWeek = 1;
        if (week == 0) {
            nowWeek = CustomSPUtil.getInstance().get(MyApplication.getContext().getString(R.string.sp_now_week), 1);
            if (nowWeek == 0) nowWeek = 1;
        } else {
            nowWeek = week;
        }
        Objects.requireNonNull(getActivity()).runOnUiThread(()-> tvNowWeek.setText("第" + nowWeek + "周"));
    }

    private Boolean isLogin() {
        return CustomSPUtil.getInstance().get(mContext.getString(R.string.sp_is_login), false);
    }

    public static CourseFragment newInstance(String param1) {
        Bundle args = new Bundle();
        args.putString(PARAMTER_1, param1);
        CourseFragment fragment = new CourseFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
