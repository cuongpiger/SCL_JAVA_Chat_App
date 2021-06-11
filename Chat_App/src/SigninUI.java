import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class SigninUI extends JFrame implements ActionListener {
    private JPanel panel1;
    private JTextField vAccount;
    private JPasswordField vPassword;
    private JButton vSignIn;
    private JButton vSignUp;
    private static Client iClient;
    private static HomeUI iHomeUI;

    public void showDialog(String pText) {
        JOptionPane.showMessageDialog(this, pText);
    }

    public void showHomePage() {
        iHomeUI = new HomeUI("Home", iClient);
        iHomeUI.setSize(350, 500);
        iHomeUI.setLocationRelativeTo(null);
        iHomeUI.setVisible(true);

        iHomeUI.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                iClient.sendPackage("CLOSE", iClient.getiUsername());
            }
        });
    }

    public void updateHomepage(ArrayList<String> pClients) {
        iHomeUI.updateHomepage(pClients);
    }

    public void updateChatUI(Message pMessage) {
        iHomeUI.updateChatUI(pMessage);
    }

    public SigninUI(String pTitle) {
        super(pTitle);
        setContentPane(panel1);

        vSignIn.addActionListener(this);
        vSignUp.addActionListener(this);
    }

    public User signupUser() {
        String account = vAccount.getText();
        String password = Arrays.toString(vPassword.getPassword());
        return new User(account, password);
    }

    public void actionPerformed(ActionEvent pEvent) {
        if (pEvent.getSource() == vSignUp) {
            User signup_user = signupUser();
            new ClientSignUp(this, signup_user).start();
        }

        if (pEvent.getSource() == vSignIn) {
            User signup_user = signupUser();
            iClient = new Client(this, signup_user);
            iClient.start();
        }
    }
}
