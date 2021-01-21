package com.example.demo.Utility;

import org.springframework.stereotype.Component;

import java.util.Date;

public interface StringDataUtility {

    String convertStatusToInt(String str);

    Date dateOf(String dateToConvert);
}
