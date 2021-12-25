package com.passamu.crypt;

/**
 * Exception, si le déchiffrement échoue. {@link CBC} lève cette exception, si le dernier bloc n'est pas une
 * conclusion légale d'un flux de décryptage.
 */
public final class DecryptException extends Exception {

    /**
     * Créé l'exeption
     */
    public DecryptException() {
        super("Decryption failed.");
    }
}
