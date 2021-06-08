import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainWindowUI extends JFrame implements ActionListener {
    private JButton button;
    private JPanel main_panel;
    private int cnt = 0;
    private static JFrame sub_subwindow = null;

    private static void showSubWindow() {
        sub_subwindow = new SubWindowUI("Sub Window");
        sub_subwindow.setSize(300, 500);
        sub_subwindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        sub_subwindow.setLocationRelativeTo(null);
        sub_subwindow.setVisible(true);
    }

    public MainWindowUI(String pTitle) {
        super(pTitle);
        setContentPane(main_panel);
        button.addActionListener(this);
    }

    public void actionPerformed(ActionEvent pEvent) {
        if (pEvent.getSource() == button) {
//            button.setText(Integer.toString( ++cnt));
            setVisible(false);
            showSubWindow();
        }
    }
}
