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
	private List<Autore> best; //struttura dati che contiene il percorso migliore
	private List<Autore> rimanenti;
	int peso=0;
	
	public Model() {
		dao = new ArtsmiaDAO();
	}
	
	public void creaGrafo (String role) {
		this.idMap = new HashMap<>();
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
	
	
	////////////////////////////////////RICORSIONE FATTA DA ME
	/*private List<Autore> dammiRima(List<Autore> parziale, int artist_id){
		Autore a = new Autore(artist_id);
		List<Autore> temp = new ArrayList<>();
		
		for (Autore c : Graphs.neighborListOf(this.grafo, a)) {
			if (this.grafo.getEdgeWeight(grafo.getEdge(a,c))==peso && !parziale.contains(c)) {
				temp.add(c);
			}
		}
		
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
		
	}*/
	
	//////////////////////////////////////////RICORSIONE DA ALBERTO
	//metodo che ci ritornerà un percorso, lista di vertici
	public List<Autore> trovaPercorso(int artist_id){
		this.best= new ArrayList<Autore>();
		List <Autore> parziale = new ArrayList <>();
		//posso già aggiungere a parziale il nodo sorgente
		Autore a = new Autore(artist_id);
		parziale.add(a);
		
		//lancio la ricorsione
		ricorsione(parziale, -1); //sappiamo che questo peso nel grafo non c'è
		
		return best;	
	}
	
	//concetto di trovare un percorso, esplorare le varie direzioni a partire da un nodo sorgente
	//abbiamo il vincolo di cercare percorsi in cui tutti gli archi abbiano lo stesso peso
	//ho il nodo sorgente che potrà avere più vicini, quando esploro il primo devo guardare prima il peso
	//dell'arco che contiene i due vertici e continuare poi il percorso solo con archi con quel peso
	//quando torno indietro al livello iniziale, il peso potrebbe cambiare e io continuo solo con archi di quel peso
	
	//il peso è quello con cui dobbiamo far continuare la ricorsione e che impostiamo con un numero che sappiamo
	//a priori non esserci nel grafo per discriminare il fatto che siamo nel primo livello della ricorsione
	private void ricorsione(List<Autore> parziale, int peso) {
		//se il peso è -1 so che sono al primo livello della ricorsione e so che devo salvarmi il peso tra il nodo
		//e i suoi vicini e continuare la ricorsione solo con quel peso 
		
		//prendo l'ultimo nodo inserito in parziale
		Autore ultimo = parziale.get(parziale.size()-1);
		//prendo tutti i vicini del nodo per esplorare i percorsi
		List <Autore> vicini = Graphs.neighborListOf(this.grafo, ultimo);
		for (Autore a: vicini) {
			//non vogliamo cicli, quindi controlliamo che parziale non contenga già il vertice che stiamo considerando
			if(peso==-1 && !parziale.contains(a)) {
				parziale.add(a);
				//esplora solo gli archi che avranno questo peso qui
				ricorsione(parziale, (int) this.grafo.getEdgeWeight(this.grafo.getEdge(ultimo, a)));
				parziale.remove(a);
			} else { //step successivo della ricorsione, non aggiungiamo un qualunque vicino, ma solo quelli collegati
					 //con archi di questo peso
				if(this.grafo.getEdgeWeight(this.grafo.getEdge(ultimo, a))==peso && !parziale.contains(a)) {
					//possiamo continuare il percorso
					parziale.add(a);
					ricorsione(parziale, peso);
					parziale.remove(a);
				}
				
			}	
		}
		
		//ricerca del percorso più lungo
		if (parziale.size()>best.size()) {
			this.best=new ArrayList <>(parziale);
		}
		
		//la condizione di terminazione è implicita
	}
	
	
}
