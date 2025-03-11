package com.shopme.service;

import com.shopme.constant.SettingCategory;
import com.shopme.dao.CurrencyRepository;
import com.shopme.dao.SettingRepository;
import com.shopme.entity.Currency;
import com.shopme.entity.Setting;
import com.shopme.util.CurrencySettingBag;
import com.shopme.util.EmailSettingBag;
import com.shopme.util.PaymentSettingBag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SettingService {
    @Autowired
    private SettingRepository settingRepo;

    @Autowired
    private CurrencyRepository currencyRepo;

    public List<Setting> getGeneralSettings(){
        return settingRepo.findByTwoCategories(SettingCategory.GENERAL, SettingCategory.CURRENCY);
    }

    public EmailSettingBag getEmailSettings(){
        List<Setting> settings = settingRepo.findByCategory(SettingCategory.MAIL_SERVER);
        settings.addAll(settingRepo.findByCategory(SettingCategory.MAIL_TEMPLATES));

        return new EmailSettingBag(settings);
    }

    public CurrencySettingBag getCurrencySettings(){
        List<Setting> settings = settingRepo.findByCategory(SettingCategory.CURRENCY);
        return new CurrencySettingBag(settings);
    }

    public PaymentSettingBag getPaymentSettings(){
        List<Setting> settings = settingRepo.findByCategory(SettingCategory.PAYMENT);
        return new PaymentSettingBag(settings);
    }

    public String getCurrencyCode(){
        Setting setting = settingRepo.findByKey("CURRENCY_ID");
        Integer currencyId = Integer.parseInt(setting.getValue());
        Currency currency = currencyRepo.findById(currencyId).get();

        return currency.getCode();
    }
}
