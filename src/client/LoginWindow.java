package client;

import static client.ChatWindow.user;
import static client.ChatWindow.userID;
import interactions.Message;
import static interactions.MessageType.DISCONNECTED;
import static interactions.MessageType.UPDATE;
import java.io.*;
import java.util.Optional;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.*;
import static javafx.application.Application.launch;
import javafx.beans.property.*;
import javafx.event.ActionEvent;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.effect.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.*;
import javafx.stage.Stage;
import javafx.util.Duration;

public class LoginWindow extends Application {

    private static BorderPane root;
    private static Stage loginWindow;
    private static Scene loginScene;
    public static ComboBox<String> setServer;//create a custom control
    private static Button login;
    private static Hyperlink createAcctLink, passLink;
    private static Label conErr, vUser, vPass;
    public static BooleanProperty VALID = new SimpleBooleanProperty(false);
    private static TextField userNm;
    private static PasswordField passWd;
    private static Text mBr, fGt;
    private static Reflection r1;
    private static Glow g1;
    private static TitledPane tp;
    private static VBox vb;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryWindow) throws Exception {
        loginWindow = primaryWindow;
        loginWindow.setTitle("Video Conference");
        File iconImg = new File("src/images/icon.jpg");
        String iconLoc = iconImg.toURI().toURL().toString();
        loginWindow.getIcons().add(new Image(iconLoc));
        
        loginWindow.setResizable(false);
        
        root = new BorderPane();
        loginScene = new Scene(root, 540, 435);
        loginScene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());

        r1 = new Reflection();
        r1.setFraction(0.8F);
        r1.setTopOffset(5);

        g1 = new Glow();
        g1.setLevel(0.7);

        setServer();
    }

    public static void setServer() throws Exception {
        setServer = new ComboBox<>();
        setServer.getItems().addAll("Connect", "Disconnect");
        setServer.setValue("Disconnect");
        setServer.setId("combo-off");
        setServer.setMinWidth(120);

        vUser = new Label();
        vPass = new Label();

        createLoginWindow();
    }

    public static void verify(String allow, boolean correct) {
        String connection = "/c/" + userNm.getText() + "/e/";
        if (allow.startsWith("/y/")) {
            loginWindow.close();
            try {
                new Thread(new ServerConnector()).start();
                new ChatWindow(userNm.getText());
                ServerConnector.connect(connection);
                Message update = new Message();
                update.setUsername(userNm.getText());
                update.setType(UPDATE);
                update.setMsg("joined the chat");
                ServerConnector.send(update);
            } catch (Exception ex) {
                System.out.println(ex);
            }
        } else if (allow.startsWith("/x/")) {
            String msg = allow.substring(3, allow.length());
            vUser.setText("");
            vPass.setText(msg);
            vPass.setTextFill(Color.RED);
            //err voice
        } else {
            String msg = allow.substring(3, allow.length());
            vPass.setText("");
            vUser.setText(msg);
            vUser.setTextFill(Color.RED);
            //err voice
        }
    }

    public static void createLoginWindow() throws Exception {

        Text appTitle;
        File myLogo = new File("src\\images\\icon.jpg");
        String myLogoUrl = myLogo.toURI().toURL().toString();
        Image logo = new Image(myLogoUrl);
        Circle deLogo = new Circle(85,85,30);
        deLogo.setStroke(Color.SEAGREEN);
        deLogo.setFill(new ImagePattern(logo));
        deLogo.setEffect(new DropShadow(+25d, 0d, +2d, Color.DARKSEAGREEN));

        conErr = new Label();
        setServer.setOnAction(e -> {
            String val = setServer.getValue();
            if ("Connect".equals(val)) {
                boolean connect = ServerSetter.display();
                if (connect == false) {
                    setServer.setValue("Disconnect");
                    setServer.setId("combo-off");
                    conErr.setText("Connection failed");
                    conErr.setStyle("-fx-text-fill: red");
                    //display text for a few seconds
                } else {
                    setServer.setId("combo-on");
                    setServer.setEffect(g1);
                    conErr.setText("Connection success");
                    conErr.setStyle("-fx-text-fill: green");
                }
            } else if ("Disconnect".equals(val)) {
                try {
                    Message gout = new Message();
                    gout.setMsg("/d/" + userID + "/e/");
                    gout.setUsername(user);
                    gout.setType(DISCONNECTED);
                    ServerConnector.send(gout);
                   // ServerConnector.closeConnection();
                    setServer.setValue("Disconnect");
                    setServer.setId("combo-off");
                    conErr.setText("Connection stopped");
                    conErr.setStyle("-fx-text-fill: red");
                } catch (IOException ex) {
                    Platform.runLater(() -> {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Connection Error!!!");
                    alert.setHeaderText("Server not Found");
                    alert.setContentText("Please check connection with server.");
                    alert.showAndWait();
                });
                }
            }
        });

        //set 3d text
        appTitle = new Text("Cocochut");
        appTitle.setId("app-title");

        Pane separator = new Pane();
        separator.setPrefWidth(1100);
        separator.setPrefHeight(40);

        Pane separator2 = new Pane();
        separator2.setPrefHeight(30);

        VBox sper = new VBox();
        sper.getChildren().addAll(separator2, appTitle);

        VBox kona = new VBox();
        kona.getChildren().addAll(setServer, conErr);

        HBox hb1 = new HBox();
        hb1.getChildren().addAll(deLogo, sper, separator, kona);
        hb1.setSpacing(5);
        hb1.setPadding(new Insets(3));

        //mIDDLE PART
        File userIcon = new File("src\\images\\user.png");
        String localurl = userIcon.toURI().toURL().toString();
        Image avatar = new Image(localurl, true);
        ImageView avatarView = new ImageView(avatar);
        avatarView.setFitHeight(100);
        avatarView.setFitWidth(100);
        avatarView.setEffect(new DropShadow(+25d, 0d, +2d, Color.CHOCOLATE));
        Text instr = new Text("Enter your login credentials");
        instr.setFont(Font.font("serif", FontWeight.BOLD, 20));
        instr.setEffect(r1);

        //Login area        
        userNm = new TextField();
        userNm.setMinWidth(190);
        userNm.setMaxWidth(190);
        userNm.setPromptText("              User Name");
        passWd = new PasswordField();
        passWd.setId("pass-word");
        passWd.setMinWidth(190);
        passWd.setMaxWidth(190);
        passWd.setPromptText("               Password");
        passWd.disableProperty().bind(userNm.textProperty().isEmpty());

        login = new Button("Login");
        login.setMinWidth(190);
        login.disableProperty().bind(userNm.textProperty().isEmpty().or(passWd.textProperty().isEmpty()));
        
        login.setOnAction(e -> {
            try {
                ServerConnector.getLoginDetails(userNm.getText(), passWd.getText());
            } catch (IOException | ClassNotFoundException ex) {
                Logger.getLogger(LoginWindow.class.getName()).log(Level.SEVERE, null, ex);
            } 
        });

        mBr = new Text("Are you a member? ");
        fGt = new Text("Forgot Password? ");
        passLink = new Hyperlink("Click here...");
        passLink.setOnAction(e -> {
            userNm.clear();
            passWd.clear();
        });

        createAcctLink = new Hyperlink("Create account...");
        createAcctLink.setOnAction((ActionEvent e) -> {
            userNm.clear();
            passWd.clear();
            try {
                loginWindow.close();
                new RegistrationWindow();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        vUser.setText("");
        
        HBox UN = new HBox(5);
        Pane lebolda = new Pane();
        lebolda.setMinWidth(160);
        lebolda.getChildren().add(vUser);
        Pane lebolda2 = new Pane();
        lebolda2.setMinWidth(160);
        UN.getChildren().addAll(lebolda2,userNm,lebolda);
        UN.setAlignment(Pos.CENTER);
        
        HBox PN = new HBox(5);
        Pane pebolda = new Pane();
        pebolda.setMinWidth(160);
        pebolda.getChildren().add(vPass);
        Pane pebolda2 = new Pane();
        pebolda2.setMinWidth(160);
        PN.getChildren().addAll(pebolda2,passWd,pebolda);
        PN.setAlignment(Pos.CENTER);

        HBox hBox1 = new HBox();
        hBox1.getChildren().addAll(mBr, createAcctLink);
        hBox1.setAlignment(Pos.CENTER);

        HBox hBox2 = new HBox();
        hBox2.getChildren().addAll(fGt, passLink);
        hBox2.setAlignment(Pos.CENTER);

        vb = new VBox(12);
        vb.setAlignment(Pos.CENTER);
        vb.getChildren().addAll(avatarView, instr, UN, PN,login, hBox1, hBox2);
        vb.setId("child-style");
        tp = new TitledPane("Login", vb);

        root.setTop(hb1);
        root.setCenter(tp);
        root.setId("root-style");

        int numberOfSquares = 60;
        while (numberOfSquares > 0) {
            generateAnimation();
            numberOfSquares--;
        }

        loginWindow.setOnCloseRequest(e -> {
            e.consume();
            closeProgram();
        });
        loginWindow.setScene(loginScene);
        loginWindow.show();
    }

    private static void closeProgram() {
        Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmation Dialog");
                alert.setHeaderText(null);
                alert.setContentText("Are you sure you want to exit?");
                Optional <ButtonType> action = alert.showAndWait();
                if(action.get() == ButtonType.OK){
                    loginWindow.close();
                }
            });
        
    }

    /* This method is used to generate the animation on the login window, It will generate random ints to determine
     * the size, speed, starting points and direction of each square.
     */
    public static void generateAnimation() {
        Random rand = new Random();
        int sizeOfSqaure = rand.nextInt(50) + 1;
        int speedOfSqaure = rand.nextInt(10) + 5;
        int startXPoint = rand.nextInt(420);
        int startYPoint = rand.nextInt(350);
        int direction = rand.nextInt(5) + 1;

        KeyValue moveXAxis = null;
        KeyValue moveYAxis = null;
        Rectangle r1 = null;

        switch (direction) {
            case 1:
                // MOVE LEFT TO RIGHT
                r1 = new Rectangle(0, startYPoint, sizeOfSqaure, sizeOfSqaure);
                moveXAxis = new KeyValue(r1.xProperty(), 350 - sizeOfSqaure);
                break;
            case 2:
                // MOVE TOP TO BOTTOM
                r1 = new Rectangle(startXPoint, 0, sizeOfSqaure, sizeOfSqaure);
                moveYAxis = new KeyValue(r1.yProperty(), 420 - sizeOfSqaure);
                break;
            case 3:
                // MOVE LEFT TO RIGHT, TOP TO BOTTOM
                r1 = new Rectangle(startXPoint, 0, sizeOfSqaure, sizeOfSqaure);
                moveXAxis = new KeyValue(r1.xProperty(), 350 - sizeOfSqaure);
                moveYAxis = new KeyValue(r1.yProperty(), 420 - sizeOfSqaure);
                break;
            case 4:
                // MOVE BOTTOM TO TOP
                r1 = new Rectangle(startXPoint, 420 - sizeOfSqaure, sizeOfSqaure, sizeOfSqaure);
                moveYAxis = new KeyValue(r1.xProperty(), 0);
                break;
            case 5:
                // MOVE RIGHT TO LEFT
                r1 = new Rectangle(420 - sizeOfSqaure, startYPoint, sizeOfSqaure, sizeOfSqaure);
                moveXAxis = new KeyValue(r1.xProperty(), 0);
                break;
            case 6:
                //MOVE RIGHT TO LEFT, BOTTOM TO TOP
                r1 = new Rectangle(startXPoint, 0, sizeOfSqaure, sizeOfSqaure);
                moveXAxis = new KeyValue(r1.xProperty(), 350 - sizeOfSqaure);
                moveYAxis = new KeyValue(r1.yProperty(), 420 - sizeOfSqaure);
                break;
            default:
                System.out.println("default");
        }

        r1.setFill(Color.SNOW);
        r1.setOpacity(0.3);

        KeyFrame keyFrame = new KeyFrame(Duration.millis(speedOfSqaure * 1000), moveXAxis, moveYAxis);
        Timeline timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.setAutoReverse(true);
        timeline.getKeyFrames().add(keyFrame);
        timeline.play();
        root.getChildren().add(root.getChildren().size() - 1, r1);
    }
}
