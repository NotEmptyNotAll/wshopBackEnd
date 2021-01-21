package com.example.demo.Utility.Impl;

import com.example.demo.Utility.StringDataUtility;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class StringDataUtilityImpl implements StringDataUtility {
    @Override
    public String convertStatusToInt(String str) {
        if (str != null) {
            String result = "";
            switch (Integer.parseInt(str)) {
                case 0:
                    result = "не выполнена";
                    break;
                case 1:
                    result = "выполнена";
                    break;
                case 2:
                    result = "выполняется";
                    break;
                case 3:
                    result = "пауза";
                    break;
            }
            return result;
        } else {
            return "";
        }
    }


    @Override
    public Date dateOf(String dateToConvert) {
        System.out.println(dateToConvert);
        return java.sql.Date.valueOf(dateToConvert);
    }
}
