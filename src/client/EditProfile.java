package client;

import java.io.File;
import java.net.MalformedURLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.binding.BooleanBinding;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class EditProfile{
    
    static Button save;
    static TextField fn, ln, un, address, fonNum; 
    static PasswordField pass;
    static ComboBox<String> county, work, rln, gend;
    boolean answer;
    String message;
    static DatePicker birth;
    static Stage window; 
    static Scene editScene;
    
    public static void edit(){
        
        try {
            window = new Stage();
            window.initModality(Modality.APPLICATION_MODAL);
            window.setTitle("Video Conference");
            File iconImg = new File("src/images/skull.jpg");
            String iconLoc = iconImg.toURI().toURL().toString();
            window.getIcons().add(new Image(iconLoc));
            } catch (MalformedURLException ex) {
            Logger.getLogger(EditProfile.class.getName()).log(Level.SEVERE, null, ex);
        }
            
            TitledPane root = new TitledPane();
            root.setText("Edit Profile");
            editScene = new Scene(root,450,550);
            Text fir,las,use,cou,occ,rls,eme,fon,sex ,dob ;
            Label fErr,lErr,uErr,eRR,foErr;
            sex = new Text("Gender:");
            dob = new Text("Date of Birth:");
            gend = new ComboBox<>();
            gend.getItems().addAll("Male","Female");
            gend.setPromptText("Select gender");
            birth = new DatePicker();
            fn = new TextField();
            ln = new TextField();
            un = new TextField();
            fon = new Text("Phone number: ");
            pass = new PasswordField();
            address= new TextField();
            county = new ComboBox<>();
            county.getItems().addAll("Mombasa","Kwale","Tana River" ,"Kisumu", "Nairobi","Taita Taveta",
                    "Kakamega", "Kilifi", "Bungoma", "Lamu", "Busia","Nakuru","Garissa","Wajir",
                    "Mandera","Marsabit","Isiolo","Meru","Tharaka Nithi","Embu","Kitui","Machakos",
                    "Makueni","Nyandarua","Nyeri","Kirinyaga","Murang'a","Kiambu","Turakana",
                    "West Pokot","Samburu","Trans-Nzoia","Uasin Gishu","Elgeyo-Marakwet","Nandi",
                    "Baringo","Laikipia","Narok","Kajiado","Kericho","Bomet","Vihiga","Siaya",
                    "Homa bay","Migori","Kisii","Nyamira");
            county.setPromptText("Select county");
            work= new ComboBox<>();
            work.getItems().addAll("Doctor", "Student", "Self employed", "Unemployed", "Teacher", "Pilot", "Others");
            work.setPromptText("Select occupation");
            rln= new ComboBox<>();
            rln.getItems().addAll("Single", "Married", "Divorced", "Widowed");
            rln.setPromptText("Relationship status");
            Label shw = new Label("");
            fir = new Text("First Name:");
            las= new Text("Last Name:");
            use= new Text("User Name:");
            cou= new Text("County:");
            occ= new Text("Occupation:");
            rls= new Text("Relationship Status:");
            eme= new Text("Email Address:");
            fErr = new Label("");
            lErr= new Label("");
            uErr= new Label("");
            eRR= new Label("");
            foErr = new Label("");
            save = new Button("Save");
            save.setOnAction(e ->{
                String msge = "Are you sure you want to save?";
                Boolean ans = false;
                if(ans) {
                    Boolean fNme = RegistrationWindow.chkFName(fn.getText());
                    if(fNme == true){
                        Boolean lNme = RegistrationWindow.chkLName(ln.getText());
                        if(lNme == true){
                            Boolean uNme = RegistrationWindow.chkUName(un.getText());
                            if(uNme == true){
                                Boolean adr = RegistrationWindow.chkAddress(address.getText());
                                if(adr == true){
                                    Boolean fone = RegistrationWindow.chkFonNum(fonNum.getText());
                                    if(fone == true){
                                        ServerConnector.getEditedDetails(fn.getText(),ln.getText(),un.getText(),address.getText()
                                                ,fonNum.getText(),county.getSelectionModel().getSelectedItem(), work.getSelectionModel().getSelectedItem(),
                                                rln.getSelectionModel().getSelectedItem());
                                        
                                        fn.clear();
                                        ln.clear();
                                        un.clear();
                                        address.clear();
                                        fonNum.clear();
                                        county.setValue("");
                                        work.setValue("");
                                        rln.setValue("");
                                         
                                        shw.setText("Changes saved!");
                                        shw.setTextFill(Color.GREEN);
                                    }
                                    else
                                        foErr.setText("Phone Number invalid");
                                }
                                else
                                    eRR.setText("Email invalid");
                            }
                            else
                                uErr.setText("Invalid UserName");
                            //To do code if username is taken
                        }
                        else
                            lErr.setText("Invalid Name");
                    }
                    else
                        fErr.setText("Invalid Name");
                }
            }); GridPane child = new GridPane();
            child.add(shw,0,0);
            child.add(fir,0,1);
            child.add(las,0,2);
            child.add(use,0,3);
            child.add(eme,0,4);
            child.add(fon,0,5);
            child.add(sex,0,6);
            child.add(cou,0,7);
            child.add(occ,0,8);
            child.add(dob,0,9);
            child.add(rls,0,10);
            child.add(fn,1,1);
            child.add(ln,1,2);
            child.add(un,1,3);
            child.add(address,1,4);
            child.add(fonNum,1,5);
            child.add(gend,1,6);
            child.add(county, 1, 7);
            child.add(work,1,8);
            child.add(birth,1,9);
            child.add(rln,1,10);
            child.add(fErr, 2,1);
            child.add(lErr, 2, 2);
            child.add(uErr,2 ,3);
            child.add(eRR,2 ,4);
            child.add(foErr,2,5);
            child.add(save,2,11);
            BooleanBinding bb = new BooleanBinding(){
                {
                    super.bind(fn.textProperty(),ln.textProperty(),un.textProperty(),
                            address.textProperty(),pass.textProperty());
                }
                
                @Override
                protected boolean computeValue(){
                    return(fn.getText().isEmpty()
                            || ln.getText().isEmpty()
                            || un.getText().isEmpty()
                            || address.getText().isEmpty()
                            ||pass.getText().isEmpty());}
            };  save.disableProperty().bind(bb);
        child.setHgap(5);
            child.setVgap(10);
            child.setPadding(new Insets(10));
            root.setContent(child);
            window.setScene(editScene);
            window.showAndWait();
        
    }
    
}
