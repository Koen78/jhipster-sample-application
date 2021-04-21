package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class OverviewTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Overview.class);
        Overview overview1 = new Overview();
        overview1.setId(1L);
        Overview overview2 = new Overview();
        overview2.setId(overview1.getId());
        assertThat(overview1).isEqualTo(overview2);
        overview2.setId(2L);
        assertThat(overview1).isNotEqualTo(overview2);
        overview1.setId(null);
        assertThat(overview1).isNotEqualTo(overview2);
    }
}
