package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingDtoIncoming;
import ru.practicum.shareit.booking.dto.BookingDtoOutcoming;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.practicum.shareit.constant.Constant.USER_ID;


@WebMvcTest(controllers = BookingController.class)
class BookingControllerTest {
    @Autowired
    private ObjectMapper mapper;
    @MockBean
    private BookingService bookingService;
    @Autowired
    private MockMvc mvc;

    @Test
    void createBookingTest() throws Exception {
        UserDto userOne = UserDto.builder()
                .name("User1")
                .id(1L)
                .email("user1@user.om")
                .build();
        ItemDto itemOne = ItemDto.builder()
                .id(1L)
                .name("item1")
                .description("description item 1")
                .available(true).build();
        BookingDtoIncoming bookingOneDto = BookingDtoIncoming.builder()
                .itemId(1L)
                .start(LocalDateTime.of(2024, Month.APRIL, 5, 23, 23))
                .end(LocalDateTime.of(2024, Month.APRIL, 8, 23, 23))
                .build();
        BookingDtoOutcoming bookingOneDtoOut = BookingDtoOutcoming.builder()
                .status("WAITING")
                .booker(userOne)
                .item(itemOne)
                .end(bookingOneDto.getEnd())
                .start(bookingOneDto.getStart())
                .build();

        when(bookingService.createBooking(any(), anyLong())).thenReturn(bookingOneDtoOut);

        mvc.perform(post("/bookings")
                        .content(mapper.writeValueAsString(bookingOneDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(USER_ID, 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(bookingOneDtoOut.getStatus()))
                .andExpect(jsonPath("$.item.id").value(itemOne.getId()))
                .andExpect(jsonPath("$.booker.id").value(userOne.getId()));
    }

    @Test
    void changeStatusBookingTest() throws Exception {
        UserDto userOne = UserDto.builder()
                .name("User1")
                .id(1L)
                .email("user1@user.om")
                .build();
        ItemDto itemOne = ItemDto.builder()
                .id(1L)
                .name("item1")
                .description("description item 1")
                .available(true).build();
        BookingDtoIncoming bookingOneDto = BookingDtoIncoming.builder()
                .itemId(1L)
                .start(LocalDateTime.of(2024, Month.APRIL, 5, 23, 23))
                .end(LocalDateTime.of(2024, Month.APRIL, 8, 23, 23))
                .build();
        BookingDtoOutcoming bookingOneDtoOut = BookingDtoOutcoming.builder()
                .status("APPROVED")
                .booker(userOne)
                .item(itemOne)
                .end(bookingOneDto.getEnd())
                .start(bookingOneDto.getStart())
                .build();

        when(bookingService.updateBooking(anyLong(), anyLong(), anyString())).thenReturn(bookingOneDtoOut);

        mvc.perform(patch("/bookings/{bookingId}", "1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(USER_ID, 1L)
                        .param("approved", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(bookingOneDtoOut.getStatus()))
                .andExpect(jsonPath("$.item.id").value(itemOne.getId()))
                .andExpect(jsonPath("$.booker.id").value(userOne.getId()));

    }

    @Test
    void getBookingByIdTest() throws Exception {
        UserDto userOne = UserDto.builder()
                .name("User1")
                .id(1L)
                .email("user1@user.om")
                .build();
        ItemDto itemOne = ItemDto.builder()
                .id(1L)
                .name("item1")
                .description("description item 1")
                .available(true).build();
        BookingDtoIncoming bookingOneDto = BookingDtoIncoming.builder()
                .itemId(1L)
                .start(LocalDateTime.of(2024, Month.APRIL, 5, 23, 23))
                .end(LocalDateTime.of(2024, Month.APRIL, 8, 23, 23))
                .build();
        BookingDtoOutcoming bookingOneDtoOut = BookingDtoOutcoming.builder()
                .status("APPROVED")
                .booker(userOne)
                .item(itemOne)
                .end(bookingOneDto.getEnd())
                .start(bookingOneDto.getStart())
                .build();

        when(bookingService.getBookingById(anyLong(), anyLong())).thenReturn(bookingOneDtoOut);

        mvc.perform(get("/bookings/{bookingId}", "1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(USER_ID, 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(bookingOneDtoOut.getStatus()))
                .andExpect(jsonPath("$.item.id").value(itemOne.getId()))
                .andExpect(jsonPath("$.booker.id").value(userOne.getId()));
    }

    @Test
    void getUserBookingsTest() throws Exception {
        UserDto userOne = UserDto.builder()
                .name("User1")
                .id(1L)
                .email("user1@user.om")
                .build();
        ItemDto itemOne = ItemDto.builder()
                .id(1L)
                .name("item1")
                .description("description item 1")
                .available(true).build();
        BookingDtoIncoming bookingOneDto = BookingDtoIncoming.builder()
                .itemId(1L)
                .start(LocalDateTime.of(2024, Month.APRIL, 5, 23, 23))
                .end(LocalDateTime.of(2024, Month.APRIL, 8, 23, 23))
                .build();
        BookingDtoOutcoming bookingOneDtoOut = BookingDtoOutcoming.builder()
                .status("APPROVED")
                .booker(userOne)
                .item(itemOne)
                .end(bookingOneDto.getEnd())
                .start(bookingOneDto.getStart())
                .build();

        when(bookingService.getAllByUser(anyLong(), anyString(), anyInt(), anyInt()))
                .thenReturn(List.of(bookingOneDtoOut));

        mvc.perform(get("/bookings")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(USER_ID, 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].status").value(bookingOneDtoOut.getStatus()))
                .andExpect(jsonPath("$.[0].item.id").value(itemOne.getId()))
                .andExpect(jsonPath("$.[0].booker.id").value(userOne.getId()));
    }

    @Test
    void getOwnerBookings() throws Exception {
        UserDto userOne = UserDto.builder()
                .name("User1")
                .id(1L)
                .email("user1@user.om")
                .build();
        ItemDto itemOne = ItemDto.builder()
                .id(1L)
                .name("item1")
                .description("description item 1")
                .available(true).build();
        BookingDtoIncoming bookingOneDto = BookingDtoIncoming.builder()
                .itemId(1L)
                .start(LocalDateTime.of(2024, Month.APRIL, 5, 23, 23))
                .end(LocalDateTime.of(2024, Month.APRIL, 8, 23, 23))
                .build();
        BookingDtoOutcoming bookingOneDtoOut = BookingDtoOutcoming.builder()
                .status("APPROVED")
                .booker(userOne)
                .item(itemOne)
                .end(bookingOneDto.getEnd())
                .start(bookingOneDto.getStart())
                .build();

        when(bookingService.getAllByOwner(anyLong(), anyString(), anyInt(), anyInt()))
                .thenReturn(List.of(bookingOneDtoOut));

        mvc.perform(get("/bookings/owner")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(USER_ID, 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].status").value(bookingOneDtoOut.getStatus()))
                .andExpect(jsonPath("$.[0].item.id").value(itemOne.getId()))
                .andExpect(jsonPath("$.[0].booker.id").value(userOne.getId()));
    }
}