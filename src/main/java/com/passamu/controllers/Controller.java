package com.passamu.controllers;

import com.passamu.App;
import com.passamu.data.DataModel;
import com.passamu.data.EntriesRepository;
import com.passamu.models.ListModel;
import com.passamu.utils.CopyUtils;
import com.passamu.utils.PasswordGenerator;
import com.passamu.utils.ThreadWorker;
import com.passamu.utils.UIFunction;
import com.passamu.xml.Entries;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.Vector;

import static com.passamu.utils.UIFunction.*;


public class Controller implements Initializable {

    private final DataModel model = DataModel.getInstance();

    @FXML
    private Button btnAccount;

    @FXML
    private Button btnAdd;

    @FXML
    private Button btnAll;

    @FXML
    private Button btnCard;

    @FXML
    private Button btnEdit;

    @FXML
    private Button btnFavoris;

    @FXML
    private Button btnLoadFile;

    @FXML
    private ComboBox<String> typeBox;

    @FXML
    private ComboBox<String> folderBox;

    @FXML
    private Button btnMoney;

    @FXML
    private Button btnNewFile;

    @FXML
    private Button btnPasswordGenerator;

    @FXML
    private Button btnTrash;

    @FXML
    private Button btnquit;

    @FXML
    private Label crdCat;

    @FXML
    private ImageView crdFavorite;

    @FXML
    private ImageView crdLogo;

    @FXML
    private Text fileName;

    @FXML
    private Label crdNom;

    @FXML
    private Text crdNote;

    @FXML
    private Text nbOfEntries;

    @FXML
    private Label crdNoteText;

    @FXML
    private PasswordField crdPassword;

    @FXML
    private Hyperlink crdURL;

    @FXML
    private TextField crdUsername;

    @FXML
    private Label crdWebsiteTitle;

    @FXML
    private Line crdbar1;

    @FXML
    private Line crdbar2;

    @FXML
    private PasswordField editConfirmPassword;

    @FXML
    private TextArea editNotes;

    @FXML
    private Button editDelete;

    @FXML
    private PasswordField editPassword;

    @FXML
    private Button editSave;

    @FXML
    private Label editName;

    @FXML
    private TextField editConfirmPasswordShow;

    @FXML
    private Button editBtnConfirmPasswordShow;

    @FXML
    private Button editBtnPasswordShow;

    @FXML
    private TextField editPasswordShow;

    @FXML
    private TextField editUrlWebsite;

    @FXML
    private TextField editUsername;

    @FXML
    private Button hidePassword;

    @FXML
    private VBox listList;

    @FXML
    private ScrollPane listScroll;

    @FXML
    private ScrollPane scrollFolder;

    @FXML
    private TextField editTitle;

    @FXML
    private TextField crdPasswordShow;

    @FXML
    private Button addSave;

    @FXML
    private Button passCopy;

    @FXML
    private BorderPane pnEdit;

    @FXML
    private BorderPane pnLogin;

    @FXML
    private BorderPane pnTotal;

    @FXML
    private BorderPane pnSettings;

    @FXML
    private AnchorPane scene;

    @FXML
    private TextField searchBar;

    @FXML
    private Button userCopy;

    private boolean[] isSelected;
    Stage stage;

    public int indexSelectedCard;

    private Entries entryDelete = new Entries();

