package dao;

import model.Usuario;
import model.UsuarioExisteException;

public interface UsuarioDAO {

    public void cadastrar(Usuario usuario)
            throws IllegalArgumentException, IndexOutOfBoundsException, UsuarioExisteException, ClassNotFoundException;

    public Usuario buscar(Usuario usuario) throws ClassNotFoundException;
}
