package com.uade.tpo.marketplace.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

import org.hibernate.Hibernate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uade.tpo.marketplace.entities.CarritoHabitacion;
import com.uade.tpo.marketplace.entities.Categoria;
import com.uade.tpo.marketplace.entities.Gestor;
import com.uade.tpo.marketplace.entities.Habitacion;
import com.uade.tpo.marketplace.entities.Hotel;
import com.uade.tpo.marketplace.entities.Imagen;
import com.uade.tpo.marketplace.entities.ReservaHabitacion;
import com.uade.tpo.marketplace.entities.dto.CarritoHabitacionDTO;
import com.uade.tpo.marketplace.entities.dto.HabitacionDTO;
import com.uade.tpo.marketplace.entities.dto.ReservaHabitacionDTO;
import com.uade.tpo.marketplace.enums.TipoHabitacion;
import com.uade.tpo.marketplace.exceptions.CategoriaNotFoundException;
import com.uade.tpo.marketplace.exceptions.GestorNotFoundException;
import com.uade.tpo.marketplace.exceptions.HabitacionDuplicateException;
import com.uade.tpo.marketplace.exceptions.HabitacionNotFoundException;
import com.uade.tpo.marketplace.exceptions.HotelNotFoundException;
import com.uade.tpo.marketplace.exceptions.ImagenNotFoundException;
import com.uade.tpo.marketplace.repository.CarritoHabitacionRepository;
import com.uade.tpo.marketplace.repository.CategoriaRepository;
import com.uade.tpo.marketplace.repository.GestorRepository;
import com.uade.tpo.marketplace.repository.HabitacionRepository;
import com.uade.tpo.marketplace.repository.HotelRepository;
import com.uade.tpo.marketplace.repository.ImagenRepository;
import com.uade.tpo.marketplace.repository.ReservaHabitacionRepository;

@Service
public class HabitacionServiceImpl implements HabitacionService {
        @Autowired
        private HabitacionRepository habitacionRepository;
        @Autowired
        private GestorRepository gestorRepository;
        @Autowired
        private CategoriaRepository categoriaRepository;
        @Autowired
        private HotelRepository hotelRepository;
        @Autowired
        GestorService gestorService;
        @Autowired
        CategoriaService categoriaService;
        @Autowired
        ReservaHabitacionRepository reservaHabitacionRepository;
        @Autowired
        CarritoHabitacionRepository carritoHabitacionRepository;
        @Autowired
        ImagenRepository imagenRepository;

        @Override
        public List<HabitacionDTO> getHabitaciones() {
                List<Habitacion> habitaciones = habitacionRepository.findAll();
                return habitaciones.stream().map(habitacion -> this.habitacionToHabitacionDTO(habitacion)).toList();
        }

        @Override
        public Optional<Habitacion> getHabitacionByNombreHotelAndNumeroHabitacion(String nombreHotel,
                        String numeroHabitacion) throws HabitacionNotFoundException {
                Hotel hotel = hotelRepository.findByNombre(nombreHotel)
                                .orElseThrow(() -> new HabitacionNotFoundException());
                return habitacionRepository.findByHotelAndNumeroHabitacion(hotel, numeroHabitacion);
        }

        @Transactional
        public Habitacion createHabitacion(
                        TipoHabitacion tipoHabitacion,
                        int capacidad,
                        double precioPorNoche,
                        String numeroHabitacion,
                        List<MultipartFile> imagenes,
                        String username,
                        String categoria,
                        int ambientes,
                        int banos,
                        int dormitorios,
                        int camas,
                        String hotel) throws GestorNotFoundException, CategoriaNotFoundException,
                        HotelNotFoundException, HabitacionDuplicateException, IOException {

                Gestor gestor = gestorRepository.findByUsername(username)
                                .orElseThrow(() -> new GestorNotFoundException());
                Categoria cate = categoriaRepository.findByNombre(categoria)
                                .orElseThrow(() -> new CategoriaNotFoundException());
                Hotel h = hotelRepository.findByNombre(hotel)
                                .orElseThrow(() -> new HotelNotFoundException());
                if (habitacionRepository.findByHotelAndNumeroHabitacion(h, numeroHabitacion).isPresent()) {
                        throw new HabitacionDuplicateException();
                }
                List<Imagen> imagenesHabitacion = imagenes.stream()
                                .map(imagen -> {
                                        Imagen newImagen = new Imagen();
                                        try {
                                                newImagen.setImagen(Base64.getEncoder().encodeToString(imagen.getBytes()));
                                        } catch (IOException e) {
                                                e.printStackTrace();
                                        }
                                        return newImagen;
                                }).toList();
                imagenesHabitacion.forEach(imagen -> imagen.setAlojamiento(h));
                Habitacion habitacion = new Habitacion(
                                tipoHabitacion,
                                capacidad,
                                precioPorNoche,
                                numeroHabitacion,
                                gestor,
                                cate,
                                ambientes,
                                banos,
                                dormitorios,
                                camas,
                                h,
                                new ArrayList<>(),
                                new ArrayList<>(),
                                imagenesHabitacion);
                Habitacion savedHabitacion = habitacionRepository.save(habitacion);
                for (Imagen imagen : imagenesHabitacion) {
                        imagen.setHabitacion(savedHabitacion);
                        imagenRepository.save(imagen);
                }

                return savedHabitacion;
        }

