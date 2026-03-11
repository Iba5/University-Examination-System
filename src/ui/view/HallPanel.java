package src.ui.view;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.util.List;

import src.models.Hall;
import src.services.HallService;

public class HallPanel extends JPanel {

    private JTextField idField;
    private JTextField nameField;
    private JTextField capacityField;

    private JTable hallTable;
    private DefaultTableModel tableModel;

    private JSplitPane splitPane;
    private JPanel formContainerRef;
    private JPanel tableContainerRef;

    private HallService service = new HallService();

    public HallPanel() {

        setLayout(new BorderLayout(25, 25));
        setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        setBackground(new Color(15, 15, 20));

        add(createTitle(), BorderLayout.NORTH);
        add(createMainContent(), BorderLayout.CENTER);

        loadTable();
        addTableSelectionListener();
    }

    private JLabel createTitle() {

        JLabel title = new JLabel("HALLS");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 30));

        return title;
    }

    private JPanel createMainContent() {

        JPanel main = new JPanel(new BorderLayout(25,25));
        main.setOpaque(false);

        formContainerRef = createFormContainer();
        tableContainerRef = createTableContainer();

        splitPane = new JSplitPane(
                JSplitPane.VERTICAL_SPLIT,
                formContainerRef,
                tableContainerRef
        );

        splitPane.setDividerSize(0);
        splitPane.setBorder(null);
        splitPane.setResizeWeight(0.55);

        splitPane.setOpaque(false);
        splitPane.setBackground(new Color(15,15,20));

        main.add(splitPane, BorderLayout.CENTER);

        addMouseResizeLogic();

        return main;
    }

    private void addMouseResizeLogic() {

        formContainerRef.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                SwingUtilities.invokeLater(() ->
                        splitPane.setDividerLocation(0.65));
            }
        });

        tableContainerRef.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                SwingUtilities.invokeLater(() ->
                        splitPane.setDividerLocation(0.35));
            }
        });
    }

    private JPanel createFormContainer() {

        JPanel container = createGlowPanel(25);
        container.setLayout(new BorderLayout());
        container.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        container.add(createFormPanel(), BorderLayout.CENTER);

        return container;
    }

    private JPanel createFormPanel() {

        JPanel form = new JPanel(new GridBagLayout());
        form.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 12, 12, 12);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        idField = createStyledField();
        nameField = createStyledField();
        capacityField = createStyledField();

        JButton addBtn = createStyledButton("Add");
        JButton updateBtn = createStyledButton("Update");
        JButton deleteBtn = createStyledButton("Delete");

        addBtn.addActionListener(e -> addHall());
        updateBtn.addActionListener(e -> updateHall());
        deleteBtn.addActionListener(e -> deleteHall());

        gbc.gridx = 0; gbc.gridy = 0;
        form.add(createLabel("ID:"), gbc);
        gbc.gridx = 1;
        form.add(idField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        form.add(createLabel("Name:"), gbc);
        gbc.gridx = 1;
        form.add(nameField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        form.add(createLabel("Capacity:"), gbc);
        gbc.gridx = 1;
        form.add(capacityField, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        gbc.gridwidth = 2;

        JPanel btnPanel = new JPanel();
        btnPanel.setOpaque(false);
        btnPanel.add(addBtn);
        btnPanel.add(updateBtn);
        btnPanel.add(deleteBtn);

        form.add(btnPanel, gbc);

        return form;
    }

    private JPanel createTableContainer() {

        JPanel container = createGlowPanel(30);
        container.setLayout(new BorderLayout());
        container.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        container.add(createTable(), BorderLayout.CENTER);

        return container;
    }

    private JScrollPane createTable() {

        tableModel = new DefaultTableModel();
        tableModel.setColumnIdentifiers(new String[]{
                "ID",
                "Name",
                "Capacity"
        });

        hallTable = new JTable(tableModel);
        hallTable.setRowHeight(40);
        hallTable.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        hallTable.setForeground(Color.WHITE);
        hallTable.setBackground(new Color(22, 22, 30));
        hallTable.setGridColor(new Color(40, 40, 50));
        hallTable.setSelectionBackground(new Color(0, 180, 255));
        hallTable.setSelectionForeground(Color.BLACK);

        JTableHeader header = hallTable.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 20));
        header.setForeground(new Color(0, 220, 255));
        header.setBackground(new Color(18, 18, 25));
        header.setPreferredSize(new Dimension(header.getWidth(), 50));

        JScrollPane scroll = new JScrollPane(hallTable);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(new Color(22, 22, 30));

        return scroll;
    }

    /* TABLE CLICK LISTENER */

    private void addTableSelectionListener(){

        hallTable.addMouseListener(new MouseAdapter(){

            public void mouseClicked(MouseEvent e){

                int row = hallTable.getSelectedRow();

                if(row >= 0){

                    idField.setText(tableModel.getValueAt(row,0).toString());
                    nameField.setText(tableModel.getValueAt(row,1).toString());
                    capacityField.setText(tableModel.getValueAt(row,2).toString());

                    idField.setEditable(false);
                }
            }
        });
    }

    /* DATABASE METHODS */

    private void loadTable(){

        tableModel.setRowCount(0);

        try{

            List<Hall> halls = service.getAll();

            for(Hall h : halls){

                tableModel.addRow(new Object[]{
                        h.getHallId(),
                        h.getHallName(),
                        h.getSeatingCapacity()
                });
            }

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private boolean validateInput(){

        if(idField.getText().isEmpty()){
            showPopup("Enter Hall ID");
            return false;
        }

        if(nameField.getText().isEmpty()){
            showPopup("Enter Hall Name");
            return false;
        }

        if(capacityField.getText().isEmpty()){
            showPopup("Enter Capacity");
            return false;
        }

        try{
            Integer.parseInt(capacityField.getText());
        }catch(Exception e){
            showPopup("Capacity must be a number");
            return false;
        }

        return true;
    }

    private void addHall(){

        try{

            if(!validateInput()) return;

            Hall h = new Hall();

            h.setHallId(Integer.parseInt(idField.getText()));
            h.setHallName(nameField.getText());
            h.setSeatingCapacity(Integer.parseInt(capacityField.getText()));  // Assuming setter is setHallCapacity; adjust if different

            service.addSingle(h);

            showPopup("Hall Added Successfully");

            loadTable();
            clearFields();

        }catch(Exception e){

            e.printStackTrace();
            showPopup(e.getMessage());
        }
    }

    private void updateHall(){

        try{

            if(!validateInput()) return;

            boolean confirm = showConfirmPopup("Update Hall ID: "+idField.getText()+" ?");

            if(!confirm) return;

            Hall h = new Hall();

            h.setHallId(Integer.parseInt(idField.getText()));
            h.setHallName(nameField.getText());
            h.setSeatingCapacity(Integer.parseInt(capacityField.getText()));  // Assuming setter is setHallCapacity; adjust if different

            service.update(h);

            showPopup("Hall Updated Successfully");

            loadTable();
            clearFields();

        }catch(Exception e){

            e.printStackTrace();
            showPopup(e.getMessage());
        }
    }

    private void deleteHall(){

        try{

            if(idField.getText().isEmpty()){
                showPopup("Select a hall first");
                return;
            }

            boolean confirm = showConfirmPopup("Delete Hall ID: "+idField.getText()+" ?");

            if(!confirm) return;

            service.deleteById(Integer.parseInt(idField.getText()));

            showPopup("Hall Deleted Successfully");

            loadTable();
            clearFields();

        }catch(Exception e){

            e.printStackTrace();
            showPopup(e.getMessage());
        }
    }

    private void clearFields(){

        idField.setText("");
        nameField.setText("");
        capacityField.setText("");

        idField.setEditable(true);
    }

  

    /* UI METHODS (UNCHANGED) */

    // --- Your original UI methods remain exactly the same here ---
    
    private JPanel createGlowPanel(int arc){

        return new JPanel(){

            protected void paintComponent(Graphics g){

                Graphics2D g2 = (Graphics2D) g.create();

                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);

                for(int i=8;i>=2;i-=2){

                    g2.setColor(new Color(0,180,255,35));
                    g2.setStroke(new BasicStroke(i));

                    g2.drawRoundRect(
                            i/2,
                            i/2,
                            getWidth()-i,
                            getHeight()-i,
                            arc,
                            arc);
                }

                g2.setColor(new Color(22,22,30));

                g2.fillRoundRect(
                        6,
                        6,
                        getWidth()-12,
                        getHeight()-12,
                        arc,
                        arc);

                g2.dispose();
            }
        };
    }

     /* POPUPS */

   private void showPopup(String message){

    JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), true);
    dialog.setUndecorated(true);

    JPanel panel = new JPanel(new BorderLayout(20,20));
    panel.setBackground(new Color(22,22,30));
    panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0,200,255),2),
            BorderFactory.createEmptyBorder(25,30,25,30)
    ));

    JLabel msg = new JLabel(message, SwingConstants.CENTER);
    msg.setForeground(Color.WHITE);
    msg.setFont(new Font("Segoe UI",Font.BOLD,16));

    JButton ok = new JButton("OK");
    ok.setFocusPainted(false);
    ok.setFont(new Font("Segoe UI",Font.BOLD,14));
    ok.setForeground(Color.WHITE);
    ok.setBackground(new Color(0,140,255));
    ok.setPreferredSize(new Dimension(90,35));

    ok.addActionListener(e -> dialog.dispose());

    JPanel btnPanel = new JPanel();
    btnPanel.setOpaque(false);
    btnPanel.add(ok);

    panel.add(msg,BorderLayout.CENTER);
    panel.add(btnPanel,BorderLayout.SOUTH);

    dialog.add(panel);
    dialog.pack();
    dialog.setLocationRelativeTo(this);
    dialog.setVisible(true);
}

    
    private boolean showConfirmPopup(String message){

    final boolean[] result = {false};

    JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), true);
    dialog.setUndecorated(true);

    JPanel panel = new JPanel(new BorderLayout(20,20));
    panel.setBackground(new Color(22,22,30));
    panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0,200,255),2),
            BorderFactory.createEmptyBorder(25,30,25,30)
    ));

    JLabel msg = new JLabel(message, SwingConstants.CENTER);
    msg.setForeground(Color.WHITE);
    msg.setFont(new Font("Segoe UI",Font.BOLD,16));

    JButton yes = new JButton("Yes");
    JButton no = new JButton("No");

    yes.setBackground(new Color(0,160,255));
    no.setBackground(new Color(180,50,50));

    yes.setForeground(Color.WHITE);
    no.setForeground(Color.WHITE);

    yes.setFocusPainted(false);
    no.setFocusPainted(false);

    yes.setFont(new Font("Segoe UI",Font.BOLD,14));
    no.setFont(new Font("Segoe UI",Font.BOLD,14));

    yes.setPreferredSize(new Dimension(90,35));
    no.setPreferredSize(new Dimension(90,35));

    yes.addActionListener(e -> {
        result[0] = true;
        dialog.dispose();
    });

    no.addActionListener(e -> dialog.dispose());

    JPanel btnPanel = new JPanel();
    btnPanel.setOpaque(false);
    btnPanel.add(yes);
    btnPanel.add(no);

    panel.add(msg,BorderLayout.CENTER);
    panel.add(btnPanel,BorderLayout.SOUTH);

    dialog.add(panel);
    dialog.pack();
    dialog.setLocationRelativeTo(this);
    dialog.setVisible(true);

    return result[0];
}

   /* UI METHODS */
 private JTextField createStyledField() {

        JTextField field = new JTextField(15) {

            protected void paintComponent(Graphics g) {

                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);

                int arc = 20;

                for (int i = 6; i >= 2; i -= 2) {
                    g2.setColor(new Color(0, 180, 255, 35));
                    g2.setStroke(new BasicStroke(i));
                    g2.drawRoundRect(
                            i/2, i/2,
                            getWidth()-i, getHeight()-i,
                            arc, arc);
                }

                g2.setColor(new Color(28,28,38));
                g2.fillRoundRect(
                        4,4,
                        getWidth()-8,getHeight()-8,
                        arc,arc);

                g2.dispose();
                super.paintComponent(g);
            }
        };

        field.setOpaque(false);
        field.setForeground(Color.WHITE);
        field.setCaretColor(new Color(0,200,255));
        field.setFont(new Font("Segoe UI",Font.PLAIN,16));
        field.setBorder(BorderFactory.createEmptyBorder(10,15,10,15));

        return field;
    }
   private JComboBox<String> createStyledComboBox(String[] items) {

    JComboBox<String> combo = new JComboBox<>(items) {

        @Override
        protected void paintComponent(Graphics g) {

            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

            int arc = 20;

            // glow border
            for (int i = 6; i >= 2; i -= 2) {
                g2.setColor(new Color(0, 180, 255, 35));
                g2.setStroke(new BasicStroke(i));
                g2.drawRoundRect(
                        i / 2,
                        i / 2,
                        getWidth() - i,
                        getHeight() - i,
                        arc,
                        arc
                );
            }

            // inner dark background
            g2.setColor(new Color(28, 28, 38));
            g2.fillRoundRect(
                    4,
                    4,
                    getWidth() - 8,
                    getHeight() - 8,
                    arc,
                    arc
            );

            g2.dispose();

            super.paintComponent(g);
        }
    };

    combo.setOpaque(false);
    combo.setForeground(Color.WHITE);
    combo.setFont(new Font("Segoe UI", Font.PLAIN, 16));
    combo.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 40));

    // remove default white arrow button
    combo.setUI(new BasicComboBoxUI() {

        @Override
        protected JButton createArrowButton() {

            JButton arrow = new JButton() {

                @Override
                protected void paintComponent(Graphics g) {

                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                            RenderingHints.VALUE_ANTIALIAS_ON);

                    int w = getWidth();
                    int h = getHeight();

                    g2.setColor(Color.WHITE);

                    int size = 6;
                    int x = w / 2;
                    int y = h / 2;

                    Polygon triangle = new Polygon();
                    triangle.addPoint(x - size, y - size / 2);
                    triangle.addPoint(x + size, y - size / 2);
                    triangle.addPoint(x, y + size);

                    g2.fill(triangle);

                    g2.dispose();
                }
            };

            arrow.setOpaque(false);
            arrow.setContentAreaFilled(false);
            arrow.setBorder(null);

            return arrow;
        }
    });

    // dropdown list style
    combo.setRenderer(new DefaultListCellRenderer() {

        @Override
        public Component getListCellRendererComponent(
                JList<?> list,
                Object value,
                int index,
                boolean isSelected,
                boolean cellHasFocus) {

            JLabel label = (JLabel) super.getListCellRendererComponent(
                    list, value, index, isSelected, cellHasFocus);

            label.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            label.setOpaque(true);
            label.setBackground(new Color(22, 22, 30));
            label.setForeground(Color.WHITE);

            if (isSelected) {
                label.setBackground(new Color(0, 140, 255));
            }

            return label;
        }
    });

    return combo;
}


   private JButton createStyledButton(String text) {

        JButton btn = new JButton(text) {

            @Override
            protected void paintComponent(Graphics g) {

                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);

                int arc = 25;

                for (int i = 8; i >= 2; i -= 2) {
                    g2.setColor(new Color(0, 180, 255, 40));
                    g2.setStroke(new BasicStroke(i));
                    g2.drawRoundRect(
                            i / 2,
                            i / 2,
                            getWidth() - i,
                            getHeight() - i,
                            arc,
                            arc
                    );
                }

                g2.setColor(new Color(0, 140, 255));
                g2.fillRoundRect(
                        6,
                        6,
                        getWidth() - 12,
                        getHeight() - 12,
                        arc,
                        arc
                );

                g2.dispose();
                super.paintComponent(g);
            }
        };

        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
        btn.setBorder(BorderFactory.createEmptyBorder(12, 30, 12, 30));
        btn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        return btn;
    }

    private JLabel createLabel(String text) {

    JLabel label = new JLabel(text);

    label.setForeground(Color.WHITE);
    label.setFont(new Font("Segoe UI", Font.BOLD, 16));

    return label;
}

}