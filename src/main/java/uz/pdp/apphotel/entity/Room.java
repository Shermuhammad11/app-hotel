package uz.pdp.apphotel.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"number_room", "hotel_id"})})
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, name = "number_room")
    private Integer numberRoom;

    @Column(nullable = false)
    private Integer floorNumber;

    @Column(nullable = false)
    private Integer sizeRoom;

    @ManyToOne(optional = false)
    @JoinColumn(name = "hotel_id", referencedColumnName = "id")
    private Hotel hotel;
}