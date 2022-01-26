package uz.pdp.apphotel.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import uz.pdp.apphotel.entity.Room;

import java.util.List;

public interface RoomRepository extends PagingAndSortingRepository<Room, Integer> {

    Page<Room> findAllByHotel_Id(Integer hotel_id, Pageable pageable);

}
