package com.uade.tpo.marketplace.service;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uade.tpo.marketplace.entities.Carrito;
import com.uade.tpo.marketplace.entities.CarritoDepartamento;
import com.uade.tpo.marketplace.entities.Departamento;
import com.uade.tpo.marketplace.entities.dto.CarritoDepartamentoDTO;
import com.uade.tpo.marketplace.exceptions.CarritoNotFoundException;
import com.uade.tpo.marketplace.exceptions.DepartamentoNotFoundException;
import com.uade.tpo.marketplace.repository.CarritoDepartamentoRepository;

@Service
public class CarritoDepartamentoServiceImpl implements CarritoDepartamentoService {

    @Autowired
    private CarritoDepartamentoRepository carritoDepartamentoRepository;
    @Autowired
    private CarritoService carritoService;
    @Autowired
    private DepartamentoService departamentoService;

    @Override
    public List<CarritoDepartamentoDTO> getCarritosDepartamento() {
        return carritoDepartamentoRepository.findAll().stream()
                .map(this::carritoDepartamentoDTO)
                .toList();
    }

    @Override
    public List<CarritoDepartamentoDTO> getCarritosDepartamentosByUsuario(String username) throws CarritoNotFoundException {
        return carritoDepartamentoRepository.findByUsuario(username)
                .stream()
                .map(this::carritoDepartamentoDTO)
                .toList();
    }

    @Override
    public CarritoDepartamentoDTO createCarritoDepartamento(Long carritoId, Long departamentoId, String nombreReserva, int cantidad, Date checkIn, Date checkOut, Double precio) throws CarritoNotFoundException, DepartamentoNotFoundException {
            CarritoDepartamento carritoDepartamento = new CarritoDepartamento();
            Carrito carrito = carritoService.getCarritoById(carritoId);
            Optional<Departamento> departamento = departamentoService.getDepartamentoById(departamentoId);

            carritoDepartamento.setCarrito(carrito);
            carritoDepartamento.setDepartamento(departamento.get());
            carritoDepartamento.setNombreReserva(nombreReserva);
            carritoDepartamento.setCantidad(cantidad);
            carritoDepartamento.setCheckIn(checkIn);
            carritoDepartamento.setCheckOut(checkOut);
            carritoDepartamento.setPrecio(precio);
            return carritoDepartamentoDTO(carritoDepartamentoRepository.save(carritoDepartamento));
    }

    @Override
    public void deleteCarritoDepartamento(Long id) throws CarritoNotFoundException {
        if (!carritoDepartamentoRepository.existsById(id)) {
            throw new CarritoNotFoundException();
        }
        carritoDepartamentoRepository.deleteById(id);
        carritoDepartamentoRepository.flush();
    }

    @Override
    public List<CarritoDepartamentoDTO> getCarritosDepartamentosByCarritoId(Long carritoId) {
        return carritoDepartamentoRepository.findByCarritoId(carritoId)
                .stream()
                .map(this::carritoDepartamentoDTO)
                .toList();
    }

    @Override
    public List<CarritoDepartamentoDTO> getCarritosDepartamentosByDepartamentoId(Long departamentoId) {
        return carritoDepartamentoRepository.findByDepartamentoId(departamentoId)
                .stream()
                .map(this::carritoDepartamentoDTO)
                .toList();
    }

    @Override
    public CarritoDepartamentoDTO carritoDepartamentoDTO(CarritoDepartamento carritoDepartamento) {
        return new CarritoDepartamentoDTO(
                carritoDepartamento.getId(),
                carritoDepartamento.getTitularReserva(),
                carritoDepartamento.getNombreReserva(),
                carritoDepartamento.getCantidad(),
                carritoDepartamento.getCheckIn(),
                carritoDepartamento.getCheckOut(),
                carritoDepartamento.getCarrito().getId(),
                carritoDepartamento.getPrecio(),
                carritoDepartamento.getDepartamento().getId()
        );
    }

    @Override
    public CarritoDepartamentoDTO save(CarritoDepartamento carritoDepartamento) {
        CarritoDepartamento saved = carritoDepartamentoRepository.save(carritoDepartamento);
        return carritoDepartamentoDTO(saved);
    }
}
