package com.projekt.service;

import com.projekt.dao.StudentCourseDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("studentRoleService")
public class StudentCourseServiseImpl implements StudentCourseServise {
    @Autowired
    StudentCourseDao studentCourseDao;
    @Override
    public void addStudentToCourse(int student_id, int course_id) {
        studentCourseDao.addStudentToCourse(student_id, course_id);
    }

    @Override
    public boolean isInCourse(int user_id) {
        return studentCourseDao.isInCourse(user_id);
    }

    @Override
    public void deleteStudentFromCourse(int studentId, int courseId) {
        studentCourseDao.deleteStudentFromCourse(studentId, courseId);
    }

    @Override
    public void deleteAllStudentsFromCourse(int courseId) {
        studentCourseDao.deleteAllStudentsFromCourse(courseId);
    }
}
