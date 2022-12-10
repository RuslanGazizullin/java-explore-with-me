package ru.practicum.explore_with_me.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.explore_with_me.model.Comment;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("select c from Comment c " +
            "where (?1 is null or c.author.id = ?1) " +
            "and (?2 is null or c.event.id in ?2) " +
            "and (?3 is null or c.status in ?3) " +
            "and (c.createdOn >= ?4) " +
            "and (cast(?5 as date) is null or c.createdOn <= ?5) " +
            "order by c.createdOn")
    Page<Comment> findAllByAuthor(Long author, List<Long> events, List<String> statuses,
                                  LocalDateTime rangeStart, LocalDateTime rangeEnd, Pageable pageable);

    @Query("select c from Comment c " +
            "where (?1 is null or c.author.id in ?1) " +
            "and (?2 is null or c.event.id in ?2) " +
            "and (?3 is null or c.status in ?3) " +
            "and (c.createdOn >= ?4) " +
            "and (cast(?5 as date) is null or c.createdOn <= ?5) " +
            "order by c.createdOn")
    Page<Comment> findAllByAdmin(List<Long> authors, List<Long> events, List<String> statuses,
                                 LocalDateTime rangeStart, LocalDateTime rangeEnd, Pageable pageable);

    @Query("select c from Comment c " +
            "where upper(c.text) like upper(concat('%', ?1, '%')) " +
            "and (c.createdOn >= ?2) " +
            "and (cast(?3 as date) is null or c.createdOn <= ?3) " +
            "order by c.createdOn")
    Page<Comment> findAll(String text, LocalDateTime rangeStart, LocalDateTime rangeEnd, Pageable pageable);
}
