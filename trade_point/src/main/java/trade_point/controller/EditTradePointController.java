package trade_point.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import trade_point.entity.TradePoint;
import trade_point.entity.User;
import trade_point.exception.UserNotFoundException;
import trade_point.payload.EditSellerRequest;
import trade_point.service.EditTradePointService;
import trade_point.config.swagger2.SwaggerMethodToDocument;

import java.util.ArrayList;
import java.util.List;

@Api(value = "This controller allow to edit trade point")
@RestController
@RequestMapping(value = "/w/user/manager/trade-points")
public class EditTradePointController {
    private final EditTradePointService editTradePointService;

    @Autowired
    public EditTradePointController(EditTradePointService editTradePointService) {
        this.editTradePointService = editTradePointService;
    }

    @SwaggerMethodToDocument
    @PostMapping("{id}")
//    @PreAuthorize(value = "hasRole('ROLE_MANAGER')")
    @ApiOperation(value = "To add a seller to another trade point")
    public ResponseEntity<Object> addSellerTradePoint(@RequestBody EditSellerRequest editSellerRequest,
                                                      @PathVariable("id") TradePoint tradePoint)
            throws UserNotFoundException {
        return editTradePointService.AddSeller(editSellerRequest, tradePoint);
    }

    @SwaggerMethodToDocument
    @GetMapping("{id}")
//    @PreAuthorize(value = "hasRole('ROLE_MANAGER')")
    @ApiOperation(value = "To show seller to another trade point")
    public List<User> showAllSellersOnThisTradePoint(@PathVariable("id") TradePoint tradePoint) {
        return new ArrayList<>(tradePoint.getUsers());
    }


}

