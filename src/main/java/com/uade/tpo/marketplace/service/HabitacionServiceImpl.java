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

import com.uade.tpo.marketplace.entities.Categoria;
import com.uade.tpo.marketplace.entities.Gestor;
import com.uade.tpo.marketplace.entities.Habitacion;
import com.uade.tpo.marketplace.entities.Hotel;
import com.uade.tpo.marketplace.entities.Imagen;
import com.uade.tpo.marketplace.entities.dto.HabitacionDTO;
import com.uade.tpo.marketplace.enums.TipoHabitacion;
import com.uade.tpo.marketplace.exceptions.CategoriaNotFoundException;
import com.uade.tpo.marketplace.exceptions.GestorNotFoundException;
import com.uade.tpo.marketplace.exceptions.HabitacionDuplicateException;
import com.uade.tpo.marketplace.exceptions.HabitacionNotFoundException;
import com.uade.tpo.marketplace.exceptions.HotelNotFoundException;
import com.uade.tpo.marketplace.exceptions.ImagenNotFoundException;
import com.uade.tpo.marketplace.repository.HabitacionRepository;

@Service
public class HabitacionServiceImpl implements HabitacionService {
        @Autowired
        private HabitacionRepository habitacionRepository;
        @Autowired
        private GestorService gestorService;
        @Autowired
        private CategoriaService categoriaService;
        @Autowired
        private HotelService hotelService;
        @Autowired
        private ImagenService imagenService;

        @Override
        public List<HabitacionDTO> getHabitaciones() {
                List<Habitacion> habitaciones = habitacionRepository.findAll();
                return habitaciones.stream().map(habitacion -> this.habitacionToHabitacionDTO(habitacion)).toList();
        }

        @Override
        public Optional<Habitacion> getHabitacionByNombreHotelAndNumeroHabitacion(String nombreHotel,
                        String numeroHabitacion) throws HabitacionNotFoundException, HotelNotFoundException {
                Hotel hotel = hotelService.getHotelByNombre(nombreHotel)
                                .orElseThrow(() -> new HotelNotFoundException());
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

                Gestor gestor = gestorService.getGestorByUsername(username)
                                .orElseThrow(() -> new GestorNotFoundException());
                Categoria cate = categoriaService.getCategoriaByNombre(categoria)
                                .orElseThrow(() -> new CategoriaNotFoundException());
                Hotel h = hotelService.getHotelByNombre(hotel)
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
                        imagenService.save(imagen);
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
        public void deleteHabitacion(String nombreHotel, String numeroHabitacion) throws HabitacionNotFoundException,
                        HotelNotFoundException {
                Habitacion habitacion = habitacionRepository
                                .findByHotelAndNumeroHabitacion(hotelService.getHotelByNombre(nombreHotel)
                                                .orElseThrow(() -> new HotelNotFoundException()), numeroHabitacion)
                                .orElseThrow(() -> new HabitacionNotFoundException());
                habitacionRepository.delete(habitacion);
        }

        @Override
        @Transactional
        public Habitacion updateHabitacion(String nombreHotel, String numeroHabitacion, HabitacionDTO habitacionRequest, String gestor)
                        throws HabitacionNotFoundException, GestorNotFoundException, CategoriaNotFoundException, IOException,
                        ImagenNotFoundException,HotelNotFoundException {

                Habitacion habitacion = habitacionRepository
                                .findByHotelAndNumeroHabitacion(hotelService.getHotelByNombre(nombreHotel)
                                                .orElseThrow(() -> new HotelNotFoundException()), numeroHabitacion)
                                .orElseThrow(() -> new HabitacionNotFoundException());
                Gestor g = gestorService.getGestorByUsername(gestor)
                                .orElseThrow(() -> new GestorNotFoundException());
                habitacion.setTipoHabitacion(habitacionRequest.getTipoHabitacion());
                habitacion.setCapacidad(habitacionRequest.getCapacidad());
                habitacion.setPrecioPorNoche(habitacionRequest.getPrecioPorNoche());

                habitacion.setGestor(g);
                habitacion.setNumeroHabitacion(habitacionRequest.getNumeroHabitacion());
                List<Imagen> currentImages = new ArrayList<>();
                
                // Mantener imágenes existentes si se proporcionan sus IDs
                if (habitacionRequest.getImagenesIds() != null && !habitacionRequest.getImagenesIds().isEmpty()) {
                        currentImages = habitacionRequest.getImagenesIds().stream()
                                .map(imagenId -> {
                                        try {
                                                return imagenService.getImagenById(imagenId).orElseThrow(() -> new ImagenNotFoundException());
                                        } catch (ImagenNotFoundException e) {
                                                e.printStackTrace();
                                        }
                                        return null;
                                })
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
                                                return imagenService.save(newImagen);
                                        }).toList();
                        currentImages.addAll(newImages);
                }
                habitacion.setImagenes(currentImages);

                return habitacionRepository.save(habitacion);
        }

        @Override
        public List<HabitacionDTO> getHabitacionesByHotel(String nombreHotel) throws HabitacionNotFoundException, HotelNotFoundException {
                Hotel hotel = hotelService.getHotelByNombre(nombreHotel)
                                .orElseThrow(() -> new HotelNotFoundException());
                List<Habitacion> habitaciones = habitacionRepository.findByHotel(hotel);
                return habitaciones.stream().map(habitacion -> this.habitacionToHabitacionDTO(habitacion)).toList();
        }

        @Override
        public Optional<Habitacion> getHabitacionById(Long habitacionId) {
                return habitacionRepository.findById(habitacionId);
        }

        @Override
        public Optional<Habitacion> findById(Long habitacionId) {
                return habitacionRepository.findById(habitacionId);
        }
}
