package et.com.delivereth.config;

import java.time.Duration;

import org.ehcache.config.builders.*;
import org.ehcache.jsr107.Eh107Configuration;

import org.hibernate.cache.jcache.ConfigSettings;
import io.github.jhipster.config.JHipsterProperties;

import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.*;

@Configuration
@EnableCaching
public class CacheConfiguration {

    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        JHipsterProperties.Cache.Ehcache ehcache = jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration = Eh107Configuration.fromEhcacheCacheConfiguration(
            CacheConfigurationBuilder.newCacheConfigurationBuilder(Object.class, Object.class,
                ResourcePoolsBuilder.heap(ehcache.getMaxEntries()))
                .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(ehcache.getTimeToLiveSeconds())))
                .build());
    }

    @Bean
    public HibernatePropertiesCustomizer hibernatePropertiesCustomizer(javax.cache.CacheManager cacheManager) {
        return hibernateProperties -> hibernateProperties.put(ConfigSettings.CACHE_MANAGER, cacheManager);
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            createCache(cm, et.com.delivereth.repository.UserRepository.USERS_BY_LOGIN_CACHE);
            createCache(cm, et.com.delivereth.repository.UserRepository.USERS_BY_EMAIL_CACHE);
            createCache(cm, et.com.delivereth.domain.User.class.getName());
            createCache(cm, et.com.delivereth.domain.Authority.class.getName());
            createCache(cm, et.com.delivereth.domain.User.class.getName() + ".authorities");
            createCache(cm, et.com.delivereth.domain.TelegramUser.class.getName());
            createCache(cm, et.com.delivereth.domain.TelegramUser.class.getName() + ".orders");
            createCache(cm, et.com.delivereth.domain.Restorant.class.getName());
            createCache(cm, et.com.delivereth.domain.Restorant.class.getName() + ".foods");
            createCache(cm, et.com.delivereth.domain.Food.class.getName());
            createCache(cm, et.com.delivereth.domain.Food.class.getName() + ".orderedFoods");
            createCache(cm, et.com.delivereth.domain.Order.class.getName());
            createCache(cm, et.com.delivereth.domain.Order.class.getName() + ".orderedFoods");
            createCache(cm, et.com.delivereth.domain.OrderedFood.class.getName());
            createCache(cm, et.com.delivereth.domain.KeyValuPairHolder.class.getName());
            createCache(cm, et.com.delivereth.domain.TelegramRestaurantUser.class.getName());
            createCache(cm, et.com.delivereth.domain.TelegramRestaurantUser.class.getName() + ".restorants");
            createCache(cm, et.com.delivereth.domain.Restorant.class.getName() + ".telegramRestaurantUsers");
            createCache(cm, et.com.delivereth.domain.TelegramDeliveryUser.class.getName());
            createCache(cm, et.com.delivereth.domain.TelegramDeliveryUser.class.getName() + ".orders");
            createCache(cm, et.com.delivereth.domain.TelegramDeliveryUser.class.getName() + ".restorants");
            createCache(cm, et.com.delivereth.domain.Restorant.class.getName() + ".telegramDeliveryUsers");
            createCache(cm, et.com.delivereth.domain.TelegramRestaurantUser.class.getName() + ".orders");
            // jhipster-needle-ehcache-add-entry
        };
    }

    private void createCache(javax.cache.CacheManager cm, String cacheName) {
        javax.cache.Cache<Object, Object> cache = cm.getCache(cacheName);
        if (cache == null) {
            cm.createCache(cacheName, jcacheConfiguration);
        }
    }

}
