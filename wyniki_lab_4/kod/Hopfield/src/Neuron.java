// definition of Neuron class type
public class Neuron {
	public int weightv[];		// weight vector
	public int activation;		// activation result
	
	public Neuron(int in[]){
		weightv = in;
	}
	
	// method to determine if a neuron
	// would fire or not
	public int act(boolean x[]){
		int a = 0;
		
		for(int i = 0; i < x.length; i++)
			if(x[i])
				a += weightv[i];
		
		return a;
	}
}
