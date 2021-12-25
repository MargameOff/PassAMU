
package com.passamu.utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Random;

/**
 * Classe d'utilitaire liée à la cryptographie.
 */
public final class CryptUtils {

    private CryptUtils() {
    }


    /**
     * Calculez le hachage SHA-256, avec 1000 itérations par défaut (RSA PKCS5).
     *
     * @param text mot de passe texte
     * @return hash du mot de passe
     * @throws Exception si une erreur s'est produite
     */
    public static byte[] getPKCS5Sha256Hash(final char[] text) throws Exception {
        return getSha256Hash(text, 1000);
    }


    /**
     * Calculez le hachage SHA-256.
     *
     * @param text mot de passe texte
     * @return hash du mot de passe
     * @throws Exception si une erreur s'est produite
     */
    public static byte[] getSha256Hash(final char[] text) throws Exception {
        return getSha256Hash(text, 0);
    }

    /**
     * Calculez le hachage SHA-256.
     *
     * @param text mot de passe texte
     * @param iteration nombre d'itérations
     * @return hash du mot de passe
     * @throws Exception si une erreur s'est produite
     */
    private static byte[] getSha256Hash(final char[] text, final int iteration) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.reset();
        byte[] bytes = new String(text).getBytes(StandardCharsets.UTF_8);
        byte[] digest = md.digest(bytes);
        for (int i = 0; i < iteration; i++) {
            md.reset();
            digest = md.digest(digest);
        }
        return digest;
    }

    /**
     * Obtenez un générateur de nombres aléatoires.
     *
     * Il essaie d'abord de revenir avec un générateur aléatoire sécurisé non déterministe, s'il n'y arrive
     * pour une raison quelconque, il revient avec le générateur aléatoire uniforme.
     *
     * @return le générateur de nombres aléatoires.
     */
    public static Random newRandomNumberGenerator() {
        Random ret;
        try {
            ret = new SecureRandom();
        } catch (Exception e) {
            ret = new Random();
        }
        return ret;
    }
}
