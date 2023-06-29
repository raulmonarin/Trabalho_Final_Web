package controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dao.UsuarioDAOImpl;
import model.Usuario;

@WebServlet("/login")
public class ServletLogin extends HttpServlet {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        RequestDispatcher rd;

        Usuario usuario;
		try {
			usuario = new UsuarioDAOImpl().buscar(new Usuario(request.getParameter("nome"), request.getParameter("senha")));
			
			if (usuario.getId() != -1) {
			    HttpSession objSessao = request.getSession();
			    if (objSessao == null) {
			        objSessao = request.getSession(true);
			    }
			    objSessao.setAttribute("usuario", usuario);

			    rd = request.getRequestDispatcher("/WEB-INF/views/agenda.jsp");
			} else {
			    request.setAttribute("nome", Util.decodificar(usuario.getNome()));
			    request.setAttribute("erro", "Erro: Usuário e/ou senha inválido(s)");
			    rd = request.getRequestDispatcher("/WEB-INF/views/index.jsp");
			}

			rd.forward(request, response);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
    }
}
