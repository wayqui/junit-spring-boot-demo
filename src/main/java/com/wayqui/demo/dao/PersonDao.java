package com.wayqui.demo.dao;

import com.wayqui.demo.dto.PersonDto;
import java.util.List;

public interface PersonDao {

    List<PersonDto> findAll();
}
