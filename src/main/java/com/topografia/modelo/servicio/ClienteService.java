
package com.topografia.modelo.servicio;

import com.topografia.modelo.dao.ClienteRepository;
import com.topografia.modelo.entidades.Cliente;
import java.util.List;

public class ClienteService {
    private final ClienteRepository repo = new ClienteRepository();

    public List<Cliente> listarClientes() {
        return repo.findAll();
    }

    public void guardarCliente(Cliente cliente) {
        repo.save(cliente);
    }

    public void eliminarCliente(Cliente cliente) {
        repo.delete(cliente);
    }
}
