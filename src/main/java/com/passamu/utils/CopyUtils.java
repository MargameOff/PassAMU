package com.passamu.utils;

import java.awt.*;
import java.awt.datatransfer.*;

/**
 * Classe Utilitaire liée au presse-papiers du système.
 *
 */
public class CopyUtils {
    /**
     * Contenu du presse-papiers vide.
     */
    private static final EmptyClipboardContent EMPTY = new EmptyClipboardContent();

    private CopyUtils() {
    }


    /**
     * Définit le texte dans le presse-papiers du système.
     *
     * @param str text
     * @throws Exception lorsque le presse-papiers n'est pas accessible
     */
    public static void setClipboardContent(String str) throws Exception {
        if (str == null || str.isEmpty()) {
            clearClipboardContent();
            return;
        }
        try {
            StringSelection selection = new StringSelection(str);
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selection, selection);
        } catch (Throwable throwable) {
            throw new Exception("Cannot set clipboard content.");
        }
    }


    /**
     * Efface le presse-papiers du système.
     *
     * @throws Exception lorsque le presse-papiers n'est pas accessible
     */
    public static void clearClipboardContent() throws Exception {
        try {
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(EMPTY, EMPTY);
        } catch (Throwable throwable) {
            throw new Exception("Cannot set clipboard content.");
        }
    }

    /**
     * Obtenez du texte du presse-papiers du système.
     *
     * @return le texte, ou {@code null} s'il n'y a pas de contenu
     */
    public static String getClipboardContent() {
        String result = null;
        try {
            Transferable contents = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null);
            if (contents != null && contents.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                result = String.valueOf(contents.getTransferData(DataFlavor.stringFlavor));
            }
        } catch (Throwable throwable) {
            // ignore
        }
        return result == null || result.isEmpty() ? null : result;
    }

    /**
     * Classe représentant un contenu de presse-papiers vide. Avec l'aide de cette classe, le contenu du
     * presse-papiers peut être effacé.
     */

    protected static final class EmptyClipboardContent implements Transferable, ClipboardOwner {

        @Override
        public DataFlavor[] getTransferDataFlavors() {
            return new DataFlavor[0];
        }

        @Override
        public boolean isDataFlavorSupported(DataFlavor flavor) {
            return false;
        }

        @Override
        public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException {
            throw new UnsupportedFlavorException(flavor);
        }

        @Override
        public void lostOwnership(Clipboard clipboard, Transferable contents) {
        }
    }

    
    /**
     * Fonction copiant dans le presse papier le contenu d'un string entrée en paramètre
     * @param content texte à copier
     */
    public static void copyEntryField(String content) {
        try {
            setClipboardContent(content);
        } catch (Exception e) {
            UIFunction.openErrorDialog(e.getMessage());
        }
    }
}
