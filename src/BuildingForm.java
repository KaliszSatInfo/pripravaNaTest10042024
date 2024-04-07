import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.AbstractTableModel;
import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class BuildingForm extends JFrame {
    private JPanel panel;
    private JTextField txtId;
    private JTextField txtName;
    private JCheckBox chcBoxFinished;
    private JTextField txtDate;
    private JTable table;
    private JButton prevButton;
    private JButton nextButton;
    private int index = 0;
    private final List<Building> buildingList = new ArrayList<>();
    private File selectedFile;

    public BuildingForm() {
        setContentPane(panel);
        setSize(500, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        initMenu();
        prevButton.addActionListener(_ -> displayPrev());
        nextButton.addActionListener(_ -> displayNext());
    }

    public void initMenu() {
        JMenuBar jmBar = new JMenuBar();
        setJMenuBar(jmBar);

        JMenuItem chooseItem = new JMenuItem("choose file");
        jmBar.add(chooseItem);
        chooseItem.addActionListener(_ -> chooseFile());

        JMenuItem addNewBuilding = new JMenuItem("add new building");
        jmBar.add(addNewBuilding);
        addNewBuilding.addActionListener(_ -> createNewBuilding());

        JMenuItem renderTableItem = new JMenuItem("render table");
        jmBar.add(renderTableItem);
        renderTableItem.addActionListener(_-> renderTable());
    }

    public void displayPrev() {
        if (index > 0) {
            index--;
            display(getBuilding(index));
        }
    }

    public void displayNext() {
        if (index < buildingList.size() - 1) {
            index++;
            display(getBuilding(index));
        }
    }

    public void display(Building building) {
        txtId.setText(String.valueOf(building.getId()));
        txtName.setText(building.getName());
        chcBoxFinished.setSelected(building.isFinished());
        txtDate.setText(String.valueOf(building.getDateOfFinishedBuild()));
    }

    public Building getBuilding(int i) {
        return buildingList.get(i);
    }

    public void chooseFile() {
        JFileChooser fc = new JFileChooser(".");
        fc.setFileFilter(new FileNameExtensionFilter("text files", "txt"));
        int result = fc.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            selectedFile = fc.getSelectedFile();
            readFile(selectedFile);
        }
    }

    public void readFile(File selectedFile) {
        index = 0;
        buildingList.clear();
        try (Scanner sc = new Scanner(new BufferedReader(new FileReader(selectedFile)))) {
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                String[] parts = line.split(";");
                int id = Integer.parseInt(parts[0]);
                String name = parts[1];
                boolean finished = parts[2].equals("yes");
                LocalDate dateOfFinishedBuild = LocalDate.parse(parts[3]);
                buildingList.add(new Building(id, name, finished, dateOfFinishedBuild));
                display(buildingList.get(index));
            }
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(this, "File not found: " + e.getLocalizedMessage());
        }
    }

    public void createNewBuilding() {
        if (selectedFile == null) {
            JOptionPane.showMessageDialog(this, "YOU FORGOT TO SELECT A FILE YOU GODDAMN FAGGOT");
            return;
        }
        int maxId = 0;
        for (Building building : buildingList){
            if (building.getId() > maxId){
                maxId = building.getId();
            }
        }
        int nextId = maxId +1;
        JTextField idField = new JTextField(String.valueOf(nextId));
        JTextField nameField = new JTextField();
        JCheckBox finishedCheckBox = new JCheckBox();
        JTextField dateField = new JTextField();

        Object[] fields = {
                "ID:", idField,
                "Name:", nameField,
                "Finished:", finishedCheckBox,
                "Date of Finished Build (yyyy-MM-dd):", dateField
        };

        int result = JOptionPane.showConfirmDialog(this, fields, "Enter New Building Details", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            int id = Integer.parseInt(idField.getText());
            String name = nameField.getText();
            boolean finished = finishedCheckBox.isSelected();
            LocalDate dateOfFinishedBuild;
            if (dateField.getText().isEmpty()) {
                dateOfFinishedBuild = LocalDate.now();
            } else {
                dateOfFinishedBuild = LocalDate.parse(dateField.getText());
            }
            Building newBuilding = new Building(id, name, finished, dateOfFinishedBuild);
            buildingList.add(newBuilding);
            JOptionPane.showMessageDialog(this, "New building added");
            writeIntoFile(selectedFile);
        }
    }

    public void writeIntoFile(File selectedFile) {
        try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(selectedFile)))) {
            for (Building building : buildingList) {
                writer.print(building.getId() + ";" + building.getName() + ";" + (building.isFinished() ? "yes" : "no") + ";" + building.getDateOfFinishedBuild() + "\n");
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "There was a problem writing into the file: " + e.getLocalizedMessage());
        }
    }

    public void renderTable() {
        table.setModel(new BuildingTableModel());
    }

    class BuildingTableModel extends AbstractTableModel {

        @Override
        public int getRowCount() {
            return buildingList.size();
        }

        @Override
        public int getColumnCount() {
            return 4;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            Building currentBuilding = buildingList.get(rowIndex);
            return switch (columnIndex) {
                case 0 -> currentBuilding.getId();
                case 1 -> currentBuilding.getName();
                case 2 -> currentBuilding.isFinished();
                case 3 -> currentBuilding.getDateOfFinishedBuild();
                default -> null;
            };
        }
    }

    public static void main(String[] args) {
        BuildingForm b = new BuildingForm();
        b.setVisible(true);
    }
}
