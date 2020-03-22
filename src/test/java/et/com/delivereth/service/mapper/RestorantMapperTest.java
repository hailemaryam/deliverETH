package et.com.delivereth.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class RestorantMapperTest {

    private RestorantMapper restorantMapper;

    @BeforeEach
    public void setUp() {
        restorantMapper = new RestorantMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(restorantMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(restorantMapper.fromId(null)).isNull();
    }
}
