package et.com.delivereth.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class KeyValuPairHolerMapperTest {

    private KeyValuPairHolerMapper keyValuPairHolerMapper;

    @BeforeEach
    public void setUp() {
        keyValuPairHolerMapper = new KeyValuPairHolerMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(keyValuPairHolerMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(keyValuPairHolerMapper.fromId(null)).isNull();
    }
}
