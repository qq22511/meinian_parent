package com.atguigu.service;

import java.util.List;
import java.util.Map;

public interface ReportService {
    List< Integer> findMemberCountBeforeDate(List< String> months);

    Map< String, Object> getBusinessReportData();

}
