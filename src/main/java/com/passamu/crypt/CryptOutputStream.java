package com.passamu.crypt;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;

import com.passamu.crypt.CBC;
import com.passamu.utils.CryptUtils;

/**
 * Crypte les données transmises et les stocke dans le {@link java.io.OutputStream} sous-jacent. Sinon
 * le vecteur initial est fourni dans le constructeur, le chiffrement sera initialisé avec des données aléatoires
 * et ces données seront envoyées directement au flux sous-jacent.
 */
public class CryptOutputStream extends OutputStream {

    /**
     * Chiffreur
     */
    private final CBC _cipher;

    /**
     * Buffer pour l'envoi de {@code byte} uniques.
     */
    private final byte[] _buffer = new byte[1];

    /**
     * Initialise le chiffrement avec la clé et les valeurs initiales données.
     *
     * @param parent sous-jacent {@link java.io.OutputStream}
     * @param key key pour l'algorithme de chiffrement
     * @param iv valeurs initiales pour le schéma CBC
     */
    public CryptOutputStream(OutputStream parent, byte[] key, byte[] iv) {
        this._cipher = new CBC(iv, key, parent);
    }

    /**
     * Initialise le chiffrement avec la clé donnée. Les valeurs initiales pour le schéma CBC seront
     * aléatoire et envoyé au flux sous-jacent.
     *
     * @param parent sous-jacent {@link java.io.OutputStream}
     * @param key key pour l'algorithme de chiffrement
     * @throws IOException si les valeurs initiales ne peuvent pas être écrites dans le flux sous-jacent
     */
    public CryptOutputStream(OutputStream parent, byte[] key)
            throws IOException {
        byte[] iv = new byte[16];
        Random rnd = CryptUtils.newRandomNumberGenerator();
        rnd.nextBytes(iv);
        parent.write(iv);

        this._cipher = new CBC(iv, key, parent);
    }



    /**
     * Crypte un seul {@code byte}.
     *
     * @param b {@code byte} à chiffrer
     * @throws IOException si les données chiffrées ne peuvent pas être écrites dans le flux sous-jacent
     */
    @Override
    public void write(int b) throws IOException {
        this._buffer[0] = (byte) b;
        this._cipher.encrypt(this._buffer);
    }


    /**
     * Crypte un tableau de {@code byte}.
     *
     * @param b tableau de {@code byte} à chiffrer
     * @throws IOException si les données chiffrées ne peuvent pas être écrites dans le flux sous-jacent
     */
    @Override
    public void write(byte[] b) throws IOException {
        this._cipher.encrypt(b);
    }


    /**
     * Finalise le cryptage et ferme le flux sous-jacent.
     *
     * @throws IOException si le cryptage échoue ou si les données cryptées ne peuvent pas être écrites sur le
     * flux sous-jacent
     */
    @Override
    public void close() throws IOException {
        this._cipher.finishEncryption();
    }
}
