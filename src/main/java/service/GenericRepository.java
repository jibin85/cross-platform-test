package service;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.transaction.Transactional;

@Transactional
public interface GenericRepository<T> extends PanacheRepository<T> {
    default T findById(String id) {
        return find("id", id).firstResult();
    }
    default T findStickyNoteById(Long noteId) {
        return find("noteId", noteId).firstResult();
    }
}