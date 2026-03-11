package src.ui.view;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import javax.swing.*;
import javax.swing.plaf.basic.BasicComboBoxUI;  // Add this import
import javax.swing.table.*;
import java.util.List;

import src.models.*;
import src.services.*;

public class AllocationPanel extends JPanel {

    private JComboBox<String> rollBox;
    private JComboBox<String> examBox;
    private JComboBox<String> hallBox;
    private JComboBox<String> invigilatorBox;

    private JTable allocationTable;
    private DefaultTableModel tableModel;

    private AllocationService allocationService = new AllocationService();
    private ExamService examService = new ExamService();
    private HallService hallService = new HallService();
    private InvigilatorService invigilatorService = new InvigilatorService();
    private StudentService studentService = new StudentService();

    public AllocationPanel() {

        setLayout(new BorderLayout(25,25));
        setBorder(BorderFactory.createEmptyBorder(30,40,30,40));
        setBackground(new Color(15,15,20));

        add(createTitle(),BorderLayout.NORTH);
        add(createMainPanel(),BorderLayout.CENTER);

        loadStudents();
        loadExams();
        loadHalls();
        loadInvigilators();
        loadTable();
        addTableSelectionListener();
    }

    private void addTableSelectionListener(){

    allocationTable.addMouseListener(new MouseAdapter(){

        public void mouseClicked(MouseEvent e){

            int row = allocationTable.getSelectedRow();

            if(row >= 0){

                String roll = tableModel.getValueAt(row,1).toString();
                int examId = Integer.parseInt(tableModel.getValueAt(row,2).toString());
                int hallId = Integer.parseInt(tableModel.getValueAt(row,3).toString());

                for(int i=0;i<rollBox.getItemCount();i++){
                    if(rollBox.getItemAt(i).startsWith(roll)){
                        rollBox.setSelectedIndex(i);
                        break;
                    }
                }

                for(int i=0;i<examBox.getItemCount();i++){
                    if(examBox.getItemAt(i).startsWith(String.valueOf(examId))){
                        examBox.setSelectedIndex(i);
                        break;
                    }
                }

                for(int i=0;i<hallBox.getItemCount();i++){
                    if(hallBox.getItemAt(i).startsWith(String.valueOf(hallId))){
                        hallBox.setSelectedIndex(i);
                        break;
                    }
                }
            }
        }
    });
}

    private JLabel createTitle(){

        JLabel title = new JLabel("ALLOCATION");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI",Font.BOLD,30));

        return title;
    }

   private JSplitPane splitPane;
private JPanel formContainerRef;
private JPanel tableContainerRef;

private JPanel createMainPanel(){

    JPanel main = new JPanel(new BorderLayout(25,25));
    main.setOpaque(false);

    formContainerRef = createFormPanel();
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

    main.add(splitPane,BorderLayout.CENTER);

    addMouseResizeLogic();

    return main;
}
private JPanel createTableContainer(){

    JPanel container = createGlowPanel(30);
    container.setLayout(new BorderLayout());
    container.setBorder(BorderFactory.createEmptyBorder(25,25,25,25));

    container.add(createTablePanel(),BorderLayout.CENTER);

    return container;
}
private void addMouseResizeLogic(){

    formContainerRef.addMouseListener(new MouseAdapter(){

        public void mouseEntered(MouseEvent e){

            SwingUtilities.invokeLater(() ->
                    splitPane.setDividerLocation(0.65)
            );
        }
    });

    tableContainerRef.addMouseListener(new MouseAdapter(){

        public void mouseEntered(MouseEvent e){

            SwingUtilities.invokeLater(() ->
                    splitPane.setDividerLocation(0.35)
            );
        }
    });
}
    private JPanel createFormPanel(){

        JPanel container = createGlowPanel(25);
        container.setLayout(new BorderLayout());
        container.setBorder(BorderFactory.createEmptyBorder(25,25,25,25));

        JPanel form = new JPanel(new GridBagLayout());
        form.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10,10,10,10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        rollBox = createStyledComboBox(new String[]{});
        examBox = createStyledComboBox(new String[]{});
        hallBox = createStyledComboBox(new String[]{});
        invigilatorBox = createStyledComboBox(new String[]{});

        JButton allocateBtn = createStyledButton("Allocate");
        JButton updateBtn = createStyledButton("Update");
        JButton deleteBtn = createStyledButton("Delete");

        allocateBtn.addActionListener(e -> allocateSeat());
        updateBtn.addActionListener(e -> updateAllocation());
        deleteBtn.addActionListener(e -> deleteAllocation());

        gbc.gridx=0; gbc.gridy=0;
        form.add(createLabel("Student:"),gbc);
        gbc.gridx=1;
        form.add(rollBox,gbc);

        gbc.gridx=0; gbc.gridy=1;
        form.add(createLabel("Exam:"),gbc);
        gbc.gridx=1;
        form.add(examBox,gbc);

        gbc.gridx=0; gbc.gridy=2;
        form.add(createLabel("Hall:"),gbc);
        gbc.gridx=1;
        form.add(hallBox,gbc);

        gbc.gridx=0; gbc.gridy=3;
        form.add(createLabel("Invigilator:"),gbc);
        gbc.gridx=1;
        form.add(invigilatorBox,gbc);

        gbc.gridx=0; gbc.gridy=4;
        gbc.gridwidth=2;

        JPanel btnPanel = new JPanel();
        btnPanel.setOpaque(false);

        btnPanel.add(allocateBtn);
        btnPanel.add(updateBtn);
        btnPanel.add(deleteBtn);

        form.add(btnPanel,gbc);

        container.add(form);

        return container;
    }

