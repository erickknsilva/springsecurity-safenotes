package tech.buildrun.safenotes.service.dto;

import tech.buildrun.safenotes.entity.Note;

public record NoteResponse(
        long id,
        String title,
        String content
) {


    public static NoteResponse fromDto(Note note) {
        return new NoteResponse(
                note.getId(),
                note.getTitle(),
                note.getContent()
        );
    }
}
