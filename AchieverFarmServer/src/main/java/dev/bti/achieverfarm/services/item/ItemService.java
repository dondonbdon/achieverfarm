package dev.bti.achieverfarm.services.item;

import dev.bti.achieverfarm.common.Constants;
import dev.bti.achieverfarm.enums.InventoryUpdateType;
import dev.bti.achieverfarm.exceptions.item.ItemException;
import dev.bti.achieverfarm.models.*;
import dev.bti.achieverfarm.models.req.ItemReq;
import dev.bti.achieverfarm.services.inventory.InventoryRepository;
import dev.bti.achieverfarm.services.stock.StockRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ItemService {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private InventoryRepository inventoryRepository;

    public Item getItem(String itemId) throws ItemException {
        return itemRepository.findById(itemId).orElseThrow(() -> Constants.Throws.ItemNotFound);
    }

    public List<Item> getItemsWithOptions(ItemOptions options) {
        Query query = new Query();

        if (options.getType() != null) {
            query.addCriteria(Criteria.where("type").is(options.getType()));
        }

        if (options.getLastId() != null) {
            ObjectId lastObjectId = new ObjectId(options.getLastId());
            query.addCriteria(Criteria.where("_id").gt(lastObjectId));
        }

        if (options.getCount() != null) {
            query.limit(options.getCount());
        }

        if (options.getSearchTerm() != null && !options.getSearchTerm().isEmpty()) {
            String searchTerm = options.getSearchTerm();
            query.addCriteria(Criteria.where("itemInformation.name").regex(".*" + searchTerm + ".*", "i"));
        }

        return mongoTemplate.find(query, Item.class);
    }

    public List<Item> getItemsOrderedBefore() {
        return getItemsWithOptions(new ItemOptions());
    }

    public List<Item> getItemsRecommendations() {
        return getItemsWithOptions(new ItemOptions());
    }

    public void addItem(ItemReq itemReq) {
        Item item = new Item(itemReq);
        Stock stock = new Stock(itemReq.getStockReq(), item.getId());
        item.setStockId(stock.getId());

        if (stock.getUpdateType() == InventoryUpdateType.AUTO) {
            Inventory inventory = new Inventory();

            Availability availability = new Availability();
            availability.setTotal(stock.getQuantityAvailable());
            availability.setSold(0D);
            availability.setAvailable(stock.getQuantityAvailable());

            inventory.setId(item.getId());
            inventory.setToday(availability);
            inventory.setLastUpdated(System.currentTimeMillis());

            inventoryRepository.save(inventory);
        }

        stockRepository.save(stock);
        itemRepository.save(item);
    }

    public void addItems(List<ItemReq> items) {
        for (ItemReq itemReq : items) {
            addItem(itemReq);
        }
    }

    public void deleteItem(String itemId) throws ItemException {

    }

    public void deleteItems(List<String> itemIds) throws ItemException {

    }

    public void updateItem(String itemId, ItemReq updatedItem) throws ItemException {

    }

    public void updateItems(Map<String, ItemReq> itemsToUpdate) throws ItemException {

    }

    public void updatePricingInfo(String itemId, PricingInfo pricingInfo) throws ItemException {
        Item item = getItem(itemId);
        item.setPricingInfo(pricingInfo);
        itemRepository.save(item);
    }
}
