package com.app.projectlogistics;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {
        "com.app.projectlogistics.Configuration",
        "com.app.projectlogistics.Controller",
        "com.app.projectlogistics.DataTransferObject",
        "com.app.projectlogistics.Enum",
        "com.app.projectlogistics.ExceptionHandler",
        "com.app.projectlogistics.Repository",
        "com.app.projectlogistics.SecurityFilters",
        "com.app.projectlogistics.Service",
        "com.app.projectlogistics.UserDetailServices",
        "com.app.projectlogistics.UtilityFiles",
        "com.app.projectlogistics.ValueObject"})
public class ProjectLogisticsApplication {

    public static void main(String[] args) {

        SpringApplication.run(ProjectLogisticsApplication.class, args);
    }

}
