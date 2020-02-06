package com.projekt.validator;

import com.projekt.model.Course;
import com.projekt.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class DueDateValidator implements Validator{
    @Autowired
    CourseService courseService;

    private static final DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public boolean supports(Class<?> aClass) {
        return Course.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        Course course = (Course) o;
        Date today = new Date();
        Date dueDate = course.getDueDate();

        if (today.compareTo(dueDate) > 0) {
            errors.rejectValue("description", "PerformAction.false");
        }
    }
}
