package trade_point.controller;

import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import trade_point.entity.TradePoint;
import trade_point.payload.TradePointRequest;
import trade_point.service.TradePointService;

@Api(value = "This controller allow to add new trade point")
@RestController
@RequestMapping(value = "/w/user/manager/")
public class TradePointController {
    private final TradePointService tradePointService;

    @Autowired
    public TradePointController(TradePointService tradePointService) {
        this.tradePointService = tradePointService;
    }

    @PostMapping("/")
//    @PreAuthorize(value = "hasRole('ROLE_MANAGER')")
    public boolean create(@RequestBody TradePointRequest tradePoint) {
        return tradePointService.create(tradePoint);
    }
}