    private boolean editIsFavorite = false;

    
    /** 
     * @param e est la variable de type ActionEvent prise en paramètre afin de fermer la fenêtre concernée, optimisée pour JavaFX.
     */
    @FXML
    void buttonCat(ActionEvent e){
        String textSelectButton = ((Button)e.getSource()).getText();
        switch (textSelectButton){
            case "All":
                loadFile(model.getEntries());
                if (model.getEntries().getEntry().size()>0){
                    indexSelectedCard =0;
                    loadCard(indexSelectedCard);
                }
                break;
            case "Bin":
                loadFile(entryDelete);
                if (entryDelete.getEntry().size()>0){
                    loadCard(entryDelete.getEntry().get(0).getId(), entryDelete);
                }
                break;
            case "Favorites":
                Entries find = new Entries();
                for (int i=0;i<model.getEntries().getEntry().size();i++){
                    if (model.getEntries().getEntry().get(i).isFavorite() == true){
                        find.getEntry().add(model.getEntries().getEntry().get(i));
                    }
                }
                loadFile(find);
                if (find.getEntry().size()>0){
                    indexSelectedCard = find.getEntry().get(0).getId();
                    loadCard(indexSelectedCard);
                }

                break;
            default:
                System.out.println("Error de filtre");
                break;
        }
    }
    @FXML
    void buttonType(ActionEvent e){
        String textSelectButton = ((Button)e.getSource()).getText();
        Entries find = new Entries();
        for (int i=0;i<model.getEntries().getEntry().size();i++){

            if (textSelectButton.equals(model.getEntries().getEntry().get(i).getType())){
                find.getEntry().add(model.getEntries().getEntry().get(i));
            }
        }
        loadFile(find);
        if (find.getEntry().size()>0){
            indexSelectedCard = find.getEntry().get(0).getId();
            loadCard(indexSelectedCard);
        }

    }

    @FXML
    void handleButtonClicks(){}

    @FXML
    void copyUsername(){
        CopyUtils.copyEntryField(crdUsername.getText());
    }

    @FXML
    void copyPassword(){
        CopyUtils.copyEntryField(crdPassword.getText());
    }
    
