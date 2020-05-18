/*
 * package org.zerock.config;
 * 
 * import java.sql.Connection;
 * 
 * import javax.sql.DataSource;
 * 
 * import org.apache.ibatis.session.ExecutorType; import
 * org.apache.ibatis.session.SqlSession; import
 * org.apache.ibatis.session.SqlSessionFactory; import
 * org.apache.ibatis.session.TransactionIsolationLevel; import
 * org.mybatis.spring.SqlSessionFactoryBean; import
 * org.mybatis.spring.annotation.MapperScan; import
 * org.springframework.context.annotation.Bean; import
 * org.springframework.context.annotation.ComponentScan; import
 * org.springframework.context.annotation.Configuration; import
 * org.springframework.context.annotation.EnableAspectJAutoProxy; import
 * org.springframework.jdbc.datasource.DataSourceTransactionManager; import
 * org.springframework.stereotype.Component; import
 * org.springframework.transaction.annotation.EnableTransactionManagement;
 * 
 * import com.zaxxer.hikari.HikariConfig; import
 * com.zaxxer.hikari.HikariDataSource;
 * 
 * @Configuration
 * 
 * @ComponentScan(basePackages = {"org.zerock.service"})
 * 
 * @ComponentScan(basePackages = "org.zerock.aop")
 * 
 * @EnableAspectJAutoProxy
 * 
 * @EnableTransactionManagement
 * 
 * @MapperScan(basePackages = {"org.zerock.mapper"}) public class RootConfig {
 * 
 * @Bean public DataSource dataSource() { HikariConfig hikariConfig = new
 * HikariConfig();
 * hikariConfig.setDriverClassName("net.sf.log4jdbc.sql.jdbcapi.DriverSpy");
 * hikariConfig.setJdbcUrl("jdbc:log4jdbc:oracle:thin:@localhost:1521:XE");
 * hikariConfig.setUsername("java"); hikariConfig.setPassword("java");
 * 
 * HikariDataSource datasource = new HikariDataSource(hikariConfig);
 * 
 * return datasource; }
 * 
 * 
 * @Bean public SqlSessionFactory sqlSessionFactory() throws Exception{
 * SqlSessionFactoryBean sqlSessionFactory = new SqlSessionFactoryBean();
 * sqlSessionFactory.setDataSource(dataSource()); return
 * (SqlSessionFactory)sqlSessionFactory.getObject(); }
 * 
 * @Bean public DataSourceTransactionManager txManger() { return new
 * DataSourceTransactionManager(dataSource()); } }
 */