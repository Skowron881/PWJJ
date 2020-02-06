package com.projekt.service;

import com.projekt.dao.UserDocumentDao;
import com.projekt.model.UserDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("userDocumentService")
public class UserDocumentServiceImpl implements UserDocumentService {
    @Autowired
    UserDocumentDao documentDao;

    @Override
    public void save(UserDocument userDocument, int user_id, int courseId) {
        documentDao.save(userDocument, user_id, courseId);
    }

    @Override
    public List<UserDocument> findAllForTeacher(int id) {
        return documentDao.findAllForTeacher(id);
    }

    @Override
    public List<UserDocument> findAllForStudent(int course_id, int user_id, int teacher_id) {
        return documentDao.findAllForStudent(course_id, user_id, teacher_id);
    }

    @Override
    public UserDocument findById(int id) {
        return documentDao.findById(id);
    }

    @Override
    public void delete(int id) {
        documentDao.delete(id);
    }

    @Override
    public void deleteAllFromCourse(int course_id) {
        documentDao.deleteAllFromCourse(course_id);
    }
}
