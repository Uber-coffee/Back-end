package trade_point.service;

import org.springframework.stereotype.Service;
import trade_point.entity.TradePoint;
import trade_point.payload.TradePointRequest;
import trade_point.repository.TradePointRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class TradePointService {
    private final TradePointRepository tradePointRepository;

    public TradePointService(TradePointRepository tradePointRepository) {
        this.tradePointRepository = tradePointRepository;
    }

    public TradePoint create(TradePointRequest tradePoint) {
        if (tradePointRepository.existsByName(tradePoint.getName())) {
            return null;
        } else {
            TradePoint point = new TradePoint();

            point.setAddress(tradePoint.getAddress());
            point.setLatitude(tradePoint.getLatitude());
            point.setLongitude(tradePoint.getLongitude());
            point.setName(tradePoint.getName());
            point.setIsActive(true);

            tradePointRepository.save(point);
             return point;
        }
    }

    public List<TradePoint> allTradePointService () {
        return tradePointRepository.findAll();
    }
}
