package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {
    ItemDto createItem(ItemRequestDto itemRequestDto, Long userId);

    ItemDto updateItem(Long itemId, ItemRequestDto itemRequestDto, Long userId);

    Item getItemById(Long itemId);

    ItemResponseDto getItemDtoById(Long itemId, Long userId);

    List<ItemWithBookingsDto> getUserItems(Long userId);

    List<ItemDto> searchItems(String text);

    CommentResponseDto addComment(Long itemId, CommentRequestDto commentRequestDto, Long userId);

    List<ItemDto> getItemsByRequestId(Long requestId);
}