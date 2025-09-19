package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemRequestDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;

import jakarta.validation.Valid;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;
    private static final String USER_ID_HEADER = "X-Sharer-User-Id";

    @PostMapping
    public ItemDto createItem(@Valid @RequestBody ItemRequestDto itemRequestDto,
                              @RequestHeader(USER_ID_HEADER) Long userId) {
        Item item = ItemMapper.toItem(itemRequestDto);
        Item createdItem = itemService.createItem(item, userId);
        return ItemMapper.toDto(createdItem);
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@PathVariable Long itemId,
                              @RequestBody ItemRequestDto itemRequestDto,
                              @RequestHeader(USER_ID_HEADER) Long userId) {
        Item item = ItemMapper.toItem(itemRequestDto);
        Item updatedItem = itemService.updateItem(itemId, item, userId);
        return ItemMapper.toDto(updatedItem);
    }

    @GetMapping("/{itemId}")
    public ItemDto getItemById(@PathVariable Long itemId) {
        Item item = itemService.getItemById(itemId);
        return ItemMapper.toDto(item);
    }

    @GetMapping
    public List<ItemDto> getUserItems(@RequestHeader(USER_ID_HEADER) Long userId) {
        return itemService.getUserItems(userId).stream()
                .map(ItemMapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/search")
    public List<ItemDto> searchItems(@RequestParam String text) {
        return itemService.searchItems(text).stream()
                .map(ItemMapper::toDto)
                .collect(Collectors.toList());
    }
}
