package com.passamu.crypt;

/**
 * Implémentation de l'AES avec une taille de clé de 256 bits.
 * Pour rendre cette implémentation plus puissante, seule la version avec la clée la plus haute sera implémentée.
 *
 */
public final class AES256 {

    /**
     * Nombre de bytes nécessaires pour les mots de 32 bits.
     */
    private static final int WORD_SIZE = 4;

    /**
     * Nombre de bytes pour un bloc de données. La taille est identique à la taille de l'intérieur,
     * qui est nécessaire dans le cryptage ou le décryptage.
     * L'état peut être considéré comme une matrice carrée, modélisée comme une liste de vecteurs colonnes.
     */
    private static final int BLOCK_SIZE = 16;

    /**
     * nombre de la clée en byte
     */
    private static final int KEY_SIZE = 32;

    /**
     * nombre de tours
     */
    private static final int ROUNDS = 14;

    /**
     * Taille de la clé étendue. Pour chaque tour, un bloc sera nécessaire.
     */
    private static final int EXPANDED_KEY_SIZE = (ROUNDS + 1) * BLOCK_SIZE;

    /**
     * clé étendue
     */
    private final byte[] _expandedKey;

    /**
     * etat temporaire nécessaire entre les tours de cryptage ou de décryptage
     */
    private final byte[] _tmp;

