import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class Main extends JFrame {

    private int i = 0;
    public int index;
    JTextField textField;
    public Main() {
        super("Список контактов");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(5, 5));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        final DefaultListModel listModel = new DefaultListModel();

        baseList(listModel);
    final JList list = new JList(listModel);
        list.setSelectedIndex(0);
        list.setFocusable(false);

        list.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                JList list = (JList)evt.getSource();
                if (evt.getClickCount() == 1) {
                    index = list.locationToIndex(evt.getPoint());
                    System.out.printf("%d\n", index);
                }
                    if (evt.getClickCount() == 2) {

                    // Double-click detected
                    index = list.locationToIndex(evt.getPoint());
                    System.out.printf("%d\n",index);

                   new OpenContact((String)list.getModel().getElementAt(index));

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
                           // .getConnection("jdbc:postgresql://10.0.34.101/contacs_list", "postgres", "123");
                    c.setAutoCommit(false);

                    String sql;

                    stmt = c.createStatement();
                    ResultSet rs = stmt.executeQuery("SELECT \n" +
                            "  max(people.\"Id\")\n" +
                            "FROM \n" +
                            "  public.people;\n");

                    System.out.println("-- Opened");
                    rs.next();
                    int id = rs.getInt("Max")+1;

                    rs.close();
                    stmt.close();
                    c.commit();

                    stmt = c.createStatement();
                    sql = "INSERT INTO PEOPLE VALUES ('"+textField.getText()+"',"+id+",'"+"temp"+"',true);";

                    stmt.executeUpdate(sql);
                    stmt.close();
                    c.commit();

                    c.close();

                } catch (Exception e) {
                    e.printStackTrace();
                    System.err.println(e.getClass().getName() + ": " + e.getMessage());
                    System.exit(0);
                }
              baseList(listModel);
            }
        });
        buttonsPanel.add(addButton);

        JButton renameButton = new JButton("Переименовать");
        renameButton.setFocusable(false);
        renameButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent k) {
                Connection c;

                Statement stmt;
                try {
                    Class.forName("org.postgresql.Driver");
                    c = DriverManager
                            .getConnection("jdbc:postgresql://194.213.122.94:35101/contacs_list", "postgres", "123");
                           // .getConnection("jdbc:postgresql://10.0.34.101/contacs_list", "postgres", "123");
                    String sql;
                    c.setAutoCommit(false);


                        stmt = c.createStatement();
                        sql= ("UPDATE PEOPLE\n" +
                                " SET \n" +
                                "   \"Name\" = '"+textField.getText()+"',\n" +

                                "   \"Status\" = 'true'\n" +
                                "  WHERE \n" +
                                "      \"Name\" = '"+(String)list.getModel().getElementAt(index)+"' AND" +
                                " \"Status\" = 'true';");



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
                baseList(listModel);
            }
        });
        buttonsPanel.add(renameButton);


        final JButton removeButton = new JButton("Удалить");
        removeButton.setFocusable(false);
        removeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent k) {
                Connection c;

                Statement stmt;
                try {
                    Class.forName("org.postgresql.Driver");
                    c = DriverManager
                            .getConnection("jdbc:postgresql://194.213.122.94:35101/contacs_list", "postgres", "123");
                            //.getConnection("jdbc:postgresql://10.0.34.101/contacs_list", "postgres", "123");
                    String sql;
                    c.setAutoCommit(false);

                    System.out.printf("%s",(String)list.getModel().getElementAt(index));

                        stmt = c.createStatement();
                        sql= ("UPDATE PEOPLE\n" +
                                " SET \n" +
                                "   \"Name\" = '"+(String)list.getModel().getElementAt(index)+"',\n" +
                                "   \"Status\" = 'false'\n" +
                                "  WHERE \n" +
                                "      \"Name\" = '"+(String)list.getModel().getElementAt(index)+"';");


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
                baseList(listModel);
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


    public void baseList(DefaultListModel listModel){
        Connection c;
        Statement stmt;
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager
                    .getConnection("jdbc:postgresql://194.213.122.94:35101/contacs_list", "postgres", "123");
           // .getConnection("jdbc:postgresql://10.0.34.101/contacs_list", "postgres", "123");
            c.setAutoCommit(false);
            System.out.println("-- Opened database successfully");
            String sql;

            //--------------- SELECT DATA ------------------

            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT \n" +
                    "  people.\"Name\", \n" +

                    "  people.\"Status\"\n" +
                    "FROM \n" +
                    "  public.people;\n");
            listModel.clear();
            while (rs.next()) {
                if(rs.getBoolean("Status")) {
                    String name;
                    name=rs.getString("Name");
                    listModel.addElement(name);
                }

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


    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                JFrame.setDefaultLookAndFeelDecorated(true);
                new Main();
            }
        });
    }
}