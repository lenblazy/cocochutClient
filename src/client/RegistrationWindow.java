package client;

import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.application.Platform;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import static javafx.geometry.Pos.CENTER;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.shape.Circle;
import javafx.scene.shape.SVGPath;
import javafx.scene.text.*;
import javafx.stage.Stage;

public class RegistrationWindow {

    private static Stage regWindow;
    private static Scene regScene;
    private static BorderPane root;
    private static DropShadow d1;
    private static Reflection r1;
    private static TextField fn, ln, un, addr, fonNum;
    private static DatePicker birthDate;
    private static PasswordField pass1, pass2;
    private static ComboBox<String> genda, loc, occup, rlnState;
    private static Label fNerr, lNerr, uNerr, emerr, foNerr, doberr;
    private static BooleanProperty VALID;
    private static Button submit;
    private static SVGPath grantedIcon, deniedIcon;

    public RegistrationWindow() throws Exception {
        regWindow = new Stage();
        regWindow.setTitle("Cocochut");
        File iconImg = new File("src/images/icon.jpg");
        String iconLoc = iconImg.toURI().toURL().toString();
        regWindow.getIcons().add(new Image(iconLoc));
        root = new BorderPane();
        regScene = new Scene(root, 570, 640);
        regScene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());

        //Text Effects
        d1 = new DropShadow();
        d1.setOffsetX(2.0f);
        d1.setOffsetY(2.0f);
        d1.setColor(Color.GREEN);

        r1 = new Reflection();
        r1.setFraction(0.8F);
        r1.setTopOffset(5);

