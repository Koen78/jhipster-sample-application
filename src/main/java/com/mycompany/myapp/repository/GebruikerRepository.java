package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Gebruiker;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Gebruiker entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GebruikerRepository extends JpaRepository<Gebruiker, Long> {}
