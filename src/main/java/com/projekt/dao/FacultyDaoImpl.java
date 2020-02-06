package com.projekt.dao;

import com.projekt.model.Faculty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class FacultyDaoImpl implements FacultyDao {
    @Autowired
    DataSource datasource;
    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public List<Faculty> findAll() {
        String sql = "select * from faculty";
        List<Faculty> faculties = jdbcTemplate.query(sql, new FacultyMapper());
        return faculties;
    }

    @Override
    public Faculty getFacultyById(int id) {
        String sql = "select * from faculty where id='" + id + "';";
        List<Faculty> faculties = jdbcTemplate.query(sql, new FacultyMapper());
        return faculties.size() > 0 ? faculties.get(0) : null;
    }
}

class FacultyMapper implements RowMapper<Faculty> {

    public Faculty mapRow(ResultSet rs, int arg1) throws SQLException {
        Faculty faculty = new Faculty();

        faculty.setId(rs.getInt("id"));
        faculty.setName(rs.getString("name"));

        return faculty;
    }
}
