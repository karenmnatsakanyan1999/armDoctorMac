package com.armdoctor.utill;

import org.apache.commons.lang3.RandomStringUtils;

public class TokenGenerate {
    public static String generateVerifyCode(){
        return RandomStringUtils.random(6,true,true);

    }
    public static String generateResetToken(){
        return RandomStringUtils.random(10,true,false);
    }
}
