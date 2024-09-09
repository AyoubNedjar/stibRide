package model.dao;

import javafx.util.Pair;
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

public class StopDao implements Dao<Pair<Integer, Integer>, StopDto>{

    private Connection conn;


    private StopDao() throws RepositoryException{
        conn = DBManager.getInstance().getConnection();
    }

    public static StopDao getInstance() throws RepositoryException {
        return StopDao.StopDaoHolder.getInstance();
    }
    @Override
    public Pair<Integer, Integer> insert(StopDto item) throws RepositoryException {
        return null;
    }

    @Override
    public void delete(Pair<Integer, Integer> key) throws RepositoryException {

    }

    @Override
    public void update(StopDto item) throws RepositoryException {

    }

    public List<StopDto> selectAll() throws RepositoryException {
        List<StopDto> stops = new ArrayList<>();
        String sql = "SELECT id_line, id_station, id_order FROM Stops order by id_line, id_order";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                stops.add(new StopDto(rs.getInt(1), rs.getInt(2), rs.getInt(3)));
            }
        } catch (SQLException e) {
            throw new RepositoryException("Retrieve all stops failed", e);
        }
        return stops;
    }


    public StopDto select(Pair<Integer, Integer> key) throws RepositoryException {
        String sql = "SELECT id_line, id_station, id_order FROM Stops WHERE id_line = ? AND id_station = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, key.getKey());
            pstmt.setInt(2, key.getValue());
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new StopDto(rs.getInt(1), rs.getInt(2), rs.getInt(3));
            } else {
                return null;  // No stop found with the provided key
            }
        } catch (SQLException e) {
            throw new RepositoryException("Retrieve stop failed", e);
        }
    }


    public List<StopDto> selectLine(Integer line) throws RepositoryException {
        List<StopDto> stops = new ArrayList<>();
        String sql = "SELECT id_line, id_station, id_order FROM Stops WHERE id_line = ? ORDER BY id_order";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, line);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                stops.add(new StopDto(rs.getInt(1), rs.getInt(2), rs.getInt(3)));
            }
        } catch (SQLException e) {
            throw new RepositoryException("Retrieve stop failed", e);
        }
        return stops;
    }

    public List<StopDto> getAdjNextStop(Pair<Integer, Integer> key) throws RepositoryException{

        List<StopDto> stops = new ArrayList<>();
        StopDto stop = select(key);
        /*
            Select * From Stops where id_order = ?
         */
        String sql = " Select id_line, id_station, id_order From Stops st Join Stations s On st.id_station = s.id" +
                                        " where id_line = ? AND id_order = ? ";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, stop.getIdLine());
            pstmt.setInt(2, stop.getOrder()+1);

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                stops.add(new StopDto(rs.getInt(1), rs.getInt(2), rs.getInt(3)));
            }
        } catch (SQLException e) {
            throw new RepositoryException("Retrieve stop failed", e);
        }
        return stops;
    }

    public List<StopDto> getAdjPreviousStop(Pair<Integer, Integer> key) throws RepositoryException{

        List<StopDto> stops = new ArrayList<>();
        StopDto stop = select(key);
        /*
            Select * From Stops where id_order = ?
         */
        String sql = " Select id_line, id_station, id_order From Stops st Join Stations s On st.id_station = s.id" +
                " where id_line = ? AND id_order = ? ";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, stop.getIdLine());
            pstmt.setInt(2, stop.getOrder()-1);

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                stops.add(new StopDto(rs.getInt(1), rs.getInt(2), rs.getInt(3)));
            }
        } catch (SQLException e) {
            throw new RepositoryException("Retrieve stop failed", e);
        }
        return stops;
    }





    private static class StopDaoHolder {

        private static StopDao getInstance() throws RepositoryException {
            return new StopDao();
        }
    }
}
