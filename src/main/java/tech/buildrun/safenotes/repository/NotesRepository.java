package tech.buildrun.safenotes.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.buildrun.safenotes.entity.Note;

import java.util.List;
import java.util.Optional;

public interface NotesRepository extends JpaRepository<Note, Long> {

    List<Note> findAllByOwnerId(long l);

    Optional<Note> findByTitle(String title);

    boolean existsByIdAndOwnerId(long noteId, long l);
}
