import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

class Animal {
    private String name;
    private boolean isPredator;

    public Animal(String name, boolean isPredator) {
        this.name = name;
        this.isPredator = isPredator;
    }

    public String getName() {
        return name;
    }

    public boolean isPredator() {
        return isPredator;
    }

    @Override
    public String toString() {
        return name + (isPredator ? " (Predator)" : " (Herbivore)");
    }
}

class Cage {
    private int cageNumber;
    private double size;
    private int maxCapacity;
    private List<Animal> animals;

    public Cage(int cageNumber, double size, int maxCapacity) {
        this.cageNumber = cageNumber;
        this.size = size;
        this.maxCapacity = maxCapacity;
        this.animals = new ArrayList<>();
    }

    public int getCageNumber() {
        return cageNumber;
    }

    public boolean addAnimal(Animal animal) {
        if (animals.size() < maxCapacity) {
            animals.add(animal);
            return true;
        }
        return false;
    }

    public List<Animal> getAnimals() {
        return animals;
    }

    @Override
    public String toString() {
        return "Cage " + cageNumber + " (" + animals.size() + "/" + maxCapacity + " animals)";
    }
}

public class ZooGUI {
    private JFrame frame;
    private DefaultListModel<String> cageListModel;
    private DefaultListModel<String> animalListModel;
    private List<Cage> cages;
    private JList<String> cageList;
    private JList<String> animalList;

    public ZooGUI() {
        cages = new ArrayList<>();
        frame = new JFrame("Zoo Management");
        frame.setSize(500, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        JPanel panel = new JPanel(new GridLayout(1, 2));
        cageListModel = new DefaultListModel<>();
        animalListModel = new DefaultListModel<>();

        cageList = new JList<>(cageListModel);
        animalList = new JList<>(animalListModel);

        panel.add(new JScrollPane(cageList));
        panel.add(new JScrollPane(animalList));

        JPanel controlPanel = new JPanel();
        JButton addCageButton = new JButton("Add Cage");
        JButton addAnimalButton = new JButton("Add Animal");
        controlPanel.add(addCageButton);
        controlPanel.add(addAnimalButton);

        frame.add(panel, BorderLayout.CENTER);
        frame.add(controlPanel, BorderLayout.SOUTH);

        addCageButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String cageNumberStr = JOptionPane.showInputDialog("Enter Cage Number:");
                if (cageNumberStr != null && !cageNumberStr.trim().isEmpty()) {
                    try {
                        int cageNumber = Integer.parseInt(cageNumberStr);
                        Cage cage = new Cage(cageNumber, 50.0, 2);  // Default max capacity = 2
                        cages.add(cage);
                        cageListModel.addElement(cage.toString());
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(frame, "Invalid number entered!", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        addAnimalButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (cages.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "No cages available! Add a cage first.");
                    return;
                }

                String name = JOptionPane.showInputDialog("Enter Animal Name:");
                if (name == null || name.trim().isEmpty()) return;

                boolean isPredator = JOptionPane.showConfirmDialog(null, "Is it a predator?", "Animal Type", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
                Animal animal = new Animal(name, isPredator);

                // Let user select a cage
                String[] cageOptions = cages.stream().map(Cage::toString).toArray(String[]::new);
                String selectedCageStr = (String) JOptionPane.showInputDialog(
                        frame, "Select a cage:", "Choose Cage",
                        JOptionPane.QUESTION_MESSAGE, null, cageOptions, cageOptions[0]
                );

                if (selectedCageStr != null) {
                    Cage selectedCage = cages.get(cageListModel.indexOf(selectedCageStr));
                    if (selectedCage.addAnimal(animal)) {
                        animalListModel.addElement(animal.toString());
                        updateCageList();
                    } else {
                        JOptionPane.showMessageDialog(frame, "Cage is full!", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        frame.setVisible(true);
    }

    private void updateCageList() {
        cageListModel.clear();
        for (Cage cage : cages) {
            cageListModel.addElement(cage.toString());
        }
    }

    public static void main(String[] args) {
        new ZooGUI();
    }
}
