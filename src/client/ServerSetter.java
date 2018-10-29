package client;

import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.*;

public class ServerSetter{
    static TextField ipAddr, portNo;
    static int portNumber;
    static String ipAddress;
    static Button enter;
    static boolean connectionStatus = false;
    
    public static boolean display(){
        
        String theStyle = "-fx-background-color: whitesmoke;"+"-fx-font-family: serif;"+"-fx-font-size: 11pt;";
        Text instr, ip, pot,ipNoEg,portNoEg;
        Stage window = new Stage();
        
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Video Conference");
        window.setMinWidth(300);
        
        instr = new Text("Enter Server details to connect");
        ip = new Text("IP Address: ");
        pot = new Text("Port Number: ");
        enter = new Button("Enter");
        ipAddr = new TextField("localhost"); 
        portNo = new TextField("1357"); 
        enter.setOnAction(e ->{
            String addr = ipAddr.getText();
            int port = Integer.parseInt(portNo.getText());
            boolean success = ServerConnector.openConnection(addr,port);
            connectionStatus = success;
            window.close();
        });
        
        ipNoEg = new Text("(Eg. 127.0.0.1)");
        portNoEg = new Text("(Eg. 1357)");
        
        VBox root = new VBox();
        
        GridPane ips = new GridPane();
        ips.add(ip,0,1);
        ips.add(ipAddr,0,2); 
        ips.add(ipNoEg,0,3);
        GridPane.setHalignment(ip,HPos.CENTER);
        GridPane.setHalignment(ipNoEg,HPos.CENTER);
        ips.setAlignment(Pos.CENTER);
        ips.setVgap(5);
        
        GridPane pots = new GridPane();
        pots.add(pot,0,0);
        pots.add(portNo,0,1); 
        pots.add(portNoEg,0,2);
        GridPane.setHalignment(pot,HPos.CENTER);
        GridPane.setHalignment(portNoEg,HPos.CENTER);
        pots.setAlignment(Pos.CENTER);
        pots.setVgap(5);
        
        VBox info = new VBox();
        info.getChildren().addAll(ips,pots);
        info.setSpacing(20);
        
        root.setStyle(theStyle);
        root.getChildren().addAll(instr,info, enter);
        root.setSpacing(40);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(20));
        
        Scene scene = new Scene(root, 290, 350);
        window.setScene(scene);
        window.showAndWait();
        return connectionStatus;
    }

}
    

