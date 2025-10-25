package tech.buildrun.safenotes.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.buildrun.safenotes.entity.Note;

public interface NotesRepository extends JpaRepository<Note, Long> {
}
