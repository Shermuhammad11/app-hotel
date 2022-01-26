package uz.pdp.apphotel.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import uz.pdp.apphotel.entity.Hotel;
import uz.pdp.apphotel.payload.HotelDto;
import uz.pdp.apphotel.repository.HotelRepository;

import java.util.Optional;

@RestController
@RequestMapping("/hotel")
public class HotelController {

    private final HotelRepository hotelRepository;


    public HotelController(HotelRepository hotelRepository) {
        this.hotelRepository = hotelRepository;
    }


    @GetMapping("/{id}")
    public Hotel getHotelById(@PathVariable Integer id){
        Optional<Hotel> byId = hotelRepository.findById(id);
        return byId.orElseGet(Hotel::new);
    }


    @GetMapping
    public Page<Hotel> getHotels(@RequestParam(defaultValue = "0") Integer pageNo,
                                 @RequestParam(defaultValue = "10") Integer pageSize){

        if (pageNo != 0)
            pageNo--;

        Pageable pageable = PageRequest.of(pageNo, pageSize);

        return hotelRepository.findAll(pageable);
    }


    @PostMapping
    public String addHotel(@RequestBody HotelDto hotelDto){
        Hotel hotel = new Hotel();
        hotel.setName(hotelDto.getHotelName());
        try {
            hotel = hotelRepository.save(hotel);
            return "Hotel saved! Hotel id: " + hotel.getId();
        }
        catch (Exception e){
            return "This hotel already exist !";
        }
    }


    @PutMapping("/{id}")
    public String updateHotelById(@PathVariable Integer id, @RequestBody HotelDto hotelDto){

        Optional<Hotel> optionalHotel = hotelRepository.findById(id);

        if(optionalHotel.isPresent()){
            Hotel hotel = optionalHotel.get();

            String hotelName = hotelDto.getHotelName();

            if (!hotel.getName().equals(hotelName)) {
                hotel.setName(hotelName);
                try {
                    hotelRepository.save(hotel);
                    return "Hotel Updated SuccessFully !";
                } catch (Exception e) {
                    return "This hotel already exist !";
                }
            }

            return "Hotel Updated SuccessFully !";
        }

        return "Hotel Not Found !";
    }


    @DeleteMapping("/{id}")
    public String deleteHotelById(@PathVariable Integer id){
        try {
            hotelRepository.deleteById(id);
            return "Hotel deleted Successfully!";
        }
        catch (Exception e){
            return "Hotel Not Found !";
        }
    }


}