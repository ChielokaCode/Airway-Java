package org.airway.airwaybackend.service;

import org.airway.airwaybackend.dto.SeatListDto;
import org.airway.airwaybackend.model.SeatList;

import java.util.List;

public interface SeatService {
    List<SeatListDto> getSeatListForSeat (Long seatId);
    List<SeatListDto> convertToSeatDTO (List<SeatList> seatLists);
}
