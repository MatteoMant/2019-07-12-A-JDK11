package it.polito.tdp.food.model;

public class Event implements Comparable<Event>{
	
	public enum EventType {
		INIZIO_PREPARAZIONE,
		FINE_PREPARAZIONE
	}
	
	private Double tempo;
	private EventType tipo;
	private Food cibo;
	private Stazione stazione;
	
	public Event(Double tempo, EventType tipo, Food cibo, Stazione stazione) {
		super();
		this.tempo = tempo;
		this.tipo = tipo;
		this.cibo = cibo;
		this.stazione = stazione;
	}

	public Double getTempo() {
		return tempo;
	}

	public EventType getTipo() {
		return tipo;
	}

	public Food getCibo() {
		return cibo;
	}

	public Stazione getStazione() {
		return stazione;
	}

	@Override
	public int compareTo(Event other) {
		return this.getTempo().compareTo(other.getTempo());
	}
	
}
