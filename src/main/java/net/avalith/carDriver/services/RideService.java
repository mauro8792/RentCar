package net.avalith.carDriver.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.avalith.carDriver.exceptions.InvalidLicenseException;
import net.avalith.carDriver.exceptions.InvalidRequestException;
import net.avalith.carDriver.exceptions.NotFoundException;
import net.avalith.carDriver.models.License;
import net.avalith.carDriver.models.Mishap;
import net.avalith.carDriver.models.Point;
import net.avalith.carDriver.models.Ride;
import net.avalith.carDriver.models.RideLog;
import net.avalith.carDriver.models.Sale;
import net.avalith.carDriver.models.User;
import net.avalith.carDriver.models.Vehicle;
import net.avalith.carDriver.models.VehicleCategory;
import net.avalith.carDriver.models.dtos.RidePointDto;
import net.avalith.carDriver.models.dtos.requests.DestinationPointDtoRequest;
import net.avalith.carDriver.models.dtos.requests.MishapDtoRequest;
import net.avalith.carDriver.models.dtos.requests.RideDtoRequest;
import net.avalith.carDriver.models.dtos.requests.RideDtoUpdateRequest;
import net.avalith.carDriver.models.enums.RideState;
import net.avalith.carDriver.models.enums.TariffType;
import net.avalith.carDriver.repositories.PointRepository;
import net.avalith.carDriver.repositories.RideLogRepository;
import net.avalith.carDriver.repositories.RideRepository;
import net.avalith.carDriver.repositories.SalesRepository;
import net.avalith.carDriver.repositories.UserRepository;
import net.avalith.carDriver.repositories.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static net.avalith.carDriver.utils.Constants.INVALID_LICENSE;
import static net.avalith.carDriver.utils.Constants.NOT_FOUND_POINT;
import static net.avalith.carDriver.utils.Constants.NOT_FOUND_RIDE;
import static net.avalith.carDriver.utils.Constants.NOT_FOUND_USER;
import static net.avalith.carDriver.utils.Constants.NOT_FOUND_VEHICLE;
import static net.avalith.carDriver.utils.Constants.POINT_KEY;
import static net.avalith.carDriver.utils.Constants.RIDE_ENDED;
import static net.avalith.carDriver.utils.Constants.RIDE_KEY;
import static net.avalith.carDriver.utils.Constants.VEHICLE_IN_RIDE;
import static net.avalith.carDriver.utils.Constants.VEHICLE_IN_USE;
import static net.avalith.carDriver.utils.Constants.VEHICLE_NOT_IN_RIDE;


@Service
public class RideService {

    @Autowired
    private RideRepository rideRepository;

    @Autowired
    private PointRepository pointRepository;

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LicenseService licenseService;

    @Autowired
    private SalesRepository salesRepository;

    @Autowired
    private MishapService mishapService;

    @Autowired
    private RideLogRepository rideLogRepository;

    @Autowired
    private RedisTemplate<String, Ride> redisTemplate;

    @Autowired
    private RedisTemplate<String, Point> redisTemplatePoint;

