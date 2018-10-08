import com.sun.org.apache.bcel.internal.classfile.SourceFile;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class OpenContact extends JFrame {

    private int i = 0;
    JTextField textField;
    public int index;
    public OpenContact(String name) {
        super("Телефоны");
        //setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(5, 5));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        final DefaultListModel listModel = new DefaultListModel();

        baseList(listModel, name);
       /* for (i = 0; i < 25; i++) {
            listModel.addElement("Элемент списка " + i);
        }
*/
        final JList list = new JList(listModel);
        list.setSelectedIndex(0);
        list.setFocusable(false);

        list.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                JList list = (JList)evt.getSource();
                if (evt.getClickCount() == 1) {

                    // Double-click detected
                    index = list.locationToIndex(evt.getPoint());
                    System.out.printf("%d\n",index);
                    System.out.printf("%s\n",(String)list.getModel().getElementAt(index));
                    System.out.printf("========\n");
                }
            }
        });

        mainPanel.add(new JScrollPane(list), BorderLayout.CENTER);

        JPanel masterPanel = new JPanel();
        BorderLayout masterL = new BorderLayout();
        GridLayout buttonsL = new GridLayout(1,3,5,5);
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(buttonsL);
        masterPanel.setLayout(masterL);
        masterPanel.add(buttonsPanel,BorderLayout.NORTH);
        mainPanel.add(masterPanel, BorderLayout.SOUTH);

        JButton addButton = new JButton("Добавить");
        addButton.setFocusable(false);
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent k) {
                Connection c;
                Statement stmt;
                try {
                    Class.forName("org.postgresql.Driver");
                    c = DriverManager
                            .getConnection("jdbc:postgresql://194.213.122.94:35101/contacs_list", "postgres", "123");
                          //  .getConnection("jdbc:postgresql://10.0.34.101/contacs_list", "postgres", "123");
                    c.setAutoCommit(false);

                    String sql;

                    stmt = c.createStatement();
                    sql = "insert into mobile values (\n" +
                            "  (SELECT \n" +
                            "  max(mobile.\"Id\")\n" +
                            "FROM \n" +
                            "  public.mobile)+1,'"+textField.getText()+"');;";

                    System.out.printf("yaaaaaa\n");

                    System.out.printf("noooooo\n");
                    stmt.executeUpdate(sql);
                    stmt.close();
                    c.commit();



                    stmt = c.createStatement();
                    sql = "insert into tomobile values (\n" +
                            "((SELECT \n" +
                            "  max(tomobile.id)\n" +
                            "FROM \n" +
                            "  public.tomobile)+1)\n" +
                            "  ,\n" +
                            "(SELECT people.\"Id\"\n" +
                            "from people\n" +
                            "Where \"Name\"='"+name+"' AND\n" +
                            "\"Status\"='true'),\n" +
                            "  (SELECT \n" +
                            "  max(mobile.\"Id\")\n" +
                            "FROM \n" +
                            "  public.mobile),'true');";
                    System.out.printf("yaaaaaa\n");

                    System.out.printf("noooooo\n");
                    stmt.executeUpdate(sql);
                    stmt.close();
                    c.commit();

                    c.close();

                } catch (Exception e) {
                    e.printStackTrace();
                    System.err.println(e.getClass().getName() + ": " + e.getMessage());
                    System.exit(0);
                }
                baseList(listModel,name);
            }
        });
        buttonsPanel.add(addButton);

        JButton renameButton = new JButton("Переименовать");
        renameButton.setFocusable(false);
       // renameButton.setEnabled(false);

        renameButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent k) {
                Connection c;

                Statement stmt;
                try {
                    Class.forName("org.postgresql.Driver");
                    c = DriverManager
                            .getConnection("jdbc:postgresql://194.213.122.94:35101/contacs_list", "postgres", "123");
                          //  .getConnection("jdbc:postgresql://10.0.34.101/contacs_list", "postgres", "123");
                    String sql;
                    c.setAutoCommit(false);


                    stmt = c.createStatement();
                    sql= ("Update mobile\n" +
                            " set number='"+textField.getText()+"'\n" +
                            " where \n" +
                            " number='"+(String)list.getModel().getElementAt(index)+"';");



                    stmt.executeUpdate(sql);

                    System.out.println("-- Operation SELECT done successfully");
                    //rs.close();
                    stmt.close();
                    c.commit();

                    c.close();

                } catch (Exception e) {
                    e.printStackTrace();
                    System.err.println(e.getClass().getName() + ": " + e.getMessage());
                    System.exit(0);
                }
                baseList(listModel,name);
            }
        });
        buttonsPanel.add(renameButton);


        final JButton removeButton = new JButton("Удалить");
        removeButton.setFocusable(false);
       // removeButton.setEnabled(false);
        removeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent k) {
                Connection c;

                Statement stmt;
                try {
                    Class.forName("org.postgresql.Driver");
                    c = DriverManager
                            .getConnection("jdbc:postgresql://194.213.122.94:35101/contacs_list", "postgres", "123");
                          //  .getConnection("jdbc:postgresql://10.0.34.101/contacs_list", "postgres", "123");
                    String sql;
                    c.setAutoCommit(false);

                    System.out.printf("%s\n",(String)list.getModel().getElementAt(index));
                    System.out.println("================");
                    stmt = c.createStatement();
                    sql= ("Update tomobile\n" +
                            "set tomobile =\n" +
                            "(Select max(mobile.\"Id\")\n" +
                            "from mobile\n" +
                            "where mobile.number='"+(String)list.getModel().getElementAt(index)+"'),\n" +
                            "\"Status\" = false\n" +
                            "where tomobile =(Select max(mobile.\"Id\")\n" +
                            "from mobile\n" +
                            "where mobile.number='"+(String)list.getModel().getElementAt(index)+"') AND\n" +
                            "\"Status\" = true;");


                    stmt.executeUpdate(sql);

                    System.out.println("-- Operation SELECT done successfully");

                    stmt.close();
                    c.commit();

                    c.close();

                } catch (Exception e) {
                    e.printStackTrace();
                    System.err.println(e.getClass().getName() + ": " + e.getMessage());
                    System.exit(0);
                }
                baseList(listModel,name);
            }
        });
        buttonsPanel.add(removeButton);

        list.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                if (list.getSelectedIndex() >= 0) {
                    removeButton.setEnabled(true);
                    renameButton.setEnabled(true);
                } else {
                    removeButton.setEnabled(false);
                    renameButton.setEnabled(false);
                }
            }
        });

        textField = new JTextField();
        masterL.setVgap(5);
        masterPanel.add(textField,BorderLayout.SOUTH);
        getContentPane().add(mainPanel);

        setPreferredSize(new Dimension(500, 600));
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }


    public void baseList(DefaultListModel listModel, String name){
        Connection c;
        Statement stmt;
        listModel.clear();
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager
                    .getConnection("jdbc:postgresql://194.213.122.94:35101/contacs_list", "postgres", "123");
                    //.getConnection("jdbc:postgresql://10.0.34.101/contacs_list", "postgres", "123");
            c.setAutoCommit(false);
            System.out.println("-- Opened database successfully");
            String sql;

            //--------------- SELECT DATA ------------------



            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT \n" +
                    "  mobile.\"number\" \n" +
                    "FROM \n" +
                    "  public.people, \n" +
                    "  public.tomobile, \n" +
                    "  public.mobile\n" +
                    "WHERE \n" +
                    "  tomobile.topeople = people.\"Id\" AND\n" +
                    "  tomobile.tomobile = mobile.\"Id\" AND\n" +
                    "  people.\"Name\"='"+name +"' AND" +
                    " tomobile.\"Status\"=true;\n");

            while (rs.next()) {
                listModel.addElement(rs.getString("Number"));

            }
            rs.close();
            stmt.close();
            c.commit();
            System.out.println("-- Operation SELECT done successfully");

            c.close();

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        System.out.println("-- All Operations done successfully");
    }

    public static void open(String name) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                JFrame.setDefaultLookAndFeelDecorated(true);
                new OpenContact(name);
            }
        });
    }
}