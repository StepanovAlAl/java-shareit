package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.*;

import java.util.List;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;
    private static final String USER_ID_HEADER = "X-Sharer-User-Id";

    @PostMapping
    public ItemDto createItem(@RequestBody ItemRequestDto itemRequestDto,
                              @RequestHeader(USER_ID_HEADER) Long userId) {
        return itemService.createItem(itemRequestDto, userId);
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@PathVariable Long itemId,
                              @RequestBody ItemRequestDto itemRequestDto,
                              @RequestHeader(USER_ID_HEADER) Long userId) {
        return itemService.updateItem(itemId, itemRequestDto, userId);
    }

    @GetMapping("/{itemId}")
    public ItemResponseDto getItemById(@PathVariable Long itemId,
                                       @RequestHeader(USER_ID_HEADER) Long userId) {
        return itemService.getItemDtoById(itemId, userId);
    }

    @GetMapping
    public List<ItemWithBookingsDto> getUserItems(@RequestHeader(USER_ID_HEADER) Long userId) {
        return itemService.getUserItems(userId);
    }

    @GetMapping("/search")
    public List<ItemDto> searchItems(@RequestParam String text) {
        return itemService.searchItems(text);
    }

    @PostMapping("/{itemId}/comment")
    public CommentResponseDto addComment(@PathVariable Long itemId,
                                         @RequestBody CommentRequestDto commentRequestDto,
                                         @RequestHeader(USER_ID_HEADER) Long userId) {
        return itemService.addComment(itemId, commentRequestDto, userId);
    }
}