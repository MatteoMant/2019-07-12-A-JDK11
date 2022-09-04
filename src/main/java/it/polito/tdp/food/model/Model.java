package it.polito.tdp.food.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.food.db.FoodDao;

public class Model {

	private FoodDao dao;
	private Graph<Food, DefaultWeightedEdge> grafo;
	private Map<Integer, Food> idMap;
	
	public Model() {
		dao = new FoodDao();
		idMap = new HashMap<>();
		dao.listAllFoods(idMap);
	}
	
	public void creaGrafo(int nPorzioni) {
		grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		
		// Aggiunta dei vertici
		Graphs.addAllVertices(this.grafo, this.dao.getAllVertici(nPorzioni));
		
		// Aggiunta degli archi
		for (Adiacenza a : dao.getAllAdiacenze(idMap)) {
			if (this.grafo.containsVertex(a.getF1()) && this.grafo.containsVertex(a.getF2())) {
				Graphs.addEdge(this.grafo, a.getF1(), a.getF2(), a.getPeso());
			}
		}
		
		
	}
	
	public List<FoodCalories> getAdiacentiCalorieMassime(Food f) {
		List<FoodCalories> result = new LinkedList<>();
		List<Food> adiacenti = Graphs.neighborListOf(this.grafo, f);
				
		for (Food adiacente : adiacenti) {
			double peso = this.grafo.getEdgeWeight(this.grafo.getEdge(f, adiacente));
			result.add(new FoodCalories(adiacente, peso));
		}
		
		Collections.sort(result);

		return result;
	}
	
	public String simula(Food cibo, int K) {
		Simulatore sim = new Simulatore(this.grafo, this) ;
		sim.setNumStazioni(K);
		sim.init(cibo);
		sim.run();
		String messaggio = String.format("Preparati %d cibi in %f minuti\n", 
				sim.getCibiPreparati(), sim.getTempoTotPreparazione());
		return messaggio ;
	}
	
	public Set<Food> getVertici(){
		return this.grafo.vertexSet();
	}
	
	public int getNumVertici() {
		return this.grafo.vertexSet().size();
	}
	
	public int getNumArchi() {
		return this.grafo.edgeSet().size();
	}
	
}
