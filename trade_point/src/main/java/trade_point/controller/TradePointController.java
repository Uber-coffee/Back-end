package trade_point.controller;

import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import trade_point.entity.TradePoint;
import trade_point.payload.TradePointRequest;
import trade_point.service.TradePointService;
import trade_point.util.Views;

import java.util.List;

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
        return tradePointService.create(tradePoint) != null;
    }

    @GetMapping("/trade-points")
    @JsonView(Views.Compact.class)
    public List<TradePoint> showAllTradePoints(){
        return tradePointService.allTradePointService();
    }
}
