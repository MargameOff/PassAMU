package com.passamu.data;

import java.util.List;
import java.util.stream.Collectors;

import com.passamu.models.ListModel;
import com.passamu.xml.Entries;

/**
 * Data model des données de l'application
 *
 * @author Gabor_Bata
 *
 */
public class DataModel {

    private static DataModel INSTANCE;


    private Entries entries = new Entries();
    private String fileName = null;
    private byte[] password = null;

    private DataModel() {
    }


    
    /**
     * Getter d'Instance, en créer une s'il n'y en a pas.
     * @return DataModel
     */
    public static synchronized DataModel getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new DataModel();
        }
        return INSTANCE;
    }

    
    /** 
     * Getter pour Entries.
     * @return Entries
     */
    public final Entries getEntries() {
        return this.entries;
    }

    
    /** 
     * Setter pour Entries.
     * @param entries
     */
    public final void setEntries(final Entries entries) {
        this.entries = entries;
    }

    
    /** 
     * Getter pour le nom du fichier.
     * @return String
     */
    public final String getFileName() {
        return this.fileName;
    }

    
    /** 
     * Setter pour le nom du fichier.
     * @param fileName
     */
    public final void setFileName(final String fileName) {
        this.fileName = fileName;
    }


    
    /** 
     * Getter pour le mot de passe.
     * @return byte[]
     */
    public byte[] getPassword() {
        return this.password;
    }

    
    /** 
     * Setter pour le mot de passe.
     * @param password
     */
    public void setPassword(byte[] password) {
        this.password = password;
    }

    public final void clear() {
        this.entries.getEntry().clear();
        this.entries.getListFolder().clear();
        this.fileName = null;
        this.password = null;
    }

    
    /** 
     * Getter pour les titres
     * @return List<String>
     */
    public List<String> getTitles() {
        return this.entries.getEntry().stream()
                .map(ListModel::getListName)
                .collect(Collectors.toList());
    }

    
    /** 
     * Getter pour entry en triant par les titres
     * @param title
     * @return ListModel
     */
    public ListModel getEntryByTitle(String title) {
        int entryIndex = getEntryIndexByTitle(title);
        if (entryIndex != -1) {
            return this.entries.getEntry().get(entryIndex);
        }
        return null;
    }

    
    /**
     * Recupere l'index à l'aide d'un nom d'entrée
     * @param title
     * @return int
     */
    private int getEntryIndexByTitle(String title) {
        return getTitles().indexOf(title);
    }
}
