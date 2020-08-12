package net.avalith.carDriver.services;

import net.avalith.carDriver.exceptions.NotFoundException;
import net.avalith.carDriver.models.Mishap;
import net.avalith.carDriver.models.Ride;
import net.avalith.carDriver.repositories.MishapRepository;
import net.avalith.carDriver.repositories.RideRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static net.avalith.carDriver.utils.Constants.NOT_FOUND_RIDE;

@Service
public class MishapService {

    @Autowired
    private MishapRepository mishapRepository;

    @Autowired
    private RideRepository rideRepository;

    public List<Mishap> getAll(){
        return mishapRepository.findAll();
    }

    public Mishap save(Mishap mishap, Long idRide){
        Ride rideSearch= rideRepository.findById(idRide)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_RIDE));

        mishap.setRide(rideSearch);

        return mishapRepository.save(mishap);
    }
}
