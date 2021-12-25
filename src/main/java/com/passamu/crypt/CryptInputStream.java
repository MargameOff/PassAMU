package com.passamu.crypt;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;



/**
 * Lit à partir d'un {@link java.io.InputStream} crypté et fournit les données décryptées.
 * la clé de cryptage est fournie avec le constructeur. Le vecteur d'initialisation peut également être fourni.
 */
public class CryptInputStream extends InputStream {

    /**
     * Taille maximale des données qui seront lues à partir du flux sous-jacent.
     */
    private static final int FETCH_BUFFER_SIZE = 32;

    /**
     * Flux sous-jacent qui fournit les données cryptées.
     */

    private final InputStream _parent;

    /**
     * Chiffreur
     */
    private final CBC _cipher;

    /**
     * Dechifreur
     */
    private final ByteArrayOutputStream _decrypted;

    /**
     * Buffer de données non cryptées. Si le Buffer est complètement retourné, un autre bloc de données sera
     * être décrypté.
     */
    private byte[] _buffer = null;

    /**
     * Nombre de {@code byte} déjà renvoyés par {@link CryptInputStream#_buffer}.
     */
    private int _bufferUsed = 0;

    /**
     * Buffer pour stocker les données cryptées.
     */
    private final byte[] _fetchBuffer = new byte[FETCH_BUFFER_SIZE];

    /**
     * Signaux, si les dernières données cryptées ont été lues. Si nous manquons de tampons, le flux est à son
     * finir.
     */
    private boolean _lastBufferRead = false;

    /**
     * Crée un chiffrement avec la clé et iv fournis.
     *
     * @param parent Stream qui fournit les données cryptées
     * @param key key pour l'algorithme de chiffrement
     * @param iv valeurs initiales pour le schéma CBC
     */
    public CryptInputStream(InputStream parent, byte[] key, byte[] iv) {
        this._parent = parent;
        this._decrypted = new ByteArrayOutputStream();
        this._cipher = new CBC(iv, key, this._decrypted);
    }

    /**
     * Crée un chiffrement avec la clé. L'iv sera lu à partir du flux {@code parent}. S'il y a
     * pas assez de {@code byte} dans le flux, une {@link java.io.IOException} sera levée.
     *
     * @param parent Stream qui fournit les données cryptées
     * @param key key pour l'algorithme de chiffrement
     * @throws IOException si le iv ne peut pas être lu
     */
    public CryptInputStream(InputStream parent, byte[] key) throws IOException {
        this._parent = parent;
        byte[] iv = new byte[16];
        int readed = 0;
        while (readed < 16) {
            int cur = parent.read(iv, readed, 16 - readed);
            if (cur < 0) {
                throw new IOException("No initial values in stream.");
            }
            readed += cur;
        }
        this._decrypted = new ByteArrayOutputStream();
        this._cipher = new CBC(iv, key, this._decrypted);
    }

    /**
     * Essaie de lire les prochaines données déchiffrées du flux de sortie
     */
    private void readFromStream() {
        if (this._decrypted.size() > 0) {
            this._buffer = this._decrypted.toByteArray();
            this._decrypted.reset();
        }
    }

    /**
     * Renvoie le {@code byte} déchiffré suivant. S'il n'y a plus de données, {@code -1} sera
     * retourné. Si le déchiffrement échoue ou si le flux sous-jacent lance un
     * {@link java.io.IOException}, une {@link java.io.IOException} sera lancée.
     *
     * @return déchiffré {@code byte} ou {@code -1}
     * @throws IOException si le déchiffrement échoue ou si le flux sous-jacent lève une exception
     */
    @Override
    public int read() throws IOException {
        while (this._buffer == null || this._bufferUsed >= this._buffer.length) {
            if (this._lastBufferRead) {
                return -1;
            }

            this._bufferUsed = 0;
            this._buffer = null;

            int readed = this._parent.read(this._fetchBuffer, 0, FETCH_BUFFER_SIZE);
            if (readed < 0) {
                this._lastBufferRead = true;
                try {
                    this._cipher.finishDecryption();
                    readFromStream();
                } catch (DecryptException ex) {
                    throw new IOException("Can't decrypt");
                }
            } else {
                this._cipher.decrypt(this._fetchBuffer, readed);
                readFromStream();
            }
        }

        return this._buffer[this._bufferUsed++] & 0xff;
    }

    /**
     * Ferme le flux parent.
     *
     * @throws IOException si le flux parent lève une exception
     */
    @Override
    public void close() throws IOException {
        this._parent.close();
    }
}
