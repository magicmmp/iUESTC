/*
 * Created by Febers 2018.
 * Copyright (c). All rights reserved.
 *
 * Last Modified 18-6-17 下午2:22
 *
 */

package com.febers.iuestc.module.exam.view;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioGroup;

import com.febers.iuestc.adapter.AdapterExam;
import com.febers.iuestc.base.BaseApplication;
import com.febers.iuestc.R;
import com.febers.iuestc.base.BaseCode;
import com.febers.iuestc.base.BaseEvent;
import com.febers.iuestc.base.BaseSwipeActivity;
import com.febers.iuestc.module.exam.presenter.ExamContract;
import com.febers.iuestc.entity.BeanExam;
import com.febers.iuestc.module.exam.presenter.ExamPresenterImpl;
import com.febers.iuestc.module.login.view.LoginActivity;
import com.febers.iuestc.util.CustomSPUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * mType 1为期末考试， 2为期中考试
 */
public class ExamActivity extends BaseSwipeActivity implements ExamContract.View{

    private static final String TAG = "ExamActivity";

    private ExamContract.Presenter examPresenter = new ExamPresenterImpl(this);
    private List<BeanExam> mExamList = new ArrayList<>();
    private SmartRefreshLayout smartRefreshLayout;
    private AdapterExam adapterExam;
    private RecyclerView rvExam;
    private ImageView ivEmpty;
    private Toolbar mToolbar;


    private RadioGroup rgExam;
    private int mType = 1;
    private String examName = "exam_1";

    @Override
    protected int setView() {
        return R.layout.activity_exam;
    }

    @Override
    protected int setMenu() {
        return R.menu.exam_menu;
    }

    @Override
    protected void findViewById() {
        mToolbar = findViewById(R.id.tb_exam);
        rvExam = findViewById(R.id.rv_exam);
        rgExam = findViewById(R.id.rg_exam);
        ivEmpty = findViewById(R.id.iv_exam_empty);
        smartRefreshLayout = findViewById(R.id.srl_exam);
    }

    @Override
    protected void initView() {
        mToolbar.setTitle("我的考试");
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        adapterExam = new AdapterExam(this, mExamList);
        rvExam.setLayoutManager(new LinearLayoutManager(this));
        rvExam.setNestedScrollingEnabled(false);
        rvExam.setAdapter(adapterExam);
        rgExam.setOnCheckedChangeListener( (RadioGroup group, int checkedId) -> {
                switch (checkedId) {
                    case R.id.rb_exam_final:
                        mType = 1;
                        dateRequest(false);
                        break;
                    case R.id.rb_exam_middle:
                        mType = 2;
                        dateRequest(false);
                        break;
                    default:
                        break;
                }
        });

        dateRequest(false);
        smartRefreshLayout.setEnableLoadMore(false);
        smartRefreshLayout.autoRefresh();
        smartRefreshLayout.setOnRefreshListener( (RefreshLayout refreshLayout) -> {
           dateRequest(true);
        });
    }

    @Override
    public void showExam(BaseEvent<List<BeanExam>> event) {
        runOnUiThread( () -> {
            if (event.getDate().size() == 0) {
                ivEmpty.setVisibility(View.VISIBLE);
            } else {
                ivEmpty.setVisibility(View.GONE);
            }
            if (event.getCode() == BaseCode.UPDATE) {
                smartRefreshLayout.finishRefresh(true);
            }
            adapterExam.setNewData(event.getDate());
        });
    }

    @Override
    public void dateRequest(Boolean isRefresh) {
        if (!CustomSPUtil.getInstance().get(getString(R.string.sp_is_login), false)) {
            if (isRefresh) {
                statusToFail();
                return;
            }
            return;
        }
        getExam(isRefresh, mType);
    }

    private void getExam(Boolean isRefresh, int type) {
        if (isRefresh) {
            if (!BaseApplication.checkNetConnecting()) {
                onError("当前网络不可用");
                return;
            }
        }
        examPresenter.examRequest(isRefresh, type);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_exam_refresh:
                getExam(true, mType);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void statusToFail() {
        smartRefreshLayout.finishRefresh(false);
        startActivityForResult(new Intent(ExamActivity.this, LoginActivity.class), BaseCode.STATUS);
    }

    @Override
    public void onError(String error) {
        smartRefreshLayout.finishRefresh(false);
        super.onError(error);
    }
}
