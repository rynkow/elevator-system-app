package com.rynkow.elevatorsystem.server.controller;

import com.rynkow.elevatorsystem.server.model.ElevatorSystem;
import com.rynkow.elevatorsystem.server.model.ElevatorSystemState;
import com.rynkow.elevatorsystem.server.model.interfaces.IElevatorSystem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
public class ElevatorSystemRestController {

    private IElevatorSystem elevatorSystem;

    public ElevatorSystemRestController(){
        elevatorSystem = new ElevatorSystem(6, 6);
    }

    @GetMapping("/elevators")
    public ElevatorSystemState getSystemState(){
        return elevatorSystem.getState();
    }

    @PatchMapping("/elevators")
    public ElevatorSystemState nextStep(){
        elevatorSystem.nextStep();
        return elevatorSystem.getState();
    }

    @PutMapping("elevators/{maxFloor}/{elevatorCount}")
    public ElevatorSystemState setSystemParameters(@PathVariable Integer elevatorCount, @PathVariable Integer maxFloor){
        elevatorSystem = new ElevatorSystem(maxFloor, elevatorCount);
        return elevatorSystem.getState();
    }

    @PutMapping("/destination/{elevatorId}/{destination}")
    public ResponseEntity<?> newDestination(@PathVariable Integer destination, @PathVariable Integer elevatorId){
        if (!elevatorSystem.newElevatorDestination(elevatorId, destination))
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/request/{floor}/{direction}")
    public ResponseEntity<?> newRequest(@PathVariable Integer direction, @PathVariable Integer floor){
        boolean successful = false;
        if (direction < 0 && elevatorSystem.newDownRequest(floor))
            return new ResponseEntity<>(HttpStatus.OK);
        else if (direction > 0 && elevatorSystem.newUpRequest(floor))
            return new ResponseEntity<>(HttpStatus.OK);

        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
