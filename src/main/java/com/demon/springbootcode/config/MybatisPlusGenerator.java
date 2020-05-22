package com.demon.springbootcode.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.converts.MySqlTypeConvert;
import com.baomidou.mybatisplus.generator.config.converts.OracleTypeConvert;
import com.baomidou.mybatisplus.generator.config.rules.DbColumnType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.VelocityTemplateEngine;

/**
 * @author: Demon
 * @date 2019/7/28 17:31
 * @description: 代码生成器
 */
public class MybatisPlusGenerator {

    private static final String AUTHOR = "demon";

    public static void getAutoGenerator(DbType dbType,String outputDir,String[] include,String driverName,String url,String username,String password,String moduleName,String packageName){
        AutoGenerator generator = new AutoGenerator();
        generator
                .setGlobalConfig(getGlobalConfig(outputDir))
                .setDataSource(getDataSourceConfig(dbType,driverName,url,username,password))
                .setStrategy(getStrategyConfig(include))
                .setPackageInfo(getPackageConfig(moduleName, packageName))
                .setCfg(getInjectionConfig())
                //设置模板引擎
                .setTemplateEngine(new VelocityTemplateEngine())
                .setTemplate(getTemplateConfig())
        ;
        generator.execute();
    }

    /**
     * 全局配置
     */
    private static GlobalConfig getGlobalConfig(String outputDir) {
        GlobalConfig config = new GlobalConfig();
        return config
                // 是否支持AR模式
                .setActiveRecord(true)
                // 作者
                .setAuthor(AUTHOR)
                // 生成路径，是否覆盖
                .setOutputDir(outputDir).setFileOverride(true)
                // 主键策略,数据库自增
                .setIdType(IdType.AUTO)
                // 设置生成的service接口的名字的首字母是否为I(默认Service类前面有一个I)
                // 自定义文件命名，注意 %s 会自动填充表实体属性
                .setXmlName("%sMapper").setMapperName("%sMapper")
                .setServiceName("%sService").setServiceImplName("%sServiceImpl").setControllerName("%sController")
                // 二级缓存mybatis-ehcache，基本ResultMap，基本的列
                .setEnableCache(false).setBaseResultMap(true).setBaseColumnList(true)
                ;
    }


    /**
     * 数据源配置
     */
    private static DataSourceConfig getDataSourceConfig(DbType dbType,String driverName,String url,String username,String password) {
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setDbType(dbType);
        dsc.setDriverName(driverName);
        dsc.setUrl(url);
        dsc.setUsername(username);
        dsc.setPassword(password);
        switch (dbType){
            case MYSQL:
                dsc.setTypeConvert(getMySqlTypeConvert());
                break;
            case ORACLE:
                dsc.setTypeConvert(getOracleTypeConvert());
                break;
            default:
                break;
        }
        return dsc;
    }

    /**
     * 策略配置
     * include：方法内容传值参数，可不传值，
     * 如不传值：则对应数据库全部表，
     * 如传值：则对应填写表
     */
    private static StrategyConfig getStrategyConfig(String[] include) {
        StrategyConfig stConfig = new StrategyConfig();
        return stConfig
                // 全局大写命名
                .setCapitalMode(true)
                // 数据库表映射到实体的命名策略
                .setNaming(NamingStrategy.underline_to_camel)
                .setColumnNaming(NamingStrategy.underline_to_camel)
                .setSuperEntityClass(Model.class)
                .setEntityLombokModel(true)
                .setEntitySerialVersionUID(true)
                .setEntityTableFieldAnnotationEnable(true)
                .setRestControllerStyle(true)
                .setControllerMappingHyphenStyle(true)
                // 需要生成的表 <include> 与 <exclude> 只能配置一项！
                .setInclude(include)
                // 排除生成的表
                // .setExclude("")
                .setEntityBuilderModel(true)
                ;
    }

    /**
     * 包配置
     */
    private static PackageConfig getPackageConfig(String moduleName, String packageName) {
        PackageConfig pkConfig = new PackageConfig();
        return pkConfig
                .setModuleName(moduleName)
                // 设置父包
                .setParent(packageName)
                // 实体类
                .setEntity("entity")
                // 接口映射的xml文件
                .setXml("mapper")
                // dao层
                .setMapper("dao")
                // Service 层
                .setService("service")
                // Controller 层
                .setController("controller")
                ;
    }

    /**
     * 自定义配置
     */
    private static InjectionConfig getInjectionConfig() {
        return new InjectionConfig() {
            @Override
            public void initMap() {

            }
        };
    }

    /**
     * 自定义代码模板
     * 指定自定义模板路径, 位置：/resources/templates/entity.java.ftl(或者是.vm)
     * 注意不要带上.ftl(或者是.vm), 会根据使用的模板引擎自动识别
     * 当前一下的模板信息是从jar包源码的/resources/templates找到填写，
     * 如没有自定义有模板，可直接返回null即可
     */
    private static TemplateConfig getTemplateConfig() {
        return new TemplateConfig()
                .setEntity("templates/entity.java")
                .setEntityKt("templates/entity.kt")
                .setXml("templates/mapper.xml")
                .setMapper("templates/mapper.java")
                .setService("templates/service.java")
                .setServiceImpl("templates/serviceImpl.java")
                ;
    }

    private static MySqlTypeConvert getMySqlTypeConvert(){
        return new MySqlTypeConvert() {
            @Override
            public DbColumnType processTypeConvert(GlobalConfig globalConfig, String fieldType) {
                System.out.println("转换类型：" + fieldType);
                //tinyint转换成byte
                if (fieldType.toLowerCase().contains("tinyint")) {
                    return DbColumnType.BYTE;
                }
                //将数据库中datetime转换成date
                if (fieldType.toLowerCase().contains("datetime")) {
                    return DbColumnType.DATE;
                }
                //将数据库中datetime转换成date
                if (fieldType.toLowerCase().contains("date")) {
                    return DbColumnType.DATE;
                }
                //将数据库中blob转换成byte[]
                if (fieldType.toLowerCase().contains("blob")) {
                    return DbColumnType.BYTE_ARRAY;
                }
                return (DbColumnType) super.processTypeConvert(globalConfig, fieldType);
            }
        };
    }

    private static OracleTypeConvert getOracleTypeConvert(){
        return new OracleTypeConvert() {
            @Override
            public DbColumnType processTypeConvert(GlobalConfig globalConfig, String fieldType) {
                System.out.println("转换类型：" + fieldType);
                //将数据库中datetime转换成date
                //将数据库中datetime转换成date
                if (fieldType.toLowerCase().contains("datetime")) {
                    return DbColumnType.DATE;
                }
                //将数据库中datetime转换成date
                if (fieldType.toLowerCase().contains("date")) {
                    return DbColumnType.DATE;
                }
                return (DbColumnType) super.processTypeConvert(globalConfig, fieldType);
            }
        };
    }
}
