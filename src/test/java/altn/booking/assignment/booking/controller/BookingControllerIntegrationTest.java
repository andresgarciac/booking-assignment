package altn.booking.assignment.booking.controller;

import altn.booking.assignment.booking.repository.BookingRepository;
import altn.booking.assignment.booking.repository.RoomAvailabilityRepository;
import altn.booking.assignment.booking.repository.entity.Booking;
import altn.booking.assignment.booking.repository.entity.RoomAvailability;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.springframework.http.MediaType.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureTestDatabase
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestPropertySource(locations = "classpath:application-test.properties")
class BookingControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private RoomAvailabilityRepository roomAvailabilityRepository;
    private LocalDateTime actualDate = LocalDateTime.from(LocalDate.now().atStartOfDay());

    @BeforeAll
    public void setup() {
        List<RoomAvailability> availabilities = new ArrayList<>();
        availabilities.add(RoomAvailability.builder().id("11").availabilityDate(actualDate).build());
        availabilities.add(RoomAvailability.builder().id("12").availabilityDate(actualDate.plusDays(1)).build());
        availabilities.add(RoomAvailability.builder().id("13").availabilityDate(actualDate.plusDays(2)).build());
        availabilities.add(RoomAvailability.builder().id("14").availabilityDate(actualDate.plusDays(3)).build());
        availabilities.add(RoomAvailability.builder().id("15").availabilityDate(actualDate.plusDays(4)).build());

        roomAvailabilityRepository.saveAll(availabilities);
        bookingRepository.save(Booking.builder()
                .transactionId("1")
                .reservationDateId("14")
                .userId("1019")
                .build()
        );
    }

    @Test
    void get_room_availability_OK() throws Exception {
        mockMvc.perform(get("/booking/availability"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.dates").isArray())
                .andExpect(jsonPath("$.dates[0]", is(actualDate.plusDays(1) + ":00")));
    }

    @Test
    void place_booking_OK() throws Exception {
        String requestJson= "{\n" + "\"dates\": [ \""+actualDate.plusDays(1)+"\", \""+actualDate.plusDays(2)+"\"]," +
                "\t\"userId\": \"1019\"\n" + "}";

        mockMvc.perform(post("/booking/place").contentType(APPLICATION_JSON_VALUE)
                        .content(requestJson))
                .andExpect(status().isOk());
    }

    @Test
    void place_booking_not_available_date_BAD_REQUEST() throws Exception {
        String requestJson= "{\n" + "\t\"dates\": [ \""+ actualDate.plusDays(6) +"\"],\n" +
                "\t\"userId\": \"1019\"\n" + "}";

        mockMvc.perform(post("/booking/place").contentType(APPLICATION_JSON_VALUE)
                        .content(requestJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$", is("Date no available to place a booking")));
    }

    @Test
    void place_booking_today_BAD_REQUEST() throws Exception {
        String requestJson= "{\n" + "\t\"dates\": [ \""+ actualDate +"\"],\n" +
                "\t\"userId\": \"1019\"\n" + "}";

        mockMvc.perform(post("/booking/place").contentType(APPLICATION_JSON_VALUE)
                        .content(requestJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$", is("The date "+actualDate+" is not valid to place a booking")));
    }

    @Test
    void place_booking_30_days_in_advance_BAD_REQUEST() throws Exception {
        String requestJson= "{\n" + "\t\"dates\": [ \""+ actualDate.plusDays(31) +"\"],\n" +
                "\t\"userId\": \"1019\"\n" + "}";

        mockMvc.perform(post("/booking/place").contentType(APPLICATION_JSON_VALUE)
                        .content(requestJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$", is("The date "+actualDate.plusDays(31)+" is 30 days after")));
    }

    @Test
    void place_booking_more_than_3_days_BAD_REQUEST() throws Exception {
        String requestJson = "{\n" +
                "\"dates\": [ \""+ actualDate.plusDays(1) +"\", \""+ actualDate.plusDays(2) +"\", " +
                        "\""+actualDate.plusDays(3)+"\", \""+actualDate.plusDays(4)+"\"]," +
                "\t\"userId\": \"1019\"\n" + "}";


        mockMvc.perform(post("/booking/place").contentType(APPLICATION_JSON_VALUE)
                        .content(requestJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$", is("You can't book more than 3 days")));
    }

    @Test
    void modify_booking_by_transactionId_OK() throws Exception {
        String requestJson= "{\n" + "\"dates\": [ \""+actualDate.plusDays(4)+"\"]," +
                "\t\"userId\": \"1019\"\n" + "}";

        mockMvc.perform(put("/booking/modify/1").contentType(APPLICATION_JSON_VALUE)
                        .content(requestJson))
                .andExpect(status().isOk());
    }

    @Test
    void modify_booking_by_transactionId_NOT_FOUND() throws Exception {
        String requestJson= "{\n" + "\"dates\": [ \""+actualDate.plusDays(4)+"\"]," +
                "\t\"userId\": \"1019\"\n" + "}";

        mockMvc.perform(put("/booking/modify/2").contentType(APPLICATION_JSON_VALUE)
                        .content(requestJson))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$", is("The booking doesn't exist")));
    }

    @Test
    void cancel_booking_by_transactionId_OK() throws Exception {
        mockMvc.perform(delete("/booking/cancel/1"))
                .andExpect(status().isOk());
    }

    @Test
    void cancel_booking_by_transactionId_NOT_FOUND() throws Exception {
        mockMvc.perform(delete("/booking/cancel/2"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$", is("The booking doesn't exist")));
    }
}