package ru.practicum.shareit.item;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {
    Item createItem(Item item, Long userId);

    Item updateItem(Long itemId, Item item, Long userId);

    Item getItemById(Long itemId);

    List<Item> getUserItems(Long userId);

    List<Item> searchItems(String text);
}