    /**
     * permutation des bytes dans le fichier affin de commencer l'etape d'encryption dans la sBox.
     */
    private final byte[] _sBox = {(byte) 0x63, (byte) 0x7c, (byte) 0x77,
            (byte) 0x7b, (byte) 0xf2, (byte) 0x6b, (byte) 0x6f, (byte) 0xc5,
            (byte) 0x30, (byte) 0x01, (byte) 0x67, (byte) 0x2b, (byte) 0xfe,
            (byte) 0xd7, (byte) 0xab, (byte) 0x76, (byte) 0xca, (byte) 0x82,
            (byte) 0xc9, (byte) 0x7d, (byte) 0xfa, (byte) 0x59, (byte) 0x47,
            (byte) 0xf0, (byte) 0xad, (byte) 0xd4, (byte) 0xa2, (byte) 0xaf,
            (byte) 0x9c, (byte) 0xa4, (byte) 0x72, (byte) 0xc0, (byte) 0xb7,
            (byte) 0xfd, (byte) 0x93, (byte) 0x26, (byte) 0x36, (byte) 0x3f,
            (byte) 0xf7, (byte) 0xcc, (byte) 0x34, (byte) 0xa5, (byte) 0xe5,
            (byte) 0xf1, (byte) 0x71, (byte) 0xd8, (byte) 0x31, (byte) 0x15,
            (byte) 0x04, (byte) 0xc7, (byte) 0x23, (byte) 0xc3, (byte) 0x18,
            (byte) 0x96, (byte) 0x05, (byte) 0x9a, (byte) 0x07, (byte) 0x12,
            (byte) 0x80, (byte) 0xe2, (byte) 0xeb, (byte) 0x27, (byte) 0xb2,
            (byte) 0x75, (byte) 0x09, (byte) 0x83, (byte) 0x2c, (byte) 0x1a,
            (byte) 0x1b, (byte) 0x6e, (byte) 0x5a, (byte) 0xa0, (byte) 0x52,
            (byte) 0x3b, (byte) 0xd6, (byte) 0xb3, (byte) 0x29, (byte) 0xe3,
            (byte) 0x2f, (byte) 0x84, (byte) 0x53, (byte) 0xd1, (byte) 0x00,
            (byte) 0xed, (byte) 0x20, (byte) 0xfc, (byte) 0xb1, (byte) 0x5b,
            (byte) 0x6a, (byte) 0xcb, (byte) 0xbe, (byte) 0x39, (byte) 0x4a,
            (byte) 0x4c, (byte) 0x58, (byte) 0xcf, (byte) 0xd0, (byte) 0xef,
            (byte) 0xaa, (byte) 0xfb, (byte) 0x43, (byte) 0x4d, (byte) 0x33,
            (byte) 0x85, (byte) 0x45, (byte) 0xf9, (byte) 0x02, (byte) 0x7f,
            (byte) 0x50, (byte) 0x3c, (byte) 0x9f, (byte) 0xa8, (byte) 0x51,
            (byte) 0xa3, (byte) 0x40, (byte) 0x8f, (byte) 0x92, (byte) 0x9d,
            (byte) 0x38, (byte) 0xf5, (byte) 0xbc, (byte) 0xb6, (byte) 0xda,
            (byte) 0x21, (byte) 0x10, (byte) 0xff, (byte) 0xf3, (byte) 0xd2,
            (byte) 0xcd, (byte) 0x0c, (byte) 0x13, (byte) 0xec, (byte) 0x5f,
            (byte) 0x97, (byte) 0x44, (byte) 0x17, (byte) 0xc4, (byte) 0xa7,
            (byte) 0x7e, (byte) 0x3d, (byte) 0x64, (byte) 0x5d, (byte) 0x19,
            (byte) 0x73, (byte) 0x60, (byte) 0x81, (byte) 0x4f, (byte) 0xdc,
            (byte) 0x22, (byte) 0x2a, (byte) 0x90, (byte) 0x88, (byte) 0x46,
            (byte) 0xee, (byte) 0xb8, (byte) 0x14, (byte) 0xde, (byte) 0x5e,
            (byte) 0x0b, (byte) 0xdb, (byte) 0xe0, (byte) 0x32, (byte) 0x3a,
            (byte) 0x0a, (byte) 0x49, (byte) 0x06, (byte) 0x24, (byte) 0x5c,
            (byte) 0xc2, (byte) 0xd3, (byte) 0xac, (byte) 0x62, (byte) 0x91,
            (byte) 0x95, (byte) 0xe4, (byte) 0x79, (byte) 0xe7, (byte) 0xc8,
            (byte) 0x37, (byte) 0x6d, (byte) 0x8d, (byte) 0xd5, (byte) 0x4e,
            (byte) 0xa9, (byte) 0x6c, (byte) 0x56, (byte) 0xf4, (byte) 0xea,
            (byte) 0x65, (byte) 0x7a, (byte) 0xae, (byte) 0x08, (byte) 0xba,
            (byte) 0x78, (byte) 0x25, (byte) 0x2e, (byte) 0x1c, (byte) 0xa6,
            (byte) 0xb4, (byte) 0xc6, (byte) 0xe8, (byte) 0xdd, (byte) 0x74,
            (byte) 0x1f, (byte) 0x4b, (byte) 0xbd, (byte) 0x8b, (byte) 0x8a,
            (byte) 0x70, (byte) 0x3e, (byte) 0xb5, (byte) 0x66, (byte) 0x48,
            (byte) 0x03, (byte) 0xf6, (byte) 0x0e, (byte) 0x61, (byte) 0x35,
            (byte) 0x57, (byte) 0xb9, (byte) 0x86, (byte) 0xc1, (byte) 0x1d,
            (byte) 0x9e, (byte) 0xe1, (byte) 0xf8, (byte) 0x98, (byte) 0x11,
            (byte) 0x69, (byte) 0xd9, (byte) 0x8e, (byte) 0x94, (byte) 0x9b,
            (byte) 0x1e, (byte) 0x87, (byte) 0xe9, (byte) 0xce, (byte) 0x55,
            (byte) 0x28, (byte) 0xdf, (byte) 0x8c, (byte) 0xa1, (byte) 0x89,
            (byte) 0x0d, (byte) 0xbf, (byte) 0xe6, (byte) 0x42, (byte) 0x68,
            (byte) 0x41, (byte) 0x99, (byte) 0x2d, (byte) 0x0f, (byte) 0xb0,
            (byte) 0x54, (byte) 0xbb, (byte) 0x16};

