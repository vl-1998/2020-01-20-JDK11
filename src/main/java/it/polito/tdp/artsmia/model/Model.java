package it.polito.tdp.artsmia.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.artsmia.db.ArtsmiaDAO;

public class Model {
	private SimpleWeightedGraph <Autore,DefaultWeightedEdge> grafo;
	private Map <Integer, Autore> idMap;
	private ArtsmiaDAO dao;
	private List<Adiacenze> adiacenze;
	private List<Autore> best;
	private List<Autore> rimanenti;
	int peso=0;
	
	public Model() {
		idMap = new HashMap<>();
		dao = new ArtsmiaDAO();
	}
	
	public void creaGrafo (String role) {
		this.grafo= new SimpleWeightedGraph <> (DefaultWeightedEdge.class);
		this.dao.loadAutore(idMap, role);
		//aggiungo i vertici
		Graphs.addAllVertices(this.grafo, idMap.values());
		//for (Autore a : idMap.values()) {
			//if (!this.grafo.containsVertex(a)) {
		//		this.grafo.addVertex(a);
			//}
		//}
		
		for (Adiacenze a: this.dao.getAdiacenze(role)) {
			if (this.grafo.getEdge(a.getA1(), a.getA2())==null) { // se l'arco non esiste già lo aggiungo
				Graphs.addEdgeWithVertices(this.grafo, a.getA1(), a.getA2(), a.getPeso());
			}
		}
	}
	public Set<Autore> getVertici(){
		Set <Autore> aa = this.grafo.vertexSet();
		return aa;
	}
	
	public int vertexNumber() {
		return this.grafo.vertexSet().size();
	}
	public int edgeNumber() {
		return this.grafo.edgeSet().size();
	}
	
	public List<Adiacenze> getAdiacenze(String role) {
		this.adiacenze= new ArrayList<>(this.dao.getAdiacenze(role));
		
		return adiacenze;
	}
	
	private List<Autore> dammiRima(List<Autore> parziale, int artist_id){
		Autore a = new Autore(artist_id);
		List<Autore> temp = new ArrayList<>();
		
		for (Autore c : Graphs.neighborListOf(this.grafo, a)) {
			if (this.grafo.getEdgeWeight(grafo.getEdge(a,c))==peso && !parziale.contains(c)) {
				temp.add(c);
			}
		}
		
		
		/*for (Autore c: temp) {
			if(this.grafo.getEdgeWeight(grafo.getEdge(a,c))!=peso) {
				temp.remove(c);
			}
			if (parziale.contains(c)==true) {
				temp.remove(c);
			}
		}*/
		return temp;
	}
	
	public List<Autore> trovaPercorso(int artist_id){
		List<Autore> parziale = new ArrayList<>();
		this.best= new ArrayList<>();
		Autore a = new Autore(artist_id);

		parziale.add(a);
		
		Set <Integer> pesi = new HashSet <>();
		for (Autore pp:Graphs.neighborListOf(this.grafo, a)) {
			pesi.add((int) this.grafo.getEdgeWeight(this.grafo.getEdge(a, pp)));
		}
		
		
		for (Integer peso: pesi) {
			this.peso=peso;
			trovaRicorsivo(parziale,artist_id, this.dammiRima(parziale, artist_id));
		}
		
		return this.best;
		
	}

	private void trovaRicorsivo(List<Autore> parziale, int artist_id, List<Autore> rimanenti) {
		//Autore a = new Autore(artist_id);

		if(rimanenti.isEmpty()) {
			if(parziale.size()>best.size()) {
				this.best= new ArrayList<>(parziale);

			}
		} else {
			//rimanenti = new ArrayList<>(dammiRima(parziale,a.getAutor_id() ));
			for (Autore aa: rimanenti) {
				parziale.add(aa);
				//List <Autore> disponibili = new ArrayList<>(dammiRima(parziale,aa.getAutor_id()));
				trovaRicorsivo(parziale, aa.getAutor_id(), this.dammiRima(parziale, aa.getAutor_id()));
				parziale.remove(aa);
			}
		}
		
	}

	
	
}
