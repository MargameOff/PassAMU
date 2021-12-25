package com.passamu.crypt;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Implémente le Cipher Block Chaining Mode. En tant que chiffre, la classe {@link AES256} sera
 * utilisé.
 *
 */
public class CBC{

    /**
     * taille d'un bloc en {@code byte}s
     */
    private static final int BLOCK_SIZE = 16;

    /**
     * chifreur
     */
    private final AES256 _cipher;

    /**
     * dernier block calculé
     */
    private final byte[] _current;

    /**
     * blocage temporaire. Il ne sera utilisé que pour le décryptage.
     */
    private byte[] _buffer = null;

    /**
     * block temporaire.
     */
    private final byte[] _tmp;

    /**
     * buffer du dernier bloc de sortie. Il ne sera utilisé que pour le décryptage.
     */
    private byte[] _outBuffer = null;

    /**
     * Le Buffer de sortie est-il rempli ?
     */
    private boolean _outBufferUsed = false;

    /**
     * Buffer temporaire pour accumuler des blocs entiers de données
     */
    private final byte[] _overflow;

    /**
     * Combien de {@code byte} de {@link CBC#_overflow} sont utilisés ?
     */
    private int _overflowUsed;

    private final OutputStream _output;


    /**
     * Crée les Buffers temporaires.
     *
     * @param iv valeur initiale de {@link CBC#_tmp}
     * @param key key pour {@link CBC#_cipher}
     * @param output Flux de sortie où les données chiffrées ou déchiffrées sont écrites
     */
    public CBC(byte[] iv, byte[] key, OutputStream output) {
        this._cipher = new AES256(key);
        this._current = new byte[BLOCK_SIZE];
        System.arraycopy(iv, 0, this._current, 0, BLOCK_SIZE);
        this._tmp = new byte[BLOCK_SIZE];
        this._buffer = new byte[BLOCK_SIZE];
        this._outBuffer = new byte[BLOCK_SIZE];
        this._outBufferUsed = false;
        this._overflow = new byte[BLOCK_SIZE];
        this._overflowUsed = 0;
        this._output = output;
    }


    /**
     * Crypte un bloc.
     *
     * @param inBuffer tableau contenant le bloc d'entrée
     * @param outBuffer stockage du bloc crypté
     */
    private void encryptBlock(byte[] inBuffer, byte[] outBuffer) {
        for (int i = 0; i < BLOCK_SIZE; ++i) {
            this._current[i] ^= inBuffer[i];
        }
        this._cipher.encrypt(this._current, 0, this._current, 0);
        System.arraycopy(this._current, 0, outBuffer, 0, BLOCK_SIZE);
    }


    /**
     * Décrypte un bloc.
     *
     * @param inBuffer stockage du bloc crypté
     */
    private void decryptBlock(byte[] inBuffer) {
        System.arraycopy(inBuffer, 0, this._buffer, 0, BLOCK_SIZE);
        this._cipher.decrypt(this._buffer, 0, this._tmp, 0);
        for (int i = 0; i < BLOCK_SIZE; ++i) {
            this._tmp[i] ^= this._current[i];
            this._current[i] = this._buffer[i];
            this._outBuffer[i] = this._tmp[i];
        }
    }


    /**
     * Chiffre le tableau. L'ensemble du tableau sera crypté.
     *
     *  @param data {@code byte} qui doivent être cryptées
     * @throws IOException si l'écriture échoue
     */
    public void encrypt(byte[] data) throws IOException {
        if (data != null) {
            encrypt(data, data.length);
        }
    }


    /**
     * Décrypte le tableau. L'ensemble du tableau sera déchiffré.
     *
     * @param data {@code byte} qui doivent être déchiffrées
     * @throws IOException si l'écriture échoue
     */
    public void decrypt(byte[] data) throws IOException {
        if (data != null) {
            decrypt(data, data.length);
        }
    }


    /**
     * Crypte une partie du tableau. Seuls les {@code length} {@code byte} du tableau seront
     * crypté.
     *
     * @param data {@code byte} qui doivent être cryptées
     * @param length nombre de {@code byte} qui doivent être chiffrés
     * @throws IOException si l'écriture échoue
     */
    public void encrypt(byte[] data, int length) throws IOException {
        if (data == null || length <= 0) {
            return;
        }

        for (int i = 0; i < length; ++i) {
            this._overflow[this._overflowUsed++] = data[i];
            if (this._overflowUsed == BLOCK_SIZE) {
                encryptBlock(this._overflow, this._outBuffer);
                this._output.write(this._outBuffer);
                this._overflowUsed = 0;
            }
        }
    }


    /**
     * Décrypte une partie du tableau. Seuls les {@code length} {@code byte} du tableau seront
     * être décrypté.
     *
     * @param data {@code byte} qui doivent être déchiffrées
     * @param length nombre de {@code byte} qui doivent être déchiffrés
     * @throws IOException si l'écriture échoue
     */
    public void decrypt(byte[] data, int length) throws IOException {
        if (data == null || length <= 0) {
            return;
        }

        for (int i = 0; i < length; ++i) {
            this._overflow[this._overflowUsed++] = data[i];
            if (this._overflowUsed == BLOCK_SIZE) {
                if (this._outBufferUsed) {
                    this._output.write(this._outBuffer);
                }
                decryptBlock(this._overflow);
                this._outBufferUsed = true;
                this._overflowUsed = 0;
            }
        }
    }


    /**
     * Termine le processus de cryptage.
     *
     * @throws IOException si l'écriture échoue
     */
    public void finishEncryption() throws IOException {
        byte pad = (byte) (BLOCK_SIZE - this._overflowUsed);
        while (this._overflowUsed < BLOCK_SIZE) {
            this._overflow[this._overflowUsed++] = pad;
        }

        encryptBlock(this._overflow, this._outBuffer);
        this._output.write(this._outBuffer);
        this._output.close();
    }


    /**
     * Termine le processus de décryptage.
     *
     * @throws IOException si l'écriture échoue
     */
    public void finishDecryption() throws DecryptException, IOException {
        if (this._overflowUsed != 0) {
            throw new DecryptException();
        }
        if (!this._outBufferUsed) {
            return;
        }

        int pad = this._outBuffer[BLOCK_SIZE - 1] & 0xff;
        if (pad <= 0 || pad > BLOCK_SIZE) {
            throw new DecryptException();
        }

        int left = BLOCK_SIZE - pad;
        if (left > 0) {
            this._output.write(this._outBuffer, 0, left);
        }
        this._output.close();
    }
}
