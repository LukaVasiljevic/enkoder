import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.event.*;
import java.io.*;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.StandardCharsets;

import static java.nio.charset.StandardCharsets.*;

public class konverter extends JFrame{
    private JPanel osnovniProzor;
    private JLabel enc;
    private JComboBox encCB;
    private JLabel dec;
    private JComboBox decCB;
    private JTextArea encTxt;
    private JButton konvertBtn;
    private JButton ponistiBtn;
    private JButton sacuvajBtn;
    private JTextArea decTxt;
    private JScrollPane encSkrol, decSkrol;

    public String[] tipovi = new String[] {"ASCII","UTF-8","UTF-16", "UTF-32"};

    public konverter(String naziv)
    {
        super(naziv);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(osnovniProzor);
        osnovniProzor.setBorder(new EmptyBorder(10, 10, 10, 10));
        this.pack();

        dec.requestFocusInWindow();

        // dodavanje elemenata u kombo bokseve, postavljanje placeholdera, ubacivanje paddinga
        encTxt.setText("Унеси текст...");
        encTxt.setBorder(BorderFactory.createCompoundBorder(encTxt.getBorder(), BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        decTxt.setBorder(BorderFactory.createCompoundBorder(decTxt.getBorder(), BorderFactory.createEmptyBorder(5, 5,5, 5)));

        for (String instanca : tipovi)
        {
            encCB.addItem(instanca);
            decCB.addItem(instanca);
        }

        encTxt.addComponentListener(new ComponentAdapter() {
        });
        encTxt.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if(encTxt.getText().equals("Унеси текст..."))
                    encTxt.setText("");
            }

            @Override
            public void focusLost(FocusEvent e) {
                if(encTxt.getText().equals(""))
                    encTxt.setText("Унеси текст...");
            }
        });
        encTxt.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseExited(MouseEvent e) {
                if(encTxt.getText().equals(""))
                {
                    encTxt.setText("Унеси текст...");
                    dec.requestFocusInWindow(); // gubi fokus
                }

            }
        });

        konvertBtn.setText("Конвертуј");
        ponistiBtn.setText("Поништи");
        sacuvajBtn.setText("Сачувај у фајлу");

        // brisanje teksta iz oba prozora za tekst
        ponistiBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
             //   if(!encTxt.getText().isEmpty() && !encTxt.getText().equals("Unesi tekst..."))
              //      encTxt.setText("");
                if(!decTxt.getText().isEmpty())
                    decTxt.setText("");
            }
        });

        sacuvajBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if(!decTxt.getText().equals(""))
                {
                    File dat = new File("ispis.txt");
                    FileOutputStream fout = null;
                    try
                    {
                        fout = new FileOutputStream(dat);
                    }
                    catch(FileNotFoundException e)
                    {
                        System.out.println("datoteke nema");
                    }
                    try
                    {
                        String izlaz = decTxt.getText();
                        for (char k :izlaz.toCharArray())
                        {
                            fout.write(k);
                        }
                    }
                    catch(IOException e)
                    {
                        System.out.println("greska u radu s datotekom");
                    }
                    try {
                        fout.close();
                        decTxt.setText("");
                        JOptionPane.showMessageDialog(osnovniProzor, "Текст је сачуван!", "Обавештење",
                                JOptionPane.PLAIN_MESSAGE);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else
                    JOptionPane.showMessageDialog(osnovniProzor, "Поље је празно!",
                            "Грешка", JOptionPane.ERROR_MESSAGE);

            }
        });
        konvertBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent)
            {
                byte[] bajtovi;
                String izlaz = "";
                if(encCB.getSelectedItem().equals("ASCII"))
                    bajtovi = encTxt.getText().getBytes(US_ASCII);
                else if (encCB.getSelectedItem().equals("UTF-8"))
                    bajtovi = encTxt.getText().getBytes(UTF_8);
                else if (encCB.getSelectedItem().equals("UTF-16"))
                    bajtovi = encTxt.getText().getBytes(UTF_16);
                else// if(encCB.getSelectedItem().equals("UTF-32"))
                    bajtovi = encTxt.getText().getBytes(Charset.forName("UTF_32"));

                if(decCB.getSelectedItem().equals("ASCII"))
                    izlaz = new String(bajtovi, StandardCharsets.US_ASCII);
                else if (decCB.getSelectedItem().equals("UTF-8"))
                    izlaz = new String(bajtovi, StandardCharsets.UTF_8);
                else if (decCB.getSelectedItem().equals("UTF-16"))
                    izlaz = new String(bajtovi, StandardCharsets.UTF_16);
                else// if(decCB.getSelectedItem().equals("UTF-32"))
                    izlaz = new String(bajtovi, Charset.forName("UTF_32"));

                decTxt.setText(izlaz);
            }

        });
    }

    public static void main(String[] args) {
        JFrame prozor = new konverter("Промена енкодинга");
        prozor.setSize(600, 400);
        prozor.setLocationRelativeTo(null);

        prozor.setVisible(true);
    }

}
