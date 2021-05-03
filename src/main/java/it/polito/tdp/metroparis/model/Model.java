package it.polito.tdp.metroparis.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;
import org.jgrapht.traverse.BreadthFirstIterator;
import org.jgrapht.traverse.DepthFirstIterator;

import it.polito.tdp.metroparis.db.MetroDAO;

public class Model {
	
	Graph<Fermata, DefaultEdge> grafo ;

	public void creaGrafo() {
		this.grafo = new SimpleGraph<>(DefaultEdge.class) ;
		
		MetroDAO dao = new MetroDAO() ;
		List<Fermata> fermate = dao.getAllFermate() ;
		
//		for(Fermata f : fermate) {
//			this.grafo.addVertex(f) ;
//		}
		//altro modo per mettere i vertici
		Graphs.addAllVertices(this.grafo, fermate) ;
		
		// Aggiungiamo gli archi
//		for(Fermata f1: this.grafo.vertexSet()) {
//			for(Fermata f2: this.grafo.vertexSet()) {
//				if(!f1.equals(f2) && dao.fermateCollegate(f1, f2)) {
//					this.grafo.addEdge(f1, f2) ;
//				}
//			}
//		}
		//altro modo per mettere gli archi
		List<Connessione> connessioni = dao.getAllConnessioni(fermate) ;
		for(Connessione c: connessioni) {
			this.grafo.addEdge(c.getStazP(), c.getStazA()) ;
		}
		
		System.out.format("Grafo creato con %d vertici e %d archi\n",
				this.grafo.vertexSet().size(), this.grafo.edgeSet().size()) ;
		//System.out.println(this.grafo) ;
		
		/*Fermata f;
		Set<DefaultEdge> archi= this.grafo.edgesOf(f); //trovo tutti gli archi adiacenti ad un vertice
		for(DefaultEdge e : archi) {*/
			/*Fermata f1= this.grafo.getEdgeSource(e);
			//oppure:
			Fermata f2 = this.grafo.getEdgeTarget(e);
			if(f1.equals(f)) {
				//f2 e' quello che mi serve
			} else {
				//e' f1 quello che mi serve
			}*/
			
			//posso sostituire tutta la roba precedente di questo for con questo metodo statico di Grapghs:	
			//f1=Graphs.getOppositeVertex(grafo, e, f);
		//}
		
		/*con questo metodo posso sostituire tutto cio' che c'e' tra Fermata f e la fine del for
		List<Fermata> fermateAdiacenti=Graphs.successorListOf(grafo, f); //ottengo i successori
		Graphs.predecessorListOf(grafo, f); //ottengo i predecessori
		*/
		
	}
	
	public List<Fermata> fermateRaggiungibili( Fermata partenza){
		BreadthFirstIterator<Fermata, DefaultEdge> bfv= new BreadthFirstIterator<>(this.grafo,partenza);
		List<Fermata> result= new ArrayList<>();
		
		DepthFirstIterator<Fermata,DefaultEdge> dfv = new DepthFirstIterator<>(this.grafo,partenza);
		
		while(bfv.hasNext()) {
			Fermata f = bfv.next();
			result.add(f);
		}
		
		//per dfv il ciclo while e' identico. Cambia solo l'iteratore e quindi l'ordine dei vertici nella lista
		
		return result;
	}
	
	public Fermata trovaFermata(String nome) { //se lo uso troppe volte e rallenta il programma, creo mappa id per velocizzare
		for (Fermata f : grafo.vertexSet()) {
			if(f.getNome().equals(nome))
				return f;
		}
		return null;
	}
	
}
