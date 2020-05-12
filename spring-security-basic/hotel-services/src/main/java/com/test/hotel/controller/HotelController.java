package com.test.hotel.controller;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import org.springframework.http.HttpStatus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.test.hotel.exception.GuestNotFoundException;
import com.test.hotel.model.Hotel;
import com.test.hotel.model.HotelModel;
import com.test.hotel.repository.HotelRepository;

@RestController
@RequestMapping("/guest")
public class HotelController {

	private static final Logger LOGGER = LoggerFactory.getLogger(HotelController.class);

	@Autowired
	HotelRepository hotelRepository;

	public HotelController(HotelRepository hotelRepository) {
		super();
		this.hotelRepository = hotelRepository;
	}

	@RequestMapping("/allGuest")
	public List<Hotel> getllGuests() {
		System.out.println("Inside the All Guest Method");
		List<Hotel> hotel = hotelRepository.findAll();
		return hotel;
	}

	@GetMapping("/{id}")
	public Hotel getGuest(@PathVariable Long id) {

		Optional<Hotel> guest = hotelRepository.findById(id);
		if (guest.isPresent()) {
			return guest.get();
		}
		throw new GuestNotFoundException("Guest Not Found In Hotel with id: " + id);

	}

	@PostMapping
	public ResponseEntity<Hotel> addGuest(@RequestBody HotelModel model) {

		Hotel hotel = hotelRepository.save(model.translateModelToGuest());
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(hotel.getId()).toUri();
		return ResponseEntity.created(uri).body(hotel);
	}

	@PutMapping("/{id}")
	public Hotel updateGuest(@PathVariable Long id, @RequestBody HotelModel model) {
		Optional<Hotel> existing = this.hotelRepository.findById(id);
		if (!existing.isPresent()) {
			throw new GuestNotFoundException("Guest not found with id: " + id);
		}
		Hotel guest = model.translateModelToGuest();
		guest.setId(id);
		return this.hotelRepository.save(guest);
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.RESET_CONTENT)
	public void deleteGuest(@PathVariable Long id) {
		Optional<Hotel> existing = this.hotelRepository.findById(id);
		if (!existing.isPresent()) {
			throw new GuestNotFoundException("Guest not found with id: " + id);
		}
		this.hotelRepository.deleteById(id);
	}

}
