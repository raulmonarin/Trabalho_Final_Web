package model;

public class ContatoNaoExisteException extends Exception {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ContatoNaoExisteException() {
        super("Contato não existe.");
    }

    public ContatoNaoExisteException(String mensagem) {
        super(mensagem);
    }
}
