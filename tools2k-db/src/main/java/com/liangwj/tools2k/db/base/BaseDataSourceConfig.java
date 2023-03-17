package com.liangwj.tools2k.db.base;

import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateProperties;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateSettings;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;

/**
 * <pre>
 * 多数据源的配置的基类
 *
 * &#64;Configuration
 * &#64;EnableTransactionManagement
 * &#64;EnableJpaRepositories(entityManagerFactoryRef = DataSourceRSyslogConfig.ENTITY_MANAGER_FACTORY,
 * 		transactionManagerRef = DataSourceRSyslogConfig.TRANSACTION_MANAGER,
 * 		basePackageClasses = {
 * 			SystemEventsDomain.class
 * 		})
 * </pre>
 *
 * @author rock
 */
@Order(value = 1000)
public abstract class BaseDataSourceConfig {

	private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(BaseDataSourceConfig.class);

	@Autowired
	private JpaProperties jpaProperties;

	@Autowired
	private HibernateProperties hibernateProperties;

	private DruidDataSource datasource;
	private JdbcTemplate jdbcTemplate;
	private LocalContainerEntityManagerFactoryBean entityManagerFactory;
	private PlatformTransactionManager transactionManager;

	/**
	 * @Bean(name = DATA_SOURCE)
	 * @ConfigurationProperties(prefix = "spring.datasource_rsyslog") 的方法：
	 */
	public abstract DruidDataSource dataSource();

	/** @Bean(name = ENTITY_MANAGER_FACTORY) */
	// public abstract PlatformTransactionManager
	// transactionManager(EntityManagerFactoryBuilder builder);

	/** @Bean(name = TRANSACTION_MANAGER) */
	// public abstract LocalContainerEntityManagerFactoryBean
	// entityManagerFactory(EntityManagerFactoryBuilder builder);

	/** @Bean(name = JDBC_TEMPLATE) */
	// public abstract JdbcTemplate jdbcTemplate();

	protected abstract String getEntityPackageName();

	protected abstract String getName();

	protected DruidDataSource getDataSource() {
		if (this.datasource == null) {
			log.debug("创建 datasource: {}", getName());
			this.datasource = DruidDataSourceBuilder.create().build();
			if (this.datasource.getValidationQuery() == null) {
				// 校验的sql，如果validationQuery为null，testOnBorrow、testOnReturn、testWhileIdle都不会起作用。
				this.datasource.setValidationQuery("select 1 from dual");
			}
		}
		return this.datasource;
	}

	/**
	 * entity管理器
	 *
	 * @param builder
	 * @return
	 */
	protected LocalContainerEntityManagerFactoryBean getEntityManagerFactory(EntityManagerFactoryBuilder builder) {
		if (this.entityManagerFactory == null) {
			log.debug("创建 LocalContainerEntityManagerFactoryBean：{}", this.getName());
			DataSource dataSource = this.dataSource();
			// DataSource dataSource = this.getDataSource();

			final LocalContainerEntityManagerFactoryBean res = builder.dataSource(dataSource)
					.properties(this.jpaProperties.getProperties()) // 设置jpa配置
					.properties(getVendorProperties()) // 设置hibernate配置
					.packages(this.getEntityPackageName()) // 设置实体类所在位置
					.persistenceUnit(this.getName()) // 设置持久化单元名，用于@PersistenceContext注解获取EntityManager时指定数据源
					.build();
			this.entityManagerFactory = res;
		}
		return this.entityManagerFactory;
	}

	protected Map<String, Object> getVendorProperties() {
		// this.hibernateProperties.isUseNewIdGeneratorMappings()
		Map<String, Object> res = hibernateProperties.determineHibernateProperties(jpaProperties.getProperties(),
				new HibernateSettings());

		log.debug("Hibernate配置:{}", res);
		return res;
	}

	public PlatformTransactionManager getTransactionManager(EntityManagerFactoryBuilder builder) {
		if (this.transactionManager == null) {
			log.debug("创建 PlatformTransactionManager：{}", this.getName());
			this.transactionManager = new JpaTransactionManager(getEntityManagerFactory(builder).getObject());
		}
		return this.transactionManager;
	}

	protected JdbcTemplate getJdbcTemplate() {
		if (this.jdbcTemplate == null) {
			log.debug("创建 JdbcTemplate：{}", this.getName());
			this.jdbcTemplate = new JdbcTemplate(this.dataSource());
		}
		return this.jdbcTemplate;
	}

}
