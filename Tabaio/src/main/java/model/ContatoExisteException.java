package model;

public class ContatoExisteException extends Exception {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ContatoExisteException() {
        super("Contato já existe.");
    }

    public ContatoExisteException(String mensagem) {
        super(mensagem);
    }
}
