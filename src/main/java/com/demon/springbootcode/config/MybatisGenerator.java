package com.demon.springbootcode.config;

import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.*;
import org.mybatis.generator.exception.InvalidConfigurationException;
import org.mybatis.generator.internal.DefaultShellCallback;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: Demon
 * @date 2019/12/5 11:35
 * @description:
 */
public class MybatisGenerator {

    public static void getAutoGenerator(String outputDir,String[] include,String driverName,String url,String username,String password,String moduleName,String packageName){
        File outputFile = new File(outputDir);
        boolean flag = true;
        //配置xml配置项
        List<String> warnings = new ArrayList<>();
        try {
            if(!outputFile.exists()){
                flag = outputFile.mkdir();
            }
            Configuration configuration = new Configuration();
            Context context = new Context(ModelType.CONDITIONAL);
            context.setTargetRuntime("MyBatis3");
            context.setId("defaultContext");
            //生成的Java文件的编码
            context.addProperty("javaFileEncoding","utf-8");
            context.addProperty("beginningDelimiter","`");
            context.addProperty("endingDelimiter","`");
            //格式化java代码
            context.addProperty("javaFormatter","org.mybatis.generator.api.dom.DefaultJavaFormatter");
            //格式化xml代码
            context.addProperty("xmlFormatter","org.mybatis.generator.api.dom.DefaultXmlFormatter");
            //格式化信息
            PluginConfiguration pluginConfiguration = new PluginConfiguration();
            pluginConfiguration.setConfigurationType("org.mybatis.generator.plugins.SerializablePlugin");
            pluginConfiguration.setConfigurationType("org.mybatis.generator.plugins.ToStringPlugin");
            pluginConfiguration.setConfigurationType("org.mybatis.generator.plugins.UnmergeableXmlMappersPlugin");
            context.addPluginConfiguration(pluginConfiguration);

            //设置是否去除生成注释
            CommentGeneratorConfiguration commentGeneratorConfiguration = new CommentGeneratorConfiguration();
            commentGeneratorConfiguration.addProperty("suppressAllComments","true");
            commentGeneratorConfiguration.addProperty("suppressDate","true");
            context.setCommentGeneratorConfiguration(commentGeneratorConfiguration);

            //数据库
            getJdbcConnectionConfiguration(context,driverName,url,username,password);
            //数据库 - 表
            getTableConfiguration(context,include,driverName,url,username,password);
            //数据库 - 实体类
            getJavaModelGeneratorConfiguration(context,outputDir,moduleName,packageName);
            // dao
            getJavaClientGeneratorConfiguration(context,outputDir,moduleName,packageName);
            // mapper
            getSqlMapGeneratorConfiguration(context,outputDir,moduleName);
            //添加context
            configuration.addContext(context);
            DefaultShellCallback callback = new DefaultShellCallback(true);
            MyBatisGenerator myBatisGenerator = new MyBatisGenerator(configuration,callback, warnings);
            myBatisGenerator.generate(null);
            System.out.println("生成Mybatis配置成功！");
        } catch (InvalidConfigurationException | SQLException | IOException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            if(flag){
                try {
                    Runtime.getRuntime().exec("cmd /c start " + outputFile);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 数据库 - 连接
     */
    private static void getJdbcConnectionConfiguration(Context context,String driverName,String url,String username,String password){
        JDBCConnectionConfiguration jdbcConnectionConfiguration = new JDBCConnectionConfiguration();
        jdbcConnectionConfiguration.setDriverClass(driverName);
        jdbcConnectionConfiguration.setConnectionURL(url);
        jdbcConnectionConfiguration.setUserId(username);
        jdbcConnectionConfiguration.setPassword(password);
        context.setJdbcConnectionConfiguration(jdbcConnectionConfiguration);
    }

    /**
     * 数据库 - 表
     */
    private static void getTableConfiguration(Context context,String[] tableNames,String driverName,String url,String username,String password){
        if(tableNames.length == 0){
            List<String> list= getTableName(driverName,url,username,password);
            tableNames = list.toArray(new String[0]);
        }
        for (String tableName:tableNames){
            TableConfiguration tableConfiguration = new TableConfiguration(context);
            tableConfiguration.setTableName(tableName);

            tableConfiguration.setCountByExampleStatementEnabled(false);
            tableConfiguration.setDeleteByExampleStatementEnabled(false);
            tableConfiguration.setUpdateByExampleStatementEnabled(false);
            tableConfiguration.setSelectByExampleStatementEnabled(false);

            tableConfiguration.setInsertStatementEnabled(true);
            tableConfiguration.setDeleteByPrimaryKeyStatementEnabled(true);
            tableConfiguration.setUpdateByPrimaryKeyStatementEnabled(true);
            tableConfiguration.setSelectByPrimaryKeyStatementEnabled(true);

            tableConfiguration.addProperty("modelOnly","false");
            tableConfiguration.addProperty("useActualColumnNames","false");
            context.addTableConfiguration(tableConfiguration);
        }
    }

    /**
     *  实体类
     */
    private static void getJavaModelGeneratorConfiguration(Context context,String outputDir,String moduleName,String packageName){
        JavaModelGeneratorConfiguration modelGeneratorConfiguration = new JavaModelGeneratorConfiguration();
      /*  modelGeneratorConfiguration.setTargetProject("src/main/java");*/
        modelGeneratorConfiguration.setTargetProject(outputDir);
        modelGeneratorConfiguration.setTargetPackage(packageName+"."+moduleName+".entity");
        modelGeneratorConfiguration.addProperty("enableSubPackages","true");
        modelGeneratorConfiguration.addProperty("trimStrings","true");
        context.setJavaModelGeneratorConfiguration(modelGeneratorConfiguration);
    }

    /**
     *  dao
     */
    private static void getJavaClientGeneratorConfiguration(Context context,String outputDir,String moduleName,String packageName){
        JavaClientGeneratorConfiguration javaClientGeneratorConfiguration = new JavaClientGeneratorConfiguration();
        javaClientGeneratorConfiguration.setConfigurationType("XMLMAPPER");
        /*javaClientGeneratorConfiguration.setTargetProject("src/main/java");*/
        javaClientGeneratorConfiguration.setTargetProject(outputDir);
        javaClientGeneratorConfiguration.setTargetPackage(packageName+"."+moduleName+".dao");
        javaClientGeneratorConfiguration.addProperty("enableSubPackages","true");
        context.setJavaClientGeneratorConfiguration(javaClientGeneratorConfiguration);
    }

    /**
     *  mapper
     */
    private static void getSqlMapGeneratorConfiguration(Context context,String outputDir,String moduleName){
        SqlMapGeneratorConfiguration sqlMapGeneratorConfiguration = new SqlMapGeneratorConfiguration();
       /* sqlMapGeneratorConfiguration.setTargetProject("src/main/resources");*/
        sqlMapGeneratorConfiguration.setTargetProject(outputDir);
        sqlMapGeneratorConfiguration.setTargetPackage("mybatis.mapper");
        sqlMapGeneratorConfiguration.addProperty("enableSubPackages","true");
        context.setSqlMapGeneratorConfiguration(sqlMapGeneratorConfiguration);
    }


    /**
     * 获取所有表名
     * @return 返回
     */
    private static  List<String> getTableName(String driverName,String url,String username,String password){
        List<String> list = new ArrayList<>();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            Class.forName(driverName).newInstance();
            connection = java.sql.DriverManager.getConnection(url, username, password);
            String sql=" SELECT TABLE_NAME FROM information_schema. TABLES WHERE TABLE_SCHEMA = (SELECT DATABASE())";
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String tableName = resultSet.getString("TABLE_NAME");
                list.add(tableName);
            }
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return list;
    }

}
