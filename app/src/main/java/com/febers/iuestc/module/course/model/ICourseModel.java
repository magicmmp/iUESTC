package com.febers.iuestc.module.course.model;

/**
 * Created by 23033 on 2018/3/27.
 */

public interface ICourseModel {
    void getCourseListRequest(Boolean isRefresh) throws Exception;
    void loadLocalFile();
}
