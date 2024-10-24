package logicaalmacenamiento;

import logicabancaria.Usuario;
import java.util.ArrayList;
import java.util.List;

public class UsuarioManager {
    private static UsuarioManager instancia;
    private List<Usuario> usuarios;

    private UsuarioManager() {
        usuarios = new ArrayList<>();
    }

    public static UsuarioManager getInstancia() {
        if (instancia == null) {
            instancia = new UsuarioManager();
        }
        return instancia;
    }

    public void agregar(Usuario usuario) {
        usuarios.add(usuario);
    }

    public Usuario buscarUsuarioPorId(String id) {
        for (Usuario usuario : usuarios) {
            if (usuario.getId().equals(id)) {
                return usuario;
            }
        }
        return null;
    }
    
    public List<Usuario> getUsuarios() {
    return usuarios;
    }

}