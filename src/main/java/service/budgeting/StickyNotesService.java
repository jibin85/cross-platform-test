package service.budgeting;

import entity.budgeting.StickyNotes;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import model.budgeting.sticky_notes.NewStickyNoteRequest;
import model.budgeting.sticky_notes.StickyNoteDeleteRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.GenericRepository;

import java.util.List;
import java.util.Objects;

@ApplicationScoped
@Transactional
public class StickyNotesService implements GenericRepository<StickyNotes> {

    private static final Logger logger = LoggerFactory.getLogger(StickyNotesService.class);

    // Create and Save StickyNote content
    public void saveStickyNoteContent(String userId, NewStickyNoteRequest newStickyNoteRequest){
        logger.info("Class: StickyNotesService, Method: saveStickyNoteContent -- STARTED");
        StickyNotes stickyNotes = new StickyNotes();
        stickyNotes.setUserId(userId);
        stickyNotes.setNoteId(newStickyNoteRequest.getNoteId());
        stickyNotes.setNoteColor(newStickyNoteRequest.getNoteColor());
        stickyNotes.setTimestamp(newStickyNoteRequest.getTimestamp());
        stickyNotes.setStickyNotesValue(newStickyNoteRequest.getStickyNoteValue());
        persist(stickyNotes);
        logger.info("Class: StickyNotesService, Method: saveStickyNoteContent -- EXECUTED");
    }

    // Retrieve all sticky notes for a particular user
    public List<StickyNotes> findStickyNotesByUserId(String userId) {
        logger.info("Class: StickyNotesService, Method: findStickyNotesByUserId -- STARTED for userId: {}", userId);
        List<StickyNotes> stickyNotes = find("userId", userId).list();
        logger.info("Class: StickyNotesService, Method: findStickyNotesByUserId -- EXECUTED, found {} notes",
                stickyNotes != null ? stickyNotes.size() : 0);
        return stickyNotes;
    }

    // Update existing StickyNote content
    public void updateStickyNoteContent(NewStickyNoteRequest newStickyNoteRequest){
        logger.info("Class: StickyNotesService, Method: updateStickyNoteContent -- STARTED");
        if(Objects.nonNull(findStickyNoteById(newStickyNoteRequest.getNoteId()))) {
            update("stickyNotesValue = ?1 where noteId = ?2", newStickyNoteRequest.getStickyNoteValue(), newStickyNoteRequest.getNoteId());
        }
        logger.info("Class: StickyNotesService, Method: updateStickyNoteContent -- EXECUTED");
    }

    // Delete StickyNote content
    public void deleteStickyNoteContent(StickyNoteDeleteRequest stickyNoteDeleteRequest){
        logger.info("Class: StickyNotesService, Method: deleteStickyNoteContent -- STARTED");
        StickyNotes stickyNote = findStickyNoteById(stickyNoteDeleteRequest.getNoteId());
        if(Objects.nonNull(stickyNote)){
            delete(stickyNote);
            logger.info("Sticky Note with ID: {} has been deleted.",stickyNote.getNoteId());
        }
        logger.info("Class: StickyNotesService, Method: deleteStickyNoteContent -- EXECUTED");
    }
}