package tech.buildrun.safenotes.service;


import jakarta.transaction.Transactional;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import tech.buildrun.safenotes.controller.dto.NoteRequest;
import tech.buildrun.safenotes.entity.Note;
import tech.buildrun.safenotes.repository.NotesRepository;
import tech.buildrun.safenotes.repository.UserRepository;
import tech.buildrun.safenotes.service.dto.NoteResponse;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NoteService {

    private final UserRepository userRepository;
    private final NotesRepository notesRepository;

    public NoteService(UserRepository userRepository, NotesRepository notesRepository) {
        this.userRepository = userRepository;
        this.notesRepository = notesRepository;
    }


    public List<NoteResponse> listNotes(Jwt jwt) {
        String userId = jwt.getSubject();
        return notesRepository.findAllByOwnerId(Long.parseLong(userId))
                .stream()
                .map(NoteResponse::fromDto)
                .collect(Collectors.toList());

    }

    public NoteResponse createNote(Jwt jwt, NoteRequest request) {
        var user = userRepository.getReferenceById(Long.valueOf(jwt.getSubject()));

        Note savedNote = notesRepository.save(new Note(request.title(), request.content(), user));

        return NoteResponse.fromDto(savedNote);
    }

    @Transactional
    public void updateNote(Long noteId, NoteRequest noteRequest) {

        var note = notesRepository.getReferenceById(noteId);

        note.setTitle(noteRequest.title());
        note.setContent(noteRequest.content());

        notesRepository.save(note);
    }

    public void deleteNote(Long noteId) {

        notesRepository.deleteById(noteId);

    }

    @Transactional
    public NoteResponse noteRead(Long noteId) {

        var note = notesRepository.getReferenceById(noteId);
        return NoteResponse.fromDto(note);
    }
}
