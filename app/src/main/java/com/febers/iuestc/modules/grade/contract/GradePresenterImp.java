/*
 * Created by Febers 2018.
 * Copyright (c). All rights reserved.
 *
 * Last Modified 18-6-7 上午11:30
 *
 */

package com.febers.iuestc.modules.grade.contract;

import com.febers.iuestc.modules.grade.model.BeanAllGrade;
import com.febers.iuestc.modules.grade.model.BeanGrade;
import com.febers.iuestc.modules.grade.model.GradeModel;
import com.febers.iuestc.modules.grade.model.IGradeModel;

import java.util.List;

public class GradePresenterImp extends GradeContract.Presenter {

    public GradePresenterImp(GradeContract.View view) {
        super(view);
    }

    private static final String TAG = "GradePresenterImp";

    private IGradeModel gradeModel = new GradeModel(this);

    @Override
    public void gradeRequest(Boolean isRefresh) {
        try {
            gradeModel.gradeService(isRefresh, "");
        } catch (Exception e) {
            e.printStackTrace();
            mView.onError("获取成绩出现错误");
        }
    }

    @Override
    public void gradeResult(String status, List<BeanAllGrade> allGrades, List<BeanGrade> gradeList) {
        if (mView == null) {
            return;
        }
        mView.showGrade(allGrades, gradeList);
    }
}