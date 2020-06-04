package com.wayqui.demo.service;

import com.wayqui.demo.dao.PersonDao;
import com.wayqui.demo.dto.PersonDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PersonServiceImpl implements PersonService {

    @Autowired
    PersonDao personDao;

    @Override
    public List<PersonDto> getAllPersons() {
        return personDao.findAll();
    }
}
