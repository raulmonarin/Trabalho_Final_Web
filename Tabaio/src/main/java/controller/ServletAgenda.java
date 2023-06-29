package controller;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.ContatoDAOImpl;
import model.Contato;
import model.Usuario;

@WebServlet("/agenda")
public class ServletAgenda extends HttpServlet {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
    protected void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");

        ArrayList<Contato> contatos = null;
        if (!request.getSession().isNew() && usuario != null) {
			try {
				contatos = new ContatoDAOImpl().carregar(usuario);
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            if (!contatos.isEmpty()) {
                request.setAttribute("contatos", contatos);
            } else {
                request.setAttribute("agendavazia", "A agenda ainda não possui contatos.");
            }
        }
        request.getRequestDispatcher("/WEB-INF/views/agenda.jsp").forward(request, response);
    }
}