    /** 
     * @return boolean est la valeur de retour de cette fonction qui permet de vérifier la validité de l'item, il faut que le mot de passe de confirmation corresponde bien au mot de passe de base et qu'aucun champs obligatoire ne soit vide.
     */
    boolean verifCard(){
        if (editPasswordVisibility){
            editPassword.setText(editPasswordShow.getText());
        }
        if (editConfirmPasswordVisibility){
            editConfirmPassword.setText(editConfirmPasswordShow.getText());
        }
        boolean verifPassword = editConfirmPassword.getText().equals(editPassword.getText());
        boolean verifEmpty = editTitle.getText() == "" || editUsername.getText() == "" || editPassword.getText() == "" || editUrlWebsite.getText() == "" || editConfirmPassword.getText() == "";
        if (verifEmpty) {
            UIFunction.openErrorDialog("Some required fields are empty");
            return false;
        }
        if (!verifPassword) {
            UIFunction.openErrorDialog("Passwords not matching");
            return false;
        }
        if (!(editUrlWebsite.getText().startsWith("http://") || editUrlWebsite.getText().startsWith("https://"))){
            editUrlWebsite.setText("https://" + editUrlWebsite.getText());
        }
        return true;

    }

    
    /** 
     * @param e est la variable de type ActionEvent prise en paramètre. On utilise cette fonction pour créer et sauvegarder notre nouvel item.
     */
    @FXML
    void savingCard(ActionEvent e) {
        if (verifCard()){
            model.getEntries().getEntry().add(new ListModel(model.getEntries().getEntry().size(),editTitle.getText(), editUsername.getText(), editPassword.getText(), editUrlWebsite.getText(), editNotes.getText(), typeBox.getValue(), folderBox.getValue(), editIsFavorite));
            loadFile(model.getEntries());
            pnTotal.toFront();
            indexSelectedCard=(model.getEntries().getEntry().size()-1);
            crdVisible(true);
            loadCard(indexSelectedCard);
        }

    }

    
    /** 
     * @param e est la variable de type ActionEvent prise en paramètre. On utilise cette fonction pour supprimer l'item que l'on veut.
     */
    @FXML
    void deleteCard(ActionEvent e) {

        String option = UIFunction.openYesNoDialog("Do you really want to delete this entry?");
        if (option == "1") {
            entryDelete.getEntry().add(model.getEntries().getEntry().get(indexSelectedCard));
            model.getEntries().getEntry().remove(indexSelectedCard);
            loadFile(model.getEntries());
            pnTotal.toFront();
        }
        btnEdit.setVisible(false);
        crdVisible(false);
    }
    boolean crdPasswordVisibility=false, editPasswordVisibility=false, editConfirmPasswordVisibility=false;

    
    /** 
     * @param e est la variable de type ActionEvent prise en paramètre. Cette fonction permet d'actionner l'affichage du mot de passe en toute lettre.
     */
    @FXML
    void showCrdPassword(ActionEvent e){
        crdPasswordVisibility = crdPasswordVisibility ? false : true;
        changePasswordVisibility(crdPassword,crdPasswordShow,crdPasswordVisibility);
    }

    
    /** 
     * @param e est la variable de type ActionEvent prise en paramètre. Cette fonction permet d'actionner l'affichage du mot de passe (dans l'interface d'édition d'un item) en toute lettre.
     */
    @FXML
    void showEditPassword(ActionEvent e){
        editPasswordVisibility = editPasswordVisibility ? false : true;
        changePasswordVisibility(editPassword,editPasswordShow,editPasswordVisibility);
    }

    
    /** 
     * @param e est la variable de type ActionEvent prise en paramètre. Cette fonction permet d'actionner l'affichage du mot de passe DE CONFIRMATION en toute lettre dans l'interface d'édition d'une carte.
     */
    @FXML
    void showEditConfirmPassword(ActionEvent e){
        editConfirmPasswordVisibility = editConfirmPasswordVisibility ? false : true;
        changePasswordVisibility(editConfirmPassword,editConfirmPasswordShow,editConfirmPasswordVisibility);
    }

    
    /** 
     * @param e est la variable de type ActionEvent prise en paramètre. Cette fonction permet d'éditer les informations d'un carte déjà existante avec les informations que l'on désire.
     */
    @FXML
    void editingCard(ActionEvent e) {

        addSave.setVisible(false);
        editSave.setVisible(true);
        editDelete.setVisible(true);
        editName.setText("Edit");
        editIsFavorite= model.getEntries().getEntry().get(indexSelectedCard).isFavorite();
        String adresse = editIsFavorite ? "icons/star.png" : "icons/starvide.png";
        EditLogoIsSave.setImage(new Image(String.valueOf(App.class.getResource(adresse))));
        editUsername.setText(model.getEntries().getEntry().get(indexSelectedCard).getListUser());
        editTitle.setText(model.getEntries().getEntry().get(indexSelectedCard).getListName());
        editPassword.setText(model.getEntries().getEntry().get(indexSelectedCard).getPassword());
        editConfirmPassword.setText(editPassword.getText());
        editUrlWebsite.setText(model.getEntries().getEntry().get(indexSelectedCard).getWebsite());
        editNotes.setText(model.getEntries().getEntry().get(indexSelectedCard).getNotes());
        typeBox.setValue(model.getEntries().getEntry().get(indexSelectedCard).getType());
        folderBox.setValue(model.getEntries().getEntry().get(indexSelectedCard).getFolder());
        pnEdit.toFront();
    }

    
    /** 
     * @param pass PasswordField
     * @param text TextField
     * @param active Boolean
     * @Description : Cette fonction permet de changer le statut de visibilité du mot de passe, masqué ou non.
     */
    void changePasswordVisibility(PasswordField pass,TextField text, Boolean active){
        if (active){
            pass.setVisible(false);
            text.setVisible(true);
            text.setText(pass.getText());
        } else {
            text.setVisible(false);
            pass.setVisible(true);
            pass.setText(text.getText());
        }
    }

    
    /** 
     * @param e est la variable de type ActionEvent prise en paramètre. Cette fonction permet de quitter le fichier en cours d'utilisation (sans sauvegarde automatique).
     */
    @FXML
    void quitFile(ActionEvent e) {
        saveFile(false);
        pnLogin.toFront();
    }

