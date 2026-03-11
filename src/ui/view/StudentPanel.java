package src.ui.view;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.*;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import src.models.Students;
import src.services.StudentService;

public class StudentPanel extends JPanel {

    private JTextField idField;
    private JTextField nameField;

    private JComboBox<String> semesterBox;
    private JComboBox<String> departmentBox;

    private JTable studentTable;
    private DefaultTableModel tableModel;

    private StudentService service = new StudentService();

    public StudentPanel(){

        setLayout(new BorderLayout(25,25));
        setBorder(BorderFactory.createEmptyBorder(30,40,30,40));
        setBackground(new Color(15,15,20));

        add(createTitle(),BorderLayout.NORTH);
        add(createMainContent(),BorderLayout.CENTER);

        loadTable();
    }

    private JLabel createTitle(){

        JLabel title = new JLabel("STUDENTS");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI",Font.BOLD,30));

        return title;
    }

    private JPanel createMainContent(){

        JPanel main = new JPanel(new BorderLayout(25,25));
        main.setOpaque(false);

        main.add(createFormContainer(),BorderLayout.NORTH);
        main.add(createTableContainer(),BorderLayout.CENTER);

        return main;
    }

    private JPanel createFormContainer(){

        JPanel container = createGlowPanel(25);
        container.setLayout(new BorderLayout());
        container.setBorder(BorderFactory.createEmptyBorder(25,25,25,25));

        container.add(createFormPanel(),BorderLayout.CENTER);

        return container;
    }

    private JPanel createFormPanel(){

        JPanel form = new JPanel(new GridBagLayout());
        form.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10,10,10,10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        idField = createStyledField();
        nameField = createStyledField();

        semesterBox = createStyledComboBox(new String[]{
                "1","2","3","4","5","6","7","8"
        });

        departmentBox = createStyledComboBox(new String[]{
                "Computer Science Engineering",
                "Information Technology",
                "Electronics Engineering",
                "Mechanical Engineering",
                "Civil Engineering"
        });

        JButton addBtn = createStyledButton("Add");
        JButton updateBtn = createStyledButton("Update");
        JButton deleteBtn = createStyledButton("Delete");

        gbc.gridx=0; gbc.gridy=0;
        form.add(createLabel("Roll Number:"),gbc);
        gbc.gridx=1;
        form.add(idField,gbc);

        gbc.gridx=0; gbc.gridy=1;
        form.add(createLabel("Name:"),gbc);
        gbc.gridx=1;
        form.add(nameField,gbc);

        gbc.gridx=0; gbc.gridy=2;
        form.add(createLabel("Semester:"),gbc);
        gbc.gridx=1;
        form.add(semesterBox,gbc);

        gbc.gridx=0; gbc.gridy=3;
        form.add(createLabel("Department:"),gbc);
        gbc.gridx=1;
        form.add(departmentBox,gbc);

        gbc.gridx=0; gbc.gridy=4;
        gbc.gridwidth=2;

        JPanel btnPanel = new JPanel();
        btnPanel.setOpaque(false);

        btnPanel.add(addBtn);
        btnPanel.add(updateBtn);
        btnPanel.add(deleteBtn);

        form.add(btnPanel,gbc);

        addBtn.addActionListener(e -> addStudent());
        updateBtn.addActionListener(e -> updateStudent());
        deleteBtn.addActionListener(e -> deleteStudent());

        return form;
    }

    private JPanel createTableContainer(){

        JPanel container = createGlowPanel(30);
        container.setLayout(new BorderLayout());
        container.setBorder(BorderFactory.createEmptyBorder(25,25,25,25));

        container.add(createTable(),BorderLayout.CENTER);

        return container;
    }