    /**
     * Inverse la permutation s-box.
     */
    private final byte[] _invSBox = {(byte) 0x52, (byte) 0x09, (byte) 0x6a,
            (byte) 0xd5, (byte) 0x30, (byte) 0x36, (byte) 0xa5, (byte) 0x38,
            (byte) 0xbf, (byte) 0x40, (byte) 0xa3, (byte) 0x9e, (byte) 0x81,
            (byte) 0xf3, (byte) 0xd7, (byte) 0xfb, (byte) 0x7c, (byte) 0xe3,
            (byte) 0x39, (byte) 0x82, (byte) 0x9b, (byte) 0x2f, (byte) 0xff,
            (byte) 0x87, (byte) 0x34, (byte) 0x8e, (byte) 0x43, (byte) 0x44,
            (byte) 0xc4, (byte) 0xde, (byte) 0xe9, (byte) 0xcb, (byte) 0x54,
            (byte) 0x7b, (byte) 0x94, (byte) 0x32, (byte) 0xa6, (byte) 0xc2,
            (byte) 0x23, (byte) 0x3d, (byte) 0xee, (byte) 0x4c, (byte) 0x95,
            (byte) 0x0b, (byte) 0x42, (byte) 0xfa, (byte) 0xc3, (byte) 0x4e,
            (byte) 0x08, (byte) 0x2e, (byte) 0xa1, (byte) 0x66, (byte) 0x28,
            (byte) 0xd9, (byte) 0x24, (byte) 0xb2, (byte) 0x76, (byte) 0x5b,
            (byte) 0xa2, (byte) 0x49, (byte) 0x6d, (byte) 0x8b, (byte) 0xd1,
            (byte) 0x25, (byte) 0x72, (byte) 0xf8, (byte) 0xf6, (byte) 0x64,
            (byte) 0x86, (byte) 0x68, (byte) 0x98, (byte) 0x16, (byte) 0xd4,
            (byte) 0xa4, (byte) 0x5c, (byte) 0xcc, (byte) 0x5d, (byte) 0x65,
            (byte) 0xb6, (byte) 0x92, (byte) 0x6c, (byte) 0x70, (byte) 0x48,
            (byte) 0x50, (byte) 0xfd, (byte) 0xed, (byte) 0xb9, (byte) 0xda,
            (byte) 0x5e, (byte) 0x15, (byte) 0x46, (byte) 0x57, (byte) 0xa7,
            (byte) 0x8d, (byte) 0x9d, (byte) 0x84, (byte) 0x90, (byte) 0xd8,
            (byte) 0xab, (byte) 0x00, (byte) 0x8c, (byte) 0xbc, (byte) 0xd3,
            (byte) 0x0a, (byte) 0xf7, (byte) 0xe4, (byte) 0x58, (byte) 0x05,
            (byte) 0xb8, (byte) 0xb3, (byte) 0x45, (byte) 0x06, (byte) 0xd0,
            (byte) 0x2c, (byte) 0x1e, (byte) 0x8f, (byte) 0xca, (byte) 0x3f,
            (byte) 0x0f, (byte) 0x02, (byte) 0xc1, (byte) 0xaf, (byte) 0xbd,
            (byte) 0x03, (byte) 0x01, (byte) 0x13, (byte) 0x8a, (byte) 0x6b,
            (byte) 0x3a, (byte) 0x91, (byte) 0x11, (byte) 0x41, (byte) 0x4f,
            (byte) 0x67, (byte) 0xdc, (byte) 0xea, (byte) 0x97, (byte) 0xf2,
            (byte) 0xcf, (byte) 0xce, (byte) 0xf0, (byte) 0xb4, (byte) 0xe6,
            (byte) 0x73, (byte) 0x96, (byte) 0xac, (byte) 0x74, (byte) 0x22,
            (byte) 0xe7, (byte) 0xad, (byte) 0x35, (byte) 0x85, (byte) 0xe2,
            (byte) 0xf9, (byte) 0x37, (byte) 0xe8, (byte) 0x1c, (byte) 0x75,
            (byte) 0xdf, (byte) 0x6e, (byte) 0x47, (byte) 0xf1, (byte) 0x1a,
            (byte) 0x71, (byte) 0x1d, (byte) 0x29, (byte) 0xc5, (byte) 0x89,
            (byte) 0x6f, (byte) 0xb7, (byte) 0x62, (byte) 0x0e, (byte) 0xaa,
            (byte) 0x18, (byte) 0xbe, (byte) 0x1b, (byte) 0xfc, (byte) 0x56,
            (byte) 0x3e, (byte) 0x4b, (byte) 0xc6, (byte) 0xd2, (byte) 0x79,
            (byte) 0x20, (byte) 0x9a, (byte) 0xdb, (byte) 0xc0, (byte) 0xfe,
            (byte) 0x78, (byte) 0xcd, (byte) 0x5a, (byte) 0xf4, (byte) 0x1f,
            (byte) 0xdd, (byte) 0xa8, (byte) 0x33, (byte) 0x88, (byte) 0x07,
            (byte) 0xc7, (byte) 0x31, (byte) 0xb1, (byte) 0x12, (byte) 0x10,
            (byte) 0x59, (byte) 0x27, (byte) 0x80, (byte) 0xec, (byte) 0x5f,
            (byte) 0x60, (byte) 0x51, (byte) 0x7f, (byte) 0xa9, (byte) 0x19,
            (byte) 0xb5, (byte) 0x4a, (byte) 0x0d, (byte) 0x2d, (byte) 0xe5,
            (byte) 0x7a, (byte) 0x9f, (byte) 0x93, (byte) 0xc9, (byte) 0x9c,
            (byte) 0xef, (byte) 0xa0, (byte) 0xe0, (byte) 0x3b, (byte) 0x4d,
            (byte) 0xae, (byte) 0x2a, (byte) 0xf5, (byte) 0xb0, (byte) 0xc8,
            (byte) 0xeb, (byte) 0xbb, (byte) 0x3c, (byte) 0x83, (byte) 0x53,
            (byte) 0x99, (byte) 0x61, (byte) 0x17, (byte) 0x2b, (byte) 0x04,
            (byte) 0x7e, (byte) 0xba, (byte) 0x77, (byte) 0xd6, (byte) 0x26,
            (byte) 0xe1, (byte) 0x69, (byte) 0x14, (byte) 0x63, (byte) 0x55,
            (byte) 0x21, (byte) 0x0c, (byte) 0x7d};


