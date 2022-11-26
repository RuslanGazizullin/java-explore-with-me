package ru.practicum.explore_with_me.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.explore_with_me.model.Event;
import ru.practicum.explore_with_me.model.EventState;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    @Query("select e from Event e " +
            "where (upper(e.annotation) like upper(concat('%', ?1, '%')) " +
            "or upper(e.description) like upper(concat('%', ?1, '%')))" +
            "and e.category in ?2 " +
            "and e.paid = ?3 " +
            "and e.eventDate >= ?4 " +
            "and e.eventDate <= ?5 " +
            "order by e.eventDate")
    Page<Event> findAll(String text, List<Long> categories, Boolean paid, LocalDateTime rangeStart,
                        LocalDateTime rangeEnd, Pageable pageable);

    @Query("select e from Event e " +
            "where (upper(e.annotation) like upper(concat('%', ?1, '%')) " +
            "or upper(e.description) like upper(concat('%', ?1, '%')))" +
            "and e.category in ?2 " +
            "and e.paid = ?3 " +
            "and e.eventDate >= ?4 " +
            "order by e.eventDate")
    Page<Event> findAll(String text, List<Long> categories, Boolean paid, LocalDateTime rangeStart, Pageable pageable);

    Event findByIdAndState(Long id, EventState state);

    Page<Event> findAllByInitiator(Pageable pageable, Long initiator);

    Optional<Event> findByIdAndInitiator(Long id, Long initiator);

    List<Long> findAllByCategory(Long category);

    @Query("select e.id from Event e where e.initiator = ?1")
    List<Long> findAllIdByInitiator(Long initiator);

    @Query("select e from Event e " +
            "where e.initiator in ?1 " +
            "and e.category in ?2 " +
            "and e.eventDate>=?3 " +
            "and e.eventDate<=?4")
    Page<Event> findAllByAdmin(List<Long> users, List<Long> category, LocalDateTime rangeStart,
                               LocalDateTime rangeEnd, Pageable pageable);

    @Query("select e from Event e " +
            "where e.initiator in ?1 " +
            "and e.category in ?2 " +
            "and e.eventDate>=?3 ")
    Page<Event> findAllByAdmin(List<Long> users, List<Long> category, LocalDateTime rangeStart,
                               Pageable pageable);
}
