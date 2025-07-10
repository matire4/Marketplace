package com.uade.tpo.marketplace.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.uade.tpo.marketplace.entities.CarritoHabitacion;
import com.uade.tpo.marketplace.entities.Categoria;
import com.uade.tpo.marketplace.entities.Gestor;
import com.uade.tpo.marketplace.entities.Habitacion;
import com.uade.tpo.marketplace.entities.Hotel;
import com.uade.tpo.marketplace.entities.Imagen;
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
        private HabitacionRepository habitacionRepository;
        @Autowired
        private HabitacionService habitacionService;
        @Autowired
        private ReservaHabitacionRepository reservaHabitacionRepository;
        @Autowired
        private CarritoHabitacionRepository carritoHabitacionRepository;
        @Autowired
        private ImagenRepository imagenRepository;

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
                        List<HabitacionDTO> habitacionesParaCrear,
                        List<MultipartFile> imagenesNuevas)
                        throws HotelDuplicateException, GestorNotFoundException,
                        CategoriaNotFoundException, IOException {

                List<Hotel> hoteles = hotelRepository.findByEmail(email);
                if (hoteles.isEmpty()) {

                        Gestor gestor = gestorRepository.findByUsername(username)
                                        .orElseThrow(() -> new GestorNotFoundException());
                        Categoria c = categoriaRepository.findByNombre(categoria)
                                        .orElseThrow(() -> new CategoriaNotFoundException());
                        List<Imagen> imagenes = new ArrayList<>();
                        if (imagenesNuevas != null && !imagenesNuevas.isEmpty()) {
                                for (MultipartFile imagen : imagenesNuevas) {
                                        Imagen newImagen = new Imagen();
                                        try {
                                                newImagen.setImagen(
                                                                Base64.getEncoder().encodeToString(imagen.getBytes()));
                                        } catch (IOException e) {
                                                e.printStackTrace();
                                        }
                                        imagenes.add(imagenRepository.save(newImagen));
                                }
                        }

                        Hotel hotel = new Hotel(description, direccion, ciudad, pais, gestor, c, imagenes,
                                        nombre, telefono, email);

                        Hotel savedHotel = hotelRepository.save(hotel);

                        // Asociar las imágenes con el hotel guardado
                        for (Imagen imagen : imagenes) {
                                imagen.setAlojamiento(savedHotel);
                                imagenRepository.save(imagen);
                        }

                        if (habitacionesParaCrear != null) {
                                habitacionesParaCrear.forEach(habitacion -> {
                                        List<ReservaHabitacion> r = new ArrayList<>();
                                        List<CarritoHabitacion> carr = new ArrayList<>();

                                        if (habitacion.getReservas() != null) {
                                                r = habitacion.getReservas().stream()
                                                                .map(reserva -> reservaHabitacionRepository
                                                                                .findById(reserva.getId())
                                                                                .orElse(null))
                                                                .toList();
                                        }

                                        if (habitacion.getCarritos() != null) {
                                                carr = habitacion.getCarritos().stream()
                                                                .map(carrito -> carritoHabitacionRepository
                                                                                .findById(carrito.getId())
                                                                                .orElse(null))
                                                                .toList();
                                        }
                                        List<Imagen> imagenesHabitacion = new ArrayList<>();
                                        if (habitacion.getImagenesNuevas() != null
                                                        && !habitacion.getImagenesNuevas().isEmpty()) {
                                                for (MultipartFile imagen : habitacion.getImagenesNuevas()) {
                                                        Imagen newImagen = new Imagen();
                                                        try {
                                                                newImagen.setImagen(Base64.getEncoder()
                                                                                .encodeToString(imagen.getBytes()));
                                                        } catch (IOException e) {
                                                                e.printStackTrace();
                                                        }
                                                        imagenesHabitacion.add(imagenRepository.save(newImagen));
                                                }
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
                                                        savedHotel,
                                                        r,
                                                        carr,
                                                        imagenesHabitacion);

                                        Habitacion savedHabitacion = habitacionRepository.save(newHabitacion);

                                        // Asociar las imágenes con la habitación guardada
                                        for (Imagen imagen : imagenesHabitacion) {
                                                imagen.setHabitacion(savedHabitacion);
                                                imagenRepository.save(imagen);
                                        }

                                        savedHotel.getHabitaciones().add(savedHabitacion);
                                });
                        }

                        return savedHotel;
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
                // Solo enviar IDs de imágenes, no las imágenes completas
                hotelDTO.setImagenesIds(hotel.getImagenes().stream().map(i -> i.getId()).toList());
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
        public HotelDTO updateHotel(Long id, HotelDTO hotel)
                        throws HotelNotFoundException, HotelDuplicateException {
                Hotel h = hotelRepository.findById(id).orElseThrow(() -> new HotelNotFoundException());
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

                List<Imagen> currentImages = new ArrayList<>();

                // Mantener imágenes existentes si se proporcionan sus IDs
                if (hotel.getImagenesIds() != null && !hotel.getImagenesIds().isEmpty()) {
                        currentImages = hotel.getImagenesIds().stream()
                                        .map(imagenId -> imagenRepository.findById(imagenId).orElse(null))
                                        .filter(imagen -> imagen != null)
                                        .toList();
                }

                // Agregar nuevas imágenes
                if (hotel.getImagenesNuevas() != null && !hotel.getImagenesNuevas().isEmpty()) {
                        List<Imagen> newImages = hotel.getImagenesNuevas().stream()
                                        .map(imagen -> {
                                                Imagen newImagen = new Imagen();
                                                try {
                                                        newImagen.setImagen(Base64.getEncoder()
                                                                        .encodeToString(imagen.getBytes()));
                                                } catch (IOException e) {
                                                        e.printStackTrace();
                                                }
                                                return imagenRepository.save(newImagen);
                                        }).toList();
                        currentImages.addAll(newImages);
                }
                h.setImagenes(currentImages);

                hotelRepository.save(h);
                return hotelToHotelDTO(h);
        }

        @Override
        public Optional<Hotel> findById(Long id) {
                return hotelRepository.findById(id);
        }

        @Override
        public Optional<Hotel> getHoytelById(Long id) {
                // TODO Auto-generated method stub
                return Optional.empty();
        }
}
