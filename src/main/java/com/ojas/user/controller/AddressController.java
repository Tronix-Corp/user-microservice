package com.ojas.user.controller;

import com.ojas.user.dto.Address;
import com.ojas.user.dto.CustomResponse;
import com.ojas.user.dto.DeleteAddressRequest;
import com.ojas.user.dto.User;
import com.ojas.user.service.AddressService;
import com.ojas.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

@Validated
@RequiredArgsConstructor
@RestController
public class AddressController {
    private final AddressService addressService;
    private final UserService userService;
    LogManager logManager = LogManager.getLogManager();
    Logger logger = logManager.getLogger(Logger.GLOBAL_LOGGER_NAME);

    @PostMapping("users/addresses/add")
    public CustomResponse addAddress(@RequestBody @Valid Address address, @NonNull HttpServletRequest request) {
        try {
            if(address.getCity().isBlank() || address.getPinCode().isBlank() || address.getLoc().isBlank() || address.getState().isBlank()) {
                logger.log(Level.WARNING, "Address Field(s) Empty.");
                throw new Exception("Address Field(s) Empty.");
            }
            String userEmail = userService.extractEmailFromRequest(request);
            User user = userService.getUserByEmail(userEmail);
            boolean status = addressService.saveAddress(user.getId(), address);
            if(!status) {
                throw new Exception();
            }
            logger.log(Level.INFO, "Address added Successfully.");
            return new CustomResponse("Address added Successfully.", HttpStatus.CREATED);
        } catch(Exception e) {
            logger.log(Level.WARNING, "Encountered a problem while adding address. - {}", e.getMessage());
            return new CustomResponse("Encountered a problem while adding address.", HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("users/addresses/view")
    public ResponseEntity<List<Address>> getAllAddresses(@NonNull HttpServletRequest request) {
        try {
            String userEmail = userService.extractEmailFromRequest(request);
            User user = userService.getUserByEmail(userEmail);
            List<Address> addresses = addressService.getAllAddresses(user.getId());
            logger.log(Level.INFO, "Addresses of user fetched Successfully.");
            return new ResponseEntity<>(addresses, HttpStatus.OK);
        } catch(Exception e) {
            logger.log(Level.WARNING, "Encountered a problem while fetching addresses of user - {}",e.getMessage());
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
        }
    }
    @DeleteMapping("users/addresses/delete/{id}")
    public CustomResponse deleteAddress(@NonNull HttpServletRequest request, @PathVariable("id") Integer addressId) {
        try {
            String userEmail = userService.extractEmailFromRequest(request);
            User user = userService.getUserByEmail(userEmail);
            boolean status = addressService.deleteAddress(user.getId(), addressId);
            if(!status) {
                throw new Exception();
            }
            logger.log(Level.INFO, "Address deleted Successfully.");
            return new CustomResponse("Address deleted successfully.", HttpStatus.OK);
        }
        catch(Exception e) {
            logger.log(Level.WARNING, "Encountered a problem while deleting the address of user. - deleteAddress in AddressController " + e.getMessage());
            return new CustomResponse("Encountered a problem while deleting the address of user.", HttpStatus.BAD_REQUEST);
        }
    }
}