    /**
     * Remplace tous les byte dans un mot. Le mot tableau sera modifié.
     *
     * @param value tableau de valeurs dans lequel les premiers byte seront remplacés.
     * Ce tableau sera modifié.
     * @return renvoie la value modifiée
     */
    private byte[] substituteWord(byte[] value) {
        for (int i = 0; i < WORD_SIZE; ++i) {
            value[i] = this._sBox[value[i] & 0xff];
        }
        return value;
    }

    /**
     * Faites pivoter les byte dans un mot. Les byte seront tourné à gauche d'un octet
     * La modification sera en place, donc l'argument d'origine est changé après
     * l'execution de la méthode.
     *
     * @param value Tableau dans lequel les premiers byte seront modifiés en raison de
     * la rotation. Le contenu de ce tableau est modifié par cette fonction.
     */
    private byte[] rotate(byte[] value) {
        byte tmp = value[0];
        for (int i = 1; i < WORD_SIZE; ++i) {
            value[i - 1] = value[i];
        }
        value[WORD_SIZE - 1] = tmp;
        return value;
    }

    /**
     * Développe la clé. La clé entrante fait KEY_SIZE bytes. il sera agrandi
     * jusqu'à une longueur de EXPANDED_KEY_SIZE bytes. La clé étendue sera stockée dans la variable du meme nom
     * Le cryptage et le décryptage utiliseront la clé étendue.
     *
     * @param key clée pour l'algorithme AES
     */
    public AES256(byte[] key) {
        this._expandedKey = new byte[EXPANDED_KEY_SIZE];
        this._tmp = new byte[BLOCK_SIZE];

        System.arraycopy(key, 0, this._expandedKey, 0, KEY_SIZE);

        for (int i = KEY_SIZE; i < EXPANDED_KEY_SIZE; i += WORD_SIZE) {
            System.arraycopy(this._expandedKey, i - WORD_SIZE, this._tmp, 0, WORD_SIZE);

            if (i % KEY_SIZE == 0) {
                substituteWord(rotate(this._tmp));
                this._tmp[0] ^= 1 << (i / KEY_SIZE - 1);
            } else if (i % KEY_SIZE == BLOCK_SIZE) {
                substituteWord(this._tmp);
            }

            for (int j = 0; j < WORD_SIZE; ++j) {
                this._expandedKey[i + j] = (byte) (this._expandedKey[i - KEY_SIZE + j] ^ this._tmp[j]);
            }
        }
    }


    /**
     * Combine l'état avec la clé étendue. Les byte seront combinés par XOR.
     *
     * @param index début de la partie de la clé développée, qui sera utilisée pour la combinaison
     */
    private void addRoundKey(int index) {
        for (int i = 0; i < BLOCK_SIZE; ++i) {
            this._tmp[i] = (byte) (this._tmp[i] ^ this._expandedKey[index + i]);
        }
    }


