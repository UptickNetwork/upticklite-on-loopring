// +---------------------------------------------------------------------------- 
// | UptickLite is the community version of Uptick NFT marketpalce, including    
// | three major components - service, api and web, powered by Uptick Network    
// | and Loopring. It is available to the Uptick and Loopring dev community      
// | through the open source license.                                            
// | Uptick Network is a busienss grade NFT infrastructure for NFT applications  
// | with multichian and interchain support. Loopring is a zkRollup layer2 on top
// | of Ethereum.                                                                
// +---------------------------------------------------------------------------- 
// | Copyright (c) 2022~2099 Uptick Network (https://uptick.network/)            
// | All rights reserved.                                                        
// +---------------------------------------------------------------------------- 
// | Licensed ( https://www.gnu.org/licenses/gpl-3.0.en.html )                   
// +---------------------------------------------------------------------------- 
// | Author: Uptick Network development team tech@uptickproject.com              
// +---------------------------------------------------------------------------- 
package org.uptick.market.configuration;

import com.alibaba.druid.pool.DruidDataSource;
import com.ibeetl.starter.BeetlSqlMutipleSourceCustomize;
import org.beetl.sql.core.Interceptor;
import org.beetl.sql.core.SQLManager;
import org.beetl.sql.core.db.MySqlStyle;
import org.beetl.sql.ext.spring4.BeetlSqlDataSource;
import org.beetl.sql.ext.spring4.SqlManagerFactoryBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.uptick.market.interceptor.DebugRewriteInterceptor;

import javax.sql.DataSource;


@Configuration
public class DataSourceConfig {

    @Bean(name = "core")
    @ConfigurationProperties(prefix = "spring.core")
    public DataSource zmc() {
        return new DruidDataSource();
    }

    @Bean
    public SqlManagerFactoryBean sqlManager(DataSource dataSource) {
        SqlManagerFactoryBean sqlManagerFactoryBean = new SqlManagerFactoryBean();
        sqlManagerFactoryBean.setDbStyle(new MySqlStyle());
        BeetlSqlDataSource beetlSqlDataSource = new BeetlSqlDataSource();
        beetlSqlDataSource.setMasterSource(dataSource);
        sqlManagerFactoryBean.setCs(beetlSqlDataSource);
        return sqlManagerFactoryBean;
    }

    @Bean
    public BeetlSqlMutipleSourceCustomize beetlSqlCustomize() {
        return new BeetlSqlMutipleSourceCustomize() {
            @Override
            public void customize(String dataSource, SQLManager sqlManager) {
                sqlManager.setInters(new Interceptor[]{new DebugRewriteInterceptor()});
            }

        };
    }

}
