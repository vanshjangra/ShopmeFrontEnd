package com.shopme.dao;

import com.shopme.entity.Country;
import com.shopme.entity.ShippingRate;
import com.shopme.entity.State;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShippingRateRepository extends CrudRepository<ShippingRate, Integer> {
    public ShippingRate findByCountryAndState(Country country, String state);
}
