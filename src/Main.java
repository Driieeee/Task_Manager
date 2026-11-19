import gui.MainFrame;
import javax.swing.JOptionPane;

public class Main {
    public static void main(String[] args){
        try{
            MainFrame.main(args);
        }
        catch(Exception e){
            JOptionPane.showMessageDialog(
                null,
                "The app count not start:\n" + e.getMessage(),
                "Startup Error",
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace(); // prints the details in the terminal
        }
    }
}  
