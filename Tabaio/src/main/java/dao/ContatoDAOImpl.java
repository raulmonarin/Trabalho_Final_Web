package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import controller.ConnectionFactory;
import model.Contato;
import model.ContatoExisteException;
import model.ContatoNaoExisteException;
import model.Usuario;

public class ContatoDAOImpl implements ContatoDAO {
	
	private static final String DRIVER = "com.mysql.jdbc.Driver";
    private static final String URL = "jdbc:mysql://localhost:3306/agendadb";
    private static final String USER = "root";
    private static final String PASS = "";
	
    // Create
    @Override
    public void cadastrar(int usuario_id, Contato contato)
            throws IndexOutOfBoundsException, IllegalArgumentException, ContatoExisteException, ClassNotFoundException {

        if (contato.getNome().length() > 50) {
            throw new IndexOutOfBoundsException("Nome do contato contém mais que 50 caracteres.");
        }

        if (contato.getEmail().contains(" ")) {
            throw new IllegalArgumentException("Email do contato contém espaços.");
        }
        if (contato.getEmail().length() > 70) {
            throw new IndexOutOfBoundsException("Email do contato contém mais que 70 caracteres.");
        }

        if (contato.getTelefone().contains(" ")) {
            throw new IllegalArgumentException("Telefone do contato contém espaços.");
        }
        if (contato.getTelefone().length() > 13) {
            throw new IndexOutOfBoundsException("Telefone do contato contém mais que 13 caracteres.");
        }

        if (contatoExiste(usuario_id, contato)) {
            throw new ContatoExisteException();
        }
        
        String INSERT_CONTATOS_SQL = "INSERT INTO contato(nome, email, telefone, usuario_id) VALUES(?, ?, ?, ?)";
        
        Class.forName("com.mysql.jdbc.Driver");

        try (Connection connection = DriverManager.getConnection(URL,USER,PASS);
            	PreparedStatement preparedStatement = connection.prepareStatement(INSERT_CONTATOS_SQL)){
        	preparedStatement.setString(1, contato.getNome());
        	preparedStatement.setString(2, contato.getEmail());
        	preparedStatement.setString(3, contato.getTelefone());
        	preparedStatement.setInt(4, usuario_id);

        	preparedStatement.executeUpdate();
        	
        	ResultSet rs = preparedStatement.getGeneratedKeys();
        	if (rs.next()) {
        	    contato.setId(rs.getInt(1));
        	}
        	
        }catch(SQLException e) {
        	e.printStackTrace();
        }
    }
    // Read
    @Override
    public ArrayList<Contato> carregar(Usuario usuario) throws ClassNotFoundException {

        ArrayList<Contato> contatos = new ArrayList<>();

        String SELECT_ID_CONTATO = "SELECT * FROM contato WHERE usuario_id = ?";
        
        Class.forName("com.mysql.jdbc.Driver");

        try (Connection connection = DriverManager.getConnection(URL,USER,PASS);
            	PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ID_CONTATO)){
        	preparedStatement.setInt(1, usuario.getId());
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                Contato contato = new Contato(rs.getInt("id"), rs.getString("nome"), rs.getString("email"), rs.getString("telefone"));
                contatos.add(contato);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } 

        return contatos;
    }

    // A ideia aqui é checar se o contato existe pelos seus parâmetros.
    @Override
    public boolean contatoExiste(int usuario_id, Contato contato) throws ClassNotFoundException {
    	
        String SELECT_CONTATOS_SQL = "SELECT id FROM contato WHERE nome = ? and email = ? and telefone = ? and usuario_id = ?";

        boolean existe = false;
  
        Class.forName("com.mysql.jdbc.Driver");

        try (Connection connection = DriverManager.getConnection(URL,USER,PASS);
            	PreparedStatement preparedStatement = connection.prepareStatement(SELECT_CONTATOS_SQL)){
        	preparedStatement.setString(1, contato.getNome());
        	preparedStatement.setString(2, contato.getEmail());
        	preparedStatement.setString(3, contato.getTelefone());
        	preparedStatement.setInt(4, usuario_id);

            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                existe = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return existe;
    }

    // Update
    @Override
    public void alterar(int usuario_id, Contato contato)
            throws IndexOutOfBoundsException, IllegalArgumentException, ContatoExisteException, ContatoNaoExisteException, ClassNotFoundException {

        if (contato.getNome().length() > 50) {
            throw new IndexOutOfBoundsException("Novo nome do contato contém mais que 50 caracteres.");
        }

        if (contato.getEmail().contains(" ")) {
            throw new IllegalArgumentException("Novo email do contato contém espaços.");
        }
        if (contato.getEmail().length() > 70) {
            throw new IndexOutOfBoundsException("Novo email do contato contém mais que 70 caracteres.");
        }

        if (contato.getTelefone().contains(" ")) {
            throw new IllegalArgumentException("Novo telefone do contato contém espaços.");
        }
        if (contato.getTelefone().length() > 13) {
            throw new IndexOutOfBoundsException("Novo telefone do contato contém mais que 13 caracteres.");
        }

        if (contatoExiste(usuario_id, contato)) {
            throw new ContatoExisteException();
        }
        
        String UPDATE_CONTATOS_SQL = "UPDATE contato SET nome = ?, email = ?, telefone = ? WHERE id = ? AND usuario_id = ?";

        Class.forName("com.mysql.jdbc.Driver");


        try (Connection connection = DriverManager.getConnection(URL,USER,PASS);
            	PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_CONTATOS_SQL)){
        	preparedStatement.setString(1, contato.getNome());
        	preparedStatement.setString(2, contato.getEmail());
        	preparedStatement.setString(3, contato.getTelefone());
        	preparedStatement.setInt(4, contato.getId());
        	preparedStatement.setInt(5, usuario_id);

            if (preparedStatement.executeUpdate() == 0) {
                throw new ContatoNaoExisteException();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } 
    }

    // Delete
    @Override
    public void remover(int usuario_id, Contato contato)
            throws ContatoNaoExisteException, ClassNotFoundException {

    	String UPDATE_CONTATOS_SQL = "DELETE FROM contato WHERE id = ? AND usuario_id = ?";

        Class.forName("com.mysql.jdbc.Driver");

        try (Connection connection = DriverManager.getConnection(URL,USER,PASS);
            	PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_CONTATOS_SQL)){
        	preparedStatement.setInt(1, contato.getId());
        	preparedStatement.setInt(2, usuario_id);

            if (preparedStatement.executeUpdate() == 0) {
                throw new ContatoNaoExisteException();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } 
    }
}