    private JScrollPane createTablePanel(){

    tableModel = new DefaultTableModel();

    tableModel.setColumnIdentifiers(new String[]{
            "ID","Roll Number","Exam","Hall"
    });

    allocationTable = new JTable(tableModel);

    allocationTable.setRowHeight(40);
    allocationTable.setFont(new Font("Segoe UI",Font.PLAIN,18));
    allocationTable.setForeground(Color.WHITE);
    allocationTable.setBackground(new Color(22,22,30));
    allocationTable.setGridColor(new Color(40,40,55));

    allocationTable.setSelectionBackground(new Color(0,140,255));
    allocationTable.setSelectionForeground(Color.WHITE);

    JTableHeader header = allocationTable.getTableHeader();
    header.setFont(new Font("Segoe UI",Font.BOLD,20));
    header.setForeground(new Color(0,220,255));
    header.setBackground(new Color(18,18,25));
    header.setPreferredSize(new Dimension(header.getWidth(),50));

    JScrollPane scroll = new JScrollPane(allocationTable);
    scroll.setBorder(null);
    scroll.getViewport().setBackground(new Color(22,22,30));

    return scroll;
}

    /* ---------------- DATABASE LOAD ---------------- */

    private void loadStudents(){

        rollBox.removeAllItems();

        try{

            List<Students> students = studentService.getAllStudents();

            for(Students s : students){

                rollBox.addItem(
                        s.getRollNumber()+" - "+s.getName()
                );
            }

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private void loadExams(){

        examBox.removeAllItems();

        try{

            List<Exam> exams = examService.getAll();

            for(Exam e : exams){

                examBox.addItem(
                        e.getExamId()+" - "+e.getCourseCode()
                );
            }

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private void loadHalls(){

        hallBox.removeAllItems();

        try{

            List<Hall> halls = hallService.getAll();

            for(Hall h : halls){

                hallBox.addItem(
                        h.getHallId()+" - "+h.getHallName()
                );
            }

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private void loadInvigilators(){

        invigilatorBox.removeAllItems();

        try{

            List<Invigilator> inv = invigilatorService.getAll();

            for(Invigilator i : inv){

                invigilatorBox.addItem(
                        i.getInvigilatorId()+" - "+i.getName()
                );
            }

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private void loadTable(){

        tableModel.setRowCount(0);

        try{

            List<Allocation> list = allocationService.getAll();

            for(Allocation a : list){

                tableModel.addRow(new Object[]{
                        a.getAllocationId(),
                        a.getRollNumber(),
                        a.getExamId(),
                        a.getHallId()
                });
            }

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    /* ---------------- BUTTON ACTIONS ---------------- */

    private void allocateSeat(){

        try{

            String roll = rollBox.getSelectedItem().toString().split(" - ")[0];
            int examId = Integer.parseInt(examBox.getSelectedItem().toString().split(" - ")[0]);
            int hallId = Integer.parseInt(hallBox.getSelectedItem().toString().split(" - ")[0]);

            Allocation a = new Allocation(0,roll,examId,hallId);

            allocationService.allocate(a);

            showPopup("Allocation Successful");

            loadTable();

        }catch(Exception e){

    Throwable cause = e.getCause();

    if(cause != null){
        showPopup(cause.getMessage());
    }else{
        showPopup(e.getMessage());
    }
}
    }

    private void updateAllocation(){

        try{

            boolean confirm = showConfirmPopup("Update this student's allocation?");

            if(!confirm) return;

            String rollNumber =
                    rollBox.getSelectedItem().toString().split(" - ")[0];

            int examId = Integer.parseInt(
                    examBox.getSelectedItem().toString().split(" - ")[0]
            );

            int hallId = Integer.parseInt(
                    hallBox.getSelectedItem().toString().split(" - ")[0]
            );

            Allocation allocation = new Allocation(
                    0,
                    rollNumber,
                    examId,
                    hallId
            );

            boolean updated =
                    allocationService.updateByStudentExam(allocation);

            if(updated){
                showPopup("Allocation Updated");
            }else{
                showPopup("No allocation found for this student");
            }

            loadTable();

        }catch(Exception e){

    Throwable cause = e.getCause();

    if(cause != null){
        showPopup(cause.getMessage());
    }else{
        showPopup(e.getMessage());
    }
}
    }

    private void deleteAllocation(){

        try{

            boolean confirm =
                    showConfirmPopup("Delete this student's allocation?");

            if(!confirm) return;

            String rollNumber =
                    rollBox.getSelectedItem().toString().split(" - ")[0];

            int examId = Integer.parseInt(
                    examBox.getSelectedItem().toString().split(" - ")[0]
            );

            boolean deleted =
                    allocationService.deleteByStudentExam(rollNumber,examId);

            if(deleted){
                showPopup("Allocation Deleted");
            }else{
                showPopup("Allocation not found");
            }

            loadTable();

        }catch(Exception e){

    Throwable cause = e.getCause();

    if(cause != null){
        showPopup(cause.getMessage());
    }else{
        showPopup(e.getMessage());
    }
}
    }

    /* ---------------- POPUPS ---------------- */

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

    
    /* ---------------- UI HELPERS ---------------- */
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
 
    private JLabel createLabel(String text){

        JLabel label=new JLabel(text);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Segoe UI",Font.BOLD,16));

        return label;
    }


}