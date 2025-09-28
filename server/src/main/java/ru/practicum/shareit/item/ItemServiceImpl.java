package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ItemAccessDeniedException;
import ru.practicum.shareit.exception.ItemValidationException;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import ru.practicum.shareit.item.dto.CommentRequestDto;
import ru.practicum.shareit.item.dto.CommentResponseDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.dto.ItemWithBookingsDto;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserService userService;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;

    @Override
    @Transactional
    public ItemDto createItem(ItemRequestDto itemRequestDto, Long userId) {
        User owner = userService.getUserById(userId);
        Item item = ItemMapper.toItem(itemRequestDto, owner);
        Item savedItem = itemRepository.save(item);
        return ItemMapper.toDto(savedItem);
    }

    @Override
    @Transactional
    public ItemDto updateItem(Long itemId, ItemRequestDto itemRequestDto, Long userId) {
        userService.getUserById(userId);
        Item existingItem = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Item with id " + itemId + " not found"));

        if (!existingItem.getOwner().getId().equals(userId)) {
            throw new ItemAccessDeniedException("Only owner can update item");
        }

        ItemMapper.updateItemFromDto(itemRequestDto, existingItem);
        Item updatedItem = itemRepository.save(existingItem);
        return ItemMapper.toDto(updatedItem);
    }

    @Override
    public Item getItemById(Long itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Item with id " + itemId + " not found"));
    }

    @Override
    public ItemResponseDto getItemDtoById(Long itemId, Long userId) {
        Item item = getItemById(itemId);
        List<CommentResponseDto> comments = getCommentsForItem(itemId);

        ItemResponseDto.BookingDto lastBooking = null;
        ItemResponseDto.BookingDto nextBooking = null;

        if (item.getOwner().getId().equals(userId)) {
            lastBooking = getLastBooking(itemId);
            nextBooking = getNextBooking(itemId);
        }

        return ItemMapper.toItemResponseDto(item, lastBooking, nextBooking, comments);
    }

    @Override
    public List<ItemWithBookingsDto> getUserItems(Long userId) {
        userService.getUserById(userId);
        List<Item> items = itemRepository.findByOwnerIdOrderById(userId);

        return items.stream().map(item -> {
            List<CommentResponseDto> comments = getCommentsForItem(item.getId());
            ItemWithBookingsDto.BookingDto lastBooking = getLastBookingWithDates(item.getId());
            ItemWithBookingsDto.BookingDto nextBooking = getNextBookingWithDates(item.getId());

            return ItemMapper.toItemWithBookingsDto(item, lastBooking, nextBooking, comments);
        }).collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> searchItems(String text) {
        if (text == null || text.isBlank()) {
            return List.of();
        }
        return itemRepository.searchAvailableItems(text).stream()
                .map(ItemMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CommentResponseDto addComment(Long itemId, CommentRequestDto commentRequestDto, Long userId) {
        Item item = getItemById(itemId);
        User author = userService.getUserById(userId);

        List<Booking> pastBookings = bookingRepository.findByBookerIdAndItemIdAndEndBefore(
                userId, itemId, LocalDateTime.now());

        boolean hasBooked = pastBookings.stream()
                .anyMatch(booking -> booking.getStatus() == BookingStatus.APPROVED);

        if (!hasBooked) {
            throw new ItemValidationException("User can only comment on items they have booked in the past");
        }

        Comment comment = new Comment();
        comment.setText(commentRequestDto.getText());
        comment.setItem(item);
        comment.setAuthor(author);
        comment.setCreated(LocalDateTime.now());

        Comment savedComment = commentRepository.save(comment);
        return CommentMapper.toDto(savedComment);
    }

    @Override
    public List<ItemDto> getItemsByRequestId(Long requestId) {
        List<Item> items = itemRepository.findByRequestId(requestId);
        return items.stream()
                .map(ItemMapper::toDto)
                .collect(Collectors.toList());
    }

    private ItemResponseDto.BookingDto getLastBooking(Long itemId) {
        return bookingRepository
                .findFirstByItemIdAndStartBeforeAndStatusOrderByStartDesc(
                        itemId, LocalDateTime.now(), BookingStatus.APPROVED)
                .map(booking -> new ItemResponseDto.BookingDto(booking.getId(), booking.getBooker().getId()))
                .orElse(null);
    }

    private ItemResponseDto.BookingDto getNextBooking(Long itemId) {
        return bookingRepository
                .findFirstByItemIdAndStartAfterAndStatusOrderByStartAsc(
                        itemId, LocalDateTime.now(), BookingStatus.APPROVED)
                .map(booking -> new ItemResponseDto.BookingDto(booking.getId(), booking.getBooker().getId()))
                .orElse(null);
    }

    private ItemWithBookingsDto.BookingDto getLastBookingWithDates(Long itemId) {
        return bookingRepository
                .findFirstByItemIdAndStartBeforeAndStatusOrderByStartDesc(
                        itemId, LocalDateTime.now(), BookingStatus.APPROVED)
                .map(booking -> new ItemWithBookingsDto.BookingDto(
                        booking.getId(),
                        booking.getBooker().getId(),
                        booking.getStart(),
                        booking.getEnd()))
                .orElse(null);
    }

    private ItemWithBookingsDto.BookingDto getNextBookingWithDates(Long itemId) {
        return bookingRepository
                .findFirstByItemIdAndStartAfterAndStatusOrderByStartAsc(
                        itemId, LocalDateTime.now(), BookingStatus.APPROVED)
                .map(booking -> new ItemWithBookingsDto.BookingDto(
                        booking.getId(),
                        booking.getBooker().getId(),
                        booking.getStart(),
                        booking.getEnd()))
                .orElse(null);
    }

    private List<CommentResponseDto> getCommentsForItem(Long itemId) {
        return commentRepository.findByItemIdOrderByCreatedDesc(itemId).stream()
                .map(CommentMapper::toDto)
                .collect(Collectors.toList());
    }
}