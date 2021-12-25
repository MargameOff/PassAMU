package com.passamu.xml;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.passamu.models.ListModel;

import java.util.ArrayList;
import java.util.List;
/**
 * Classe Java pour ce contenant des Données qui vont etre chiffré
 */
@JacksonXmlRootElement(localName = "entries")
public class Entries {
    /**
     * liste des entrées des mot de passe et toutes leurs informations
     */
    public List<ListModel> entryList;
    /**
     * liste des dossiers créés
     */
    public List<String> listFolder;

    /**
     * getter de la liste des models
     * @return List<ListModel>
     */
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    public List<ListModel> getEntry() {
        if (entryList == null) {
            entryList = new ArrayList<>();
        }
        return this.entryList;
    }
    public List<String> getListFolder() {
        if (listFolder == null) {
            listFolder = new ArrayList<>();
        }
        return this.listFolder;
    }

}
