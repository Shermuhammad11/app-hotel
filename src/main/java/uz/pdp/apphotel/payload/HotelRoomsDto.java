package uz.pdp.apphotel.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.pdp.apphotel.entity.Room;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data

public class HotelRoomsDto {

    private String hotelName;
    private List<Room> rooms;
}