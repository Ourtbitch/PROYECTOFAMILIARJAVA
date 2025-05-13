/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ahorrofamiliar.dao;

import com.ahorrofamiliar.utils.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JComboBox;

/**
 *
 * @author Alondra
 */
public class Utilitario {
    public void RellenarComboBox(String tabla, String valor, JComboBox combo){
        
        try{
            Connection cn = null;
            cn = DatabaseConnection.getConnection();
            String sql = "select "+valor+" from "+tabla;
            PreparedStatement st = cn.prepareStatement(sql);
            ResultSet rs = st.executeQuery();
            combo.addItem("--Elegir--");
            while (rs.next()){
                combo.addItem(rs.getString(1));
            }
        } catch( Exception ex){
            ex.printStackTrace();
        }
    }
}
