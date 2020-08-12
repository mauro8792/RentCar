package net.avalith.carDriver.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.avalith.carDriver.models.Sale;
import net.avalith.carDriver.repositories.SaleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static net.avalith.carDriver.utils.Constants.SALE_KEY;

@Service
public class SaleService {

    @Autowired
    private SaleRepository saleRepository;

    @Autowired
    private RedisTemplate<String, Sale> redisTemplate;

    public List<Sale> getAll(){
        ObjectMapper objectMapper = new ObjectMapper();
        List<Sale> list = new ArrayList<>();
        String json = "";

        if(redisTemplate.opsForHash().keys(SALE_KEY).isEmpty()){
            saleRepository.findAll()
                    .forEach((Sale sale) -> redisTemplate.opsForHash().put(SALE_KEY, sale.getId(), sale));
            redisTemplate.boundHashOps(SALE_KEY).expire(24L, TimeUnit.HOURS);
        }

        try {
            json = objectMapper.writeValueAsString(redisTemplate.opsForHash().values(SALE_KEY));
            list = objectMapper.readValue(json, new TypeReference<List<Sale>>(){});
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return list;
    }
}
