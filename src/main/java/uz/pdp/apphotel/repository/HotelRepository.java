package uz.pdp.apphotel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.apphotel.entity.Hotel;

public interface HotelRepository extends JpaRepository<Hotel, Integer> {

}

