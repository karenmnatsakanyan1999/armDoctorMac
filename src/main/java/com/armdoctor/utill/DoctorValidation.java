package com.armdoctor.utill;

import com.armdoctor.dto.requestdto.DoctorDto;
import com.armdoctor.exception.DoctorValidationException;

public class DoctorValidation {

    public static void validateFields(DoctorDto doctorDto){
        if (doctorDto.getName() == null || doctorDto.getName().isBlank()){
            throw new DoctorValidationException("User name can not be null or empty");
        }
        if (doctorDto.getSurname() == null || doctorDto.getSurname().isBlank()){
            throw new DoctorValidationException("User surname can not be null or empty");
        }
        if (doctorDto == null || doctorDto.getYear() < 1910 || doctorDto.getYear() > 2020){
            throw new DoctorValidationException("User age must between 1910 - 2020");
        }
    }
    public static void validatePassword(String password){

        if (password == null || password.isBlank()){
            throw new DoctorValidationException("password can not be null");
        }
        if (password.length() < 6){
            throw  new DoctorValidationException("Password is short");
        }
        int countOfUppercase = 0;
        int  countOfDigit = 0;
        for (int i = 0; i < password.length(); i++) {
            char c = password.charAt(i);
            if (Character.isDigit(c)) {
                countOfDigit++;
            } else if (Character.isUpperCase(c)) {
                countOfUppercase++;
            }
        }
        if (countOfUppercase< 1){
            throw new DoctorValidationException("Password must contain at least 1 uppercase");
        }
        if (countOfDigit<2){
            throw new DoctorValidationException("Password must contain at least 2 digit");
        }

    }

}
