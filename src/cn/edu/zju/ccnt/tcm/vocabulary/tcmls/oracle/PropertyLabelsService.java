package cn.edu.zju.ccnt.tcm.vocabulary.tcmls.oracle;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

public class PropertyLabelsService {

	private static String sql = "select t.f_item_id, t.f_lang, t.f_code, t.f_description from mt.t_cl_25 t";

	private Map<String, String> map = new HashMap<String, String>();
	
	public PropertyLabelsService(Connection con){
		loadAndTransform(con);
	}
	
	public String getLabel(String id){
		return map.get(id);
	}
	
	private String language(int type) {
		switch (type) {
		case 12:
			return "zh";
		case 0:
			return "en";
		default:
			return null;
		}
	}
	
	private boolean isPrefLabel(int type) {
		switch (type) {
		case 12:
			return true;
		case 0:
			return false;
		default:
			return false;
		}
	}
	

	public void loadAndTransform(Connection con)  {

		try {
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(sql);

			while (rs.next()) {
				String property = rs.getString(1);
				int lan = rs.getInt(2);
				String label = rs.getString(3);
				
				if (this.isPrefLabel(lan)){
					System.out.println(property + label + language(lan));
					this.map.put(property, label);
				}
				

				

			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public static void main(String[] args) throws InstantiationException,
			IllegalAccessException, ClassNotFoundException, SQLException {

		Connection con = DBUtils.getDBConnection();
		PropertyLabelsService hierarchy = new PropertyLabelsService(con);

		

	}
}
