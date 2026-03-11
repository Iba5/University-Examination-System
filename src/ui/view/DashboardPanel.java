package src.ui.view;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;

public class DashboardPanel extends JPanel {

    private JComboBox<String> filterBox;
    private JTable examTable;
    private DefaultTableModel examModel;

    public DashboardPanel() {

        setLayout(new BorderLayout(30,30));
        setBorder(BorderFactory.createEmptyBorder(40,50,40,50));
        setBackground(new Color(15,15,20));

        add(createTitle(),BorderLayout.NORTH);
        add(createMainContent(),BorderLayout.CENTER);

        showTodayExams();
    }

    private JLabel createTitle(){

        JLabel title = new JLabel("DASHBOARD");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI",Font.BOLD,32));

        return title;
    }

    private JPanel createMainContent(){

        JPanel main = new JPanel(new BorderLayout(30,30));
        main.setOpaque(false);

        main.add(createTopSection(),BorderLayout.NORTH);
        main.add(createDashboardBody(),BorderLayout.CENTER);

        return main;
    }

    private JPanel createTopSection(){

        JPanel panel = createGlowPanel(25);
        panel.setLayout(new FlowLayout(FlowLayout.LEFT,20,20));
        panel.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));

        JLabel label = new JLabel("Filter By:");
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Segoe UI",Font.BOLD,16));

        filterBox = createStyledComboBox(new String[]{
                "Today's Exams",
                "All Exams",
                "Upcoming Exams",
                "Completed Exams"
        });

        filterBox.addActionListener(e -> applyFilter());

        panel.add(label);
        panel.add(filterBox);

        return panel;
    }

    private JPanel createDashboardBody(){

        JPanel body = new JPanel(new GridLayout(1,2,30,30));
        body.setOpaque(false);

        body.add(createExamSection());
        body.add(createStatsSection());

        return body;
    }

    private JPanel createExamSection(){

        JPanel panel = createGlowPanel(30);
        panel.setLayout(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(25,25,25,25));

        JLabel title = new JLabel("EXAMINATION SCHEDULE");
        title.setForeground(new Color(0,200,255));
        title.setFont(new Font("Segoe UI",Font.BOLD,22));

        String[] columns = {"Subject","Hall","Time"};

        examModel = new DefaultTableModel(columns,0);
        examTable = new JTable(examModel);

        styleTable(examTable);

        JScrollPane scroll = new JScrollPane(examTable);

        scroll.setBorder(
                BorderFactory.createLineBorder(new Color(0,200,255),1,true)
        );

        scroll.getViewport().setBackground(new Color(22,22,30));

        panel.add(title,BorderLayout.NORTH);
        panel.add(scroll,BorderLayout.CENTER);

        return panel;
    }

    private JPanel createStatsSection(){

        JPanel stats = new JPanel(new GridLayout(2,2,20,20));
        stats.setOpaque(false);

        stats.add(createCard("Students","320"));
        stats.add(createCard("Subjects","25"));
        stats.add(createCard("Halls","12"));
        stats.add(createCard("Invigilators","18"));

        return stats;
    }

    private JPanel createCard(String title,String value){

        JPanel card = createGlowPanel(25);
        card.setLayout(new BorderLayout());
        card.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setForeground(new Color(0,200,255));
        titleLabel.setFont(new Font("Segoe UI",Font.BOLD,16));

        JLabel valueLabel = new JLabel(value);
        valueLabel.setForeground(Color.WHITE);
        valueLabel.setFont(new Font("Segoe UI",Font.BOLD,26));

        card.add(titleLabel,BorderLayout.NORTH);
        card.add(valueLabel,BorderLayout.CENTER);

        return card;
    }

    private void applyFilter(){

        String selected = filterBox.getSelectedItem().toString();

        if(selected.equals("Today's Exams")) showTodayExams();
        else if(selected.equals("All Exams")) showAllExams();
        else if(selected.equals("Upcoming Exams")) showUpcomingExams();
        else if(selected.equals("Completed Exams")) showCompletedExams();
    }

    private void showTodayExams(){

        examModel.setRowCount(0);

        examModel.addRow(new Object[]{"Data Structures","Hall A","09:00"});
        examModel.addRow(new Object[]{"Calculus","Hall B","11:00"});
    }

    private void showAllExams(){

        examModel.setRowCount(0);

        examModel.addRow(new Object[]{"Data Structures","Hall A","09:00"});
        examModel.addRow(new Object[]{"Calculus","Hall B","11:00"});
        examModel.addRow(new Object[]{"Operating Systems","Hall C","14:00"});
        examModel.addRow(new Object[]{"Computer Networks","Hall B","16:00"});
    }

    private void showUpcomingExams(){

        examModel.setRowCount(0);

        examModel.addRow(new Object[]{"Computer Networks","Hall C","May 21"});
        examModel.addRow(new Object[]{"Artificial Intelligence","Hall A","May 22"});
    }

    private void showCompletedExams(){

        examModel.setRowCount(0);

        examModel.addRow(new Object[]{"Operating Systems","Hall B","May 10"});
    }

    private void styleTable(JTable table){

        table.setRowHeight(40);
        table.setFont(new Font("Segoe UI",Font.PLAIN,16));
        table.setForeground(Color.WHITE);
        table.setBackground(new Color(22,22,30));

        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0,0));

        table.setSelectionBackground(new Color(0,150,255));
        table.setSelectionForeground(Color.WHITE);

        

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI",Font.BOLD,18));
        header.setForeground(new Color(0,220,255));
        header.setBackground(new Color(18,18,25));
    }

    private JComboBox<String> createStyledComboBox(String[] items){

        JComboBox<String> combo = new JComboBox<>(items);
        combo.setForeground(Color.WHITE);
        combo.setFont(new Font("Segoe UI",Font.PLAIN,16));
        combo.setBackground(new Color(22,22,30));

        return combo;
    }

    private JPanel createGlowPanel(int arc){

        return new JPanel(){

            protected void paintComponent(Graphics g){

                Graphics2D g2 = (Graphics2D)g.create();
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
                            arc
                    );
                }

                g2.setColor(new Color(22,22,30));
                g2.fillRoundRect(
                        6,
                        6,
                        getWidth()-12,
                        getHeight()-12,
                        arc,
                        arc
                );

                g2.dispose();
            }
        };
    }
}