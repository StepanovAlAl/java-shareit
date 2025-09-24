package ru.practicum.shareit.item.mapper;

import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.util.List;

public class ItemMapper {
    public static ItemDto toDto(Item item) {
        return new ItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                item.getOwner() != null ? item.getOwner().getId() : null,
                item.getRequestId()
        );
    }

    public static Item toItem(ItemRequestDto itemRequestDto, User owner) {
        Item item = new Item();
        item.setName(itemRequestDto.getName());
        item.setDescription(itemRequestDto.getDescription());
        item.setAvailable(itemRequestDto.getAvailable());
        item.setOwner(owner);
        item.setRequestId(itemRequestDto.getRequestId());
        return item;
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
            item.setRequestId(itemRequestDto.getRequestId());
        }
    }

    public static ItemResponseDto toItemResponseDto(Item item,
                                                    ItemResponseDto.BookingDto lastBooking,
                                                    ItemResponseDto.BookingDto nextBooking,
                                                    List<CommentResponseDto> comments) {
        return new ItemResponseDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                lastBooking,
                nextBooking,
                comments
        );
    }

    public static ItemWithBookingsDto toItemWithBookingsDto(Item item,
                                                            ItemWithBookingsDto.BookingDto lastBooking,
                                                            ItemWithBookingsDto.BookingDto nextBooking,
                                                            List<CommentResponseDto> comments) {
        return new ItemWithBookingsDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                item.getOwner().getId(),
                item.getRequestId(),
                lastBooking,
                nextBooking,
                comments
        );
    }
}