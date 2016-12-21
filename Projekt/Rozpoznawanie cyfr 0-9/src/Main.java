import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;


public class Main extends JFrame {

	
	private JPanel mainPanel;
	private JLabel info;
	private JTextField characterName;
	private Panel panel;
	private JPanel jPanel;
	Siec kohonen;
	private HashMap<String, ArrayList<ArrayList<Integer>>> znaki;
	
	

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				new Main().setVisible(true);
				
			}
		});
	}

	public Main() {
		super("Kohonen");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 550, 350);
		mainPanel = new JPanel();
		mainPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		mainPanel.setLayout(new BorderLayout(0, 0));
		setContentPane(mainPanel);
		
		final JButton zapiszBtn = new JButton("Zapisz");
		final JButton rozpoznajBtn = new JButton("Rozpoznaj");
		final JButton dodajBtn = new JButton("Dodaj");
		final JButton uczBtn = new JButton("Ucz");
		final JButton wczytajBtn = new JButton("Wczyaj");
		
		znaki = new HashMap<String, ArrayList<ArrayList<Integer>>>();

		jPanel = new JPanel();
		panel = new Panel();
		mainPanel.add(panel);	
		mainPanel.add(jPanel, BorderLayout.SOUTH);
		


		wczytajBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser fileChooser = new JFileChooser();
				int ret = fileChooser.showOpenDialog(panel);
				
				if(ret == JFileChooser.APPROVE_OPTION) {
					File plikDoOtwarcia = fileChooser.getSelectedFile();
					
					try {
						Scanner scanner = new Scanner(plikDoOtwarcia);
						
						int kolumna = Integer.parseInt(scanner.nextLine());
						int wiersz = Integer.parseInt(scanner.nextLine());
						int liczbaNeuronow = Integer.parseInt(scanner.nextLine());			
						Ustawienia.KOLUMNA = kolumna;
						Ustawienia.WIERSZ = wiersz;	
						ArrayList<Pair<String, ArrayList<Double>>> info = new ArrayList<Pair<String,ArrayList<Double>>>();
						
						for (int i = 0; i < liczbaNeuronow; i++) {
							ArrayList<Double> wagi = new ArrayList<Double>();
							
							for (int j = 0; j < wiersz*kolumna; j++) {
								double odczytanaWaga = Double.parseDouble(scanner.nextLine());
								
								wagi.add(odczytanaWaga);
							}		
							String nazwa = scanner.nextLine();						
							Pair<String, ArrayList<Double>> pair = new Pair<String, ArrayList<Double>>(nazwa, wagi);	
							info.add(pair);
						}
						
						kohonen = new Siec(info);
					} catch (IOException e) {
						 JOptionPane.showMessageDialog(panel, "Blad podczas otwierania pliku");
						
						e.printStackTrace();
					}
				}
				
				rozpoznajBtn.setEnabled(true);
				uczBtn.setEnabled(false);
				dodajBtn.setEnabled(false);
				zapiszBtn.setEnabled(true);
				characterName.setText("");
				characterName.setEnabled(false);
			}
		});
		wczytajBtn.setToolTipText("Otwórz konfiguracjê");
		jPanel.add(wczytajBtn);
		
		zapiszBtn.setEnabled(false);
		zapiszBtn.setToolTipText("Zapisz konfiguracjê");
		zapiszBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser fileChooser = new JFileChooser();
				int ret = fileChooser.showSaveDialog(panel);
				
				if(ret == JFileChooser.APPROVE_OPTION) {
					File saveFile = fileChooser.getSelectedFile();
					
					try {
						saveFile.createNewFile();
						String path = saveFile.getPath();
						
						FileWriter fileWrite = new FileWriter(path);
						BufferedWriter writer = new BufferedWriter(fileWrite);
						
						kohonen.zapisz(writer);
						
						writer.close();
					} catch (IOException e) {
						JOptionPane.showMessageDialog(panel, "Blad podczas zapisu pliku");
					}
				}
			}
		});
		jPanel.add(zapiszBtn);
		
		jPanel.add(new JSeparator());
		
		rozpoznajBtn.setEnabled(false);
		rozpoznajBtn.setToolTipText("Rozpoznaj cyfre");
		rozpoznajBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				ArrayList<Integer> input = panel.getInput();
				panel.znajdzCyfre();
				
				String cyfra = kohonen.rozpoznaj(input);
				
				info.setText("Rozpoznano " + cyfra);
			}
			
		});
		jPanel.add(rozpoznajBtn);
		
		JButton clearButton = new JButton("Wyczysc");
		clearButton.setToolTipText("Wyczysc panel");
		clearButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				info.setText("\n");
				
				panel.clear();
			}
			
		});
		jPanel.add(clearButton);
		
		jPanel.add(new JSeparator());
		
		characterName = new JTextField();
		characterName.setToolTipText("Wpisz cyfre");
		characterName.setFont(new Font("Dialog", Font.PLAIN, 16));
		jPanel.add(characterName);
		characterName.setColumns(1);
		
		dodajBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				ArrayList<Integer> input = panel.getInput();
				panel.znajdzCyfre();
				
				String nazwa = characterName.getText();
				
				if (!znaki.containsKey(nazwa))
					znaki.put(nazwa, new ArrayList<ArrayList<Integer>>());
				
				znaki.get(nazwa).add(input);
				
				info.setText("Added " + nazwa);
			}
		});
		dodajBtn.setToolTipText("Add character");
		jPanel.add(dodajBtn);
		
		uczBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				kohonen = new Siec(znaki);
				
				kohonen.ucz();
				
				rozpoznajBtn.setEnabled(true);
				uczBtn.setEnabled(false);
				dodajBtn.setEnabled(false);
				zapiszBtn.setEnabled(true);
				characterName.setText("");
				characterName.setEnabled(false);
				
				info.setText("Learned");
				panel.clear();
			}
		});
		jPanel.add(uczBtn);
		
		info = new JLabel("Wcisnij wzytaj by zaladowac dane");
		info.setHorizontalAlignment(SwingConstants.CENTER);
		mainPanel.add(info, BorderLayout.NORTH);
	}

}
