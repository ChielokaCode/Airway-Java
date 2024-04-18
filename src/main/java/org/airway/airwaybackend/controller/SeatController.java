package org.airway.airwaybackend.controller;

import org.airway.airwaybackend.dto.SeatListDto;
import org.airway.airwaybackend.exception.SeatListNotFoundException;
import org.airway.airwaybackend.serviceImpl.SeatServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/seat")
@CrossOrigin(origins = {"http://localhost:5173", "https://airway-ng.netlify.app"}, allowCredentials = "true")
public class SeatController {
        private final SeatServiceImpl seatServiceImp;

        public SeatController(SeatServiceImpl seatServiceImp) {
            this.seatServiceImp = seatServiceImp;
        }
        @GetMapping("/get-SeatList/{seatId}")
        public ResponseEntity<List<SeatListDto>> getSeatForClass (@PathVariable Long seatId) throws SeatListNotFoundException {
            List<SeatListDto> seatListDtosGotten =  seatServiceImp.getSeatListForSeat(seatId);
            return ResponseEntity.ok(seatListDtosGotten);
        }
}
