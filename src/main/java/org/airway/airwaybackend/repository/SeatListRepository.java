package org.airway.airwaybackend.repository;

import org.airway.airwaybackend.dto.SeatListDto;
import org.airway.airwaybackend.model.SeatList;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SeatListRepository extends JpaRepository<SeatList, Long> {
    Optional <List<SeatList>> findAllBySeat_Id(Long seatId);
    Optional <List<SeatList>> findAllBySeat_IdAndOccupied(Long seatId, Boolean isOccupied);
}