        @Override
        @Transactional(readOnly = true)
        public HabitacionDTO habitacionToHabitacionDTO(Habitacion habitacion) {
                if (habitacion == null) {
                        return null;
                }

                return HabitacionDTO.builder()
                                .id(habitacion.getId())
                                .tipoHabitacion(habitacion.getTipoHabitacion())
                                .capacidad(habitacion.getCapacidad())
                                .precioPorNoche(habitacion.getPrecioPorNoche())
                                .numeroHabitacion(habitacion.getNumeroHabitacion())
                                .ambientes(habitacion.getAmbientes())
                                .banos(habitacion.getBanos())
                                .dormitorios(habitacion.getDormitorios())
                                .camas(habitacion.getCamas())
                                // Solo enviar IDs de imágenes, no las imágenes completas
                                .imagenesIds(habitacion.getImagenes() != null
                                                && Hibernate.isInitialized(habitacion.getImagenes())
                                                                ? habitacion.getImagenes().stream()
                                                                                .map(imagen -> imagen.getId())
                                                                                .toList()
                                                                : null)
                                .gestor(habitacion.getGestor() != null ? habitacion.getGestor().getUsername() : null)
                                .categoria(habitacion.getCategoria() != null ? habitacion.getCategoria().getNombre()
                                                : null)
                                .hotel(habitacion.getHotel() != null ? habitacion.getHotel().getNombre() : null)
                                .build();
        }

        @Override
        @Transactional
        public void deleteHabitacion(String nombreHotel, String numeroHabitacion) throws HabitacionNotFoundException {
                Habitacion habitacion = habitacionRepository
                                .findByHotelAndNumeroHabitacion(hotelRepository.findByNombre(nombreHotel)
                                                .orElseThrow(() -> new HabitacionNotFoundException()), numeroHabitacion)
                                .orElseThrow(() -> new HabitacionNotFoundException());
                habitacionRepository.delete(habitacion);
        }

        @Override
        @Transactional
        public Habitacion updateHabitacion(String nombreHotel, String numeroHabitacion, HabitacionDTO habitacionRequest, String gestor)
                        throws HabitacionNotFoundException, GestorNotFoundException, CategoriaNotFoundException,
                        ImagenNotFoundException, IOException {

                Habitacion habitacion = habitacionRepository
                                .findByHotelAndNumeroHabitacion(hotelRepository.findByNombre(nombreHotel)
                                                .orElseThrow(() -> new HabitacionNotFoundException()), numeroHabitacion)
                                .orElseThrow(() -> new HabitacionNotFoundException());
                Gestor g = gestorRepository.findByUsername(gestor)
                                .orElseThrow(() -> new GestorNotFoundException());
                Categoria categoria = categoriaRepository.findByNombre(habitacionRequest.getCategoria())
                                .orElseThrow(() -> new CategoriaNotFoundException());
                habitacion.setTipoHabitacion(habitacionRequest.getTipoHabitacion());
                habitacion.setCapacidad(habitacionRequest.getCapacidad());
                habitacion.setPrecioPorNoche(habitacionRequest.getPrecioPorNoche());

                habitacion.setGestor(g);
                habitacion.setNumeroHabitacion(habitacionRequest.getNumeroHabitacion());
                habitacion.setCategoria(categoria);
                List<Imagen> currentImages = new ArrayList<>();
                
                // Mantener imágenes existentes si se proporcionan sus IDs
                if (habitacionRequest.getImagenesIds() != null && !habitacionRequest.getImagenesIds().isEmpty()) {
                        currentImages = habitacionRequest.getImagenesIds().stream()
                                .map(imagenId -> imagenRepository.findById(imagenId).orElse(null))
                                .filter(imagen -> imagen != null)
                                .toList();
                }
                
                // Agregar nuevas imágenes
                if (habitacionRequest.getImagenesNuevas() != null && !habitacionRequest.getImagenesNuevas().isEmpty()) {
                        List<Imagen> newImages = habitacionRequest.getImagenesNuevas().stream()
                                        .map(imagen -> {
                                                Imagen newImagen = new Imagen();
                                                try {
                                                        newImagen.setImagen(Base64.getEncoder().encodeToString(imagen.getBytes()));
                                                } catch (IOException e) {
                                                        e.printStackTrace();
                                                }
                                                return imagenRepository.save(newImagen);
                                        }).toList();
                        currentImages.addAll(newImages);
                }
                habitacion.setImagenes(currentImages);

                return habitacionRepository.save(habitacion);
        }

        @Override
        public List<HabitacionDTO> getHabitacionesByHotel(String nombreHotel) throws HabitacionNotFoundException {
                Hotel hotel = hotelRepository.findByNombre(nombreHotel)
                                .orElseThrow(() -> new HabitacionNotFoundException());
                List<Habitacion> habitaciones = habitacionRepository.findByHotel(hotel);
                return habitaciones.stream().map(habitacion -> this.habitacionToHabitacionDTO(habitacion)).toList();
        }

        @Override
        public Optional<Habitacion> getHabitacionById(Long habitacionId) {
                return habitacionRepository.findById(habitacionId);
        }
}
