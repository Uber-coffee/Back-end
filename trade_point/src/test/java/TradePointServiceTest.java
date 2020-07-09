//import org.junit.jupiter.api.Test;
//import org.mockito.ArgumentMatchers;
//import org.mockito.Mockito;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import trade_point.entity.TradePointUser;
//import trade_point.exception.TradePointNotFoundException;
//import trade_point.repository.TradePointRepository;
//import trade_point.service.TradePointService;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//class TradePointServiceTest {
//    @MockBean
//    private TradePointRepository pointRepository;
//
//    @Test
//    void create() throws TradePointNotFoundException {
//        TradePointService tradePointService = new TradePointService(pointRepository);
//        String name = "Bulochka";
//
//        TradePointUser tradePoint = new TradePointUser();
//        tradePoint.setAddress("SPB");
//        tradePoint.setLatitude(28.89);
//        tradePoint.setLatitude(29.99);
//        tradePoint.setName(name);
//        tradePoint.setIsActive(true);
//
//        boolean isTradePointCreated = tradePointService.create(tradePoint);
//
//        assertTrue(isTradePointCreated);
//
//        Mockito.verify(pointRepository, Mockito.times(1)).save(tradePoint);
//    }
//
//    @Test
//    public void createIsFailed() {
//        TradePointService tradePointService = new TradePointService(pointRepository);
//        String name = "Bulochka";
//
//        TradePointUser tradePoint = new TradePointUser();
//        tradePoint.setAddress("SPB");
//        tradePoint.setLatitude(28.89);
//        tradePoint.setLatitude(29.99);
//        tradePoint.setName(name);
//        tradePoint.setIsActive(true);
//
//        Mockito.doReturn(new TradePointUser())
//                .when(pointRepository)
//                .existsByName(name);
//
//        boolean isTradePointCreated = tradePointService.create(tradePoint);
//
//        assertFalse(isTradePointCreated);
//
//        Mockito.verify(pointRepository, Mockito.times(0))
//                .save(ArgumentMatchers.any(TradePointUser.class));
//    }
//}