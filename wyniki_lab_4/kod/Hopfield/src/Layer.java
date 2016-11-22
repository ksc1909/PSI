// definition of Layer class type that
// holds all neurons in the neural network
public class Layer {
	public Neuron neuron[];						// array of neurons
	public boolean output[];					// final output of neurons
	public int size;							// number of neurons in layer
	public static final double lambda = 1.0;	// constant to multiply against threshold
	
	public Layer(int weights[][]){
		size = weights[0].length;
		neuron = new Neuron[size];
		output = new boolean[size];
		
		for(int i = 0; i < size; i++)
			neuron[i] = new Neuron(weights[i]);
	}
	
	// threshold method that is used to calculate the 
	// hyperbolic tangent of each neuron in the network
	// depending on its activation value
	public boolean threshold(int k){
		double kk = k * lambda;
		double a = Math.exp(kk);
		double b = Math.exp(-kk);
		double tanh = (a - b) / (a + b);
		return(tanh >= 0);
	}
	
	// activation method to run the neural network
	public void activation(boolean pattern[]){
		for(int i = 0; i < size; i++)
		{
			neuron[i].activation = neuron[i].act(pattern);
			output[i] = threshold(neuron[i].activation);
		}
	}
}
