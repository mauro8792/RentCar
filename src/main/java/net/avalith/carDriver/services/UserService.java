package net.avalith.carDriver.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.avalith.carDriver.exceptions.AlreadyExistsException;
import net.avalith.carDriver.exceptions.NotFoundException;
import net.avalith.carDriver.models.User;
import net.avalith.carDriver.models.dtos.requests.UserDtoRequest;
import net.avalith.carDriver.models.dtos.requests.UserDtoUpdateRequest;
import net.avalith.carDriver.repositories.LicenseRepository;
import net.avalith.carDriver.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static net.avalith.carDriver.utils.Constants.NOT_FOUND_USER;
import static net.avalith.carDriver.utils.Constants.USER_ALREADY_EXISTS;
import static net.avalith.carDriver.utils.Constants.USER_KEY;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private LicenseRepository licenseRepository;

    @Autowired
    private RedisTemplate<String, User> redisTemplate;

    public User save(UserDtoRequest user){

        if(userRepository.getByDni(user.getDni()).isPresent())
            throw new AlreadyExistsException(USER_ALREADY_EXISTS);

        User userAux = userRepository.getNotActiveByDni(user.getDni())
                .orElse(null);

        if(userAux != null){
            if(userAux.getDni().equals(user.getDni()) && passwordEncoder.matches(user.getPwd(), userAux.getPwd())){
                User newUser = new User(user);
                newUser.setId(userAux.getId());
                if(userAux.getLicense()!=null)
                    newUser.setLicense(userAux.getLicense());
                newUser.setPwd(passwordEncoder.encode(newUser.getPwd()));

                return userRepository.save(newUser);
            }
        }
        user.setPwd(passwordEncoder.encode(user.getPwd()));
        User newUser = userRepository.save(new User(user));

        redisTemplate.opsForHash().put(USER_KEY, newUser.getId(), newUser);

        return newUser;
    }

    public List<User> getAll(){
        ObjectMapper objectMapper = new ObjectMapper();
        List<User> list = new ArrayList<>();
        String json = "";

        if(redisTemplate.opsForHash().keys(USER_KEY).isEmpty()){
            userRepository.getAll()
                    .forEach((User user) -> redisTemplate.opsForHash().put(USER_KEY, user.getId(), user));
            redisTemplate.boundHashOps(USER_KEY).expire(24L, TimeUnit.HOURS);
        }

        try {
            json = objectMapper.writeValueAsString(redisTemplate.opsForHash().values(USER_KEY));
            list = objectMapper.readValue(json, new TypeReference<List<User>>(){});
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return list;
    }

    public void delete(String dni){
        User user = userRepository.getByDni(dni)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_USER));

        if(userRepository.delete(dni) < 1)
            throw new NotFoundException(NOT_FOUND_USER);

        redisTemplate.opsForHash().delete(USER_KEY, user.getId());
    }

    public User update(String dni, UserDtoUpdateRequest user){
        User oldUser = userRepository.getByDni(dni)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_USER));

        User userUpdate = new User(user);
        userUpdate.setId(oldUser.getId());
        userUpdate.setDni(dni);
        userUpdate.setLicense(oldUser.getLicense());
        userUpdate.setPwd(passwordEncoder.encode(userUpdate.getPwd()));

        redisTemplate.opsForHash().put(USER_KEY, userUpdate.getId(), userUpdate);

        return userRepository.save(userUpdate);
    }
}
