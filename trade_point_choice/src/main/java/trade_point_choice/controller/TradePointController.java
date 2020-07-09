package trade_point.controller;

import auth.config.swagger2.SwaggerMethodToDocument;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import trade_point.service.TradePointService;

import java.util.List;

@Api(
        value = "This controller edit and update information about tradePoint"
)
@RestController
public class TradePointController {


    private final TradePointService tradePointService;

    public TradePointController(TradePointService tradePointService) {
        this.tradePointService = tradePointService;
    }


    @ApiOperation(value = "Get nearest tradePoint")
    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "List tradePoints",
                    response = List.class),
            @ApiResponse(
                    code = 400,
                    message = "Input failed validation",
                    response = String.class),
            @ApiResponse(
                    code = 500,
                    message = "Error in process get tradePoints",
                    response = String.class)
    })
    @SwaggerMethodToDocument
    @GetMapping("/m/tradePoint/nearest")
    public ResponseEntity<Object> getNearestPickPoint(@RequestParam Double latitude,@RequestParam Double longitude ) {
        if (latitude == null || longitude == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(tradePointService.getListNearestPickPoint(latitude, longitude), HttpStatus.OK);
    }

}
