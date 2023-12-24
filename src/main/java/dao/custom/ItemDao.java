package dao.custom;

import dao.CrudDao;
import dto.ItemDto;
import entity.Customer;
import entity.Item;

import java.sql.SQLException;
import java.util.List;

public interface ItemDao extends CrudDao<Item> {
//    boolean saveItem(ItemDto dto);
//    boolean updateItem(ItemDto dto);
//    boolean deleteItem(String code);
    ItemDto getItem(String code) throws SQLException, ClassNotFoundException;
//    List<ItemDto> allItems() throws SQLException, ClassNotFoundException;

}
