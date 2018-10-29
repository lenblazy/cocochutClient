
package client;

/**
 *
 * @author Lennox
 */
public class Client_voice {
    
    public static boolean calling = false;
    public static void main(String[] args){
        Client_fr fr = new Client_fr();
        fr.init_audio();
    }
    
    //in the startbutton action
    // call method init_audio()
    
    //in the stopbutton action
    //set: Client_voice.calling = false
}
