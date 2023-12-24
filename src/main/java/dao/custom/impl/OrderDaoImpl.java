package dao.custom.impl;

import dto.OrderDto;
import dao.custom.OrderDao;
import db.DBConnection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import dao.custom.OrderDetailsDao;
import java.sql.Connection;

public class OrderDaoImpl implements OrderDao {
    OrderDetailsDao orderDetailsDao = new OrderDetailsDaoImpl();

    public boolean saveOrder(OrderDto dto) throws ClassNotFoundException {
        Connection connection = null;
        try {
            connection = DBConnection.getInstance().getConnection();
            connection.setAutoCommit(false);

            String sql = "INSERT INTO orders VALUES(?,?,?)";
            try (PreparedStatement pstm = connection.prepareStatement(sql)) {
                pstm.setString(1, dto.getOrderId());
                pstm.setString(2, dto.getDate());
                pstm.setString(3, dto.getCustId());

                if (pstm.executeUpdate() > 0) {
                    boolean isDetailSaved = orderDetailsDao.saveOrderDetails(dto.getList());
                    if (isDetailSaved) {
                        connection.commit();
                        return true;
                    }
                }
            }
        } catch (SQLException ex) {
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException rollbackException) {
                    rollbackException.printStackTrace();
                }
            }
            ex.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.setAutoCommit(true);
                    connection.close();
                } catch (SQLException closeException) {
                    closeException.printStackTrace();
                }
            }
        }
        return false;
    }

    public OrderDto lastOrder() throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM orders ORDER BY id DESC LIMIT 1";
        try (PreparedStatement pstm = DBConnection.getInstance().getConnection().prepareStatement(sql);
             ResultSet resultSet = pstm.executeQuery()) {

            if (resultSet.next()) {
                return new OrderDto(
                        resultSet.getString(1),
                        resultSet.getString(2),
                        resultSet.getString(3),
                        null
                );
            }
        }

        return null;
    }
}
