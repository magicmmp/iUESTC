package com.febers.iuestc.module.news.view;

import android.content.Intent;
import android.os.Bundle;

import com.febers.iuestc.R;
import com.febers.iuestc.view.adapter.AdapterNews;
import com.febers.iuestc.base.BaseFragment;
import com.febers.iuestc.module.news.presenter.NewsContract;
import com.febers.iuestc.module.news.presenter.NewsPresenterImpl;
import com.febers.iuestc.entity.BeanNews;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class NewsFragment extends BaseFragment implements NewsContract.View{

    private NewsContract.Presenter newsPresenter;
    private List<BeanNews> newsList = new ArrayList<>();
    private RecyclerView rvNews;
    private AdapterNews adapterNews;
    private Boolean gotNews = false;
    private int type = 0;
    private int position = 0;

    /**
     * fragment不应该使用带参数的构造器，所以构建一个静态方法，以创建带参数的fragment
     *
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
        newsPresenter = new NewsPresenterImpl(this, type, position);
        return R.layout.fragment_news_fragment;
    }


    @Override
    protected void initView() {
        rvNews = findViewById(R.id.rv_news);
        rvNews.setLayoutManager(new LinearLayoutManager(getContext()));
        rvNews.setNestedScrollingEnabled(false);
        rvNews.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        adapterNews = new AdapterNews(getContext(), newsList);
        rvNews.setAdapter(adapterNews);
        adapterNews.setOnItemClickListener((viewHolder, beanNews, i) -> {
            Intent intent = new Intent(getContext(), NewsDetailActivity.class);
            intent.putExtra("text", beanNews.getText());
            intent.putExtra("title", beanNews.getTitle());
            intent.putExtra("url", beanNews.getNewsId());
            getActivity().startActivity(intent);
        });
    }

    @Override
    public void showNews(List<BeanNews> newsList) {
        dismissProgressDialog();
        if (newsList.size() != 0) {
            gotNews = true;
        }
        this.newsList = newsList;
        if (getActivity() == null) {
            return;
        }
        getActivity().runOnUiThread( ()-> {
            if (adapterNews != null) {
                adapterNews.setNewData(this.newsList);
            }
        });
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        if (gotNews) {
            return;
        }
        showProgressDialog();
        newsPresenter.newsRequest(true);
    }
}