    private JScrollPane createTable(){

        tableModel = new DefaultTableModel();

        tableModel.setColumnIdentifiers(new String[]{
                "Roll Number","Name","Semester","Department"
        });

        studentTable = new JTable(tableModel);

        studentTable.setRowHeight(40);
        studentTable.setFont(new Font("Segoe UI",Font.PLAIN,18));
        studentTable.setForeground(Color.WHITE);
        studentTable.setBackground(new Color(22,22,30));
        studentTable.setGridColor(Color.WHITE);
        studentTable.setSelectionBackground(new Color(0,150,255));

        JTableHeader header = studentTable.getTableHeader();
        header.setFont(new Font("Segoe UI",Font.BOLD,20));
        header.setForeground(new Color(0,220,255));
        header.setBackground(new Color(18,18,25));

        studentTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        studentTable.addMouseListener(new MouseAdapter(){

            @Override
            public void mouseClicked(MouseEvent e){

                int row = studentTable.getSelectedRow();

                if(row == -1) return;

                String roll = tableModel.getValueAt(row,0).toString();
                String name = tableModel.getValueAt(row,1).toString();
                String semester = tableModel.getValueAt(row,2).toString();
                String department = tableModel.getValueAt(row,3).toString();

                idField.setText(roll);
                nameField.setText(name);

                semesterBox.setSelectedItem(semester);
                departmentBox.setSelectedItem(department);

                idField.setEditable(false);
            }
        });

        JScrollPane scroll = new JScrollPane(studentTable);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(new Color(22,22,30));

        return scroll;
    }

    private boolean validateInput(){

        if(idField.getText().trim().isEmpty()){
            showPopup("Please enter the Roll Number.");
            return false;
        }

        if(nameField.getText().trim().isEmpty()){
            showPopup("Please enter the Student Name.");
            return false;
        }

        return true;
    }

    private void addStudent(){

        try{

            if(!validateInput()) return;

            Students s = new Students();

            s.setRollNumber(idField.getText());
            s.setName(nameField.getText());
            s.setDepartment(departmentBox.getSelectedItem().toString());
            s.setSemester(Integer.parseInt(semesterBox.getSelectedItem().toString()));

            service.add(s);

            showPopup("Student Added Successfully");

            loadTable();
            clearFields();

        }catch(Exception e){

            e.printStackTrace();
            showPopup(e.getMessage());
        }
    }

    private void updateStudent(){

        try{

            if(!validateInput()) return;

            boolean confirm = showConfirmPopup("Update Student: " + idField.getText() + " ?");

            if(!confirm) return;

            Students s = new Students();

            s.setRollNumber(idField.getText());
            s.setName(nameField.getText());
            s.setDepartment(departmentBox.getSelectedItem().toString());
            s.setSemester(Integer.parseInt(semesterBox.getSelectedItem().toString()));

            Students updated = service.update(s);

            if(updated == null){
                showPopup("Student not found.");
                return;
            }

            showPopup("Student Updated Successfully");

            loadTable();
            clearFields();

        }catch(Exception e){

            e.printStackTrace();
            showPopup(e.getMessage());
        }
    }

    private void deleteStudent(){

        try{

            boolean confirm = showConfirmPopup("Delete Student: " + idField.getText() + " ?");

            if(!confirm) return;

            Students deleted = service.delete(idField.getText());

            if(deleted == null){
                showPopup("Student not found.");
                return;
            }

            showPopup("Student Deleted Successfully");

            loadTable();
            clearFields();

        }catch(Exception e){

            e.printStackTrace();
            showPopup(e.getMessage());
        }
    }

    private void loadTable(){

        tableModel.setRowCount(0);

        try{

            List<Students> list = service.getAllStudents();

            for(Students s : list){

                tableModel.addRow(new Object[]{
                        s.getRollNumber(),
                        s.getName(),
                        s.getSemester(),
                        s.getDepartment()
                });
            }

        }catch(Exception e){

            e.printStackTrace();
        }
    }

    private void clearFields(){

        idField.setText("");
        nameField.setText("");

        semesterBox.setSelectedIndex(0);
        departmentBox.setSelectedIndex(0);

        idField.setEditable(true);
    } 

    /* Mouse  */

private void addTableSelectionListener(){

    studentTable.addMouseListener(new MouseAdapter() {

        @Override
        public void mouseClicked(MouseEvent e) {

            int row = studentTable.getSelectedRow();

            if(row >= 0){

                String roll = tableModel.getValueAt(row, 0).toString();
                String name = tableModel.getValueAt(row, 1).toString();
                String semester = tableModel.getValueAt(row, 2).toString();
                String department = tableModel.getValueAt(row, 3).toString();

                idField.setText(roll);
                nameField.setText(name);
                semesterBox.setSelectedItem(semester);
                departmentBox.setSelectedItem(department);

                idField.setEditable(false);
            }
        }
    });
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

    /* UI */

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

    private JLabel createLabel(String text){

        JLabel label=new JLabel(text);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Segoe UI",Font.BOLD,16));

        return label;
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

}