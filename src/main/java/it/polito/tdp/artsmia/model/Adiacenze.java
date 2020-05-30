package it.polito.tdp.artsmia.model;

public class Adiacenze implements Comparable<Adiacenze> {
	private Autore a1;
	private Autore a2;
	private Integer peso;
	/**
	 * @param a1
	 * @param a2
	 * @param peso
	 */
	public Adiacenze(Autore a1, Autore a2, int peso) {
		super();
		this.a1 = a1;
		this.a2 = a2;
		this.peso = peso;
	}
	public Autore getA1() {
		return a1;
	}
	public void setA1(Autore a1) {
		this.a1 = a1;
	}
	public Autore getA2() {
		return a2;
	}
	public void setA2(Autore a2) {
		this.a2 = a2;
	}
	public int getPeso() {
		return peso;
	}
	public void setPeso(int peso) {
		this.peso = peso;
	}
	@Override
	public String toString() {
		return "Autore 1=" + a1 + ", Autore 2=" + a2 + ", # mostre in comune" + peso;
	}
	@Override
	public int compareTo(Adiacenze o) {
		//ordinati in ordine decrescente di peso
		return -this.peso.compareTo(o.getPeso());
	}
	
	
	

}
