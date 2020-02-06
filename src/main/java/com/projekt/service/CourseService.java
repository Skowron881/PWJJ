package com.projekt.service;
import com.projekt.model.Course;

import java.util.Date;
import java.util.List;

public interface CourseService {
    void save (Course course);

    void delete(int course_id);

    List<Course> findByFacultyId(int id);

    Course findById(int id);

    Date getDueDateForCourse(int id);

    void setDueDate(Date dueDate, int courseId);

    boolean isCourseTeacher(int id);
}
