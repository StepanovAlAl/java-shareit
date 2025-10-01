package ru.practicum.shareit.item.mapper;

import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.User;

import java.util.List;

public class ItemMapper {

    public static Item toItem(ItemRequestDto itemRequestDto, User owner) {
        Item item = new Item();
        item.setName(itemRequestDto.getName());
        item.setDescription(itemRequestDto.getDescription());
        item.setAvailable(itemRequestDto.getAvailable());
        item.setOwner(owner);

        if (itemRequestDto.getRequestId() != null) {
            ItemRequest request = new ItemRequest();
            request.setId(itemRequestDto.getRequestId());
            item.setRequest(request);
        }

        return item;
    }

    public static ItemDto toDto(Item item) {
        Long requestId = item.getRequest() != null ? item.getRequest().getId() : null;
        return new ItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                item.getOwner().getId(),
                requestId
        );
    }

    public static ItemResponseDto toItemResponseDto(Item item,
                                                    ItemResponseDto.BookingDto lastBooking,
                                                    ItemResponseDto.BookingDto nextBooking,
                                                    List<CommentResponseDto> comments) {
        ItemResponseDto dto = new ItemResponseDto();
        dto.setId(item.getId());
        dto.setName(item.getName());
        dto.setDescription(item.getDescription());
        dto.setAvailable(item.getAvailable());
        dto.setLastBooking(lastBooking);
        dto.setNextBooking(nextBooking);
        dto.setComments(comments);
        return dto;
    }

    public static ItemWithBookingsDto toItemWithBookingsDto(Item item,
                                                            ItemWithBookingsDto.BookingDto lastBooking,
                                                            ItemWithBookingsDto.BookingDto nextBooking,
                                                            List<CommentResponseDto> comments) {
        ItemWithBookingsDto dto = new ItemWithBookingsDto();
        dto.setId(item.getId());
        dto.setName(item.getName());
        dto.setDescription(item.getDescription());
        dto.setAvailable(item.getAvailable());
        dto.setLastBooking(lastBooking);
        dto.setNextBooking(nextBooking);
        dto.setComments(comments);
        return dto;
    }

    public static void updateItemFromDto(ItemRequestDto itemRequestDto, Item item) {
        if (itemRequestDto.getName() != null) {
            item.setName(itemRequestDto.getName());
        }
        if (itemRequestDto.getDescription() != null) {
            item.setDescription(itemRequestDto.getDescription());
        }
        if (itemRequestDto.getAvailable() != null) {
            item.setAvailable(itemRequestDto.getAvailable());
        }

        if (itemRequestDto.getRequestId() != null) {
            ItemRequest request = new ItemRequest();
            request.setId(itemRequestDto.getRequestId());
            item.setRequest(request);
        }
    }
}
