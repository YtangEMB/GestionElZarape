package org.utl.dsm.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import org.utl.dsm.db.ConexionBD;

public class ControllerAuth {

    public boolean validateToken(String usuario, String token) {
        String query = "SELECT COUNT(*) FROM usuario WHERE nombre = ? AND lastToken = ?";
        ConexionBD conexion = new ConexionBD();

        try (Connection conn = conexion.open();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, usuario);
            ps.setString(2, token);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next() && rs.getInt(1) > 0) {
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            conexion.close();
        }
        return false;
    }
}
