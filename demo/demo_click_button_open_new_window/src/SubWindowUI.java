import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SubWindowUI extends JFrame implements ActionListener {
    private JPanel main_panel;
    private JLabel label;

    public SubWindowUI(String pTitle) {
        super(pTitle);
        setContentPane(main_panel);
        label.setText("Hello");
    }

    public void actionPerformed(ActionEvent pEvent) {

    }
}
