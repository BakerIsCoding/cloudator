package com.project.cloudator.entity;

import java.util.Date;

public class FileCountByDate {
    private Date date;
    private Long count;

    public FileCountByDate(Date date, Long count) {
        this.date = date;
        this.count = count;
    }

    public Date getDate() {
        return date;
    }

    public Long getCount() {
        return count;
    }
}
