package client;

import interactions.Message;
import interactions.MessageType;
import static interactions.MessageType.*;
import interactions.User;
import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.*;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import static javafx.scene.layout.Region.USE_PREF_SIZE;
import javafx.scene.media.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.*;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.util.Callback;

public class ChatWindow {

    private static Stage chatWindow;
    private static Scene chatScene;
    private static BorderPane root;
    private static ComboBox<String> onOff;
    private static TextArea gChat;
    public static String user, userID, fileLoc;
    private static TextField sach;
    private static FileChooser profImage;
    private static File profile;
    private static Image userImg;
    private static ImageView theUserImg;
    private Label onlineCountLabel;
    private static ListView userList;
    private static TabPane innerT1;
    private static TextArea[] conversations;
    private static int msgboxes = 0;
    private static ListView[] chatSpaces;

    private static void changeImage() {
        FileChooser fc = new FileChooser();
        fc.setInitialDirectory(new File("C:\\Users"));
        fc.getExtensionFilters().addAll(new ExtensionFilter("IMAGE Files", "*.png", "*.jpg", "*.gif"));
        profile = fc.showOpenDialog(null);
        if (profile != null) {
            Image userImg1 = new Image(profile.toURI().toString(), 70, 70, true, true);
            theUserImg.setImage(userImg1);
        }
    }

    private static void defaultImage() {
        profile = new File("src\\images\\default.png");
        Image userImg1 = new Image(profile.toURI().toString());
        theUserImg.setImage(userImg1);
    }

    public ChatWindow(String user) throws Exception {
        chatWindow = new Stage();
        chatWindow.setTitle("Cocochut");
        File iconImg = new File("src/images/icon.jpg");
        String iconLoc = iconImg.toURI().toURL().toString();
        chatWindow.getIcons().add(new Image(iconLoc));
        root = new BorderPane();
        chatScene = new Scene(root, 750, 650);
        chatScene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());

        onOff = new ComboBox<>();
        this.user = user;

        gChat = new TextArea();
        userList = new ListView();

        profile = new File("src\\images\\default.png");
        String prof = profile.toURI().toURL().toString();
        userImg = new Image(prof);
        theUserImg = new ImageView(userImg);
        theUserImg.setFitHeight(70);
        theUserImg.setFitWidth(70);
        conversations = new TextArea[20];
        chatSpaces = new ListView[20];

