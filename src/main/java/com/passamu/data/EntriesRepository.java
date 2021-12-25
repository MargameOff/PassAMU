package com.passamu.data;

import java.io.*;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import com.passamu.crypt.CryptInputStream;
import com.passamu.crypt.CryptOutputStream;
import com.passamu.xml.Entries;
import com.passamu.xml.XmlConverter;

/**
 * Classe de référentiel pour la lecture et l'écriture de documents XML (cryptés).
 *
 */
public final class EntriesRepository {

    /**
     * Nom du fichier a lire ou ecrire
     */
    private final String fileName;

    /**
     * clée pour l'encryption
     */
    private final byte[] key;

    /**
     * Convertisseur de POJO vers XML
     */
    private static final XmlConverter<Entries> CONVERTER = new XmlConverter<>(Entries.class);

    /**
     * Crée une instance DocumentRepository.
     *
     * @param fileName nom du fichier
     * @param key key pour le cryptage
     */
    private EntriesRepository(final String fileName, final byte[] key) {
        this.fileName = fileName;
        this.key = key;
    }

    /**
     * Crée un repository de documents sans cryptage.
     * @param fileName nom du fichier
     * @return un nouvel objet DocumentHelper
     */
    public static EntriesRepository newInstance(final String fileName) {
        return new EntriesRepository(fileName, null);
    }

    /**
     * Crée un référentiel de documents avec cryptage.
     *
     * @param fileName nom du fichier
     * @param key key pour le cryptage
     * @return un nouvel objet DocumentHelper
     */
    public static EntriesRepository newInstance(final String fileName, final byte[] key) {
        return new EntriesRepository(fileName, key);
    }

    /**
     * Lit fichier XML dans un objet {@link Entries}.
     *
     * @return le document
     * @throws FileNotFoundException si le fichier n'existe pas
     * @throws IOException lorsqu'une erreur d'E/S s'est produite
     * @throws DocumentProcessException lorsque le format de fichier ou le mot de passe est incorrect
     */
    public Entries readDocument() throws IOException, DocumentProcessException {
        InputStream inputStream = null;
        Entries entries;
        try {
            if (this.key == null) {
                inputStream = new BufferedInputStream(new FileInputStream(this.fileName));
            } else {
                inputStream = new GZIPInputStream(new CryptInputStream(new BufferedInputStream(new FileInputStream(this.fileName)), this.key));
            }
            entries = CONVERTER.read(inputStream);
        } catch (IOException e) {
            throw e;
        } catch (Exception e) {
            throw new DocumentProcessException(e.getMessage());
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return entries;
    }


    
    /** 
     * @param document
     * @throws DocumentProcessException
     * @throws IOException
     */
    public void writeDocument(final Entries document) throws DocumentProcessException, IOException {
        OutputStream outputStream = null;
        try {
            if (this.key == null) {
                outputStream = new BufferedOutputStream(new FileOutputStream(this.fileName));
            } else {
                outputStream = new GZIPOutputStream(new CryptOutputStream(new BufferedOutputStream(new FileOutputStream(this.fileName)), this.key));
            }
            CONVERTER.write(document, outputStream);
        } catch (Exception e) {
            throw new DocumentProcessException(e.getMessage());
        } finally {
            if (outputStream != null) {
                outputStream.close();
            }
        }
    }
    public void writeXML(final Entries document) throws DocumentProcessException, IOException {
        OutputStream outputStream = null;
        try {
             outputStream = new BufferedOutputStream(new FileOutputStream(this.fileName));

            CONVERTER.write(document, outputStream);
        } catch (Exception e) {
            throw new DocumentProcessException(e.getMessage());
        } finally {
            if (outputStream != null) {
                outputStream.close();
            }
        }
    }

}
