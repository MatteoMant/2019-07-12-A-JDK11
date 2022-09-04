package it.polito.tdp.food.model;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;

import it.polito.tdp.food.model.Event.EventType;
import it.polito.tdp.food.model.Food.StatoPreparazione;

public class Simulatore {
	
	// Parametri della simulazione 
	private int numStazioni;
	
	// Valori in uscita (output della simulazione)
	private int cibiPreparati;
	private double tempoTotPreparazione;
	
	// Modello del mondo
	private List<Stazione> stazioni;
	private List<Food> cibi;
	private Graph<Food, DefaultWeightedEdge> grafo;
	
	// Coda degli eventi
	private PriorityQueue<Event> queue;
	
	private Model model;
	
	public Simulatore(Graph<Food, DefaultWeightedEdge> grafo, Model model) {
		this.grafo = grafo;
		this.model = model;
	}
	
	public void init(Food partenza) {
		this.cibi = new ArrayList<>(this.grafo.vertexSet());

		for (Food cibo : this.cibi) {
			cibo.setPreparazione(StatoPreparazione.DA_PREPARARE);
		}
		
		this.stazioni = new ArrayList<>() ;
		for (int i=0; i < this.numStazioni; i++) {
			this.stazioni.add(new Stazione(true, null)) ;
		}
		
		this.tempoTotPreparazione = 0.0 ;
		this.cibiPreparati = 0;
		
		this.queue = new PriorityQueue<>();
	
		// Dopo aver creato la coda degli eventi dobbiamo pre-popolarla per poter avviare la simulazione
		
		List<FoodCalories> vicini = model.getAdiacentiCalorieMassime(partenza);
		
		for(int i=0; i<this.numStazioni && i<vicini.size(); i++) {  // se il numero di cibi adiacenti al cibo di partenza
			this.stazioni.get(i).setLibera(false);					// è inferiore al numero di stazioni mi fermo e ci saranno
			this.stazioni.get(i).setFood(vicini.get(i).getFood());  // quindi delle stazioni libere in cui non viene preparato nulla
			vicini.get(i).getFood().setPreparazione(StatoPreparazione.IN_CORSO);
			
			// la preparazione del cibo in questione verrà prima o poi terminata 
			Event e = new Event(vicini.get(i).getCalories(),
					EventType.FINE_PREPARAZIONE,
					vicini.get(i).getFood() ,
					this.stazioni.get(i)) ;
					
			queue.add(e) ;
		}
		
	}
	
	public void run() {
		while(!queue.isEmpty()) {
			Event e = queue.poll() ;
			processEvent(e) ;
		}
	}
	
	private void processEvent(Event e) {
		switch(e.getTipo()) {
		
		case INIZIO_PREPARAZIONE:
			List<FoodCalories> vicini = model.getAdiacentiCalorieMassime(e.getCibo());
			FoodCalories prossimo = null ; // devo scegliere il cibo adiacente a quello appena cucinato con il numero di calorie massimo
			for(FoodCalories vicino: vicini) { // la lista è già ordinata in ordine decrescente di calorie
				if(vicino.getFood().getPreparazione()==StatoPreparazione.DA_PREPARARE) { // controllo solo che il cibo sia da preparare 
					prossimo = vicino ; // ho trovato il nuovo cibo da preparare
					break ; // non proseguire nel ciclo
				}
			}
			
			if(prossimo != null) {
				prossimo.getFood().setPreparazione(StatoPreparazione.IN_CORSO);
				e.getStazione().setLibera(false);
				e.getStazione().setFood(prossimo.getFood());

				// la preparazione del cibo verrà prima o poi terminata
				Event e2 = new Event(e.getTempo()+prossimo.getCalories(),
						EventType.FINE_PREPARAZIONE,
						prossimo.getFood(),
						e.getStazione()) ;
						
				this.queue.add(e2) ;
			}
			
			break;
			
			
		case FINE_PREPARAZIONE:
			this.cibiPreparati++ ;
			this.tempoTotPreparazione = e.getTempo() ;
			
			e.getStazione().setLibera(true);
			e.getCibo().setPreparazione(StatoPreparazione.PREPARATO);
			
			Event e2 = new Event(e.getTempo(),
					EventType.INIZIO_PREPARAZIONE, 
					e.getCibo(),
					e.getStazione()) ;
			
			this.queue.add(e2) ;

			break;
		}
	}

	public int getCibiPreparati() {
		return cibiPreparati;
	}

	public double getTempoTotPreparazione() {
		return tempoTotPreparazione;
	}

	public void setNumStazioni(int numStazioni) {
		this.numStazioni = numStazioni;
	}
	
}
