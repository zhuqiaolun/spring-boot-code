package com.demon.springbootcode;

import com.baomidou.mybatisplus.annotation.DbType;
import com.demon.springbootcode.config.MybatisGenerator;
import com.demon.springbootcode.config.MybatisPlusGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.util.UUID;

@SpringBootTest
class SpringBootCodeApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    void createMySqlToMybatisPlus() {
        String outputDir = FileSystemView.getFileSystemView().getHomeDirectory().getAbsolutePath()+ File.separator+"myCode"+ File.separator+ UUID.randomUUID().toString();
        //不填写则全部表，生成在桌面(自动弹出文件目录)
        String[] include = new String[]{"sys_user"};
        String driverName = "com.mysql.jdbc.Driver";
        String url = "jdbc:mysql://127.0.0.1:3306/test";
        String username = "root";
        String password = "123456";
        String packageName = "com.demon";
        String moduleName = "springbootcode";
        MybatisPlusGenerator.getAutoGenerator(DbType.MYSQL, outputDir,include,driverName,url,username,password,moduleName,packageName);
    }

    @Test
    void createOracleToMybatisPlus() {
        String outputDir = FileSystemView.getFileSystemView().getHomeDirectory().getAbsolutePath()+ File.separator+"myCode"+ File.separator+ UUID.randomUUID().toString();
        //不填写则全部表，生成在桌面(自动弹出文件目录)
        String[] include = new String[]{"sys_user"};
        String driverName = "oracle.jdbc.OracleDriver";
        String url = "jdbc:oracle:thin:@127.0.0.1:1521:orcl";
        String username = "orcl";
        String password = "123456";
        String packageName = "com.demon";
        String moduleName = "springbootcode";
        MybatisPlusGenerator.getAutoGenerator(DbType.ORACLE, outputDir,include,driverName,url,username,password,moduleName,packageName);
    }


    @Test
    void createMySqlToMybatis() {
        String outputDir = FileSystemView.getFileSystemView().getHomeDirectory().getAbsolutePath()+ File.separator+"myCode"+ File.separator+ UUID.randomUUID().toString();
        //不填写则全部表，生成在桌面(自动弹出文件目录)
        String[] include = new String[]{"sys_user"};
        String driverName = "com.mysql.jdbc.Driver";
        String url = "jdbc:mysql://127.0.0.1:3306/test";
        String username = "root";
        String password = "123456";
        String packageName = "com.demo";
        String moduleName = "springbootcode";
        MybatisGenerator.getAutoGenerator(outputDir,include,driverName,url,username,password,moduleName,packageName);
    }

}
