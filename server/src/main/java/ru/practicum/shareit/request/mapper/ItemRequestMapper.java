package ru.practicum.shareit.request.mapper;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;
import java.util.stream.Collectors;

public class ItemRequestMapper {
    public static ItemRequestDto toDto(ItemRequest itemRequest, List<ItemDto> items) {
        List<ItemRequestDto.ItemResponse> itemResponses = items.stream()
                .map(item -> new ItemRequestDto.ItemResponse(
                        item.getId(),
                        item.getName(),
                        item.getOwnerId()
                ))
                .collect(Collectors.toList());

        return new ItemRequestDto(
                itemRequest.getId(),
                itemRequest.getDescription(),
                itemRequest.getCreated(),
                itemResponses
        );
    }
}