        createChatWindow();
    }

    private static void createChatWindow() throws Exception {
        root.setPadding(new Insets(2));

        onOff.setId("combo-on");
        onOff.getItems().addAll("ONLINE", "BUSY");
        onOff.setValue("ONLINE");
        onOff.setMinWidth(120);
        onOff.setOnAction(e -> {
            String action = onOff.getValue();
            if ("ONLINE".equals(action)) {
                //continue receiving clients
            } else if ("BUSY".equals(action)) {
                //If busy refuse connections else accept
            }
        });

        profImage = new FileChooser();
        profImage.getExtensionFilters().addAll(new ExtensionFilter("Images", "*.png", "*.jpg", "*.gif"));

        Text usnm = new Text("");
        usnm.setText(user);
        usnm.setFont(Font.font("Serif", FontWeight.BOLD, 20));

        File fotoChanger1 = new File("src\\images\\pic.png");
        String fotoChanger2 = fotoChanger1.toURI().toURL().toString();
        Image fotoChanger3 = new Image(fotoChanger2);
        ImageView fotoChanger4 = new ImageView(fotoChanger3);
        fotoChanger4.setFitHeight(20);
        fotoChanger4.setFitWidth(20);

        ComboBox<String> comboBox = new ComboBox();
        comboBox.getItems().addAll("Upload Photo", "Remove Photo");
        comboBox.setPromptText("Choose");
        comboBox.setMinWidth(100);
        comboBox.setVisible(false);

        fotoChanger4.setOnMouseClicked(e -> {
            comboBox.setVisible(true);
            comboBox.setOnAction(ec -> {
                String option = comboBox.getValue();
                if ("Upload Photo".equals(option)) {
                    comboBox.setVisible(false);
                    changeImage();
                } else {
                    defaultImage();
                    comboBox.setVisible(false);
                }
            });
        });

        VBox kusema = new VBox(17);
        HBox chenj = new HBox(1);
        chenj.getChildren().addAll(fotoChanger4, comboBox);
        kusema.getChildren().addAll(chenj, usnm);

        Pane jiseparator = new Pane();
        jiseparator.setPrefSize(1200, 50);

        HBox top = new HBox(3);
        top.getChildren().addAll(theUserImg, kusema, jiseparator, onOff);
        top.setPadding(new Insets(2));

        TabPane tPane = new TabPane();
        tPane.setPadding(new Insets(2));

        // .....................Start Text chat tab.............................
        Tab t1 = new Tab("Text Chat");

        innerT1 = new TabPane();
        innerT1.setSide(Side.LEFT);
        innerT1.setRotateGraphic(true);
        innerT1.setTabMinHeight(80);
        innerT1.setTabMaxHeight(80);

        t1.setContent(innerT1);
        t1.setClosable(false);
        //-----------------------------End Text chat tab------------------------

        //--------------------------Begin Voice chat tab........................
        Tab t2 = new Tab("Voice Chat");
        t2.setClosable(false);

        TabPane int2 = new TabPane();
        int2.setRotateGraphic(true);
        int2.setTabMinHeight(80);
        int2.setTabMaxHeight(80);
        int2.setSide(Side.LEFT);

        Tab voiChat1 = new Tab();
        Label l3 = new Label("Voice 1");
        l3.setRotate(90);
        StackPane stp3 = new StackPane(new Group(l3));
        stp3.setRotate(90);
        voiChat1.setGraphic(stp3);

        BorderPane voiceRoot = new BorderPane();

        Media voim;
        MediaPlayer voimp;
        MediaView voimv;
        Slider voiVolume;
        Button startVoi, pauseVoi, stopVoi, muteVoi, unmuteVoi, recordVoi;

        String voiPath = new File("src/images/starUML.mp4").getAbsolutePath();
        voim = new Media(new File(voiPath).toURI().toURL().toString());
        voimp = new MediaPlayer(voim);
        voimv = new MediaView();
        voimv.setMediaPlayer(voimp);

        DoubleProperty voiwidth = voimv.fitWidthProperty();
        DoubleProperty voiheight = voimv.fitHeightProperty();
        voiwidth.bind(Bindings.selectDouble(voimv.sceneProperty(), "width"));
        voiheight.bind(Bindings.selectDouble(voimv.sceneProperty(), "height"));

        Rectangle rect = new Rectangle(18, 18);
        rect.setFill(Color.RED);

        startVoi = new Button("Start");
        startVoi.setOnAction(e -> {
            voimp.play();
            voimp.setRate(1);
        });
        pauseVoi = new Button("Pause");
        pauseVoi.setOnAction(e -> voimp.pause());
        stopVoi = new Button("Stop");
        stopVoi.setOnAction(e -> voimp.stop());
        muteVoi = new Button("Mute");
        //.setOnAction(e -> mp.play());
        unmuteVoi = new Button("Unmute");
        //.setOnAction(e -> mp.play());
        recordVoi = new Button();
        recordVoi.setGraphic(rect);
        //.setOnAction(e -> mp.play());
        voiVolume = new Slider(0, 100, 0);

        HBox voiControls = new HBox(10);
        voiControls.setAlignment(Pos.CENTER);
        voiControls.setPadding(new Insets(5));
        voiControls.getChildren().addAll(muteVoi, stopVoi, startVoi, pauseVoi, unmuteVoi, recordVoi, voiVolume);

        voiVolume.setValue(voimp.getVolume() * 100);
        voiVolume.valueProperty().addListener(e -> {
            voimp.setVolume(voiVolume.getValue() / 100);
        });

        voiceRoot.setCenter(voimv);
        voiceRoot.setBottom(voiControls);
        voiChat1.setContent(voiceRoot);

        int2.getTabs().addAll(voiChat1);

        t2.setContent(int2);
        //TO DO CODE
        //---------------------------------------------End Voice chat tab-------

        //---------Video chat tab-----------------------------------------------
        Tab t3 = new Tab("Video Chat");
        t3.setClosable(false);

        TabPane int3 = new TabPane();
        int3.setRotateGraphic(true);
        int3.setSide(Side.LEFT);
        int3.setTabMinHeight(80);
        int3.setTabMaxHeight(80);

        Tab vidChat1 = new Tab();

        Label l4 = new Label("Beste 1");
        l4.setRotate(90);
        StackPane stp4 = new StackPane(new Group(l4));
        stp4.setRotate(90);
        vidChat1.setGraphic(stp4);

        BorderPane vidRoot = new BorderPane();

        Media m;
        MediaPlayer mp;
        MediaView mv;
        Slider vidVolume;
        Button startVid, pauseVid, stopVid, muteVid, unmuteVid, recordVid;

        String vidPath = new File("src/images/starUML.mp4").getAbsolutePath();
        m = new Media(new File(vidPath).toURI().toURL().toString());
        mp = new MediaPlayer(m);
        mv = new MediaView();
        mv.setMediaPlayer(mp);

        DoubleProperty width = mv.fitWidthProperty();
        DoubleProperty height = mv.fitHeightProperty();
        width.bind(Bindings.selectDouble(mv.sceneProperty(), "width"));
        height.bind(Bindings.selectDouble(mv.sceneProperty(), "height"));

        Rectangle recte = new Rectangle(18, 18);
        rect.setFill(Color.RED);

        startVid = new Button("Start");
        startVid.setOnAction(e -> {
            mp.play();
            mp.setRate(1);
        });
        pauseVid = new Button("Pause");
        pauseVid.setOnAction(e -> mp.pause());
        stopVid = new Button("Stop");
        stopVid.setOnAction(e -> mp.stop());
        muteVid = new Button("Mute");
        //.setOnAction(e -> mp.play());
        unmuteVid = new Button("Unmute");
        //.setOnAction(e -> mp.play());
        recordVid = new Button();
        recordVid.setGraphic(rect);
        //.setOnAction(e -> mp.play());
        vidVolume = new Slider(0, 100, 0);

        HBox vidControls = new HBox(10);
        vidControls.setAlignment(Pos.CENTER);
        vidControls.setPadding(new Insets(5));
        vidControls.getChildren().addAll(muteVid, stopVid, startVid, pauseVid, unmuteVid, recordVid, vidVolume);

        vidVolume.setValue(mp.getVolume() * 100);
        vidVolume.valueProperty().addListener(e -> {
            mp.setVolume(vidVolume.getValue() / 100);
        });

        vidRoot.setCenter(mv);
        vidRoot.setBottom(vidControls);
        vidChat1.setContent(vidRoot);

        int3.getTabs().addAll(vidChat1);
        int3.setSide(Side.LEFT);
        t3.setContent(int3);
        //TO DO CODE
        // -------------------End of Video chat tab---------------==============
        /// -------------------------Chat rooms tab---------------------------==
        Tab t4 = new Tab("Chat rooms");
        t4.setClosable(false);

        //This holds everything in the group tab
        BorderPane hol = new BorderPane();

        sach = new TextField();
        sach.setPromptText("Search for groups...");
        sach.setMaxWidth(150);
        sach.setOnAction(e -> {
            search(sach.getText());
            sach.clear();
        });

        //This is what is in the subpane
        TabPane innerT4 = new TabPane();
        innerT4.setSide(Side.LEFT);
        innerT4.setRotateGraphic(true);
        innerT4.setTabMinHeight(70);
        innerT4.setTabMaxHeight(70);

        Label l = new Label("Group 1");
        l.setRotate(90);
        StackPane stp = new StackPane(new Group(l));
        stp.setRotate(90);

        //The first subtab in the place
        Tab grup1 = new Tab();
        grup1.setGraphic(stp);

        TextArea gEmoji;
        ToggleButton joinGrp, leaveGrp;
        ToggleGroup leftGrp;
        Button sendGtxt, sendGfile;
        TableView<String> gMembers = new TableView<>();
        Text emTitle;
        TextField grpMsg, grpFile;

        gChat.setEditable(false);
        gEmoji = new TextArea();
        gEmoji.setEditable(false);

        leftGrp = new ToggleGroup();
        joinGrp = new ToggleButton("Join");
        leaveGrp = new ToggleButton("Leave");

        joinGrp.setToggleGroup(leftGrp);
        joinGrp.setOnAction(e -> {
            try {
                Message join = new Message();
                join.setMsg("/g1/");
                join.setUsername(user);
                join.setType(MessageType.GROUPMESSAGE);
                ServerConnector.send(join);
                joinGrp.setDisable(true);
                leaveGrp.setDisable(false);
            } catch (IOException ex) {
                Logger.getLogger(ChatWindow.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        leaveGrp.setToggleGroup(leftGrp);
        leaveGrp.setOnAction(e -> {
            joinGrp.setDisable(false);
            leaveGrp.setDisable(true);
        });

        sendGfile = new Button("Send file");
        sendGfile.setOnAction(e -> {
            //TO DO CODE
        });

        gMembers = new TableView<>();
        emTitle = new Text("Emoji");
        //style the text

        grpMsg = new TextField();
        grpMsg.setPromptText("Enter message...");

        sendGtxt = new Button("Send Text");
        sendGtxt.setOnAction(e -> {
            grpconsole(grpMsg.getText());
            grpMsg.clear();
        });

        grpMsg.setOnAction(ge -> {
            grpconsole(grpMsg.getText());
            grpMsg.clear();
            //TO DO CODE
        });

        grpFile = new TextField();
        grpFile.setPromptText("Choose file...");
        grpFile.setEditable(false);

        //holds the chat area and textfields
        VBox chatPlace = new VBox(5);

        HBox matext = new HBox();
        matext.getChildren().addAll(grpMsg, sendGtxt);
        matext.setSpacing(5);
        HBox.setHgrow(grpMsg, Priority.ALWAYS);

        HBox mafile = new HBox();
        File kapic2 = new File("src\\images\\search.png");
        String fileIcon2 = kapic2.toURI().toURL().toString();
        Image kapiki2 = new Image(fileIcon2);
        ImageView grpikiViu = new ImageView(kapiki2);
        grpikiViu.setFitWidth(20);
        grpikiViu.setFitHeight(20);
        grpikiViu.setOnMouseClicked(e -> {
            String msg, gruputxt;
            msg = FileManager.selectFile();
            grpFile.setText(msg);
            gruputxt = user + ": " + msg;
            sendGfile.setOnAction(se -> {
                gChat.appendText(gruputxt + "\n\r");
                grpFile.clear();
                //TO DO CODE
            });
            grpFile.setOnAction(grpe -> {
                gChat.appendText(gruputxt + "\n\r");
                grpFile.clear();
                //TO DO CODE
            });
        });

        mafile.getChildren().addAll(grpikiViu, grpFile, sendGfile);
        mafile.setSpacing(5);
        HBox.setHgrow(grpFile, Priority.ALWAYS);

        // the two buttons
        VBox grupSay = new VBox();
        grupSay.getChildren().addAll(joinGrp, leaveGrp);
        grupSay.setSpacing(20);
        joinGrp.setMinWidth(85);
        leaveGrp.setMinWidth(85);

        HBox chini = new HBox(10);
        chini.getChildren().addAll(gEmoji, grupSay);
        // finished the lower section

        chatPlace.getChildren().addAll(gChat, matext, mafile, emTitle, chini);
        chatPlace.setPadding(new Insets(3));
        VBox.setVgrow(gChat, Priority.ALWAYS);
        gChat.setMinHeight(250);
        //added to the chatplace

        hol.setPadding(new Insets(3));
        hol.setRight(gMembers);
        hol.setCenter(chatPlace);
        hol.centerProperty();

        grup1.setContent(hol);
        innerT4.getTabs().addAll(grup1);
        t4.setContent(innerT4);
        // ----------------------End Chat tab-----------------------------------
        //------------------Contacts tab========================================
        Tab t5 = new Tab("Contacts");
        t5.setClosable(false);
        //TO DO CODE

        ScrollPane scrola = new ScrollPane();

        userList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        userList.setPrefWidth(1800);
        userList.setPrefHeight(1200);

        scrola.setContent(userList);
        scrola.setFitToHeight(true);
        scrola.setFitToWidth(true);
        scrola.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrola.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

        BorderPane cons = new BorderPane();
        cons.setCenter(scrola);
        //cons.setBottom(hbo);

        t5.setContent(cons);
        //---------- ------------------end of Contacts tab=================

        tPane.getTabs().addAll(t1, t2, t3, t4, t5);
        tPane.setId("child-style");

        Hyperlink cont = new Hyperlink("Contact");
        Hyperlink blog = new Hyperlink("Blog");
        Hyperlink edit = new Hyperlink("Edit Profile");
        Hyperlink logOff = new Hyperlink("Log out");
        logOff.setOnAction(e -> {
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmation Dialog");
                alert.setHeaderText(null);
                alert.setContentText("Log you out?");
                Optional<ButtonType> action = alert.showAndWait();
                if (action.get() == ButtonType.OK) {
                    try {
                        Message gout = new Message();
                        gout.setMsg("/l/" + userID + "/e/");
                        gout.setUsername(user);
                        gout.setType(DISCONNECTED);
                        ServerConnector.send(gout);
                        ServerConnector con = new ServerConnector();
                        con.closeConnection();
                        chatWindow.close();
                        LoginWindow.setServer();
                    } catch (Exception ex) {
                        System.out.println(ex);
                    }
                }
            });
        });

        edit.setOnAction(e -> {
            EditProfile.edit();
        });

        HBox boto = new HBox();
        boto.setSpacing(10);
        boto.getChildren().addAll(cont, blog, edit, logOff);
        boto.setAlignment(Pos.BASELINE_RIGHT);

        root.setId("root-style");
        root.setBottom(boto);
        root.setCenter(tPane);
        root.setTop(top);

        chatWindow.setOnCloseRequest(e -> {
            e.consume();
            closeProgram();
        });
        chatWindow.setScene(chatScene);
        chatWindow.show();
    }

    private static void grpconsole(String gtext) {
        String text = user + " >> " + gtext;
        gChat.appendText(text + "\n\r");
        String sText = "/g/" + gtext;
        Message grupMessage = new Message();
        grupMessage.setUsername(user);
        grupMessage.setType(MessageType.GROUPMESSAGE);
        grupMessage.setMsg(sText);
        try {
            ServerConnector.send(grupMessage);
        } catch (IOException ex) {
            Logger.getLogger(ChatWindow.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static class TextPlace extends ListCell<Message> {

        @Override
        public void updateItem(Message item, boolean empty) {
            super.updateItem(item, empty);

            if (item != null) {
                HBox hBox = new HBox(10);
                String msg = item.getMsg();
                String pmsg = msg.substring(3, msg.length());
                Text sender = new Text(item.getUsername());
                ////////////////////////////////////
                Platform.runLater(() -> {
                    for (int i = 0; i <= chatSpaces.length; i++) {
                        if (chatSpaces[i] != null) {
                            if (chatSpaces[i].getId().equals(item.getUsername())) {
                                hBox.getChildren().addAll(sender);
                                conversations[i].appendText(item.getUsername() + " >> " + pmsg + "\n\r");
                                break;
                            }
                        } else if (conversations[i] == null) {
                            innerT1.getTabs().add(addTextpane(item.getUsername()));
                            innerT1.getSelectionModel().selectLast();
                            conversations[i].appendText(item.getUsername() + " >> " + pmsg + "\n\r");
                            break;
                        }

                    }
                });

                /////////////////////////////////////
                //if user is me
                hBox.setAlignment(Pos.TOP_RIGHT);
                //else
                hBox.setAlignment(Pos.TOP_LEFT);
            } else {
                setGraphic(null);
            }

        }
    }

    public static void inPMsg(Message text) {
        String msg = text.getMsg();
        String pmsg = msg.substring(3, msg.length());
        Platform.runLater(() -> {
            for (int i = 0; i <= conversations.length; i++) {
                if (conversations[i] != null) {
                    if (conversations[i].getId().equals(text.getUsername())) {
                        conversations[i].appendText(text.getUsername() + " >> " + pmsg + "\n\r");
                        break;
                    }
                } else if (conversations[i] == null) {
                    innerT1.getTabs().add(addTextpane(text.getUsername()));
                    innerT1.getSelectionModel().selectLast();
                    conversations[i].appendText(text.getUsername() + " >> " + pmsg + "\n\r");
                    break;
                }

            }
        });
    }

    public static void inGrpMsg(String text) {
        Platform.runLater(() -> {
            gChat.appendText(text + "\n\r");
        });
    }

    private static void search(String text) {
        //to do code for searching the element
    }

    private static void closeProgram() {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation Dialog");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to exit?");
            Optional<ButtonType> action = alert.showAndWait();
            if (action.get() == ButtonType.OK) {
                try {
                    ServerConnector con = new ServerConnector();
                    con.closeConnection();
                } catch (IOException ex) {
                    Logger.getLogger(ChatWindow.class.getName()).log(Level.SEVERE, null, ex);
                }
                chatWindow.close();
            }
        });

    }

    public static void setUserID(String id) {
        userID = id;
        System.out.println("user id set to: " + id);
    }

    public static String getUserID() {
        return userID;
    }

    public void setOnlineLabel(String usercount) {
        Platform.runLater(() -> onlineCountLabel.setText(usercount));
    }

    public static void setUserList(Message message) {
        Platform.runLater(() -> {
            ObservableList<User> users = FXCollections.observableList(message.getUsers());
            userList.setItems(users);
            userList.setCellFactory(new CellRenderer());
            //setOnlineLabel(String.valueOf(message.getUserlist().size()));
        });

    }

    public static Tab addTextpane(String header) {
        Tab msgTab = new Tab();
        Label l1 = new Label(header);
        l1.setRotate(90);
        StackPane stp1 = new StackPane(new Group(l1));
        stp1.setRotate(90);
        msgTab.setGraphic(stp1);
        //Layout of the nodes in the tab f1
        VBox inTab = new VBox(5);
        inTab.setAlignment(Pos.TOP_LEFT);
        inTab.setPadding(new Insets(5));

        conversations[msgboxes] = new TextArea();
        conversations[msgboxes].setId(header);
        conversations[msgboxes].setEditable(false);
        conversations[msgboxes].setPrefColumnCount(30);
        conversations[msgboxes].setPrefWidth(800);
        conversations[msgboxes].setPrefRowCount(20);
        conversations[msgboxes].setWrapText(true);
        conversations[msgboxes].setMaxWidth(900);
        msgboxes++;

        TextArea emoji = new TextArea();
        emoji.setEditable(false);

        TextField sendtxt = new TextField();
        String newtxt;
        newtxt = "@" + header;

        sendtxt.setOnAction(e -> {
            for (int i = 0; i <= conversations.length; i++) {
                if (conversations[i].getId().equals(header)) {
                    Message createMessage = new Message();
                    createMessage.setUsername(user);
                    createMessage.setType(MessageType.PRIVATEMESSAGE);
                    conversations[i].appendText(user + " >> " + sendtxt.getText() + "\n\r");
                    createMessage.setMsg(newtxt + " " + sendtxt.getText());
                    try {
                        ServerConnector.send(createMessage);
                        System.out.println(newtxt + " " + sendtxt.getText());
                        sendtxt.clear();
                    } catch (IOException ex) {
                        Logger.getLogger(ChatWindow.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    break;
                }
            }

        });

        Button sText = new Button("Send Text");
        sText.setOnAction(e -> {
            for (int i = 0; i <= conversations.length; i++) {
                if (conversations[i].getId().equals(header)) {
                    Message createMessage = new Message();
                    createMessage.setUsername(user);
                    createMessage.setType(MessageType.PRIVATEMESSAGE);
                    conversations[i].appendText(user + " >> " + sendtxt.getText() + "\n\r");
                    createMessage.setMsg(newtxt + " " + sendtxt.getText());
                    try {
                        ServerConnector.send(createMessage);
                        sendtxt.clear();
                    } catch (IOException ex) {
                        Logger.getLogger(ChatWindow.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    break;
                }
            }

        });

        TextField sendfile = new TextField();
        Text emoticon = new Text("Emoji");
        Button sFile = new Button("Send File");

        Button buzz = new Button("Buzz...");
        buzz.setMinWidth(70);
        buzz.setOnAction(e -> {
            //TO DO CODE
        });

        HBox theText = new HBox(10);
        theText.getChildren().addAll(sendtxt, sText);
        HBox.setHgrow(sendtxt, Priority.ALWAYS);
        sendtxt.setMaxWidth(600);

        File kapic = new File("src\\images\\search.png");
        String fileIcon = kapic.toURI().toString();
        Image kapiki = new Image(fileIcon);
        ImageView kapikiViu = new ImageView(kapiki);
        kapikiViu.setFitWidth(20);
        kapikiViu.setFitHeight(20);
        kapikiViu.setOnMouseClicked(e -> {

            fileLoc = FileManager.selectFile();
            sendfile.setText(fileLoc);
            sFile.setOnAction(se -> {
                sendfile.setText("");

                fileLoc = "/fl/" + fileLoc;
                //  ServerConnector.send(fileLoc);
                //TO DO CODE
            });
        });

        HBox theFile = new HBox();
        theFile.setSpacing(10);
        theFile.getChildren().addAll(kapikiViu, sendfile, sFile);
        HBox.setHgrow(sendfile, Priority.ALWAYS);
        sendfile.setMaxWidth(600);
        sendfile.setEditable(false);
        HBox.setMargin(kapikiViu, new Insets(1));

        HBox theFun = new HBox();
        theFun.setSpacing(10);
        theFun.getChildren().addAll(emoji, buzz);
        HBox.setHgrow(emoji, Priority.ALWAYS);
        emoji.setMaxWidth(600);

        for (int i = 0; i <= conversations.length; i++) {
            if (conversations[i].getId().equals(header)) {
                inTab.getChildren().addAll(conversations[i], theText, theFile, emoticon, theFun);
                break;
            }
        }
        msgTab.setContent(inTab);
        msgTab.setClosable(true);
        msgTab.setOnCloseRequest(e -> {
            //TO DO CODE
        });
        return msgTab;
    }

    static class CellRenderer implements Callback<ListView<User>, ListCell<User>> {

        @Override
        public ListCell<User> call(ListView<User> p) {

            ListCell<User> cell = new ListCell<User>() {

                @Override
                protected void updateItem(User user, boolean bln) {
                    super.updateItem(user, bln);
                    setGraphic(null);
                    setText(null);

                    Glow g1 = new Glow();
                    g1.setLevel(0.7);

                    if (user != null) {
                        HBox hBox = new HBox(10);
                        Text name = new Text(user.getName());
                        Button textChat, videoChat, voiceChat;

                        textChat = new Button();
                        File txtIcon = new File("src\\images\\mail.png");
                        String txtIcon1 = txtIcon.toURI().toString();
                        Image txtIcon2 = new Image(txtIcon1);
                        ImageView txtIcon3 = new ImageView(txtIcon2);
                        txtIcon3.setFitHeight(25);
                        txtIcon3.setFitWidth(25);
                        textChat.setGraphic(txtIcon3);
                        textChat.setOnAction(e -> {
                            innerT1.getTabs().add(addTextpane(user.getName()));
                            innerT1.getSelectionModel().selectLast();
                            textChat.setDisable(true);
                        });

                        videoChat = new Button();
                        File vidIcon = new File("src\\images\\video.png");
                        String vidIcon1 = vidIcon.toURI().toString();
                        Image vidIcon2 = new Image(vidIcon1);
                        ImageView vidIcon3 = new ImageView(vidIcon2);
                        vidIcon3.setFitHeight(25);
                        vidIcon3.setFitWidth(25);
                        videoChat.setGraphic(vidIcon3);
                        
                        videoChat.setOnAction(e -> {
                            videoChat.setDisable(true);

                        });

                        voiceChat = new Button();
                        File voiceIcon = new File("src\\images\\microphone.png");
                        String voiceIcon1 = voiceIcon.toURI().toString();
                        Image voiceIcon2 = new Image(voiceIcon1);
                        ImageView voiceIcon3 = new ImageView(voiceIcon2);
                        voiceIcon3.setFitHeight(25);
                        voiceIcon3.setFitWidth(25);
                        voiceChat.setGraphic(voiceIcon3);
                        voiceChat.setOnAction(e -> {
                            voiceChat.setDisable(true);

                        });

                        File userState = new File("src\\images\\online.png");
                        String userState1 = userState.toURI().toString();
                        Image userState2 = new Image(userState1);
                        ImageView userState3 = new ImageView(userState2);
                        userState3.setFitHeight(USE_PREF_SIZE);
                        userState3.setFitWidth(USE_PREF_SIZE);
                        userState3.setEffect(g1);

                        hBox.getChildren().addAll(userState3, name, textChat, videoChat, voiceChat);
                        hBox.setAlignment(Pos.CENTER_LEFT);

                        setGraphic(hBox);
                    }
                }
            };
            return cell;
        }
    }

}
