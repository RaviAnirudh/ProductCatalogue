package com.api.validator;

import com.api.Entity.Merchant;
import com.api.Entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileValidator {
    @Autowired
    private static RestTemplate restTemplate;

    public static boolean checkMerchantId(Long id){
//        Merchant merchant = restTemplate.getForObject("http://localhost:8080/merchant/"+id,Merchant.class);
//        if (merchant.getMerchantId() == null)
//            return false;
        return true;
    }

    public static boolean stringOnlyWithAlphabetAndSpaces(String data){
        Pattern p = Pattern.compile("^[A-Za-z ]+$");
        Matcher m = p.matcher(data);
        return m.matches();
    }
    public static boolean alphaNumericCheck(String data){
        Pattern p = Pattern.compile("[a-zA-Z0-9]+");
        Matcher m = p.matcher(data);
        return m.matches();
    }
    public static boolean OnlyIntegers(String data){
        Pattern p = Pattern.compile("^[0-9]*[1-9][0-9]*$");
        Matcher m = p.matcher(data);
        return m.matches();
    }
    public static Integer LengthOfEntity(String s){
        return s.length();
    }
    public static String validity(Product product){
        String message = "";
        if(!alphaNumericCheck(product.getProductName()) && !stringOnlyWithAlphabetAndSpaces(product.getProductName()) || LengthOfEntity(product.getProductName())>=100)
            message = message + "Invalid Name!" + " Name Should consist of only 100 characters!!";
        if(!stringOnlyWithAlphabetAndSpaces(product.getProductCategory()) || LengthOfEntity(product.getProductCategory())>=50)
            message = message + "Invalid Category!" + " Category Should consist of only 50 characters!!";
        if(!alphaNumericCheck(product.getProductDescription()) && !stringOnlyWithAlphabetAndSpaces(product.getProductDescription()) || LengthOfEntity(product.getProductDescription())>=200)
            message = message + "Invalid Description!" + " Description Should consist of only 200 characters!!";
        if(!OnlyIntegers(product.getProductQuantity().toString()))
            message = message + "Quantity cannot be 0 or negatives!";

        return message;
    }
}

