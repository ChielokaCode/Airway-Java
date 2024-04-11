package org.airway.airwaybackend.serviceImpl;

import org.airway.airwaybackend.dto.SeatListDto;
import org.airway.airwaybackend.exception.SeatListNotFoundException;
import org.airway.airwaybackend.model.SeatList;
import org.airway.airwaybackend.repository.PassengerRepository;
import org.airway.airwaybackend.repository.SeatListRepository;
import org.airway.airwaybackend.repository.SeatRepository;
import org.airway.airwaybackend.service.SeatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
public class SeatServiceImpl implements SeatService {
    private final SeatListRepository seatListRepository;
    private final SeatRepository seatRepository;

    @Autowired
    public SeatServiceImpl(SeatListRepository seatListRepository, SeatRepository seatRepository) {
        this.seatListRepository = seatListRepository;
        this.seatRepository = seatRepository;

    }

    @Override
    public List<SeatListDto> getSeatListForSeat (Long seatId) throws SeatListNotFoundException {
        List<SeatList> seatListsForClass = seatListRepository.findAllBySeat_Id(seatId).orElseThrow(()-> new SeatListNotFoundException("seat not available"));
        return convertToSeatDTO(seatListsForClass);
    }
    @Override
    public List<SeatListDto> convertToSeatDTO (List<SeatList> seatLists) throws SeatListNotFoundException {
        if (seatLists.isEmpty()){
            throw  new SeatListNotFoundException("flight not present");
        }
        List<SeatListDto> seatListDtos = new ArrayList<>();
        for(SeatList seatList: seatLists){
            SeatListDto seatListDto = new SeatListDto();
            seatListDto.setSeatLabel(seatList.getSeatLabel());
            seatListDto.setId(seatList.getId());
            seatListDto.setOccupied(seatList.getOccupied());
            seatListDto.setSeatId(seatList.getSeat().getId());
            seatListDto.setSeatLabel(seatList.getSeatLabel());
            seatListDtos.add(seatListDto);
        }
        return seatListDtos;
    }
}
