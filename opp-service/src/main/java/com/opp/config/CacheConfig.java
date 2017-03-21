package com.opp.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by ctobe on 5/6/15.
 */
@Configuration
@EnableCaching
public class CacheConfig {

    // -------- Configure Caching ---------

    // use ehcache
    @Bean
    public CacheManager cacheManager(net.sf.ehcache.CacheManager cacheManager) {
        return new EhCacheCacheManager(cacheManager);
    }

    @Bean
    public EhCacheManagerFactoryBean ehcache() {
        EhCacheManagerFactoryBean ehCacheManagerFactoryBean = new EhCacheManagerFactoryBean();
        ehCacheManagerFactoryBean.setShared(true);
        //ehCacheManagerFactoryBean.setConfigLocation(new ClassPathResource("ehcache.xml"));
        return ehCacheManagerFactoryBean;
    }

//  // Basic caching
//  @Bean
//  public CacheManager cacheManager() {
//      return new ConcurrentMapCacheManager("graphiteAllApps");
//  }

    // use guava caching
//	@Bean
//	public CacheManager cacheManager() {
//		return new GuavaCacheManager();
//	}

}
