package it.polito.tdp.artsmia.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.artsmia.model.Adiacenze;
import it.polito.tdp.artsmia.model.ArtObject;
import it.polito.tdp.artsmia.model.Autore;
import it.polito.tdp.artsmia.model.Exhibition;

public class ArtsmiaDAO {

	public List<ArtObject> listObjects() {
		
		String sql = "SELECT * from objects";
		List<ArtObject> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				ArtObject artObj = new ArtObject(res.getInt("object_id"), res.getString("classification"), res.getString("continent"), 
						res.getString("country"), res.getInt("curator_approved"), res.getString("dated"), res.getString("department"), 
						res.getString("medium"), res.getString("nationality"), res.getString("object_name"), res.getInt("restricted"), 
						res.getString("rights_type"), res.getString("role"), res.getString("room"), res.getString("style"), res.getString("title"));
				
				result.add(artObj);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Exhibition> listExhibitions() {
		
		String sql = "SELECT * from exhibitions";
		List<Exhibition> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Exhibition exObj = new Exhibition(res.getInt("exhibition_id"), res.getString("exhibition_department"), res.getString("exhibition_title"), 
						res.getInt("begin"), res.getInt("end"));
				
				result.add(exObj);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<String> elencoRuoli() {
		String sql = "SELECT distinct role " + 
				"from authorship "+
				"order by role asc";
		Connection conn = DBConnect.getConnection();
		List <String> result = new ArrayList <>();
		
		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				result.add(res.getString("role"));
			}
			conn.close();
			return result;
			
		} catch(SQLException e) {
			e.printStackTrace();
			return null;
		}
		
	}
	
	public void loadAutore (Map <Integer, Autore> idMap, String role) {
		/*String sql = "SELECT a.artist_id " + 
				"from authorship au, artists a " + 
				"where a.artist_id=au.artist_id and au.role =?";*/
		String sql = "SELECT artist_id " + 
				"from authorship " + 
				"where role =?";
		Connection conn = DBConnect.getConnection();
		
		
		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, role);
			ResultSet res = st.executeQuery();
			
			while (res.next()) {
					//Autore a = new Autore (res.getInt("a.artist_id"), role);
				Autore a = new Autore (res.getInt("artist_id"), role);
					idMap.put(a.getAutor_id(), a);
				
			}
			conn.close();
			
		} catch(SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	public List<Adiacenze> getAdiacenze (String role){
		String sql="Select ar1.artist_id, ar2.artist_id, " + 
				"COUNT(distinct e1.exhibition_id) as peso " + 
				"from artists ar1, artists ar2, authorship a1, authorship a2, exhibition_objects e1, exhibition_objects e2 " + 
				"where a1.role=? and a2.role=? " + 
				"and a1.artist_id=ar1.artist_id and a2.artist_id=ar2.artist_id " + 
				"and e1.object_id=a1.object_id and e2.object_id=a2.object_id " + 
				"and e1.exhibition_id=e2.exhibition_id " + 
				"and ar1.artist_id>ar2.artist_id " + 
				"group by  ar1.artist_id, ar2.artist_id";
		
		List <Adiacenze> adiacenze = new ArrayList<>();
		Connection conn = DBConnect.getConnection();
		
		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, role);
			st.setString(2, role);
			ResultSet res = st.executeQuery();
			
			while (res.next()) {
				Autore a1 = new Autore (res.getInt("ar1.artist_id"), role);
				Autore a2 = new Autore (res.getInt("ar2.artist_id"), role);
				Adiacenze a = new Adiacenze (a1, a2, res.getInt("peso"));
				adiacenze.add(a);
			}
			conn.close();
			return adiacenze;

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		
	}
	
}
