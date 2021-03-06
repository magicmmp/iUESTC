package com.febers.iuestc.module.news.presenter;

import com.febers.iuestc.base.BasePresenter;
import com.febers.iuestc.base.BaseView;
import com.febers.iuestc.entity.BeanNews;

import java.util.List;

public interface NewsContract {

    interface Model {
        void newsService(Boolean isRefresh, int type, int position) throws Exception;
    }

    interface View extends BaseView {
        void showNews(List<BeanNews> newsList);
    }

    abstract class Presenter extends BasePresenter<NewsContract.View> {

        public Presenter(View view) {
            super(view);
        }

        public abstract void newsRequest(Boolean isRefresh);

        public abstract void newsResult(List<BeanNews> newsList);
    }

}
