package com.uade.tpo.marketplace.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uade.tpo.marketplace.entities.CarritoHabitacion;
import com.uade.tpo.marketplace.entities.Categoria;
import com.uade.tpo.marketplace.entities.Gestor;
import com.uade.tpo.marketplace.entities.Habitacion;
import com.uade.tpo.marketplace.entities.Hotel;
import com.uade.tpo.marketplace.entities.ReservaHabitacion;
import com.uade.tpo.marketplace.entities.dto.HabitacionDTO;
import com.uade.tpo.marketplace.entities.dto.HotelDTO;
import com.uade.tpo.marketplace.exceptions.CategoriaNotFoundException;
import com.uade.tpo.marketplace.exceptions.GestorNotFoundException;
import com.uade.tpo.marketplace.exceptions.HotelDuplicateException;
import com.uade.tpo.marketplace.exceptions.HotelNotFoundException;
import com.uade.tpo.marketplace.repository.CarritoHabitacionRepository;
import com.uade.tpo.marketplace.repository.CategoriaRepository;
import com.uade.tpo.marketplace.repository.GestorRepository;
import com.uade.tpo.marketplace.repository.HabitacionRepository;
import com.uade.tpo.marketplace.repository.HotelRepository;
import com.uade.tpo.marketplace.repository.ImagenRepository;
import com.uade.tpo.marketplace.repository.ReservaHabitacionRepository;

import io.jsonwebtoken.lang.Objects;
import jakarta.transaction.Transactional;

@Service
public class HotelServiceImpl implements HotelService {
        @Autowired
        private HotelRepository hotelRepository;
        @Autowired
        private GestorRepository gestorRepository;
        @Autowired
        private CategoriaRepository categoriaRepository;
        @Autowired
        private ImagenRepository imagenRepository;
        @Autowired
        private HabitacionRepository habitacionRepository;
        @Autowired
        private HabitacionService habitacionService;
        @Autowired
        private ReservaHabitacionRepository reservaHabitacionRepository;
        @Autowired
        private CarritoHabitacionRepository carritoHabitacionRepository;

        public List<HotelDTO> getHotels() {
                List<Hotel> hoteles = hotelRepository.findAll();
                return hoteles.stream().map(hotel -> this.hotelToHotelDTO(hotel)).toList();
        }

        public Optional<Hotel> getHotelByNombre(String nombre)
                        throws HotelNotFoundException {
                return hotelRepository.findByNombre(nombre);
        }

        @Transactional
        public Hotel createHotel(String nombre,
                        String telefono,
                        String email,
                        String description,
                        String direccion,
                        String ciudad,
                        String pais,
                        String username,
                        String categoria,
                        List<Long> habitaciones,
                        List<HabitacionDTO> habitacionesParaCrear)
                        throws HotelDuplicateException, GestorNotFoundException,
                        CategoriaNotFoundException {

                List<Hotel> hoteles = hotelRepository.findByEmail(email);
                if (hoteles.isEmpty()) {

                        Gestor gestor = gestorRepository.findByUsername(username)
                                        .orElseThrow(() -> new GestorNotFoundException());
                        Categoria c = categoriaRepository.findByNombre(categoria)
                                        .orElseThrow(() -> new CategoriaNotFoundException());

                        Hotel hotel = new Hotel(description, direccion, ciudad, pais, gestor, c,
                                        nombre, telefono, email);

                        if (habitacionesParaCrear != null) {
                                habitacionesParaCrear.forEach(habitacion -> {
                                        List<ReservaHabitacion> r = new ArrayList<>();
                                        List<CarritoHabitacion> carr = new ArrayList<>();

                                        // Manejar reservas si existen
                                        if (habitacion.getReservas() != null) {
                                                r = habitacion.getReservas().stream()
                                                                .map(reserva -> reservaHabitacionRepository
                                                                                .findById(reserva.getId())
                                                                                .orElse(null))
                                                                .toList();
                                        }

                                        // Manejar carritos si existen
                                        if (habitacion.getCarritos() != null) {
                                                carr = habitacion.getCarritos().stream()
                                                                .map(carrito -> carritoHabitacionRepository
                                                                                .findById(carrito.getId())
                                                                                .orElse(null))
                                                                .toList();
                                        }

                                        Habitacion newHabitacion = new Habitacion(
                                                        habitacion.getTipoHabitacion(),
                                                        habitacion.getCapacidad(),
                                                        habitacion.getPrecioPorNoche(),
                                                        habitacion.getNumeroHabitacion(),
                                                        gestor,
                                                        c,
                                                        habitacion.getAmbientes(),
                                                        habitacion.getBanos(),
                                                        habitacion.getDormitorios(),
                                                        habitacion.getCamas(),
                                                        hotel,
                                                        r,
                                                        carr);
                                        newHabitacion.setHotel(hotel);
                                        hotel.getHabitaciones().add(newHabitacion);
                                });
                        }

                        return hotelRepository.save(hotel);
                }
                throw new HotelDuplicateException();
        }