        createRegistrationWindow();
    }

    public static void createRegistrationWindow() throws Exception {
        Text fName, lName, uName, password1, password2, gender, email, county, occupation, dob, rlnshp, phoneNum;
        Text appTitle2 = new Text("Cocochut");
        appTitle2.setId("app-title");

        fName = new Text("First Name:");
        lName = new Text("Last Name:");
        uName = new Text("User Name:");
        password1 = new Text("Password:");
        password2 = new Text("Re-enter password:");
        email = new Text("Email address:");
        gender = new Text("Gender:");
        county = new Text("County:");
        occupation = new Text("Occupation:");
        dob = new Text("Date of Birth:");
        rlnshp = new Text("Relationship Status:");
        phoneNum = new Text("Phone Number:");

        //InputFields
        fn = new TextField();
        ln = new TextField();
        un = new TextField();
        addr = new TextField();
        fonNum = new TextField();
        pass1 = new PasswordField();
        pass2 = new PasswordField();
        birthDate = new DatePicker();
        birthDate.setId("bday");
        genda = new ComboBox<>();
        loc = new ComboBox<>();
        occup = new ComboBox<>();
        rlnState = new ComboBox<>();
        submit = new Button("Submit");
        grantedIcon = new SVGPath();
        deniedIcon = new SVGPath();
        VALID = new SimpleBooleanProperty(false);
        
        addr.setPromptText("E.g  XXX@XXX.COM");
        fonNum.setPromptText("E.g  07XXXXXXXX");
        genda.setPromptText("Select gender");
        loc.setPromptText("Select county");
        occup.setPromptText("Select occupation");
        rlnState.setPromptText("Select status");
        genda.getItems().addAll("Male", "Female");
        //listener when user types into passwordfield

        deniedIcon.setFill(Color.rgb(255, 0, 0, .9));
        deniedIcon.setStroke(Color.WHITE);
        deniedIcon.setContent("M24.778,21.419 19.276,15.917 24.777, 10.415 21.949, 7.585 16.447, 13.087 "
                + "10.945,7.585 8.117,10.415 13.618,15.917 8.116, 21.419 10.946, 24.248 16.447, 18.746 21.948, 24.248z");
        deniedIcon.setVisible(false);

        grantedIcon.setFill(Color.rgb(0, 255, 0, .9));
        grantedIcon.setStroke(Color.WHITE);
        grantedIcon.setContent("M2.379,14.729 5.208,11.899 12.958, 19.648 25.877, 6.733 28.707, 9.561 12.958, 25.308z");
        grantedIcon.setVisible(false);

        StackPane accessIndicator = new StackPane();
        accessIndicator.getChildren().addAll(deniedIcon, grantedIcon);
        accessIndicator.setAlignment(Pos.CENTER_RIGHT);
        grantedIcon.visibleProperty().bind(VALID);

        pass2.textProperty().addListener((obs, ov, nv) -> {
            boolean granted = pass2.getText().equals(pass1.getText());
            VALID.set(granted);
            if (granted) {
                deniedIcon.setVisible(false);
            } else {
                deniedIcon.setVisible(true);
            }
        });

        loc.getItems().addAll("Mombasa", "Kwale", "Tana River", "Kisumu", "Nairobi", "Taita Taveta",
                "Kakamega", "Kilifi", "Bungoma", "Lamu", "Busia", "Nakuru", "Garissa", "Wajir",
                "Mandera", "Marsabit", "Isiolo", "Meru", "Tharaka Nithi", "Embu", "Kitui", "Machakos",
                "Makueni", "Nyandarua", "Nyeri", "Kirinyaga", "Murang'a", "Kiambu", "Turakana",
                "West Pokot", "Samburu", "Trans-Nzoia", "Uasin Gishu", "Elgeyo-Marakwet", "Nandi",
                "Baringo", "Laikipia", "Narok", "Kajiado", "Kericho", "Bomet", "Vihiga", "Siaya",
                "Homa bay", "Migori", "Kisii", "Nyamira");

        occup.getItems().addAll("Doctor", "Student", "Self employed", "Unemployed", "Teacher", "Pilot","lawyer", "Others");

        rlnState.getItems().addAll("Single", "Married", "Divorced", "Widowed");

        fNerr = new Label("");
        lNerr = new Label("");
        uNerr = new Label("");
        emerr = new Label("");
        foNerr = new Label("");
        doberr = new Label("");

        submit.setOnAction(e -> {
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmation Dialog");
                alert.setContentText("Send Personal Details?");
                Optional<ButtonType> action = alert.showAndWait();
                if (action.get() == ButtonType.OK) {
                    fNerr.setText("");
                    lNerr.setText("");
                    uNerr.setText("");
                    emerr.setText("");
                    foNerr.setText("");
                    doberr.setText("");
                    Boolean fNme = chkFName(fn.getText());
                    if (fNme == true) {
                        Boolean lNme = chkLName(ln.getText());
                        if (lNme == true) {
                            Boolean uNme = chkUName(un.getText());
                            if (uNme == true) {
                                Boolean granted = pass2.getText().equals(pass1.getText());
                                if (granted == true) {
                                    Boolean adr = chkAddress(addr.getText());
                                    if (adr == true) {
                                        Boolean fon = chkFonNum(fonNum.getText());
                                        if (fon == true) {
                                            try {
                                                sendRegDetails();
                                                LoginWindow.createLoginWindow();
                                                regWindow.close();
                                            } catch (Exception ex) {
                                            }
                                        } else {
                                            foNerr.setText("Phone Number invalid");
                                        }
                                    } else {
                                        emerr.setText("Email invalid");
                                    }
                                }
                            } else {
                                uNerr.setText("Invalid UserName");
                            }
                            //To do code if username is taken
                        } else {
                            lNerr.setText("Invalid Name");
                        }
                    } else {
                        fNerr.setText("Invalid Name");
                    }
                } else {
                    fn.clear();
                    ln.clear();
                    un.clear();
                    addr.clear();
                    pass1.clear();
                    pass2.clear();
                    loc.setValue("");
                    fonNum.clear();
                    occup.setValue("");
                    rlnState.setValue("");
                    genda.setValue("");
                    birthDate.setValue(null);
                    deniedIcon.setVisible(false);
                    deniedIcon.setVisible(false);
                }
            });
        });

        BooleanBinding bb3 = new BooleanBinding() {
            {
                super.bind(fn.textProperty(), ln.textProperty(),
                        un.textProperty(),
                        pass1.textProperty(), pass2.textProperty(),
                        loc.valueProperty(), occup.valueProperty(),
                        rlnState.valueProperty(), genda.valueProperty()
                );
            }

            @Override
            protected boolean computeValue() {
                return (fn.getText().isEmpty()
                        || ln.getText().isEmpty()
                        || un.getText().isEmpty()
                        || pass1.getText().isEmpty()
                        || pass2.getText().isEmpty()
                        || loc.getSelectionModel().isEmpty()
                        || occup.getSelectionModel().isEmpty()
                        || rlnState.getSelectionModel().isEmpty()
                        || genda.getSelectionModel().isEmpty());
            }
        };

        submit.disableProperty().bind(bb3);

        GridPane grid = new GridPane();
        grid.setMinSize(350, 250);
        grid.setPadding(new Insets(10));
        grid.setVgap(15);
        grid.setHgap(5);
        grid.setAlignment(Pos.CENTER);

        ColumnConstraints column1 = new ColumnConstraints(130); // fixed for labels
        ColumnConstraints column2 = new ColumnConstraints(50, 130, 170); // min,pref,max
        ColumnConstraints column3 = new ColumnConstraints(70);
        column2.setHgrow(Priority.ALWAYS);
        grid.getColumnConstraints().addAll(column1, column2, column3);

        //adding nodes to grid
        grid.add(fName, 0, 0);
        grid.add(lName, 0, 1);
        grid.add(uName, 0, 2);
        grid.add(password1, 0, 3);
        grid.add(password2, 0, 4);
        grid.add(email, 0, 5);
        grid.add(phoneNum, 0, 6);
        grid.add(gender, 0, 7);
        grid.add(county, 0, 8);
        grid.add(occupation, 0, 9);
        grid.add(dob, 0, 10);
        grid.add(rlnshp, 0, 11);

        //adding input fields
        grid.add(fn, 1, 0);
        grid.add(ln, 1, 1);
        grid.add(un, 1, 2);
        grid.add(pass1, 1, 3);
        grid.add(pass2, 1, 4);
        grid.add(addr, 1, 5);
        grid.add(fonNum, 1, 6);
        grid.add(genda, 1, 7);
        grid.add(loc, 1, 8);
        grid.add(occup, 1, 9);
        grid.add(birthDate, 1, 10);
        grid.add(rlnState, 1, 11);

        grid.add(fNerr, 2, 0);
        grid.add(lNerr, 2, 1);
        grid.add(uNerr, 2, 2);
        grid.add(accessIndicator, 2, 4);
        grid.add(emerr, 2, 5);
        grid.add(foNerr, 2, 6);
        grid.add(doberr, 2, 10);

        grid.add(submit, 2, 12);
        GridPane.setHalignment(submit, HPos.RIGHT);
        grid.setId("child-style");

        File pic = new File("src\\images\\icon.jpg");
        String regImg = pic.toURI().toURL().toString();
        Image regir = new Image(regImg);
        Circle viuDis = new Circle(85,85,30);
        viuDis.setStroke(Color.SEAGREEN);
        viuDis.setFill(new ImagePattern(regir));
        viuDis.setEffect(new DropShadow(+25d, 0d, +2d, Color.DARKSEAGREEN));

        VBox separ2 = new VBox();
        Pane separa = new Pane();
        separa.setMinHeight(30);
        separ2.getChildren().addAll(separa, appTitle2);

        HBox topViu = new HBox(5);
        topViu.getChildren().addAll(viuDis, separ2);
        topViu.setPadding(new Insets(3));

        TitledPane tite = new TitledPane();
        tite.setText("Registration");
        tite.setContent(grid);

        root.setId("root-style");
        root.setCenter(tite);
        root.setTop(topViu);
        BorderPane.setAlignment(grid, CENTER);

        regWindow.setOnCloseRequest(e -> {
            e.consume();
            closeProgram();
        });
        regWindow.setScene(regScene);
        regWindow.show();
    }

    public static Boolean chkFName(String text) {
        Pattern p = Pattern.compile("[a-zA-Z]+");
        Matcher m = p.matcher(text);
        if (m.find() && m.group().equals(text)) {
            return true;
        } else {
            return false;
        }
    }

    public static Boolean chkLName(String text) {
        Pattern p = Pattern.compile("[a-zA-Z]+");
        Matcher m = p.matcher(text);
        if (m.find() && m.group().equals(text)) {
            return true;
        } else {
            return false;
        }
    }

    public static Boolean chkAddress(String text) {
        Pattern p = Pattern.compile("[a-zA-Z0-9][a-zA-Z0-9._]*@[a-zA-Z0-9]+([.][a-zA-Z]+)+");
        Matcher m = p.matcher(text);
        if (m.find() && m.group().equals(text)) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean chkPassWord(String text) {
        Pattern p = Pattern.compile("((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{6,18})");
        Matcher m = p.matcher(text);
        if (m.matches()) {
            return true;
        } else {
            return false;
        }
    }

    public static Boolean chkFonNum(String text) {
        if (text.matches("([0][7]\\d{8})")) {
            return true;
        }
        return false;
    }

    public static Boolean chkUName(String text) {
        if (text.matches("[a-zA-Z]+[a-zA-Z0-9]*")) {
            return true;
        }
        return false;
    }

    private static void sendRegDetails() {
        String fin = fn.getText();
        String lan = ln.getText();
        String usn = un.getText();
        String pass = pass1.getText();
        String add = addr.getText();
        String fon = fonNum.getText();
        String sex = genda.getSelectionModel().getSelectedItem();
        String plec = loc.getSelectionModel().getSelectedItem();
        String work = occup.getSelectionModel().getSelectedItem();
        String dob = String.valueOf(birthDate.getValue());
        String rln = rlnState.getSelectionModel().getSelectedItem();
        try {
            ServerConnector.getRegDetails(fin, lan, usn, pass, add, fon, sex, plec, work, dob, rln);
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(RegistrationWindow.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void showState(String show) {
        if (show.startsWith("/z/")) {
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Success!");
                alert.setHeaderText("Details saved");
                alert.setContentText("Your registration details have been saved successfully.");
                alert.showAndWait();
            });
        } else {
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Failed!");
                alert.setHeaderText("Details not saved");
                alert.setContentText("Please send your registration details again.");
                alert.showAndWait();
            });
        }
    }

    private static void closeProgram() {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation Dialog");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to exit?");
            Optional<ButtonType> action = alert.showAndWait();
            if (action.get() == ButtonType.OK) {
                regWindow.close();
            }
        });
    }
}
