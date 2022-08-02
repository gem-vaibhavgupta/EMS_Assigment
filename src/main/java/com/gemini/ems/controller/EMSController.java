package com.gemini.ems.controller;

import com.gemini.ems.model.BillDetails;
import com.gemini.ems.model.Grievance;
import com.gemini.ems.model.RegisterUser;
import com.gemini.ems.service.ElectricityService;
import com.gemini.ems.service.RegistrationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.concurrent.ExecutionException;

/**
 * @author va.gupta
 * controller class electricity management
 */
@RestController
@RequestMapping("api/v1")
public class EMSController {
    private static final Logger LOGGER = LoggerFactory.getLogger(EMSController.class);

    private final RegistrationService registrationService;
    private final ElectricityService electricityService;

    public EMSController(final RegistrationService registrationService, final ElectricityService electricityService) {
        this.registrationService = registrationService;
        this.electricityService = electricityService;
    }

    /**
     * api to register customer
     *
     * @param registerUser obj
     * @return response
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody RegisterUser registerUser) throws ExecutionException, InterruptedException {
        UUID customerId = registrationService.register(registerUser);
        if (customerId != null) {
            LOGGER.info("Registered user with ID {}", customerId);
            return new ResponseEntity<>("User has been registered with ID " + customerId, HttpStatus.CREATED);
        } else
            return new ResponseEntity<>("Error while registering user", HttpStatus.INTERNAL_SERVER_ERROR);

    }

    /**
     * api to calculate the electricity bill amount
     *
     * @param noOfUnits
     * @return response
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @GetMapping("/calculate")
    public BillDetails calculateBill(@RequestParam Integer noOfUnits) throws ExecutionException,
            InterruptedException {
        LOGGER.info("Calculate bill for units {}", noOfUnits);
        return electricityService.calculateBill(noOfUnits).get();
    }

    /**
     * api to register customer grievance
     *
     * @param grievance obj
     * @return response
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @PostMapping("/grievance")
    public ResponseEntity<String> grievance(@RequestBody Grievance grievance) throws ExecutionException, InterruptedException {
        LOGGER.info("Register grievance with details {}", grievance);
        UUID grievanceId = electricityService.registerGrievance(grievance);
        if (grievanceId != null) {
            LOGGER.info("Registered grievance with ID {}", grievanceId);
            return new ResponseEntity<>("Grievance has been registered with ID " + grievanceId, HttpStatus.CREATED);
        } else
            return new ResponseEntity<>("Error while registering grievance", HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
