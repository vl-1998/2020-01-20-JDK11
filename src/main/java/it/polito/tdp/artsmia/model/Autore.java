package it.polito.tdp.artsmia.model;

import java.util.ArrayList;
import java.util.List;

public class Autore {
	private int autor_id;
	private String role;
	
	/**
	 * @param autor_id
	 * @param role
	 */
	public Autore(int autor_id, String role) {
		this.autor_id = autor_id;
		this.role = role;
	}
	public Autore(int autor_id) {
		this.autor_id = autor_id;
	}
	
	public int getAutor_id() {
		return autor_id;
	}
	public void setAutor_id(int autor_id) {
		this.autor_id = autor_id;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + autor_id;
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Autore other = (Autore) obj;
		if (autor_id != other.autor_id)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "autor_id=" + autor_id + ", role=" + role;
	}
	
	
	
	
	
	
	
	
}
