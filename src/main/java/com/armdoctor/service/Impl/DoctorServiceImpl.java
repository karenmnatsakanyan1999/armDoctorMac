package com.armdoctor.service.Impl;

import com.armdoctor.dto.requestdto.DoctorDto;
import com.armdoctor.enums.Status;
import com.armdoctor.exception.ApiException;
import com.armdoctor.exception.ResourceAlreadyExistException;
import com.armdoctor.exception.DoctorNotFoundException;
import com.armdoctor.exception.DoctorValidationException;
import com.armdoctor.model.DoctorEntity;
import com.armdoctor.model.HospitalEntity;
import com.armdoctor.repository.DoctorRepository;
import com.armdoctor.repository.HospitalRepository;
import com.armdoctor.service.DoctorService;
import com.armdoctor.utill.ArmDoctorMailSender;
import com.armdoctor.utill.TokenGenerate;
import com.armdoctor.utill.DoctorValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class DoctorServiceImpl implements DoctorService {
    @Autowired
    private DoctorRepository doctorRepository;
    @Autowired
    private HospitalRepository hospitalRepository;
    @Autowired
    private ArmDoctorMailSender mailSender;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Override
    public DoctorEntity creatUser(DoctorDto dto) throws ApiException {
        DoctorValidation.validateFields(dto);
        DoctorValidation.validatePassword(dto.getPassword());
        validateDuplicate(dto);
        String verifyCode = TokenGenerate.generateVerifyCode();


        DoctorEntity doctorEntity = new DoctorEntity();
        doctorEntity.setId(0);
        doctorEntity.setName(dto.getName());
        doctorEntity.setSurname(dto.getSurname());
        doctorEntity.setYear(dto.getYear());
        doctorEntity.setEmail(dto.getEmail());
        doctorEntity.setPassword(passwordEncoder.encode(dto.getPassword()));
        doctorEntity.setVerifyCode(verifyCode);
        doctorEntity.setStatus(Status.INACTIVE);
        doctorEntity.setRole(dto.getRole());
        doctorEntity.setProfession(dto.getProfession());
        doctorEntity.setWorkTime(dto.getWorkTime());
        List<HospitalEntity> hospitalEntities = new ArrayList<>();
//        for (int i = 0; i < dto.getHospitals().size(); i++) {
//            String s = dto.getHospitals().get(i);
//            hospitalEntities.add(hospitalRepository.getByName(s));
//        }
//        doctorEntity.setHospitalEntities(hospitalEntities);
        try{
            doctorRepository.save(doctorEntity);
        }catch (Exception e){
            throw  new ApiException("problem during saving of user");
        }
        mailSender.sendEmail(dto.getEmail(),"your verify code","your verify code "+ verifyCode);

        return doctorEntity;
    }

    @Override
    public List<DoctorEntity> getByUsername(String email) throws ApiException{
        List<DoctorEntity> entityList = null;
        try {
            entityList = doctorRepository.getByEmail(email);
        }catch (Exception e){
            throw new ApiException("problem during getting of user");
        }
        return entityList;

    }

    @Override
    public DoctorEntity verifyUser(String email, String verifyCode) throws ApiException {
        DoctorEntity doctorEntity = null;
        try {
             doctorEntity = doctorRepository.getByEmailAndVerifyCode(email, verifyCode);
            if (doctorEntity ==null){
                throw new DoctorValidationException("wrong verify code" + verifyCode );
            }else {
                doctorEntity.setStatus(Status.ACTIVE);
                doctorEntity.setVerifyCode(null);
                doctorRepository.save(doctorEntity);
            }
        }catch (Exception e){
            throw new ApiException("problem during verifying user");
        }
        return doctorEntity;
    }

    @Override
    public DoctorEntity changePassword(String oldPassword, String newPassword, String confrimPassword, String email) throws ApiException {
        DoctorEntity doctorEntity = null;
        DoctorValidation.validatePassword(newPassword);
        if (!newPassword.equals(confrimPassword)){
            throw new DoctorValidationException("Passwords don't match");
        }
        try {
            doctorEntity = doctorRepository.findByEmail(email);
        }catch (Exception e){
            throw new ApiException("problem during changing password");
        }

        if (!doctorEntity.getPassword().equals(passwordEncoder.encode(oldPassword))){
            throw new DoctorValidationException("Wrong old password");
        }
        doctorEntity.setPassword(passwordEncoder.encode(newPassword));
       try {
           doctorRepository.save(doctorEntity);
       }catch (Exception e){
           throw new ApiException("Problem during changing password");
       }
       return doctorEntity;
    }

    @Override
    public DoctorEntity sendToken(String email) throws ApiException {
        DoctorEntity doctorEntity = null;
        try {
            doctorEntity = doctorRepository.findByEmail(email);
        }catch (Exception e){
            throw new ApiException("Problem during sending email");
        }
        if (doctorEntity == null){
            throw new DoctorNotFoundException("wrong email " + email);
        }
        String resetToken = TokenGenerate.generateResetToken();
        doctorEntity.setResetToken(resetToken);
        doctorRepository.save(doctorEntity);
        mailSender.sendEmail(doctorEntity.getEmail(),"Reset Token","Your Reset Token " + resetToken);
        return doctorEntity;
    }

    @Override
    public Boolean verifyToken(String email, String token) throws ApiException {
        DoctorEntity doctorEntity =null;
        try {
            doctorEntity = doctorRepository.findByEmail(email);
        }catch (Exception e){
            throw new ApiException("porblem during verifyin token");
        }
        if (!doctorEntity.getResetToken().equals(token)){
            throw new DoctorValidationException("wrong reset Token" + token);
        }
        return true;
    }

    @Override
    public DoctorEntity forgotPassword(String email, String password, String confirmPassword) throws ApiException {
        DoctorEntity doctorEntity =null;
        DoctorValidation.validatePassword(password);
        if (!password.equals(confirmPassword)){
            throw new DoctorValidationException("passwords dont match");
        }
        try {
            doctorEntity = doctorRepository.findByEmail(email);
        }catch (Exception e){
            throw new ApiException("problem during changing password");
        }
        if (doctorEntity.getResetToken()==null){
            throw new ApiException("problem during changing password");
        }
        doctorEntity.setResetToken(null);
        doctorEntity.setPassword(passwordEncoder.encode(password));
        doctorRepository.save(doctorEntity);
        return doctorEntity;
    }

    @Override
    public DoctorEntity update(DoctorDto doctorDto) throws ApiException {
        validateDuplicate(doctorDto);
        DoctorValidation.validateFields(doctorDto);
        Optional<DoctorEntity> optionalUser = doctorRepository.findById(doctorDto.getId());
        if (optionalUser.isEmpty()){
            throw new DoctorNotFoundException("User not found with a given ID");
        }
        DoctorEntity doctorEntity = optionalUser.get();
        doctorEntity.setName( doctorDto.getName());
        doctorEntity.setSurname( doctorDto.getSurname());
        doctorEntity.setYear(doctorDto.getYear());
        doctorEntity.setEmail(doctorDto.getEmail()==null? doctorEntity.getEmail() : doctorDto.getEmail());

        try {
            doctorRepository.save(doctorEntity);
        }catch (Exception e){
            throw new ApiException("Problem during updating user");
        }
        return doctorEntity;
    }

    @Override
    public void delete(Integer id) throws ApiException {
        Optional<DoctorEntity> optionalUser = doctorRepository.findById(id);
        if (optionalUser.isEmpty()){
            throw new DoctorNotFoundException("User not found with a given ID");
        }
        try {
            doctorRepository.deleteById(id);
        }catch (Exception e){
            throw new ApiException("Problem during deleting user");
        }

    }


    private void validateDuplicate(DoctorDto doctorDto){
        if (doctorDto.getId()==null){
            List<DoctorEntity> doctorEntityList = doctorRepository.getByEmail(doctorDto.getEmail());
            if (!doctorEntityList.isEmpty()){
                throw new ResourceAlreadyExistException("User already exists");
            }
        }else {
            DoctorEntity doctorEntity = doctorRepository.getByEmailAndIdNot(doctorDto.getEmail(), doctorDto.getId());
            if (doctorEntity !=null){
                throw new ResourceAlreadyExistException("Email of User already exists");
            }
        }

    }


}
