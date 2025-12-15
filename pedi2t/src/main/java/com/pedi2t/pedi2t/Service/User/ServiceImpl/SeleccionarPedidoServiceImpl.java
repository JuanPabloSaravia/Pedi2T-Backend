package com.pedi2t.pedi2t.Service.User.ServiceImpl;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.pedi2t.pedi2t.DTO.SeleccPedidoResponseDTO;
import com.pedi2t.pedi2t.DTO.SeleccionarPedidoDTO;
import com.pedi2t.pedi2t.Entity.MenuDiaEntity;
import com.pedi2t.pedi2t.Entity.PedidoDia;
import com.pedi2t.pedi2t.Entity.PedidoEntity;
import com.pedi2t.pedi2t.Entity.PlatoEntity;
import com.pedi2t.pedi2t.Entity.UsuarioEntity;
import com.pedi2t.pedi2t.Enum.EstadoPedido;
import com.pedi2t.pedi2t.Repository.MenuDiaRepository;
import com.pedi2t.pedi2t.Repository.PedidoDiaRepository;
import com.pedi2t.pedi2t.Repository.PedidoRepository;
import com.pedi2t.pedi2t.Repository.PlatoRepository;
import com.pedi2t.pedi2t.Repository.UsuarioRepository;
import com.pedi2t.pedi2t.Service.User.SeleccionarPedidoService;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class SeleccionarPedidoServiceImpl implements SeleccionarPedidoService {


    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PlatoRepository platoRepository;
    
    @Autowired
    private PedidoRepository pedidoRepository;
    
    @Autowired
    private MenuDiaRepository menuDiaRepository;
    
    @Autowired
    private PedidoDiaRepository pedidoDiaRepository;

    @Override
    @SuppressWarnings("null")
    public SeleccPedidoResponseDTO seleccionarPedido(SeleccionarPedidoDTO seleccionarPedidoDTO) {
        
        Long idUsuario = seleccionarPedidoDTO.getIdUsuario();
        
        if (idUsuario == null) {
            throw new IllegalArgumentException("El idUsuario no puede ser nulo");
        }

        Optional<UsuarioEntity> usuarioOpt = usuarioRepository.findById(idUsuario);
        if (usuarioOpt.isEmpty()) {
            throw new IllegalArgumentException("Usuario no encontrado para id: " + idUsuario);
        }

        Long idPlato = seleccionarPedidoDTO.getIdPlato();
        if (idPlato == null) {
            throw new IllegalArgumentException("El idPlato no puede ser nulo");
        }    
        
        // Verificar que el plato existe
        Optional<PlatoEntity> platoOpt = platoRepository.findById(idPlato);
        if (platoOpt.isEmpty()) {
            throw new IllegalArgumentException("Plato no encontrado para id: " + idPlato);
        }
        
        // Obtener las entidades
        UsuarioEntity usuario = usuarioOpt.get();
        PlatoEntity plato = platoOpt.get();
        
        // Buscar el menú del día por ID (más directo y confiable)
        Long idMenuDia = seleccionarPedidoDTO.getIdMenuDia();
        if (idMenuDia == null) {
            throw new IllegalArgumentException("El idMenuDia no puede ser nulo");
        }
        
        Optional<MenuDiaEntity> menuDiaOpt = menuDiaRepository.findById(idMenuDia);
        if (menuDiaOpt.isEmpty()) {
            throw new IllegalArgumentException("Menú del día no encontrado para ID: " + idMenuDia);
        }
        
        MenuDiaEntity menuDia = menuDiaOpt.get();
        
        // Verificar que el menú esté publicado
        if (!menuDia.getPublicado()) {
            throw new IllegalArgumentException("El menú no está disponible para selección");
        }
        
        // Verificar que hay stock disponible
        if (menuDia.getStockTotal() <= 0) {
            throw new IllegalArgumentException("No hay stock disponible para este menú");
        }
        
        // Reducir el stock en 1
        menuDia.setStockTotal(menuDia.getStockTotal() - 1);
        menuDiaRepository.save(menuDia);
        
        // Crear y configurar el nuevo pedido
        PedidoEntity nuevoPedido = new PedidoEntity();
        nuevoPedido.setUsuario(usuario);
        nuevoPedido.setEstado(EstadoPedido.PENDIENTE);
        
        // Guardar el pedido
        PedidoEntity pedidoGuardado = pedidoRepository.save(nuevoPedido);

        // Calcular la fecha de entrega usando FechaUtil
        LocalDate fechaEntrega = FechaUtil.calcularSiguienteFecha(seleccionarPedidoDTO.getDiaEntrega());

        // Crear la relación PedidoDia
        PedidoDia pedidoDia = new PedidoDia();
        pedidoDia.setFechaEntrega(fechaEntrega);
        pedidoDia.setPedidoEntity(pedidoGuardado);
        pedidoDia.setMenuDia(menuDia);
        pedidoDia.setPlato(plato);
        
        // Guardar la relación PedidoDia
        pedidoDiaRepository.save(pedidoDia);

        SeleccPedidoResponseDTO pedidoResponseDTO = new SeleccPedidoResponseDTO();
        pedidoResponseDTO.setIdPedido(pedidoGuardado.getId());
        pedidoResponseDTO.setFechaEntrega(fechaEntrega);

        return pedidoResponseDTO;
    }
}