        public HotelDTO hotelToHotelDTO(Hotel hotel) {
                HotelDTO hotelDTO = new HotelDTO();

                hotelDTO.setId(hotel.getId());
                hotelDTO.setNombre(hotel.getNombre());
                hotelDTO.setTelefono(hotel.getTelefono());
                hotelDTO.setEmail(hotel.getEmail());
                hotelDTO.setDescripcion(hotel.getDescripcion());
                hotelDTO.setDireccion(hotel.getDireccion());
                hotelDTO.setCiudad(hotel.getCiudad());
                hotelDTO.setPais(hotel.getPais());
                if (hotel.getHabitaciones() != null)
                        hotelDTO.setHabitaciones(hotel.getHabitaciones()
                                        .stream()
                                        .map(habitacion -> habitacionService.habitacionToHabitacionDTO(habitacion))
                                        .toList());
                if (hotel.getReviews() != null)
                        hotelDTO.setReviews(hotel.getReviews()
                                        .stream()
                                        .map(review -> review.getId())
                                        .toList());
                if (hotel.getPreguntas() != null)
                        hotelDTO.setPreguntas(hotel.getPreguntas()
                                        .stream()
                                        .map(pregunta -> pregunta.getId())
                                        .toList());
                hotelDTO.setUsername(hotel.getGestor().getUsername());
                hotelDTO.setCategoria(hotel.getCategoria().getNombre());

                return hotelDTO;
        }

        @Override
        @Transactional
        public void deleteHotel(String nombre) throws HotelNotFoundException {
                Hotel hotel = hotelRepository.findByNombre(nombre).get();
                hotelRepository.delete(hotel);
        }

        @Override
        public HotelDTO updateHotel(String nombre, HotelDTO hotel)
                        throws HotelNotFoundException, HotelDuplicateException {
                Hotel h = hotelRepository.findByNombre(nombre).orElseThrow(() -> new HotelNotFoundException());
                h.setCategoria(categoriaRepository.findByNombre(hotel.getCategoria()).get());
                h.setCiudad(hotel.getCiudad());
                h.setDescripcion(hotel.getDescripcion());
                h.setDireccion(hotel.getDireccion());
                h.setPais(hotel.getPais());
                h.setTelefono(hotel.getTelefono());
                h.setEmail(hotel.getEmail());

                h.setNombre(hotel.getNombre());
                if (hotel.getHabitacionesParaCrear() != null) {
                        hotel.getHabitacionesParaCrear().forEach(habitacion -> {
                                hotel.addHabitacion(habitacion);
                        });

                        if (!hotel.getHabitaciones().isEmpty()) {
                                h.setHabitaciones(hotel.getHabitaciones().stream()
                                                .map(ho -> habitacionRepository.findById(ho.getId()).get()).toList());
                        }
                }

                hotelRepository.save(h);
                return hotelToHotelDTO(h);
        }
}
