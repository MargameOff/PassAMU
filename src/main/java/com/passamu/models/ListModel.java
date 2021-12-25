package com.passamu.models;

import javafx.beans.property.SimpleStringProperty;

/**
 * ListModel qui contient tout les élements d'une itération de mot de passe
 */
public class ListModel {
    private int id;
    private SimpleStringProperty website = new SimpleStringProperty();
    private SimpleStringProperty listName = new SimpleStringProperty();
    private SimpleStringProperty listUser = new SimpleStringProperty();
    private SimpleStringProperty password = new SimpleStringProperty();
    private SimpleStringProperty notes = new SimpleStringProperty();
    private SimpleStringProperty type = new SimpleStringProperty();
    private SimpleStringProperty folder = new SimpleStringProperty();
    private boolean isFavorite;

    public ListModel(){

    }

    public ListModel(int id, String listName, String listUser, String password, String website, String notes, String type, String folder, boolean isFavorite) {
        this.id = id;
        this.listName = new SimpleStringProperty(listName);
        this.listUser = new SimpleStringProperty(listUser);
        this.website = new SimpleStringProperty(website);
        this.notes = new SimpleStringProperty(notes);
        this.password = new SimpleStringProperty(password);
        this.type = new SimpleStringProperty(type);
        this.folder = new SimpleStringProperty(folder);
        this.isFavorite = isFavorite;

    }

    
    /** 
     * @return website est l'adresse du site web de type String retournée par ce getter (exemple : www.facebook.com)
     */
    public String getWebsite() {
        return website.get();
    }

    
    /** 
     * @return website est l'adresse du site web de type SimpleStringProperty étant des String optimisés pour l'affichage et la modification sur JavaFX retournée par ce getter.
     */
    public SimpleStringProperty websiteProperty() {
        return website;
    }

    
    /** 
     * @param website est l'adresse du site de type String prise en paramètre pour le fonctionnement de ce setter.
     */
    public void setWebsite(String website) {
        this.website.set(website);
    }

    
    /** 
     * @return listName est le nom de type String retourné par ce getter.
     */
    public String getListName() {
        return listName.get();
    }

    
    /** 
     * @return listName est le nom de type SimpleStringProperty étant des String optimisés pour l'affichage et la modification sur JavaFX retourné par ce getter.
     */
    public SimpleStringProperty listNameProperty() {
        return listName;
    }

    
    /** 
     * @param listName est le nom de type String prit en paramètre pour le fonctionnement de ce setter.
     */
    public void setListName(String listName) {
        this.listName.set(listName);
    }

    
    /** 
     * @return listUser est l'utilisateur retourné par ce getter.
     */
    public String getListUser() {
        return listUser.get();
    }

    
    /** 
     * @return listUser est l'utilisateur de type SimpleStringProperty étant des String optimisés pour l'affichage et la modification sur JavaFX retourné par ce getter.
     */
    public SimpleStringProperty listUserProperty() {
        return listUser;
    }

    
    /** 
     * @param listUser est le nom de type String prit en paramètre pour le fonctionnement de ce setter.
     */
    public void setListUser(String listUser) {
        this.listUser.set(listUser);
    }

    
    /** 
     * @return password est le mot de passe retourné par ce getter.
     */
    public String getPassword() {
        return password.get();
    }

    
    /** 
     * @return password est le mot de passe de type SimpleStringProperty étant des String optimisés pour l'affichage et la modification sur JavaFX retourné par ce getter.
     */
    public SimpleStringProperty passwordProperty() {
        return password;
    }

    
    /** 
     * @param password est le mot de passe en String prit en paramètre pour le fonctionnement de ce setter.
     */
    public void setPassword(String password) {
        this.password.set(password);
    }

    
    /** 
     * @return notes est la valeur de la note retournée par ce getter.
     */
    public String getNotes() {
        return notes.get();
    }

    
    /** 
     * @return notes est la valeur de la note de type SimpleStringProperty étant des String optimisés pour l'affichage et la modification sur JavaFX retourné par ce getter.
     */
    public SimpleStringProperty notesProperty() {
        return notes;
    }

    
    /** 
     * @param notes est la note en String prit en paramètre pour le fonctionnement de ce setter.
     */
    public void setNotes(String notes) {
        this.notes.set(notes);
    }

    
    /** 
     * @return type est le type en String retournée par ce getter.
     */
    public String getType() {
        return type.get();
    }

    
    /** 
     * @return type est le type en SimpleStringProperty étant des String optimisés pour l'affichage et la modification sur JavaFX retourné par ce getter.
     */
    public SimpleStringProperty typeProperty() {
        return type;
    }

    
    /** 
     * @param type est le type en String prit en paramètre pour le fonctionnement de ce setter.
     */
    public void setType(String type) {
        this.type.set(type);
    }

    
    /** 
     * @return folder est le dossier en String retournée par ce getter.
     */
    public String getFolder() {
        return folder.get();
    }

    
    /** 
     * @return folder est le dossier en SimpleStringProperty étant des String optimisés pour l'affichage et la modification sur JavaFX retourné par ce getter.
     */
    public SimpleStringProperty folderProperty() {
        return folder;
    }

    
    /** 
     * @param folder est le type en String prit en paramètre pour le fonctionnement de ce setter.
     */
    public void setFolder(String folder) {
        this.folder.set(folder);
    }

    
    /** 
     * @return boolean est le getter pour savoir si un item est en favoris ou non.
     */
    public boolean isFavorite() {
        return isFavorite;
    }

    
    /** 
     * @param favorite est le paramètre prit en compte pour le setter des items favoris.
     */
    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public int getId() {return id;}
    /** 
     * @param id est le paramètre prit en compte pour le setter de l'ID.
     */
    public void setId(int id) {this.id = id;}
}
