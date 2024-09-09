package model.dao;

import model.dto.StationDto;
import model.dto.StopDto;
import model.exception.RepositoryException;
import model.jdbc.DBManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StationNlDao implements Dao<Integer, StationDto> {

    private Connection conn;


    private StationNlDao() throws RepositoryException{
        conn = DBManager.getInstance().getConnection();
    }

    public static StationNlDao getInstance() throws RepositoryException {
        return StationNlDaoHolder.getInstance();
    }

    @Override
    public Integer insert(StationDto item) throws RepositoryException {
        return null;
    }

    @Override
    public void delete(Integer key) throws RepositoryException {

    }

    @Override
    public void update(StationDto item) throws RepositoryException {

    }

    @Override
    public List<StationDto> selectAll() throws RepositoryException {
        List<StationDto> stations = new ArrayList<>();
        String sql = "SELECT id, name FROM STATIONS_NL";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                stations.add(new StationDto(rs.getInt(1), rs.getString(2)));
            }
        } catch (SQLException e) {
            throw new RepositoryException("Retrieve all failed", e);
        }
        return stations;
    }

    public StationDto completeStation(Integer key) throws RepositoryException {
        StationDto station = select(key);  // Utilisez la méthode get existante pour obtenir les informations de base de la station.
        if (station != null) {
            String sql = "SELECT id_line, id_station, id_order FROM Stops s WHERE id_station = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, key);
                ResultSet rs = pstmt.executeQuery();
                while (rs.next()) {
                    StopDto stop = new StopDto(rs.getInt(1), rs.getInt(2), rs.getInt(3));
                    station.addStop(stop);  // Ajoute chaque arrêt à la station.
                }
            } catch (SQLException e) {
                throw new RepositoryException("Retrieve stops failed", e);
            }
        }
        return station;
    }

    @Override
    public StationDto select(Integer key) throws RepositoryException {
        String sql = "SELECT id, name FROM STATIONS_NL WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, key);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new StationDto(rs.getInt(1), rs.getString(2));
            } else {
                return null;
            }
        } catch (SQLException e) {
            throw new RepositoryException("Retrieve failed", e);
        }    }

    private static class StationNlDaoHolder {

        private static StationNlDao getInstance() throws RepositoryException {
            return new StationNlDao();
        }
    }
}
