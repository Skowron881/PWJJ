package com.projekt.dao;


import com.projekt.model.Faculty;

import java.util.List;

public interface FacultyDao {
    List<Faculty> findAll();

    Faculty getFacultyById(int id);
}
