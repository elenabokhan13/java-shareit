package ru.practicum.shareit.booking.dto;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.user.dto.UserMapper;

@Component
public class BookingMapper {
    private final ItemMapper itemMapper = new ItemMapper();
    private final UserMapper userMapper = new UserMapper();

    public BookingDtoOutcoming bookingDto(Booking booking) {
        return BookingDtoOutcoming.builder()
                .item(itemMapper.itemToDto(booking.getItem()))
                .booker(userMapper.userToDto(booking.getBooker()))
                .end(booking.getEndDate())
                .start(booking.getStartDate())
                .status(booking.getStatus())
                .id(booking.getId())
                .build();
    }
}
