package trade_point.service;

import trade_point.data.TradePointDTO;
import trade_point.entity.TradePoint;
import trade_point.repository.TradePointRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class TradePointService {

    private final TradePointRepository repository;

    public TradePointService(TradePointRepository repository) {
        this.repository = repository;
    }

    public List <TradePointDTO> getListNearestPickPoint(Double longitude, Double latitude){

        final double DEGREES_IN_ONE_KILOMETER = 0.028074;
        final double RADIUS = 6371.0;

        List<TradePoint> list= repository.findByLongitudeBetweenAndLatitudeBetween(
                longitude - DEGREES_IN_ONE_KILOMETER,
                longitude + DEGREES_IN_ONE_KILOMETER,
                latitude - DEGREES_IN_ONE_KILOMETER,
                latitude + DEGREES_IN_ONE_KILOMETER);

        List <TradePointDTO> result = new ArrayList<>();

        for (TradePoint p: list) {
            TradePointDTO tmp = new TradePointDTO();
            tmp.setPickPoint(p);

            tmp.setDistance(Precision.round(Math.acos(Math.sin(Math.toRadians(p.getLatitude())) * Math.sin(Math.toRadians(latitude)) +
                                                      Math.cos(Math.toRadians(p.getLatitude())) * Math.cos(Math.toRadians(latitude)) *
                                                      Math.cos(Math.toRadians(p.getLongitude() - longitude))) * RADIUS, 3));
            tmp.setTime((int) (tmp.getDistance() * 1000 / 90));

            result.add(tmp);
        }

        result.sort(Comparator.comparing(TradePointDTO::getDistance));
        return result;
    }
}