    /**
     * Le polynôme représenté par {@code b} sera multiplié par sa variable libre. Cette
     * multiplication a lieu dans un corps fini. Le polynôme résultant peut encore être représenté
     * par un {@code byte}.
     * Les bits {@code 0} à {@code 7} sont les coefficients des puissances {@code x} à {@code x**8}.
     *
     * @param b polynôme d'origine
     *
     * @return polynôme multiplié
     */
    private int times2(int b) {
        int result = b << 1;
        if ((b & 0x80) != 0) {
            result ^= 0x1b;
        }
        return result & 0xff;
    }


    /**
     * Deux polynômes seront multipliés entre eux. La représentation du polynôme est
     * décrite dans la fonction times2.
     * La multiplication sera effectuée par invocations successives de times2.
     *
     * @param a premier polynôme
     * @param b second polynôme
     *
     * @return résultat de la multiplication
     */
    private byte mul(int a, byte b) {
        int result = 0;
        int first = a;
        int current = b & 0xff;
        while (first != 0) {
            if ((first & 0x01) != 0) {
                result ^= current;
            }
            first >>= 1;
            current = times2(current);
        }
        return (byte) (result & 0xff);
    }

    /**
     * Modifie tous les {@code byte} dans l'état par la s-box.
     */
    private void substituteState() {
        for (int i = 0; i < BLOCK_SIZE; ++i) {
            this._tmp[i] = this._sBox[this._tmp[i] & 0xff];
        }
    }
    /**
     * Fait pivoter les trois dernières lignes de l'état.
     */
    private void shiftRows() {
        byte tmp = this._tmp[1];
        this._tmp[1] = this._tmp[5];
        this._tmp[5] = this._tmp[9];
        this._tmp[9] = this._tmp[13];
        this._tmp[13] = tmp;

        tmp = this._tmp[2];
        this._tmp[2] = this._tmp[10];
        this._tmp[10] = tmp;
        tmp = this._tmp[6];
        this._tmp[6] = this._tmp[14];
        this._tmp[14] = tmp;

        tmp = this._tmp[3];
        this._tmp[3] = this._tmp[15];
        this._tmp[15] = this._tmp[11];
        this._tmp[11] = this._tmp[7];
        this._tmp[7] = tmp;
    }

    /**
     * Mélange une colonne de l'état.
     *
     * @param index position du premier élément de la colonne
     */
    private void mixColumn(int index) {
        int s0 = mul(2, this._tmp[index]) ^ mul(3, this._tmp[index + 1])
                ^ (this._tmp[index + 2] & 0xff) ^ (this._tmp[index + 3] & 0xff);
        int s1 = (this._tmp[index] & 0xff) ^ mul(2, this._tmp[index + 1])
                ^ mul(3, this._tmp[index + 2]) ^ (this._tmp[index + 3] & 0xff);
        int s2 = (this._tmp[index] & 0xff) ^ (this._tmp[index + 1] & 0xff)
                ^ mul(2, this._tmp[index + 2]) ^ mul(3, this._tmp[index + 3]);
        int s3 = mul(3, this._tmp[index]) ^ (this._tmp[index + 1] & 0xff)
                ^ (this._tmp[index + 2] & 0xff) ^ mul(2, this._tmp[index + 3]);
        this._tmp[index] = (byte) (s0 & 0xff);
        this._tmp[index + 1] = (byte) (s1 & 0xff);
        this._tmp[index + 2] = (byte) (s2 & 0xff);
        this._tmp[index + 3] = (byte) (s3 & 0xff);
    }

    /**
     * Mélange toutes les colonnes de l'état.
     */
    private void mixColumns() {
        mixColumn(0);
        mixColumn(4);
        mixColumn(8);
        mixColumn(12);
    }


    /**
     * Crypte un bloc. Le bloc d'entrée se trouve dans {@code inBlock} à partir de la position
     * {@code inIndex}. Le {@code inBlock} ne sera pas modifié par cette méthode. Le bloc crypté
     * sera stocké dans {@code outBlock} à partir de la position {@code outIndex}.
     *
     * @param inBlock tableau contenant le bloc d'entrée
     * @param inIndex démarrage du bloc d'entrée dans {@code inBlock}
     * @param outBlock Tableau pour stocker le bloc crypté
     * @param outIndex démarrage du bloc chiffré dans {@code outBlock}
     */
    public void encrypt(byte[] inBlock, int inIndex, byte[] outBlock,
                        int outIndex) {
        System.arraycopy(inBlock, inIndex, this._tmp, 0, BLOCK_SIZE);

        addRoundKey(0);
        for (int round = 1; round < ROUNDS; ++round) {
            substituteState();
            shiftRows();
            mixColumns();
            addRoundKey(round * BLOCK_SIZE);
        }

        substituteState();
        shiftRows();
        addRoundKey(ROUNDS * BLOCK_SIZE);

        System.arraycopy(this._tmp, 0, outBlock, outIndex, BLOCK_SIZE);
    }

