package az.att.localization;

import org.springframework.data.jpa.repository.JpaRepository;

public interface LanguageRepository extends JpaRepository<LanguageEntity, Long> {

//    Optional<LanguageEntity> findByLocaleAndMessageKey(String locale, String key);
}
