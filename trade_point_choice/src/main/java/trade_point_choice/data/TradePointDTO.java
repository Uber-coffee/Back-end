package trade_point.data;

import trade_point.entity.TradePoint;

public class TradePointDTO {
    private TradePoint tradePoint;
    private Integer time;
    private Double distance;

    public TradePoint getPickPoint() {
        return tradePoint;
    }

    public void setPickPoint(TradePoint pickPoint) {
        this.tradePoint = pickPoint;
    }

    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }
}
