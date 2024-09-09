package model.dao;

import model.dto.FavoriteDto;
import model.dto.StationDto;
import model.exception.RepositoryException;
import model.jdbc.DBManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FavoriteDao implements Dao<String, FavoriteDto>{

    private Connection conn;


    private FavoriteDao() throws RepositoryException{
        conn = DBManager.getInstance().getConnection();
    }

    public static FavoriteDao getInstance() throws RepositoryException {
        return FavoriteDao.FavoriteDaoHolder.getInstance();
    }
    @Override
    public String insert(FavoriteDto item) throws RepositoryException {
        String sql = "INSERT INTO FAVORITES (name_favorite, id_station_source, id_station_destination) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, item.getKey());
            pstmt.setInt(2, item.getSource().getKey());
            pstmt.setInt(3, item.getDestination().getKey());
            pstmt.executeUpdate();
            return item.getKey();
        } catch (SQLException e) {
            throw new RepositoryException("Failed to insert favorite", e);
        }
    }

    @Override
    public void delete(String key) throws RepositoryException {
        // SQL query to delete a specific favorite by its name
        String sql = "DELETE FROM FAVORITES WHERE name_favorite = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            // Set the favorite name parameter in the prepared statement
            pstmt.setString(1, key);

            // Execute the delete operation
            int affectedRows = pstmt.executeUpdate();

            // Check if a row was actually deleted
            if (affectedRows == 0) {
                throw new RepositoryException("No favorite found with the name: " + key);
            }
        } catch (SQLException e) {
            throw new RepositoryException("Failed to delete favorite", e);
        }
    }


    public void deleteAll() throws RepositoryException {
        String sql = "DELETE FROM FAVORITES";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.executeUpdate();  // Exécute la commande DELETE, qui supprime tous les enregistrements
        } catch (SQLException e) {
            throw new RepositoryException("Failed to delete all favorites", e);
        }
    }


    @Override
    public void update(FavoriteDto item) throws RepositoryException {

    }


    @Override
    public List<FavoriteDto> selectAll() throws RepositoryException {
        List<FavoriteDto> favorites = new ArrayList<>();
        String sql = "SELECT name_favorite, id_station_source, id_station_destination FROM FAVORITES";
        try (PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                String key = rs.getString("name_favorite");
                StationDto source = new StationDto(rs.getInt("id_station_source"), "source"); // Assuming you have a way to get the full StationDto
                StationDto destination = new StationDto(rs.getInt("id_station_destination"), "destination"); // Same assumption
                favorites.add(new FavoriteDto(key, source, destination));
            }
            return favorites;
        } catch (SQLException e) {
            throw new RepositoryException("Failed to select all favorites", e);
        }
    }


    @Override
    public FavoriteDto select(String key) throws RepositoryException {
        String sql = "SELECT name_favorite, id_station_source, id_station_destination FROM FAVORITES WHERE name_favorite = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, key);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    StationDto source = new StationDto(rs.getInt("id_station_source"), "source"); // Needs actual source name
                    StationDto destination = new StationDto(rs.getInt("id_station_destination"), "destination"); // Needs actual destination name
                    return new FavoriteDto(key, source, destination);
                }
                return null;
            }
        } catch (SQLException e) {
            throw new RepositoryException("Failed to select favorite", e);
        }
    }

    private static class FavoriteDaoHolder {

        private static FavoriteDao getInstance() throws RepositoryException {
            return new FavoriteDao();
        }
    }
}
