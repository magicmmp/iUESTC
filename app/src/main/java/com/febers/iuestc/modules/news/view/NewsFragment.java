/*
 * Created by Febers 2018.
 * Copyright (c). All rights reserved.
 *
 * Last Modified 18-6-7 下午12:57
 *
 */

package com.febers.iuestc.modules.news.view;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.febers.iuestc.R;
import com.febers.iuestc.adapter.AdapterNews;
import com.febers.iuestc.base.BaseFragment;
import com.febers.iuestc.modules.news.contract.NewsContract;
import com.febers.iuestc.modules.news.contract.NewsPresenterImp;
import com.febers.iuestc.modules.news.model.BeanNews;

import java.util.ArrayList;
import java.util.List;

public class NewsFragment extends BaseFragment implements NewsContract.View{

    private NewsContract.Presenter newsPresenter;
    private List<BeanNews> list = new ArrayList<>();
    private RecyclerView rvNews;
    private AdapterNews adapterNews;
    private Boolean gotNews = false;
    private int type = 0;
    private int position = 0;

    /**
     * fragment不应该使用带参数的构造器，所以构建一个静态方法，以创建带参数的fragment
     * @param args 参数
     * @return 返回实例
     */
    public static NewsFragment getInstance(Bundle args) {
        NewsFragment fragment = new NewsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int setContentView() {
        Bundle bundle = getArguments();
        type = bundle.getInt("type");
        position = bundle.getInt("position");
        newsPresenter = new NewsPresenterImp(this, type, position);
        return R.layout.fragment_news_fragment;
    }

    @Override
    public void showNews(List<BeanNews> newsList) {
        dismissProgressDialog();
        if (newsList.size() != 0) {
            gotNews = true;
        }
        list = newsList;
        getActivity().runOnUiThread( ()-> {
            if (adapterNews != null) {
                adapterNews.notifyDataSetChanged();
                return;
            }
            adapterNews = new AdapterNews(list);
            rvNews.setAdapter(adapterNews);
        });
    }

    @Override
    protected void lazyLoad() {
        if (gotNews) {
            return;
        }
        showProgressDialog();
        newsPresenter.newsRequest(true);
    }

    @Override
    protected void initView() {
        rvNews = findViewById(R.id.rv_news);
        rvNews.setLayoutManager(new LinearLayoutManager(getContext()));
        rvNews.setNestedScrollingEnabled(false);
    }
}