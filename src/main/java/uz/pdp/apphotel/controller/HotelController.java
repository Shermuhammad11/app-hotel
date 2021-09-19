package uz.pdp.apphotel.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import uz.pdp.apphotel.entity.Hotel;
import uz.pdp.apphotel.entity.Room;
import uz.pdp.apphotel.payload.HotelDto;
import uz.pdp.apphotel.payload.HotelRoomsDto;
import uz.pdp.apphotel.repository.HotelRepository;
import uz.pdp.apphotel.repository.RoomRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/hotel")
public class HotelController {

    @Autowired
    private HotelRepository hotelRepository;
    @Autowired
    private RoomRepository roomRepository;

    @PostMapping("/addHotel")
    public String addHotel(@RequestBody HotelDto hotelDto){
        Hotel hotel = new Hotel();
        hotel.setName(hotelDto.getHotelName());
        Hotel save = hotelRepository.save(hotel);
        return "Hotel saved! Hotel id: " + save.getId();
    }

    @GetMapping("/{id}")
    public Hotel getHotel(@PathVariable Integer id){
        Optional<Hotel> byId = hotelRepository.findById(id);
        return byId.orElseGet(Hotel::new);
    }

    @GetMapping("/getHotels")
    public List<Hotel> getHotels(){
        return hotelRepository.findAll();
    }

    @GetMapping("/{id}/rooms")
    public HotelRoomsDto getHotelWithRooms(@PathVariable Integer id,
                                           @RequestParam(defaultValue = "0") Integer pageNo,
                                           @RequestParam(defaultValue = "10") Integer pageSize){

        if (pageNo != 0)
            pageNo--;

        Optional<Hotel> byId = hotelRepository.findById(id);
        if(!byId.isPresent())
            return new HotelRoomsDto();

        HotelRoomsDto hotelRoomDto = new HotelRoomsDto();
        hotelRoomDto.setHotelName(byId.get().getName());

        Pageable pageable = PageRequest.of(pageNo, pageSize);
        List<Room> allByHotel_id = roomRepository.findAllByHotel_Id(id, pageable);

        if (allByHotel_id.isEmpty())
            hotelRoomDto.setRooms(new ArrayList<>());
        else
            hotelRoomDto.setRooms(allByHotel_id);

        return hotelRoomDto;
    }

    @DeleteMapping("/{id}")
    public String deleteHotel(@PathVariable Integer id){
        try {
            hotelRepository.deleteById(id);
            return "Hotel deleted !";
        }
        catch (Exception e){
            return "Error in deleting hotel !";
        }
    }

    @PutMapping("/{id}")
    public String updateHotel(@PathVariable Integer id, @RequestBody HotelDto hotelDto){

        Optional<Hotel> optionalHotel = hotelRepository.findById(id);

        if(optionalHotel.isPresent()){
            Hotel hotel1 = optionalHotel.get();
            hotel1.setName(hotelDto.getHotelName());
            try {
                hotelRepository.save(hotel1);
                return "Hotel Updated SuccessFully !";
            }
            catch (Exception e){
                return "Error In Updating Hotel !";
            }
        }

        return "Hotel Not Found !";
    }


}