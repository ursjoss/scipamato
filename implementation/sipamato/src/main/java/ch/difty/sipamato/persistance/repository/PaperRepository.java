package ch.difty.sipamato.persistance.repository;

public interface PaperRepository {

    void create(int id, String authors, String firstAuthor, boolean firstAuthorOverridden, String title, String location, String gaols);

}
