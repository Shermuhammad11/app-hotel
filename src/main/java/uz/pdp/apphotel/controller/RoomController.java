package uz.pdp.apphotel.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import uz.pdp.apphotel.entity.Hotel;
import uz.pdp.apphotel.entity.Room;
import uz.pdp.apphotel.payload.RoomDto;
import uz.pdp.apphotel.repository.HotelRepository;
import uz.pdp.apphotel.repository.RoomRepository;

import java.util.Optional;

@RestController
@RequestMapping("/room")
public class RoomController {

    private final RoomRepository roomRepository;
    private final HotelRepository hotelRepository;


    public RoomController(RoomRepository roomRepository, HotelRepository hotelRepository) {
        this.roomRepository = roomRepository;
        this.hotelRepository = hotelRepository;
    }


    @GetMapping("/{id}")
    public Room getRoomById(@PathVariable Integer id){
        Optional<Room> byId = roomRepository.findById(id);
        return byId.orElseGet(Room::new);
    }


    @GetMapping("/getRoomsByHotelId/{id}")
    public Page<Room> getRoomsByHotelId(@PathVariable Integer id,
                                      @RequestParam(defaultValue = "0") Integer pageNo,
                                      @RequestParam(defaultValue = "10") Integer pageSize){

        if (pageNo != 0)
            pageNo--;

        Pageable pageable = PageRequest.of(pageNo, pageSize);

        return roomRepository.findAllByHotel_Id(id, pageable);
    }


    @PostMapping
    public String addRoom(@RequestBody RoomDto roomDto){

        Optional<Hotel> optionalHotel = hotelRepository.findById(roomDto.getHotelId());
        if(!optionalHotel.isPresent())
            return "Hotel not Found !";

        Room room = new Room();
        room.setNumberRoom(roomDto.getNumberRoom());
        room.setFloorNumber(roomDto.getFloorNumber());
        room.setSizeRoom(roomDto.getSizeRoom());
        room.setHotel(optionalHotel.get());

        try{
            room = roomRepository.save(room);
            return "Room Added Successfully ! Room ID: " + room.getId();
        }
        catch (Exception e){
            return "This Room already exists in given Hotel!";
        }

    }


    @PutMapping("/{id}")
    public String updateRoomById(@PathVariable Integer id, @RequestBody RoomDto roomDto){

        Optional<Room> byId = roomRepository.findById(id);

        if (byId.isPresent()){

            Room room = byId.get();

            Integer hotelId = roomDto.getHotelId();
            Integer numberRoom = roomDto.getNumberRoom();

            boolean checkChange = false;

            if (!hotelId.equals(room.getHotel().getId())){
                Optional<Hotel> optionalHotel = hotelRepository.findById(hotelId);
                if (!optionalHotel.isPresent())
                    return "Hotel Not Found !";
                room.setHotel(optionalHotel.get());
                checkChange = true;
            }
            else if (!room.getNumberRoom().equals(numberRoom)) {
                room.setNumberRoom(numberRoom);
                checkChange = true;
            }

            room.setFloorNumber(roomDto.getFloorNumber());
            room.setSizeRoom(roomDto.getSizeRoom());

            if (checkChange) {
                try {
                    roomRepository.save(room);
                    return "Room Updated Successfully !";
                } catch (Exception e) {
                    return "This Room already exists in given Hotel!";
                }
            }

            roomRepository.save(room);
            return "Room Updated Successfully !";
        }

        return "Room Not Found !";
    }


    @DeleteMapping("/{id}")
    public String deleteRoomById(@PathVariable Integer id){
        try {
            roomRepository.deleteById(id);
            return "Room deleted !";
        }
        catch (Exception e){
            return "Room Not Found !";
        }
    }

}