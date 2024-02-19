package ru.practicum.shareit.item.storage;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class ItemStorageImpl implements ItemStorage {
    private final Map<Long, Item> items = new HashMap<>();
    Long id = 0L;


    @Override
    public Item createItem(Item item) {
        id += 1;
        item.setId(id);
        items.put(id, item);
        return item;
    }

    @Override
    public Item updateItem(Long itemId, String itemName, String itemDescription, Boolean available) {
        Item itemCurrent = items.get(itemId);
        if (itemName != null) {
            itemCurrent.setName(itemName);
        }
        if (itemDescription != null) {
            itemCurrent.setDescription(itemDescription);
        }
        if (available != null) {
            itemCurrent.setAvailable(available);
        }
        items.remove(itemId);
        items.put(itemId, itemCurrent);
        return itemCurrent;
    }

    @Override
    public Collection<Item> getItems() {
        return items.values();
    }

    @Override
    public Collection<Item> getItemsByUser(Long userId) {
        return getItems().stream()
                .filter(x -> Objects.equals(x.getOwnerId(), userId))
                .collect(Collectors.toList());
    }

    @Override
    public Collection<Item> searchItems(String text) {
        return getItems().stream()
                .filter(x -> x.getName().toLowerCase().contains(text.toLowerCase())
                        || x.getDescription().toLowerCase().contains(text.toLowerCase()))
                .filter(Item::getAvailable)
                .collect(Collectors.toList());
    }

    @Override
    public Item getItemById(Long itemId) {
        return items.get(itemId);
    }
}
