package com.passamu.data;

/**
 * Exception si le traitement du document XML échoue.
 *
 */
public class DocumentProcessException extends Exception {

    public DocumentProcessException(String message) {
        super("Cannot process document due to the following exception:\n" + message);
    }
}
