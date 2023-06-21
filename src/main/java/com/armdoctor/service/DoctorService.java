package com.armdoctor.service;

import com.armdoctor.dto.requestdto.DoctorDto;
import com.armdoctor.exception.ApiException;
import com.armdoctor.model.DoctorEntity;

import java.util.List;

public interface DoctorService {
    DoctorEntity creatUser(DoctorDto dto) throws ApiException;
    List<DoctorEntity> getByUsername(String email)throws ApiException;
    DoctorEntity verifyUser(String email, String verifyCode) throws ApiException;
    DoctorEntity changePassword(String oldPassword, String newPassword, String confrimPassword, String email) throws ApiException;

    DoctorEntity sendToken(String email) throws ApiException;
    Boolean verifyToken(String email,String Token) throws ApiException;
    DoctorEntity forgotPassword(String email, String password, String confirmPassword) throws ApiException;
    DoctorEntity update(DoctorDto doctorDto) throws ApiException;
    void delete(Integer id) throws ApiException;


}
