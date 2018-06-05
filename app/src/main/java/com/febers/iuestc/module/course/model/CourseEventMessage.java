package com.febers.iuestc.module.course.model;

import com.febers.iuestc.module.course.model.BeanCourse;

import java.util.List;

/**
 * 课表的Event类
 * 使用链表传递课表信息
 * Created by 23033 on 2018/3/25.
 */

public class CourseEventMessage {

    private List<BeanCourse> beanCourseList;
    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public CourseEventMessage(String status, List<BeanCourse> list) {
        this.status = status;
        beanCourseList = list;
    }

    public List<BeanCourse> getBeanCourseList() {
        return beanCourseList;
    }

    public void setBeanCourseList(List<BeanCourse> beanCourseList) {
        this.beanCourseList = beanCourseList;
    }
}