    public Ride save(RideDtoRequest ride){

        RidePointDto ridePoint = ride.getOriginPoint();

        Vehicle vehicle = vehicleRepository.findByDomain(ride.getVehicleDomain())
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_VEHICLE));

        if(rideRepository.findRidesByVehicle(vehicle.getId()).isPresent())
            throw new InvalidRequestException(VEHICLE_IN_USE);

        Point point = pointRepository.getByLatAndLng(ridePoint.getLat(), ridePoint.getLng())
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_POINT));

        User user = userRepository.getByDni(ride.getUserDni())
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_USER));

        License license = user.getLicense();

        LocalDateTime expirationDate = new Timestamp(license.getExpirationDate().getTime())
                .toLocalDateTime();

        LocalDateTime endDate = new Timestamp(ride.getEndDate().getTime())
            .toLocalDateTime().plusDays(15L);

        license.setValidated(expirationDate.isAfter(endDate));
        licenseService.saveValidated(license);

        if(!license.getValidated())
            throw new InvalidLicenseException(INVALID_LICENSE);

        Ride rideSaved = new Ride(ride, vehicle, point, user);

        String value = ride.getTariffType().getValue(ride.getTariffType());

        rideSaved.setPrice(getPrice(value, vehicle.getCategoryVehicles(), ride.getStartDate(), ride.getEndDate()));

        rideSaved = rideRepository.save(rideSaved);

        rideLogRepository.save(new RideLog(rideSaved.getId(), rideSaved.getState()));

        redisTemplate.opsForHash().put(RIDE_KEY, rideSaved.getId(), rideSaved);

        return rideSaved;
    }

    public List<Ride> getAll(){
        ObjectMapper objectMapper = new ObjectMapper();
        List<Ride> list = new ArrayList<>();
        String json = "";

        if(redisTemplate.opsForHash().keys(RIDE_KEY).isEmpty()){
            rideRepository.findAll()
                    .forEach((Ride ride) -> redisTemplate.opsForHash().put(RIDE_KEY, ride.getId(), ride));
            redisTemplate.boundHashOps(RIDE_KEY).expire(24L, TimeUnit.HOURS);
        }

        try {
            json = objectMapper.writeValueAsString(redisTemplate.opsForHash().values(RIDE_KEY));
            list = objectMapper.readValue(json, new TypeReference<List<Ride>>(){});
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return list;
    }

    public Ride update(Long id, RideDtoUpdateRequest ride) {

        Ride oldRide = rideRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_RIDE));

        RidePointDto originRidePoint = ride.getOriginPoint();
        RidePointDto destinationRidePoint = ride.getDestinationPoint();

        Vehicle vehicle = vehicleRepository.findByDomain(ride.getVehicleDomain())
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_VEHICLE));

        Point originPoint = pointRepository.getByLatAndLng(originRidePoint.getLat(), originRidePoint.getLng())
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_POINT));

        Point destinationPoint = pointRepository.getByLatAndLng(destinationRidePoint.getLat(), destinationRidePoint.getLng())
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_POINT));

        User user = userRepository.getByDni(ride.getUserDni())
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_USER));

        Ride rideUpdate = new Ride(ride, vehicle, originPoint, destinationPoint, user);
        rideUpdate.setState(oldRide.getState());

        String value = ride.getTariffType().getValue(ride.getTariffType());

        rideUpdate.setPrice(getPrice(value, vehicle.getCategoryVehicles(), ride.getStartDate(), ride.getEndDate()));
        rideUpdate.setId(id);
        rideUpdate.setCode(oldRide.getCode());

        rideLogRepository.save(new RideLog(id, rideUpdate.getState()));
        redisTemplate.opsForHash().put(RIDE_KEY, rideUpdate.getId(), rideUpdate);

        return rideRepository.save(rideUpdate);
    }

    public Ride endRide(Long id, DestinationPointDtoRequest destinationRequest){
        Ride ride = rideRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_RIDE));

        Point destinationPoint = pointRepository.getByLatAndLng(destinationRequest.getLat(), destinationRequest.getLng())
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_POINT));

        destinationPoint.setIsDestination(true);
        destinationPoint = pointRepository.save(destinationPoint);
        redisTemplatePoint.opsForHash().put(POINT_KEY, destinationPoint.getId(), destinationPoint);

        ride.setDestinationPoint(destinationPoint);

        System.out.println(ride.getStartDate());
        Date date = ride.getStartDate();

        LocalDateTime noPayTime = new Timestamp(date.getTime())
                .toLocalDateTime()
                .minusMinutes(30);

        LocalDateTime now = LocalDateTime.now();

        if(ride.getState().equals(RideState.CANCELLED) || ride.getState().equals(RideState.FINISHED))
            throw new InvalidRequestException(RIDE_ENDED);

        if (now.isBefore(noPayTime) || now.isEqual(noPayTime))
        {
            ride.setPrice(0d);
            ride.setState(RideState.CANCELLED);
        } else{
            if(ride.getState().equals(RideState.RESERVED)){
                Instant endDate = ride.getStartDate()
                        .toInstant()
                        .plusSeconds(3600);

                ride.setPrice(getPrice(TariffType.HOUR.getValue(TariffType.HOUR), ride.getVehicle().getCategoryVehicles(), ride.getStartDate(), Date.from(endDate)));
            }

            ride.setState(RideState.FINISHED);
        }

        Double profit = ride.getPrice() * ride.getVehicle().getProvider().getCommission();

        rideLogRepository.save(new RideLog(id, ride.getState()));
        salesRepository.save(new Sale(profit, ride.getId()));
        redisTemplate.opsForHash().put(RIDE_KEY, ride.getId(), ride);

        return rideRepository.save(ride);
    }

    public Ride startRide(Long id){
        Ride ride = rideRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_RIDE));

        if(!ride.getState().equals(RideState.RESERVED))
            throw new InvalidRequestException(VEHICLE_IN_RIDE);

        ride.setState(RideState.IN_RIDE);
        rideLogRepository.save(new RideLog(ride.getId(), ride.getState()));

        redisTemplate.opsForHash().put(RIDE_KEY, ride.getId(), ride);

        return rideRepository.save(ride);
    }

    public Ride inCrashRide(Long id, MishapDtoRequest mishapRequest){
        Ride ride = rideRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_RIDE));

        if(!ride.getState().equals(RideState.IN_RIDE))
            throw new InvalidRequestException(VEHICLE_NOT_IN_RIDE);

        Mishap mishap = new Mishap(mishapRequest);
        mishap.setRide(ride);

        ride.setState(RideState.IN_CRASH);

        rideLogRepository.save(new RideLog(ride.getId(), ride.getState()));

        mishapService.save(mishap, id);

        redisTemplate.opsForHash().put(RIDE_KEY, ride.getId(), ride);

        return rideRepository.save(ride);
    }

    private Double getPrice(String value, VehicleCategory category, Date startDate, Date endDate){

        Long diff = endDate.getTime() - startDate.getTime();

        if(value.equals("priceHour")){
            Long time = TimeUnit.MINUTES.convert(diff, TimeUnit.MILLISECONDS);
            Double timePrice = Math.ceil(time/60f);

            return timePrice * category.getPriceHour();
        }

        if(value.equals("priceDay")){
            Long time = TimeUnit.MINUTES.convert(diff, TimeUnit.MILLISECONDS);
            Double timePrice = Math.ceil(time/(60f * 24f));

            return timePrice * category.getPriceDay();
        }

        if(value.equals("priceWeek")){
            Long time = TimeUnit.MINUTES.convert(diff, TimeUnit.MILLISECONDS);
            Double timePrice = Math.ceil(time/(60f * 24f));
            timePrice = Math.ceil(timePrice/7);

            return timePrice * category.getPriceWeek();
        }

        return 0d;
    }

    @Scheduled(cron = "0 */15 * ? * *")
    public void ridePosition(){
        ObjectMapper objectMapper = new ObjectMapper();
        List<Ride> list = rideRepository.findAllRidesInRide();

        list.forEach((Ride ride) ->{
                Double lat = Math.random()*(90-(-90)+1)+((-90));
                Double lng = Math.random()*(180-(-180)+1)+((-180));

                rideLogRepository.save(new RideLog(ride.getId(), ride.getState(), lat.toString(), lng.toString()));
        });
    }
}
