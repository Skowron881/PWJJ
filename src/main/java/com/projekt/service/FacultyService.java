package com.projekt.service;

import com.projekt.model.Faculty;

import java.util.List;

public interface FacultyService {
    List<Faculty> findAll();

    Faculty getFacultyById(int id);
}
