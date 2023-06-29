package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import controller.ConnectionFactory;
import controller.Util;
import model.Usuario;
import model.UsuarioExisteException;

public class UsuarioDAOImpl implements UsuarioDAO {

	
	private static final String DRIVER = "com.mysql.jdbc.Driver";
    private static final String URL = "jdbc:mysql://localhost:3306/agendadb";
    private static final String USER = "root";
    private static final String PASS = "";
	
    // Create
    public void cadastrar(Usuario usuario)
            throws IllegalArgumentException, IndexOutOfBoundsException, UsuarioExisteException, ClassNotFoundException {

        if (usuario.getNome().contains(" ")) {
            throw new IllegalArgumentException("Nome de usuário contém espaços.");
        }
        if (usuario.getNome().length() > 20) {
            throw new IndexOutOfBoundsException("Nome de usuário contém mais que 20 caracteres.");
        }

        if (usuario.getSenha().contains(" ")) {
            throw new IllegalArgumentException("Senha contém espaços.");
        }
        if (usuario.getSenha().length() > 32) {
            throw new IndexOutOfBoundsException("Senha contém mais que 32 caracteres.");
        }
        
        String INSERT_USERS_SQL = "INSERT INTO usuario(nome, senha) VALUES(?, ?)";
        
        Class.forName("com.mysql.jdbc.Driver");

        try (Connection connection = DriverManager.getConnection(URL,USER,PASS);
            	PreparedStatement preparedStatement = connection.prepareStatement(INSERT_USERS_SQL)){
            preparedStatement.setString(1, usuario.getNome());
            preparedStatement.setString(2, Util.criptografar(usuario.getSenha()));

            preparedStatement.executeUpdate();
            
        	ResultSet rs = preparedStatement.getGeneratedKeys();
        	
        	if (rs.next()) {
        	    usuario.setId(rs.getInt(1));
        	}

        }catch(SQLException e) {
        	e.printStackTrace();
        }
    
    }

    // Read
    // Se o retorno do ID do usuário for -1, então o Usuário e a Senha não existem ou não coincidem.
    public Usuario buscar(Usuario usuario) throws ClassNotFoundException {

		String SELECT_USERS_SQL = "SELECT id, senha FROM usuario WHERE nome = ?";
    	
        Class.forName("com.mysql.jdbc.Driver");

        try (Connection connection = DriverManager.getConnection(URL,USER,PASS);
            	PreparedStatement preparedStatement = connection.prepareStatement(SELECT_USERS_SQL)){
        	
        	preparedStatement.setString(1, usuario.getNome());

        	ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                usuario.setId(rs.getInt("id"));
                String criptografada = rs.getString("senha");

                if (Util.verificarSenha(usuario.getSenha(), criptografada)) {
                    usuario.setSenha("");
                } else {
                    usuario.setId(-1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return usuario;
    }
}


//
//usuario.setId(-1);
//
//Connection con = ConnectionFactory.getConnection();
//PreparedStatement ps = null;
//ResultSet rs = null;
//
//try {
//    ps = con.prepareStatement("SELECT id, senha FROM usuario WHERE nome = ?");
//    ps.setString(1, usuario.getNome());
//
//    rs = ps.executeQuery();
//
//    if (rs.next()) {
//        usuario.setId(rs.getInt("id"));
//        String criptografada = rs.getString("senha");
//
//        if (Util.verificarSenha(usuario.getSenha(), criptografada)) {
//            usuario.setSenha("");
//        } else {
//            usuario.setId(-1);
//        }
//    }
//} catch (SQLException e) {
//    e.printStackTrace();
//} finally {
//    ConnectionFactory.closeConnection(con, ps, rs);
//}
//
//return usuario;
//}
//    
