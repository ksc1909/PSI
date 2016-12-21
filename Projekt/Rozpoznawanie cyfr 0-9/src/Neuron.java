import java.util.ArrayList;
import java.util.Random;

public class Neuron {

	public String nazwa = null;
	
	private ArrayList<Double> wagi;
	private int rozmiar;
	
	
	//Konstruktor z list¹ wag neuronu
	public Neuron(ArrayList<Double> w) {
		wagi = w;
	}
	
	//Konstruktor z rozmiarem Neuronu
	public Neuron(int n) {
		wagi = new ArrayList<Double>(n);
		rozmiar = n;
		ustawWagi();
	}
	
	//Funkcja zajmujaca sie wagami 
	private void ustawWagi()
	{
		czyscwagi();
		losujWagi();
	}

	//Ta funkcja czysci wszystkie wagi 
	public void czyscwagi() {
		wagi.clear();
		
		for (int i = 0; i < rozmiar; i++)
			wagi.add(0.0);
	}
	
	//Ta funkcja losuje poczatkowe Wagi
	public void losujWagi() {
		Random rand = new Random();
		
		for (int i = 0; i < wagi.size(); i++)
			wagi.set(i, rand.nextDouble());	
		ujednolicWagi();
	}
	
	//Ta funkcja ujednolica wagi
	public void ujednolicWagi() {
		double dlugosc = 0.0;	
		for (int i = 0; i < wagi.size(); i++)
		dlugosc += Math.pow(wagi.get(i), 2);	
		dlugosc = Math.sqrt(dlugosc);	
		for (int i = 0; i < wagi.size(); i++)
			wagi.set(i, wagi.get(i) / dlugosc);
	}
	
	//Ta funkcja liczy siec
	public double liczSiec(ArrayList<Integer> input) throws IllegalArgumentException {
		if (input.size() != wagi.size())
			throw new IllegalArgumentException("Zla liczba wejsc");	
		double wynik = 0.0;	
		for (int i = 0; i < input.size(); i++)
		wynik += input.get(i) * wagi.get(i);	
		return wynik;
	}
	

	//Ta funkcja uczy siec
	public void ucz(ArrayList<Integer> input) throws IllegalArgumentException {
		ucz(input, 0.5);
	}
	
	//Ta funkcja uczy siec raz z podanym learningRate
	public void ucz(ArrayList<Integer> input, double learningRate) throws IllegalArgumentException {
		if (input.size() != wagi.size())
			throw new IllegalArgumentException("Zla liczba wejsc");
		
		for (int i = 0; i < wagi.size(); i++) {
			double delta = learningRate * (input.get(i) - wagi.get(i));
			
			wagi.set(i, wagi.get(i) + delta);
		}
		
		ujednolicWagi();
	}
	
	public String getInfo() {
		String info = "";
		
		for (double waga : wagi)
			info += waga + "\n";
		
		info += nazwa + "\n";
		
		return info;
	}
	
}
