package com.shopme.service;

import com.shopme.constant.SettingCategory;
import com.shopme.dao.SettingRepository;
import com.shopme.entity.Setting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SettingService {
    @Autowired
    private SettingRepository repo;

    public List<Setting> getGeneralSettings(){
        return repo.findByTwoCategories(SettingCategory.GENERAL, SettingCategory.CURRENCY);
    }
}
