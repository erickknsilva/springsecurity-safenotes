package tech.buildrun.safenotes.utils;

import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import tech.buildrun.safenotes.repository.NotesRepository;

@Component("noteAuthz")
public class NoteAuthz {

    private final NotesRepository notesRepository;

    public NoteAuthz(NotesRepository notesRepository) {
        this.notesRepository = notesRepository;
    }

    @Transactional(readOnly = true)
    public boolean hasAccessToNote(Jwt jwt, long noteId) {

        long userId = Long.parseLong(jwt.getSubject());

       return notesRepository.existsByIdAndOwnerId(noteId, userId);

    }
}
