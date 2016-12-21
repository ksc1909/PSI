import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Siec {

	private ArrayList<Neuron> neurony;
	private HashMap<String, ArrayList<ArrayList<Integer>>> wagiWejsciowe;
	
	//Konstruktor z wagami wejsciowymi 
	public Siec(HashMap<String, ArrayList<ArrayList<Integer>>> wagWe) {
		 wagiWejsciowe = wagWe;
		stworzSiec(wagiWejsciowe.size());
	}
	
	//Konstruktor z informacja o Neuronie
	public Siec(ArrayList<Pair<String, ArrayList<Double>>> info) {
		neurony = new ArrayList<Neuron>();	
		for (Pair<String, ArrayList<Double>> znak : info) {
			Neuron neuron = new Neuron(znak.getRight());
			neuron.nazwa = znak.getLeft();		
			neurony.add(neuron);
		}
	}
	
	//Ta funkcja Tworzy siec
	public void stworzSiec(int n) {
		neurony = new ArrayList<Neuron>();
		for (int i = 0; i < n; i++)
			neurony.add(new Neuron(Ustawienia.TOTAL));
	}
	
	//Ta funkcja znajduje zwyciezce w WTA
	public Neuron znajdzZwyciezce(ArrayList<Integer> wagWej) {
		Neuron zwyciezca = null;
		
		for (Neuron neuron : neurony)
			if (neuron.nazwa == null && (zwyciezca == null || neuron.liczSiec(wagWej) > zwyciezca.liczSiec(wagWej)))
				zwyciezca = neuron;
		
		return zwyciezca;
	}
	
	//Ta funkcja uczy sieæ
	public void ucz() {
		for (Map.Entry<String, ArrayList<ArrayList<Integer>>> znak : wagiWejsciowe.entrySet()) {
			String nazwa = znak.getKey();
			ArrayList<ArrayList<Integer>> znakiWe = znak.getValue();	
			ArrayList<Integer> pierwszy = znakiWe.get(0);
			znakiWe.remove(0);	
			Neuron zwyciezca = znajdzZwyciezce(pierwszy);
			zwyciezca.nazwa = nazwa;			
			for (ArrayList<Integer> in : znakiWe)
				zwyciezca.ucz(in);
		}
	}
	
	public String rozpoznaj(ArrayList<Integer> in) {
		Neuron zwyciezca = null;
		
		for (Neuron neuron : neurony)
			if (zwyciezca == null || neuron.liczSiec(in) > zwyciezca.liczSiec(in))
				zwyciezca = neuron;
		
		return zwyciezca.nazwa;
	}
	
	public void zapisz(BufferedWriter out) throws IOException {
		out.write(Ustawienia.KOLUMNA + "\n" + Ustawienia.WIERSZ + "\n" + neurony.size() + "\n");
		
		for (Neuron neuron : neurony)
			out.write(neuron.getInfo());
	}
	
}
