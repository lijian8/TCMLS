package com.cintcm.tcmct;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.cintcm.tcmls.Constants;

public class loadTypes {

	private static String sql = "select distinct concept_id, hierarchy from t_root";
	private static String toSql = "insert into graph (subject,property,value,user_id) values (?,?,?,?)";

	
	public void loadAndTransform(Connection con, Connection toCon) throws SQLException {

		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery(sql);
		
		ClassLabelsService cLabels = new ClassLabelsService(con);
		PreparedStatement pStmt = toCon.prepareStatement(toSql);
		
		
		while (rs.next()) {
			String instance_id = rs.getString(1);
			String class_id = rs.getString(2);	
			String class_label = cLabels.getLabel(class_id);
			
			if ((class_label != null) && (class_label != "")){
				System.out.println(instance_id + " 类型 " + class_label);	
				
				try {
					pStmt.setString(1, Constants.TCMCT + instance_id);
					pStmt.setString(2, "类型");
					pStmt.setString(3, class_label);
					pStmt.setString(4, "0");
					pStmt.executeUpdate();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			
		}

		

	}

	public static void main(String[] args) throws InstantiationException,
			IllegalAccessException, ClassNotFoundException, SQLException {

		
		Connection toCon = MySQLUtils.getConnection();

		loadTypes hierarchy = new loadTypes();

		hierarchy.loadAndTransform(toCon, toCon);

	}
}
