package model;

public class UsuarioExisteException extends Exception {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UsuarioExisteException() {
        super("Usuário já existe.");
    }

    public UsuarioExisteException(String mensagem) {
        super(mensagem);
    }
}
