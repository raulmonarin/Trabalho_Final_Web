package controller;

import java.nio.charset.StandardCharsets;
import org.mindrot.jbcrypt.BCrypt;

public class Util {

    public static String decodificar(String codificado) {
        return (new String(codificado.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8));
    }

    public static String criptografar(String senha) {
        return BCrypt.hashpw(senha, BCrypt.gensalt());
    }

    public static boolean verificarSenha(String senha, String criptografada) {
        return BCrypt.checkpw(senha, criptografada);
    }
}
