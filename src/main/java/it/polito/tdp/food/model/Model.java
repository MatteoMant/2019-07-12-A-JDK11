package it.polito.tdp.food.model;

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
	
	public List<Food> getAdiacentiCalorieMassime(Food f) {
		List<Food> result = new LinkedList<>();
		List<Food> adiacenti = Graphs.neighborListOf(this.grafo, f);
		
		for (int i = 0; i < 5; i++) {
			double pesoMax = 0.0;
			Food best = null;
			
			for (Food adiacente : adiacenti) {
				double peso = this.grafo.getEdgeWeight(this.grafo.getEdge(f, adiacente));
				if (peso > pesoMax) {
					pesoMax = peso;
					best = adiacente;
				}
			}
			result.add(best);
			adiacenti.remove(best);
		}
		return result;
	}
	
	public double getPesoArco(Food f1, Food f2) {
		
		if(this.grafo.getEdge(f1, f2) != null)
			return this.grafo.getEdgeWeight(this.grafo.getEdge(f1, f2));
		else 
			return -1;
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
