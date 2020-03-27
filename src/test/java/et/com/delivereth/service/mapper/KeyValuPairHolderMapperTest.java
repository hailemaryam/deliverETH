package et.com.delivereth.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class KeyValuPairHolderMapperTest {

    private KeyValuPairHolderMapper keyValuPairHolderMapper;

    @BeforeEach
    public void setUp() {
        keyValuPairHolderMapper = new KeyValuPairHolderMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(keyValuPairHolderMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(keyValuPairHolderMapper.fromId(null)).isNull();
    }
}
