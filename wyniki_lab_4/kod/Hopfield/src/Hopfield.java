import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

@SuppressWarnings("serial")
public class Hopfield extends JFrame implements ActionListener {
	public static final int NETWORK_SIZE = 4;							// number of neurons in the network
	JTextField matrix[][] = new JTextField[NETWORK_SIZE][NETWORK_SIZE];	// weight matrix text fields
	JComboBox input[] = new JComboBox[NETWORK_SIZE];					// input pattern combo boxes
	JTextField output[] = new JTextField[NETWORK_SIZE];					// output of network text fields
	JButton btnClear = new JButton("Clear");							// clear button
	JButton btnTrain = new JButton("Train Network");					// train button
	JButton btnRun = new JButton("Run Network");						// run button
	
	// constructor method to initialize UI
	// and event handlers
	public Hopfield(){
		setTitle("Hopfield 4 Neurons Single Layer Neural Netowrk");
		
		Container content = getContentPane();
		GridBagLayout gridBag = new GridBagLayout();
		GridBagConstraints c = new GridBagConstraints();
		
		content.setLayout(gridBag);
		c.fill = GridBagConstraints.NONE;
		c.weightx = 1.0;
		
		// input pattern label
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.anchor = GridBagConstraints.NORTHWEST;
		content.add(new JLabel("Enter the Binary Input Pattern You Want the Network to Recognize:"), c);
		
		// input pattern
		String options[] = {"0", "1"};
		JPanel inputPanel = new JPanel();
		inputPanel.setLayout(new FlowLayout());
		
		for(int i = 0; i < NETWORK_SIZE; i++)
		{
			input[i] = new JComboBox(options);
			inputPanel.add(input[i]);
		}
		
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.anchor = GridBagConstraints.CENTER;
		content.add(inputPanel, c);
		
		// train button
		btnTrain.addActionListener(this);	// event handler for train button
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.anchor = GridBagConstraints.CENTER;
		content.add(btnTrain, c);
		
		// weight matrix label
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.anchor = GridBagConstraints.NORTHWEST;
		content.add(new JLabel("Weight Matrix for the Hopfield Neural Network:"), c);
		
		// weight matrix panel
		JPanel connections = new JPanel();
		connections.setLayout(new GridLayout(NETWORK_SIZE, NETWORK_SIZE));
		
		for(int row = 0; row < NETWORK_SIZE; row++)
			for(int column = 0; column < NETWORK_SIZE; column++)
			{
				matrix[row][column] = new JTextField(3);
				matrix[row][column].setText("0");
				matrix[row][column].setEnabled(false);
				connections.add(matrix[row][column]);
			}
		
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.anchor = GridBagConstraints.CENTER;
		content.add(connections, c);
		
		// run button
		btnRun.addActionListener(this);		// event handler for run button
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.anchor = GridBagConstraints.CENTER;
		content.add(btnRun, c);
		
		// output pattern label
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.anchor = GridBagConstraints.NORTHWEST;
		content.add(new JLabel("Binary Output Pattern:"), c);
		
		// output pattern
		JPanel outputPanel = new JPanel();
		outputPanel.setLayout(new FlowLayout());
		
		for(int i = 0; i < NETWORK_SIZE; i++)
		{
			output[i] = new JTextField(3);
			output[i].setEditable(false);
			outputPanel.add(output[i]);
		}
		
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.anchor = GridBagConstraints.CENTER;
		content.add(outputPanel, c);
		
		// clear button
		btnClear.addActionListener(this);	// event handler for clear button
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.anchor = GridBagConstraints.CENTER;
		content.add(btnClear, c);
		
		// show UI and adjust window location on screen
		pack();
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Dimension d = toolkit.getScreenSize();
		setLocation((int)(d.getWidth() - this.getWidth()) / 2, (int)(d.getHeight() - this.getHeight()) / 2);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setResizable(false);
	}
	
	// method to dispatch events from buttons
	// to their respective handler functions
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == btnTrain)
			train();
		else
			if(e.getSource() == btnRun)
				run();
			else
				if(e.getSource() == btnClear)
					clear();
	}
	
	// method to train the weight matrix based
	// on current input pattern
	public void train(){
		int biInput[] = new int[NETWORK_SIZE];								// bipolar input
		int contribution[][] = new int [NETWORK_SIZE][NETWORK_SIZE];		// contribution matrix
		
		// convert input to bipolar
		for(int i = 0; i < NETWORK_SIZE; i++)
			if(input[i].getSelectedIndex() == 0)
				biInput[i] = -1;
			else
				biInput[i] = 1;
		
		// multiply bipolar input by its transpose vector
		// and fill the contribution matrix with the result
		for(int row = 0; row < NETWORK_SIZE; row++)
			for(int column = 0; column < NETWORK_SIZE; column++)
				contribution[row][column] = biInput[row] * biInput[column];
		
		// set northwest diagonal to zeros
		// in the contribution matrix
		for(int i = 0; i < NETWORK_SIZE; i++)
			contribution[i][i] = 0;
		
		// fill the weight matrix
		for(int row = 0; row < NETWORK_SIZE; row++)
			for(int column = 0; column < NETWORK_SIZE; column++)
			{
				int oldWeight = 0;	// only used if you want to train the network for more than one pattern
				int newWeight = 0;
				
				oldWeight  = Integer.parseInt(matrix[row][column].getText());	
				newWeight = oldWeight + contribution[row][column];
				
				matrix[row][column].setText("" + newWeight);
			}			
	}
	
	// method to run the neural network and
	// produce the output pattern
	public void run(){
		Layer net;
		boolean pattern[] = new boolean[NETWORK_SIZE];
		int weight[][] = new int[NETWORK_SIZE][NETWORK_SIZE];
		
		// get weight matrix from text fields
		for(int row = 0; row < NETWORK_SIZE; row++)
			for(int column = 0; column < NETWORK_SIZE; column++)
				weight[row][column] = Integer.parseInt(matrix[row][column].getText());
		
		// get pattern from input combo box
		for(int i = 0; i < NETWORK_SIZE; i++)
			if(input[i].getSelectedIndex() == 0)
				pattern[i] = false;
			else
				pattern[i] = true;
		
		net = new Layer(weight);	// initialize new network with weights
		net.activation(pattern);	// run network with pattern
		
		for(int i = 0; i < NETWORK_SIZE; i++)
		{
			// fill output
			if(net.output[i])
				output[i].setText("1");
			else
				output[i].setText("0");
			
			// color matching input and output
			if(net.output[i] == pattern[i])
				output[i].setBackground(Color.GREEN);
			else
				output[i].setBackground(Color.RED);
		}
	}
	
	// method to clear input pattern,
	// weight matrix and output pattern
	public void clear(){
		for(int i = 0; i < NETWORK_SIZE; i++)
		{
			input[i].setSelectedIndex(0);
			output[i].setText("");
			output[i].setBackground(Color.WHITE);
			
			for(int j = 0; j < NETWORK_SIZE; j++)
				matrix[i][j].setText("0");
		}
	}
	
	// main program entry point
	@SuppressWarnings("deprecation")
	public static void main(String[] args) {
		JFrame f = new Hopfield();
		f.show();
	}
}