    /**
     * Fait pivoter les trois dernières lignes de l'état. Cette méthode inverse shiftRows.
     */
    private void invShiftRows() {
        byte tmp = this._tmp[13];
        this._tmp[13] = this._tmp[9];
        this._tmp[9] = this._tmp[5];
        this._tmp[5] = this._tmp[1];
        this._tmp[1] = tmp;

        tmp = this._tmp[2];
        this._tmp[2] = this._tmp[10];
        this._tmp[10] = tmp;
        tmp = this._tmp[6];
        this._tmp[6] = this._tmp[14];
        this._tmp[14] = tmp;

        tmp = this._tmp[3];
        this._tmp[3] = this._tmp[7];
        this._tmp[7] = this._tmp[11];
        this._tmp[11] = this._tmp[15];
        this._tmp[15] = tmp;
    }

    /**
     * Modifie tous les {@code byte} de l'état. Cette méthode est l'inverse de
     * shiftRows.
     */
    private void invSubstituteState() {
        for (int i = 0; i < BLOCK_SIZE; ++i) {
            this._tmp[i] = this._invSBox[this._tmp[i] & 0xff];
        }
    }


    /**
     * Mélange une colonne de l'état. Cette méthode inverse mixColumn.
     *
     * @param index position de la première entrée de la ligne
     */
    private void invMixColumn(int index) {
        int s0 = mul(0x0e, this._tmp[index]) ^ mul(0x0b, this._tmp[index + 1])
                ^ mul(0x0d, this._tmp[index + 2]) ^ mul(0x09, this._tmp[index + 3]);
        int s1 = mul(0x09, this._tmp[index]) ^ mul(0x0e, this._tmp[index + 1])
                ^ mul(0x0b, this._tmp[index + 2]) ^ mul(0x0d, this._tmp[index + 3]);
        int s2 = mul(0x0d, this._tmp[index]) ^ mul(0x09, this._tmp[index + 1])
                ^ mul(0x0e, this._tmp[index + 2]) ^ mul(0x0b, this._tmp[index + 3]);
        int s3 = mul(0x0b, this._tmp[index]) ^ mul(0x0d, this._tmp[index + 1])
                ^ mul(0x09, this._tmp[index + 2]) ^ mul(0x0e, this._tmp[index + 3]);
        this._tmp[index] = (byte) (s0 & 0xff);
        this._tmp[index + 1] = (byte) (s1 & 0xff);
        this._tmp[index + 2] = (byte) (s2 & 0xff);
        this._tmp[index + 3] = (byte) (s3 & 0xff);
    }

    /**
     * Mélange toutes les colonnes de l'état. Cette méthode inverse mixColumns.
     */
    private void invMixColumns() {
        invMixColumn(0);
        invMixColumn(4);
        invMixColumn(8);
        invMixColumn(12);
    }


    /**
     * Décrypte un bloc. Le bloc chiffré commence à {@code inIndex} dans {@code inBlock}.
     * {@code inBlock} ne sera pas modifié par cette méthode. Le bloc déchiffré sera stocké à
     * {@code outIndex} dans {@code outBlock}.
     *
     * @param inBlock tableau contenant le bloc chiffré
     * @param inIndex point de départ du bloc chiffré
     *@param outBlock tableau pour stocker le bloc déchiffré
     * @param outIndex position du bloc déchiffré
     */
    public void decrypt(byte[] inBlock, int inIndex, byte[] outBlock,
                        int outIndex) {
        System.arraycopy(inBlock, inIndex, this._tmp, 0, BLOCK_SIZE);

        addRoundKey(ROUNDS * BLOCK_SIZE);
        for (int round = ROUNDS - 1; round > 0; --round) {
            invShiftRows();
            invSubstituteState();
            addRoundKey(round * BLOCK_SIZE);
            invMixColumns();
        }
        invShiftRows();
        invSubstituteState();
        addRoundKey(0);

        System.arraycopy(this._tmp, 0, outBlock, outIndex, BLOCK_SIZE);
    }
}
