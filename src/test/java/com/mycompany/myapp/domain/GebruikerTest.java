package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class GebruikerTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Gebruiker.class);
        Gebruiker gebruiker1 = new Gebruiker();
        gebruiker1.setId(1L);
        Gebruiker gebruiker2 = new Gebruiker();
        gebruiker2.setId(gebruiker1.getId());
        assertThat(gebruiker1).isEqualTo(gebruiker2);
        gebruiker2.setId(2L);
        assertThat(gebruiker1).isNotEqualTo(gebruiker2);
        gebruiker1.setId(null);
        assertThat(gebruiker1).isNotEqualTo(gebruiker2);
    }
}
