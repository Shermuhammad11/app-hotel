package uz.pdp.apphotel.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data

public class RoomDto {

    private Integer numberRoom;

    private Integer floorNumber;

    private Integer sizeRoom;

    private Integer hotelId;
}