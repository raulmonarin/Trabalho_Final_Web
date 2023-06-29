package controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.ContatoDAOImpl;
import model.Contato;
import model.ContatoNaoExisteException;
import model.Usuario;

@WebServlet("/remover")
public class ServletRemover extends HttpServlet {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");

        if (!request.getSession().isNew() && usuario != null) {
            try {
                Contato contato = new Contato(
                        Integer.parseInt(request.getParameter("id")),
                        Util.decodificar(request.getParameter("nome")),
                        Util.decodificar(request.getParameter("email")),
                        Util.decodificar(request.getParameter("telefone")));
                new ContatoDAOImpl().remover(usuario.getId(), contato);
                request.setAttribute("sucesso", "Contato removido com sucesso.");
            } catch (ContatoNaoExisteException | ClassNotFoundException e) {
                request.setAttribute("erro", "Erro: " + e.getMessage());
            }
        }

        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/agenda.jsp");
		dispatcher.forward(request, response);
    }
}
