package com.vigorchip.sqlhelper;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;


public class FruitDaoGenerator {


    public static final int version = 1;
    public static final String entityPackageName = "com.vigorchip.db.entity";
    public static final String daoPackageName = "com.vigorchip.db.dao";

    public static final String autoGenerateJavaPath = GreenDaoConstant.DAO_PATH;

    public static void main(String[] args) throws Exception {

        Schema schema = new Schema(version, entityPackageName);
        schema.setDefaultJavaPackageDao(daoPackageName);
        newCategoryTable(schema, "CategoryBean");
        newFoodTable(schema, "FoodBean");
        newActionTable(schema, "ActionBean");
        newCategoryHelpTable(schema, "FoodHelpBean");
        new DaoGenerator().generateAll(schema, autoGenerateJavaPath);
    }

    public static void newCategoryTable(Schema schema, String beanName) {
        Entity entity = schema.addEntity(beanName);
        entity.addIdProperty();
        entity.addStringProperty("fid");
        entity.addStringProperty("parent_id");
        entity.addStringProperty("name");
        entity.addStringProperty("bg_pic");
        entity.addStringProperty("totlestep");
        entity.addStringProperty("p2totletime1");
        entity.addStringProperty("p4totletime1");
        entity.addStringProperty("p6totletime1");
        entity.addStringProperty("p2totletime2");
        entity.addStringProperty("p4totletime2");
        entity.addStringProperty("p6totletime2");
        entity.setJavaDoc("auto greenDao generate javaBean by MonkeyKing");
        entity.setTableName("tb_catagory");
    }

    public static void newFoodTable(Schema schema, String beanName) {
        Entity entity = schema.addEntity(beanName);
        entity.addIdProperty();
        entity.addStringProperty("count");
        entity.addStringProperty("cur_step");
        entity.addStringProperty("fid");
        entity.addStringProperty("name");
        entity.addStringProperty("person");
        entity.addStringProperty("totlestep");
        entity.addStringProperty("unit");
        entity.setJavaDoc("auto greenDao generate javaBean by MonkeyKing");
        entity.setTableName("tb_food");
    }

    public static void newActionTable(Schema schema, String beanName) {
        Entity entity = schema.addEntity(beanName);
        entity.addIdProperty();
        entity.addStringProperty("fid");
        entity.addStringProperty("person");
        entity.addStringProperty("stime");
        entity.addStringProperty("etime");
        entity.addStringProperty("method");
        entity.addStringProperty("totletime");
        entity.addStringProperty("startspeed");
        entity.addStringProperty("endspeed");
        entity.addStringProperty("cur_step");
        entity.addStringProperty("totlestep");
        entity.setJavaDoc("auto greenDao generate javaBean by MonkeyKing");
        entity.setTableName("tb_action");
    }

    public static void newCategoryHelpTable(Schema schema, String beanName) {
        Entity entity = schema.addEntity(beanName);
        entity.addIdProperty();
        entity.addStringProperty("fid");
        entity.addStringProperty("cur_step");
        entity.addStringProperty("explanation");
        entity.setJavaDoc("auto greenDao generate javaBean by MonkeyKing");
        entity.setTableName("tb_foodhelp");
    }
}
