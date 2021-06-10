import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

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
        iHomeUI.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        iHomeUI.setLocationRelativeTo(null);
        iHomeUI.setVisible(true);
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

        iClient = new Client(this);
        iClient.start();
    }

    public User signupUser() {
        String account = vAccount.getText();
        String password = Arrays.toString(vPassword.getPassword());
        return new User(account, password);
    }

    public void actionPerformed(ActionEvent pEvent) {
        if (pEvent.getSource() == vSignUp) {
            User signup_user = signupUser();

            iClient.sendPackage("SIGN-UP", signup_user);
        }

        if (pEvent.getSource() == vSignIn) {
            User signup_user = signupUser();
            iClient.sendPackage("SIGN-IN", signup_user);
            iClient.setiUsername(signup_user.getiAccount());
        }
    }
}
