package com.projekt.service;

import com.projekt.dao.FacultyDao;
import com.projekt.model.Faculty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("facultyService")
public class FacultyServiceImpl implements FacultyService {
    @Autowired
    private FacultyDao facultyDao;

    @Override
    public List<Faculty> findAll() {
        return facultyDao.findAll();
    }

    @Override
    public Faculty getFacultyById(int id) {
        return facultyDao.getFacultyById(id);
    }
}
