package it.polito.tdp.imdb.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.imdb.db.ImdbDAO;

public class Model {

	private ImdbDAO dao; 
	private Graph<Actor, DefaultWeightedEdge> grafo;
	private Map<Integer, Actor> idMap;
	
	
	public Model() {
		this.dao = new ImdbDAO();
	}
	
	public List<String> listAllGenres(){
		return this.dao.listAllGenres();
	}
	
	public void creaGrafo(String genere) {
		this.grafo=new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		this.idMap = new HashMap<>();
		
		for(Actor a : dao.getAllVertici(genere)) {
			idMap.put(a.getId(), a);
		}
		//aggiungo i vertici
		Graphs.addAllVertices(this.grafo, dao.getAllVertici(genere));
		
		//aggiungo gli archi
		for (Adiacenza a : dao.getAllAdiacenze(genere, idMap)) {
			Graphs.addEdgeWithVertices(this.grafo, a.getA1(), a.getA2(), a.getPeso());
		}
	}
	
	public List<Actor> doSimile(Actor a){
		List<Actor> attoriSimili = new ArrayList<>();
		
		for(Actor att : getComponenteConnessa(a)) {
			attoriSimili.add(att);
		}
		attoriSimili.remove(a);
		Collections.sort(attoriSimili);
		return attoriSimili;
	}
	
	private Set<Actor> getComponenteConnessa(Actor a){
		ConnectivityInspector<Actor, DefaultWeightedEdge> conn = new ConnectivityInspector<Actor, DefaultWeightedEdge>(grafo);
		Set<Actor> connessi = conn.connectedSetOf(a);
		
		return connessi;
	}

	
	public Integer nVertici() {
		return this.grafo.vertexSet().size();
	}
	
	public Integer nArchi() {
		return this.grafo.edgeSet().size();
	}
	public Set<Actor> getAllVertici(){
		return this.grafo.vertexSet();
	}


}
