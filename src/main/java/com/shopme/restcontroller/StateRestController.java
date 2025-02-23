package com.shopme.restcontroller;

import com.shopme.dao.StateRepository;
import com.shopme.dto.StateDto;
import com.shopme.entity.Country;
import com.shopme.entity.State;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class StateRestController {
    @Autowired
    private StateRepository repo;

    @GetMapping("/settings/list_states_by_country/{id}")
    public List<StateDto> listByCountry(@PathVariable("id") Integer countryId) {
        List<State> listStates = repo.findByCountryOrderByNameAsc(new Country(countryId));
        List<StateDto> result = new ArrayList<>();

        for (State state : listStates) {
            result.add(new StateDto(state.getId(), state.getName()));
        }

        return result;
    }
}