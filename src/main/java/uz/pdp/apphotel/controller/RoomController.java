package uz.pdp.apphotel.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import uz.pdp.apphotel.entity.Hotel;
import uz.pdp.apphotel.entity.Room;
import uz.pdp.apphotel.payload.RoomDto;
import uz.pdp.apphotel.repository.HotelRepository;
import uz.pdp.apphotel.repository.RoomRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("hotel/room")
public class RoomController {

    @Autowired
    RoomRepository roomRepository;
    @Autowired
    HotelRepository hotelRepository;

    @PostMapping("/addRoom")
    public String addRoom(@RequestBody RoomDto roomDto){

        Optional<Hotel> optionalHotel = hotelRepository.findById(roomDto.getHotelId());
        if(!optionalHotel.isPresent())
            return "Hotel not Found !";

        try{
            Room room = new Room();
            room.setNumberRoom(roomDto.getNumberRoom());
            room.setFloorNumber(roomDto.getFloorNumber());
            room.setSizeRoom(roomDto.getSizeRoom());
            room.setHotel(optionalHotel.get());
            Room save = roomRepository.save(room);
            return "Room Added Successfully ! Room ID: " + save.getId();
        }
        catch (Exception e){
            return "Error in Adding !";
        }

    }

    @GetMapping("/getRoomById/{id}")
    public Room getRoomById(@PathVariable Integer id){
        Optional<Room> byId = roomRepository.findById(id);
        return byId.orElseGet(Room::new);
    }

    @GetMapping("/getRoomsByHotelId/{id}")
    public List<Room> getRoomsByHotel(@PathVariable Integer id,
                                      @RequestParam(defaultValue = "0") Integer pageNo,
                                      @RequestParam(defaultValue = "10") Integer pageSize){

        if (pageNo != 0)
            pageNo--;

        Pageable pageable = PageRequest.of(pageNo, pageSize);

        return roomRepository.findAllByHotel_Id(id, pageable);
    }

    @DeleteMapping("/deleteById/{id}")
    public String deleteById(@PathVariable Integer id){

        try {
            roomRepository.deleteById(id);
            return "Room deleted !";
        }
        catch (Exception e){
            return "Error in deleting room !";
        }

    }

    @PutMapping("/updateById/{id}")
    public String updateById(@PathVariable Integer id,
                             @RequestBody RoomDto roomDto){

        Optional<Room> byId = roomRepository.findById(id);

        if (byId.isPresent()){

            Room room = byId.get();

            Integer x = roomDto.getHotelId();

            if (x != null && !x.equals(room.getHotel().getId())){
                Optional<Hotel> byId1 = hotelRepository.findById(x);
                if (!byId1.isPresent())
                    return "Hotel Not Found !";
                room.setHotel(byId1.get());
            }

            x = roomDto.getNumberRoom();
            if (x != null)
                room.setNumberRoom(x);
            x = roomDto.getFloorNumber();
            if(x != null)
                room.setFloorNumber(x);
            x = roomDto.getSizeRoom();
            if(x != null)
                room.setSizeRoom(x);

            try {
                roomRepository.save(room);
                return "Room Updated Successfully !";
            }
            catch (Exception e){
                return "Error In Updating Room !";
            }
        }

        return "Room Not Found !";
    }

}