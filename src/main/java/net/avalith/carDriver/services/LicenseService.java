package net.avalith.carDriver.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import net.avalith.carDriver.exceptions.AlreadyExistsException;
import net.avalith.carDriver.exceptions.NotFoundException;
import net.avalith.carDriver.models.License;
import net.avalith.carDriver.models.User;
import net.avalith.carDriver.models.dtos.requests.LicenseDtoRequest;
import net.avalith.carDriver.models.dtos.requests.LicenseDtoRequestUpdate;
import net.avalith.carDriver.repositories.LicenseRepository;
import net.avalith.carDriver.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static net.avalith.carDriver.utils.Constants.LICENSE_ALREADY_EXISTS;
import static net.avalith.carDriver.utils.Constants.LICENSE_KEY;
import static net.avalith.carDriver.utils.Constants.NOT_FOUND_LICENSE;
import static net.avalith.carDriver.utils.Constants.NOT_FOUND_LICENSE_USER;
import static net.avalith.carDriver.utils.Constants.USER_KEY;

@Repository
@AllArgsConstructor
public class LicenseService {

    @Autowired
    private final LicenseRepository licenseRepository;

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final RedisTemplate<String, License> redisTemplate;

    @Autowired
    private RedisTemplate<String, User> redisTemplateUser;

    public License save(LicenseDtoRequest license){

        if(licenseRepository.findByNumber(license.getNumber()).isPresent())
                throw new AlreadyExistsException(LICENSE_ALREADY_EXISTS);

        User user = userRepository.getByDni(license.getNumber())
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_LICENSE_USER));

        License newLicense = licenseRepository.save(new License(license));
        user.setLicense(newLicense);
        user = userRepository.save(user);
        redisTemplateUser.opsForHash().put(USER_KEY, user.getId(), user);

        redisTemplate.opsForHash().put(LICENSE_KEY, newLicense.getId(), newLicense);

        return newLicense;
    }

    public List<License> getAll(){
        ObjectMapper objectMapper = new ObjectMapper();
        List<License> list = new ArrayList<>();
        String json = "";

        if(redisTemplate.opsForHash().keys(LICENSE_KEY).isEmpty()){
            licenseRepository.findAll()
                    .forEach((License license) -> redisTemplate.opsForHash().put(LICENSE_KEY, license.getId(), license));
            redisTemplate.boundHashOps(LICENSE_KEY).expire(24L, TimeUnit.HOURS);
        }

        try {
            json = objectMapper.writeValueAsString(redisTemplate.opsForHash().values(LICENSE_KEY));
            list = objectMapper.readValue(json, new TypeReference<List<License>>(){});
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return list;
    }

    public License updateExpirationDate(String number, LicenseDtoRequestUpdate license) {
        License oldLicense = licenseRepository.findByNumber(number)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_LICENSE));

        User user = userRepository.getByDni(oldLicense.getNumber())
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_LICENSE_USER));
        oldLicense.setExpirationDate(license.getExpirationDate());

        user.setLicense(oldLicense);
        user = userRepository.save(user);

        redisTemplate.opsForHash().put(LICENSE_KEY, oldLicense.getId(), oldLicense);
        redisTemplateUser.opsForHash().put(USER_KEY, user.getId(), user);

        return licenseRepository.save(oldLicense);
    }

    public License saveValidated(License license){
        User user = userRepository.getByDni(license.getNumber())
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_LICENSE_USER));

        License newLicense = licenseRepository.save(license);
        user.setLicense(newLicense);
        user = userRepository.save(user);
        redisTemplateUser.opsForHash().put(USER_KEY, user.getId(), user);

        redisTemplate.opsForHash().put(LICENSE_KEY, newLicense.getId(), newLicense);

        return newLicense;
    }
}
