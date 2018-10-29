package client;

import interactions.Message;
import static interactions.MessageType.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServerConnector implements Runnable {
   
    private static String uName;
    private static Socket tcpSocket;
    private int ID = -1;
    private static ObjectOutputStream output;
    private static ObjectInputStream input;
    public volatile boolean running;
    
    public void setSocket(Socket soc) {
        tcpSocket = soc;
        try {
            output = new ObjectOutputStream(tcpSocket.getOutputStream());
            input = new ObjectInputStream(tcpSocket.getInputStream());
        } catch (IOException ex) {
            Logger.getLogger(ServerConnector.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static boolean openConnection(String address, int port) {
        try {
            Socket socket = new Socket(address, port);
            ServerConnector con = new ServerConnector();
            con.setSocket(socket);
        } catch (IOException ex) {
            return false;
        }
        return true;
    }

    public void closeConnection() throws IOException {
        this.running = false;
        output.close();
        input.close();
        tcpSocket.close();
        
    }

    public static void send(Message m) throws IOException {
        
        output.writeObject(m);
        output.flush();
    }

    public static void getLoginDetails(String user, String pword) throws IOException, ClassNotFoundException {
        user = "/u/" + user;
        pword = "/p/" + pword;

        Message sendUname = new Message();
        sendUname.setType(LOGIN);
        sendUname.setMsg(user);
        output.writeObject(sendUname);
        output.flush();

        Message sendpword = new Message();
        sendpword.setMsg(pword);
        sendpword.setType(LOGIN);
        output.writeObject(sendpword);
        output.flush();

        Message msg = (Message) input.readObject();
        if (msg != null) {
            String thedata = msg.getMsg();
            if (thedata.startsWith("/x/")) {
                LoginWindow.verify(thedata, false);
            } else if (thedata.startsWith("/y/")) {
                LoginWindow.verify(thedata, true);
            } else {
                LoginWindow.verify(thedata, false);
            }
        }
    }//end getLogin details

    public static void getRegDetails(String fin, String lan, String usn, String pass,
            String add, String fon, String sex, String plec, String work, String dob, String rln) throws IOException, ClassNotFoundException {
        
        fin = "/fn/" + fin;
        lan = "/ln/" + lan;
        uName = "/un/" + usn;
        pass = "/ps/" + pass;
        add = "/em/" + add;
        fon = "/ph/" + fon;
        sex = "/xx/" + sex;
        plec = "/co/" + plec;
        work = "/oc/" + work;
        dob = "/dob/" + dob;
        rln = "/rn/" + rln;

        Message fn = new Message();
        fn.setMsg(fin);
        fn.setType(REGISTER);
        output.writeObject(fn);
        output.flush();

        Message ln = new Message();
        ln.setMsg(lan);
        ln.setType(REGISTER);
        output.writeObject(ln);
        output.flush();

        Message un = new Message();
        un.setMsg(uName);
        un.setType(REGISTER);
        output.writeObject(un);
        output.flush();

        Message pw = new Message();
        pw.setMsg(pass);
        pw.setType(REGISTER);
        output.writeObject(pw);
        output.flush();

        Message em = new Message();
        em.setMsg(add);
        em.setType(REGISTER);
        output.writeObject(em);
        output.flush();

        Message ph = new Message();
        ph.setMsg(fon);
        ph.setType(REGISTER);
        output.writeObject(ph);
        output.flush();

        Message gn = new Message();
        gn.setMsg(sex);
        gn.setType(REGISTER);
        output.writeObject(gn);
        output.flush();

        Message cou = new Message();
        cou.setMsg(plec);
        cou.setType(REGISTER);
        output.writeObject(cou);
        output.flush();

        Message oc = new Message();
        oc.setMsg(work);
        oc.setType(REGISTER);
        output.writeObject(oc);
        output.flush();

        Message bir = new Message();
        bir.setMsg(dob);
        bir.setType(REGISTER);
        output.writeObject(bir);
        output.flush();

        Message st = new Message();
        st.setMsg(rln);
        st.setType(REGISTER);
        output.writeObject(st);
        output.flush();

        Message msg = (Message) input.readObject();
        if (msg != null) {
            String thedata = msg.getMsg();
            if (thedata.startsWith("/z/")) {
                RegistrationWindow.showState(thedata);

                Message regMsg = new Message();
                regMsg.setUsername(usn);
                regMsg.setType(REGISTERED);
                regMsg.setMsg(" has registered");
                output.writeObject(regMsg);
                output.flush();
            } else {
                RegistrationWindow.showState(thedata);
            }
        }
    }

    static void getEditedDetails(String fn, String ln, String un, String addr, String fon,
            String county, String work, String rln) {
        System.out.println(fn);
        System.out.println(ln);
        System.out.println(un);
        System.out.println(addr);
        System.out.println(fon);
        System.out.println(county);
        System.out.println(work);
        System.out.println(rln);
        //send the registration values to server
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getID() {
        return ID;
    }
    /*
     private void voice_connector() {
     }*/

    @Override
    public void run() {
        this.running = true;
        try {
            
            while (tcpSocket.isConnected() && this.running) {
                Message message = null;
                message = (Message) input.readObject();
                
                if (message != null) {
                    String msg = message.getMsg();
                    
                    switch (message.getType()) {
                        case PRIVATEMESSAGE:
                            String pmsg = msg.substring(3, msg.length());
                            System.out.println(message.getUsername() + " >> "+ pmsg);
                            ChatWindow.inPMsg(message);
                            break;
                        case GROUPMESSAGE:
                            String gmsg = msg.substring(3, msg.length());
                            ChatWindow.inGrpMsg(message.getUsername() + " >> " + gmsg);
                            break;
                        case NOTIFICATION:
                            break;
                        case SERVER:
                            break;
                        case CONNECTED:
                            String userId = msg.substring(3, msg.length());
                            ChatWindow.setUserID(userId);
                            break;
                        case UPDATE:
                            ChatWindow.setUserList(message);
                            System.out.println(message.getUsers());
                            break;
                        case DISCONNECTED:
                            ChatWindow.setUserList(message);
                            Message gout = new Message();
                            gout.setMsg("/d/" + "/e/");
                            gout.setUsername(uName);
                            gout.setType(DISCONNECTED);
                            ServerConnector.send(gout);
                            break;
                        case STATUS:
                            // ChatWindow.setUserList(message);
                            break;
                        case PING:
                            Message ping = new Message();
                            ping.setMsg("/i/" + ChatWindow.getUserID() + "/e/");
                            ping.setUsername(uName);
                            ping.setType(DISCONNECTED);
                            send(ping);
                            break;
                        default:
                            System.out.println("this is what is printed" + message.getMsg());
                            break;
                    }

                }
            }
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(ServerConnector.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void connect(String msg) throws IOException {
        String uname = msg.split("/c/|/e/")[1];
        Message createMessage = new Message();
        createMessage.setUsername(uname);
        createMessage.setType(CONNECTED);
        createMessage.setMsg(msg);
        output.writeObject(createMessage);
        output.flush();
    }

}
