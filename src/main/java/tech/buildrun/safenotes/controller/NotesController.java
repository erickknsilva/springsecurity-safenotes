package tech.buildrun.safenotes.controller;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.parameters.P;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import tech.buildrun.safenotes.controller.dto.NoteRequest;
import tech.buildrun.safenotes.service.NoteService;
import tech.buildrun.safenotes.service.dto.NoteResponse;

import java.util.List;

@RestController
@RequestMapping("/notes")
public class NotesController {


    private final NoteService noteService;

    public NotesController(NoteService noteService) {
        this.noteService = noteService;
    }


    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('SCOPE_NOTE:READ')")
    public List<NoteResponse> getNotes(@AuthenticationPrincipal Jwt jwt) {

        return noteService.listNotes(jwt);
    }

    @PostMapping
    @PreAuthorize(("hasAuthority('SCOPE_NOTE:WRITE')"))
    @ResponseStatus(HttpStatus.CREATED)
    public NoteResponse createNote(@AuthenticationPrincipal Jwt jwt, @RequestBody NoteRequest noteRequest) {
        return noteService.createNote(jwt, noteRequest);
    }

    @PutMapping("/{noteId}")
    @PreAuthorize(("hasAuthority('SCOPE_NOTE:WRITE') and @noteAuthz.hasAccessToNote(#jwt, #noteId)"))
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateNote(@P("jwt") @AuthenticationPrincipal Jwt jwt, @P("noteId") @PathVariable Long noteId, @RequestBody NoteRequest noteRequest) {

        noteService.updateNote(noteId, noteRequest);
    }


    @GetMapping("/{noteId}")
    @PreAuthorize("hasAuthority('SCOPE_NOTE:READ') and @noteAuthz.hasAccessToNote(#jwt, #noteId)")
    @ResponseStatus(HttpStatus.OK)
    public NoteResponse noteRead(@P("jwt")@AuthenticationPrincipal Jwt jwt, @P("noteId") @PathVariable Long noteId ){

        return noteService.noteRead(noteId);

    }

    @DeleteMapping("/{noteId}")
    @PreAuthorize(("hasAuthority('SCOPE_NOTE:WRITE') and @noteAuthz.hasAccessToNote(#jwt, #noteId)"))
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteNote(@P("jwt") @AuthenticationPrincipal Jwt jwt, @P("noteId") @PathVariable Long noteId) {

        noteService.deleteNote(noteId);
    }

}