    @FXML
    private ImageView EditLogoIsSave;

    
    /** 
     * @param e est la variable de type ActionEvent prise en paramètre. Cette fonction permet de fermer la fenêtre en cours d'utilisation et de proposer de sauvegarder l'activité en cours s'il y a des informations déjà inscrites.
     */
    @FXML
    public void close(ActionEvent e) {
        stage = (Stage) scene.getScene().getWindow();
        if (model.getEntries().getEntry().size() != 0){
            String rep = UIFunction.openYesNoDialog("Voulez-vous sauvegarder votre document ?");
            if (rep == "1"){
                saveFile(false);
                try {
                    Thread.sleep(2700);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        }
        stage.close();
    }

    
    /** 
     * @param ev est la variable de type ActionEvent prise en paramètre. Cette fonction permet de préparer et afficher la fenêtre d'ajout d'une carte en étant sûr que ces champs soient bien vierges (Initialisation vide).
     */
    @FXML
    void addItem(ActionEvent ev) {
        typeBox.setValue(null);
        folderBox.setValue(null);
        addSave.setVisible(true);
        editDelete.setVisible(false);
        editSave.setVisible(false);
        editPassword.setText("");
        editConfirmPassword.setText("");
        editNotes.setText("");
        editUsername.setText("");
        editTitle.setText("");
        editIsFavorite=false;
        EditLogoIsSave.setImage(new Image(String.valueOf(App.class.getResource("icons/starvide.png"))));
        editUrlWebsite.setText("");
        editDelete.setVisible(false);
        pnEdit.toFront();
        editName.setText("Add");
        if (model.getEntries().getEntry().size()== 0) {
            btnEdit.setVisible(true);
        }


    }

    
    /** 
     * @param ev est la variable de type ActionEvent prise en paramètre. Cette fonction permet d'éditer le statut "favoris" d'un item, si on met un item en favoris, l'étoile jaune sera remplie, si on clique à nouveau dessus, l'étoile jaune sera vide et l'item ne sera plus dans les favoris.
     */
    @FXML
    void editIsFav(ActionEvent ev) {
        editIsFavorite = !editIsFavorite;
        String adresse = editIsFavorite ? "icons/star.png" : "icons/starvide.png";
        EditLogoIsSave.setImage(new Image(String.valueOf(App.class.getResource(adresse))));
    }

    
    /** 
     * @param ev est la variable de type ActionEvent prise en paramètre. Cette fonction permet l'ajout et l'ouverture d'un fichier. On utilise des threads (explique woula)
     */
    @FXML
    void openFile(ActionEvent ev) {
        cleanPage();

        stage = (Stage) scene.getScene().getWindow();
        File fileToLoad = showFileChooser(stage, "Select your .passamu file", "*.passamu", "PassAMU Data Files");
        if (fileToLoad == null) {
            return;
        }
        String fileNamee = fileToLoad.getPath();
        if (fileNamee == null) {
            return;
        }
        byte[] password = UIFunction.openDialog();
        if (password == null) {
            return;
        }

        ThreadWorker worker = new ThreadWorker() {
            @Override
            protected Void doInBackground() {
                try {
                    model.setEntries(EntriesRepository.newInstance(fileNamee, password).readDocument());
                    model.setFileName(fileNamee);
                    model.setPassword(password);
                } catch (FileNotFoundException e) {
                    System.out.println(e);
                } catch (IOException e) {
                    UIFunction.openErrorDialog("An error occured during the open operation.\nPlease check your password.");
                } catch (Throwable e) {
                    UIFunction.openErrorDialog("An error occured during the open operation.\n"+e.getMessage());
                }
                return null;
            }

            @Override
            protected void done() {
                try {
                    get();
                    //Créée un putain de thread graphique javaFX, a surtout pas delete oui
                    Platform.runLater(new Runnable() {
                        @Override public void run() {
                            loadFile(model.getEntries());
                            indexSelectedCard=0;
                            loadCard(indexSelectedCard);
                            pnTotal.toFront();
                            typeBox.getItems().addAll("Credit Card","Accounts","Money");
                            nbOfEntries.setText(String.valueOf(model.getEntries().getEntry().size()));
                            File file =new File(fileNamee);
                            fileName.setText(file.getName());
                            loadFolder();
                        }
                    });

                } catch (Exception e) {
                    if (e.getCause() instanceof FileNotFoundException) {
                        handleFileNotFound(fileNamee, password);
                    } else {
                        UIFunction.openErrorDialog(e.getMessage());
                    }
                }
            }

        };
        worker.execute();

    }

    
    /** 
     * @param event est la variable de type ActionEvent prise en paramètre. Si la carte est bien valide (verifcard()), cette fonction enregistre l'édition d'une carte.
     */
    @FXML
    void EditSavingCard(ActionEvent event) {
        if (verifCard()){
            model.getEntries().getEntry().get(indexSelectedCard).setListName(editTitle.getText());
            model.getEntries().getEntry().get(indexSelectedCard).setWebsite(editUrlWebsite.getText());
            model.getEntries().getEntry().get(indexSelectedCard).setFavorite(editIsFavorite);
            model.getEntries().getEntry().get(indexSelectedCard).setPassword(editPassword.getText());
            model.getEntries().getEntry().get(indexSelectedCard).setNotes(editNotes.getText());
            model.getEntries().getEntry().get(indexSelectedCard).setListUser(editUsername.getText());
            model.getEntries().getEntry().get(indexSelectedCard).setFolder(folderBox.getValue());
            model.getEntries().getEntry().get(indexSelectedCard).setType(typeBox.getValue());
            loadFile(model.getEntries());
            pnTotal.toFront();
            loadCard(indexSelectedCard);
        }

    }

    
    /** 
     * @param fileName est la variable recevant et contenant le nom du fichier que l'on veut ouvrir.
     * @param password est la variable recevant et contenant le mot de passe du fichier que l'on veut ouvrir.
     * Cette fonction nous sert à gérer le cas où le fichier que l'on veut ouvrir n'existe pas, l'application va alors proposer d'en créer un pour vous.
     */
    void handleFileNotFound(final String fileName, final byte[] password) {
        String option = UIFunction.openYesNoDialog("File not found.\nDo you want to create the file?");
        if (option == "1") {
            ThreadWorker fileNotFoundWorker = new ThreadWorker() {
                @Override
                protected Void doInBackground() throws Exception {
                    try {
                        EntriesRepository.newInstance(fileName, password).writeDocument(model.getEntries());
                        model.setFileName(fileName);
                        model.setPassword(password);
                    } catch (Exception ex) {
                        UIFunction.openErrorDialog(ex.getMessage());
                    }
                    return null;
                }
            };
            fileNotFoundWorker.execute();
        }
    }

    
    /** 
     * @param fileName est la variable de type ActionEvent prise en paramètre. Cette fonction nous permet d'écraser et remplacer les données si nous trouvons un fichier portant le même nom
     * @return boolean
     */
    private static boolean checkFileOverwrite(String fileName) {
        boolean overwriteAccepted = true;
        File file = new File(fileName);
        if (file.exists()) {
            String option = UIFunction.openYesNoDialog("File is already exists, Do you want to overwrite?");
            if (option != "1") {
                overwriteAccepted = false;
            }
        }
        return overwriteAccepted;
    }


    
    /**
     * Loader d'un fichier affin de determiner les différents elements
     *
     * @param entries d'entrée à load dans le logiciel
     */
    private void loadFile(Entries entries) {
        Node[] nodes = new Node[entries.getEntry().size()];
        VBox entry = new VBox();
        try {
            for (int i = 0; i < nodes.length; i++) {

                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(App.class.getResource("fxml/mainitem.fxml"));
                nodes[i] = loader.load();


                isSelected = new boolean[nodes.length];
                final int h = i;

                ItemController controlleri = loader.getController();
                controlleri.setInfoItem(entries.getEntry().get(i).getListName(), entries.getEntry().get(i).getListUser(), entries.getEntry().get(i).getWebsite());

                nodes[i].setOnMouseEntered(mouseEvent -> {
                    if (!isSelected[h]) {
                        nodes[h].setStyle("-fx-background-color: #181818");
                    }
                });
                nodes[i].setOnMouseExited(mouseEvent -> {
                    if (isSelected[h]) {
                        nodes[h].setStyle("-fx-background-color: #0e0e0e");
                    } else {
                        nodes[h].setStyle("-fx-background-color: #1C1C1CFF");
                    }
                });
                nodes[i].setOnMousePressed(mouseEvent -> {

                    Arrays.fill(isSelected, Boolean.FALSE);
                    isSelected[h] = true; //pour reset
                    for (Node n : nodes) {
                        n.setStyle("-fx-background-color: #1C1C1CFF");
                    }
                    if (isSelected[h]) {
                        nodes[h].setStyle("-fx-background-color: #0e0e0e");
                    }
                    indexSelectedCard = entries.getEntry().get(h).getId();
                    loadCard(indexSelectedCard);
                });
                entry.getChildren().add(nodes[i]);
            }
            listScroll.setContent(entry);


        } catch (IOException e) {
            e.printStackTrace();
        }
        nbOfEntries.setText(String.valueOf(model.getEntries().getEntry().size()));
    }
    @FXML
    void back(){
        pnTotal.toFront();
    }

    void loadCard(int i){
        loadCard(i, model.getEntries());
    }

    /** 
     * Permet de rentrer et charger une carte avec nos informations voulues, nouveauté incroyable de la part de Marius, si c'est une page officielle (exemple : Google, Facebook, etc...) alors le logo de cette page est téléchargé automatiquement. 
     */
    void loadCard(int i, Entries ent){
        crdVisible(true);
        crdFavorite.setVisible(ent.getEntry().get(i).isFavorite());
        crdURL.setText(ent.getEntry().get(i).getWebsite());
        crdNote.setText(ent.getEntry().get(i).getNotes());
        String img = ent.getEntry().get(i).getWebsite();
        try {
            URL website = new URL(img);
            String path = "https://www.google.com/s2/favicons?sz=128&domain_url="+website.getHost();
            crdLogo.setImage(new Image(path));

        } catch (MalformedURLException e) {
            UIFunction.openErrorDialog(e.getMessage());
        }
        boolean isURL = img.startsWith("http://") || img.startsWith("https://");

        crdUsername.setText(ent.getEntry().get(i).getListUser());
        crdNom.setText(ent.getEntry().get(i).getListName());
        btnEdit.setVisible(true);
        crdPassword.setText(ent.getEntry().get(i).getPassword());
        crdPasswordShow.setText(crdPassword.getText());

    }
    
    /**
     * Cette fonction permet de définir l'affichage graphique du panel CRD (panel de droite)
     * @param b est la variable de type Booléen prise en paramètre.
     */
    public void crdVisible(boolean b) {
        crdNom.setVisible(b);
        crdPassword.setVisible(b);
        crdNote.setVisible(b);
        crdUsername.setVisible(b);
        crdLogo.setVisible(b);
        crdURL.setVisible(b);
        crdCat.setVisible(b);
        crdNoteText.setVisible(b);
        crdFavorite.setVisible(b);
        crdWebsiteTitle.setVisible(b);
        crdbar1.setVisible(b);
        crdbar2.setVisible(b);
        userCopy.setVisible(b);
        passCopy.setVisible(b);
        hidePassword.setVisible(b);
    }

    
    /**
     * Permet la sauvegarde du fichier en cours.
     *
     * @param saveAs est la variable de type Booléen, et, constant prise en paramètre. Elle décide du mode de sauvegarde
     */
    public void saveFile(final boolean saveAs) {
        saveFile(saveAs, () -> {
            //constructeur par défaut
        });
    }

    /**
     * Fonction du boutton Save AS
     */
    @FXML
    void saving(){
        saveFile(true);
    }

    /**
     * Fonction du boutton Settings
     */
    @FXML
    void settings(){
        pnSettings.toFront();
    }

    /**
     *  Recherche une entrée avec la barre de recherche
     * @param event key pressé
     */
    @FXML
    void findEntry(KeyEvent event){
        if (event.getCode().equals(KeyCode.ENTER)) {
            Entries find = new Entries();
            for (int i = 0; i < model.getEntries().getEntry().size(); i++) {

                if (model.getEntries().getEntry().get(i).getListName().startsWith(searchBar.getText())) {
                    find.getEntry().add(model.getEntries().getEntry().get(i));
                }
            }
            loadFile(find);
            if (find.getEntry().size() > 0) {
                indexSelectedCard = find.getEntry().get(0).getId();
                loadCard(indexSelectedCard);
            }
        } else if (searchBar.getText().length() ==0){
            loadFile(model.getEntries());
            if (model.getEntries().getEntry().size() > 0) {
                indexSelectedCard = model.getEntries().getEntry().get(0).getId();
                loadCard(indexSelectedCard);
            }
        }
    }

    /**
     * access à l'URL via le CRD
     */
    @FXML
    void accessURL(){
        try {
            new ProcessBuilder("x-www-browser", crdURL.getText()).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Permet d'exporter EN CLAIR les données de l'application
     */
    @FXML
    void exportXML(){
        final String fileName;
        stage = (Stage) scene.getScene().getWindow();
        File file = showSaveChooser(stage, "Save", "*.xml", "XML Files");
        if (file == null) {
            return;
        }
        fileName = checkExtension(file.getPath(), "xml");
        if (!checkFileOverwrite(fileName)) {
            return;
        }
        ThreadWorker worker = new ThreadWorker() {
            @Override
            protected Void doInBackground(){
                try {
                    finish=false;
                    EntriesRepository.newInstance(fileName).writeXML(model.getEntries());
                } catch (Throwable e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void done() {
                boolean result = true;
                try {
                    get();
                    finish=true;
                } catch (Exception e) {
                    result = false;
                    showErrorMessage(e);
                }
                if (result) {
                }
            }
        };
        worker.execute();
    }
    /**
     * Fonction qui gere la sauvegarde des mots de passes à l'aide de Thread
     * @param saveAs final Boolean qui décide du mode de sauvegarde
     * @param successCallback final Runnable
     * 
     */
    public void saveFile(final boolean saveAs, final Runnable successCallback) {
        final String fileName;
        stage = (Stage) scene.getScene().getWindow();
        if (saveAs || model.getFileName() == null) {
            File file = showSaveChooser(stage, "Save", "*.passamu", "PassAMU Data Files");
            if (file == null) {
                return;
            }
            fileName = checkExtension(file.getPath(), "passamu");
            if (!checkFileOverwrite(fileName)) {
                return;
            }
        } else {
            fileName = model.getFileName();
        }

        final byte[] password;
        if (model.getPassword() == null) {
            password = UIFunction.openDialog();
            if (password == null) {
                return;
            }
        } else {
            password = model.getPassword();
        }
        ThreadWorker worker = new ThreadWorker() {
            @Override
            protected Void doInBackground(){
                try {
                    finish=false;
                    EntriesRepository.newInstance(fileName, password).writeDocument(model.getEntries());
                    model.setFileName(fileName);
                    model.setPassword(password);
                } catch (Throwable e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void done() {
                boolean result = true;
                try {
                    get();
                    finish=true;
                } catch (Exception e) {
                    result = false;
                    showErrorMessage(e);
                }
                if (result) {
                    successCallback.run();
                }
            }
        };
        worker.execute();
    }

    
    /**
     * Cette fonction Vérifie l'extension du fichier (.XXX) et la corrige si nécessaire puis la retourne.
     * @param fileName est le nom du fichier de type String prit en paramètre. 
     * @param extension est l'extension du fichier de type String prit en paramètre.
     * @return String
     */
    private String checkExtension(final String fileName, final String extension) {
        String separator = fileName.endsWith(".") ? "" : ".";
        if (!fileName.toLowerCase().endsWith(separator + extension)) {
            return fileName + separator + extension;
        }
        return fileName;
    }

    /** 
     * Efface la page et ses informations.
     */
    public void cleanPage() {
        listScroll.setContent(new VBox());
        scrollFolder.setContent(new VBox());
        folderBox.getItems().clear();
        typeBox.getItems().clear();
        model.clear();
        crdVisible(false);
        loadFolder();
    }



    
    /**
     * Cette fonction efface la page actuelle pour en remettre une neuve, vierge, et prête à l'usage par dessus. Optimisée JavaFX
     *
     * @param e est la variable de type ActionEvent prise en paramètre.
     */
    @FXML
    void newFile(ActionEvent e) {
        cleanPage();
        btnEdit.setVisible(false);
        pnTotal.toFront();
        typeBox.getItems().addAll("Credit Card","Accounts","Money");
    }

    
    /**
     *  Cette fonction appelle le constructeur de PasswordGenerator, elle sert donc à l'ouverture du Générateur de Mot de Passe'
     * @param e est la variable de type ActionEvent prise en paramètre.
     * @throws IOException
     */
    @FXML
    void generator(ActionEvent e) throws IOException {
        PasswordGenerator pass = new PasswordGenerator();

    }
    /** 
     * Permet de charger la liste des Dossier du fichier
     */
    void loadFolder(){
        scrollFolder.setContent(new VBox());
        folderBox.getItems().clear();
        Node[] nodes = new Node[model.getEntries().getListFolder().size()+1];  //+1 pour ajouter le ADD
        VBox entry = new VBox();
        try {
            for (int i = 0; i < nodes.length; i++) {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(App.class.getResource("fxml/folder.fxml"));
                nodes[i] = loader.load();
                isSelected = new boolean[nodes.length];
                final int h = i;

                FolderController controlleri = loader.getController();
                if (i== nodes.length-1){
                    controlleri.setInfo("Add", String.valueOf(App.class.getResource("icons/plus.png")));
                    nodes[i].setOnMousePressed(mouseEvent -> {
                        String folderName=null;
                        folderName= UIFunction.openFolderName();
                        if (folderName.length() != 0 || folderName!=null){
                            model.getEntries().getListFolder().add(folderName);
                            loadFolder();
                        }

                    });
                }else {
                    controlleri.setInfo(model.getEntries().getListFolder().get(i));
                    nodes[i].setOnMousePressed(mouseEvent -> {
                        System.out.println(model.getEntries().getListFolder().get(h));
                        String textSelectButton = model.getEntries().getListFolder().get(h);
                        Entries find = new Entries();
                        for (int j=0;j<model.getEntries().getEntry().size();j++){

                            if (textSelectButton.equals(model.getEntries().getEntry().get(j).getFolder())){
                                find.getEntry().add(model.getEntries().getEntry().get(j));
                            }
                        }
                        loadFile(find);
                        if (find.getEntry().size()>0){
                            indexSelectedCard = find.getEntry().get(0).getId();
                            loadCard(indexSelectedCard);
                        }

                    });
                }


                nodes[i].setOnMouseEntered(mouseEvent -> {
                });
                nodes[i].setOnMouseExited(mouseEvent -> {
                });
                entry.getChildren().add(nodes[i]);
            }

            scrollFolder.setContent(entry);
            for (int i=0;i<model.getEntries().getListFolder().size();i++){
                folderBox.getItems().add(model.getEntries().getListFolder().get(i));
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    
    /**
     * Fonction qui se lance au démmarage
     * @param url URL
     * @param resourceBundle ResourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadFolder();
        nbOfEntries.setText(String.valueOf(model.getEntries().getEntry().size()));
    }
}