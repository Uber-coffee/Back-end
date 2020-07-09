import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import trade_point.entity.TradePoint;
import trade_point.exception.TradePointNotFoundException;
import trade_point.payload.TradePointRequest;
import trade_point.repository.TradePointRepository;
import trade_point.service.TradePointService;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TradePointServiceTest {
    @MockBean
    private TradePointRepository pointRepository;

    private final Map<String, TradePoint> tradePointMap = new HashMap<>();

    @BeforeEach
    public void init() {
        pointRepository = mock(TradePointRepository.class);
        when(pointRepository.save(any(TradePoint.class))).then(
                i -> tradePointMap.put(((TradePoint) i.getArgument(0)).getAddress(), i.getArgument(0))
        );
        when(pointRepository.findByAddress(anyString())).thenAnswer(
                i -> tradePointMap.get(i.getArgument(0))
        );
        when(pointRepository.existsByAddress(anyString())).thenAnswer(
                i -> tradePointMap.get(i.getArgument(0)) != null
        );
    }

    @Test
    void create() throws TradePointNotFoundException {
        TradePointService tradePointService = new TradePointService(pointRepository);
        String name = "Bulochka";

        TradePointRequest tradePoint = new TradePointRequest();
        tradePoint.setAddress("SPB");
        tradePoint.setLatitude(28.89);
        tradePoint.setLatitude(29.99);
        tradePoint.setName(name);

        TradePoint tp = tradePointService.create(tradePoint);

        boolean isTradePointCreated = tp != null;

        assertTrue(isTradePointCreated);
    }
}