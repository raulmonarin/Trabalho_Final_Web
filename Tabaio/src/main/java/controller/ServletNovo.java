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
import model.ContatoExisteException;
import model.Usuario;

@WebServlet("/novo")
public class ServletNovo extends HttpServlet {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private void erro(HttpServletRequest request, String erro) {
        request.setAttribute("erro", "Erro: " + erro);
        request.setAttribute("nome", Util.decodificar(request.getParameter("nome")));
        request.setAttribute("email", Util.decodificar(request.getParameter("email")));
        request.setAttribute("telefone", Util.decodificar(request.getParameter("telefone")));
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    	RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/novo.jsp");
		dispatcher.forward(request, response);
//        response.sendRedirect("/WEB-INF/views/novo.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");

        if (!request.getSession().isNew() && usuario != null) {
            try {
                Contato contato = new Contato(Util.decodificar(request.getParameter("nome")), Util.decodificar(request.getParameter("email")), Util.decodificar(request.getParameter("telefone")));
                new ContatoDAOImpl().cadastrar(usuario.getId(), contato);
                request.setAttribute("sucesso", "Novo contato cadastrado com sucesso.");
            } catch (IndexOutOfBoundsException | IllegalArgumentException | ContatoExisteException | ClassNotFoundException e) {
                erro(request, e.getMessage());
            }
        }

        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/novo.jsp");
		dispatcher.forward(request, response);
//        request.getRequestDispatcher("/WEB-INF/views/novo.jsp").forward(request, response);
    }
}
