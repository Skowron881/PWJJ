package com.projekt.service;


import com.projekt.dao.CourseDao;
import com.projekt.model.Course;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service("courseService")
public class CourseServiceImpl implements CourseService {
    @Autowired
    private CourseDao courseDao;

    @Override
    public void save(Course course) {
        courseDao.save(course);
    }

    @Override
    public void delete(int course_id) {
        courseDao.delete(course_id);
    }

    @Override
    public List<Course> findByFacultyId(int id) {
        return courseDao.findByFacultyId(id);
    }

    @Override
    public Course findById(int id) {
        return courseDao.findById(id);
    }

    @Override
    public Date getDueDateForCourse(int id) {
        return courseDao.getDueDateForCourse(id);
    }

    @Override
    public void setDueDate(Date dueDate, int courseId) {
        courseDao.setDueDate(dueDate, courseId);
    }

    @Override
    public boolean isCourseTeacher(int id) {
        return courseDao.isCourseTeacher(id);
    }
}
