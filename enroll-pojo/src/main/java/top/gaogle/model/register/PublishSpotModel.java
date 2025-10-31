package top.gaogle.model.register;


import top.gaogle.domain.register.PublishSpot;

public class PublishSpotModel extends PublishSpot {

    private Integer roomCount;
    private Integer seatCount;

    public Integer getRoomCount() {
        return roomCount;
    }
    public void setRoomCount(Integer roomCount) {
        this.roomCount = roomCount;
    }

    public Integer getSeatCount() {
        return seatCount;
    }

    public void setSeatCount(Integer seatCount) {
        this.seatCount = seatCount;
    }
}
