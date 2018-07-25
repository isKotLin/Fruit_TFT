package com.vigorchip.juice.util.db;

import android.content.Context;


import com.vigorchip.db.dao.ActionBeanDao;
import com.vigorchip.db.dao.CategoryBeanDao;
import com.vigorchip.db.dao.FoodHelpBeanDao;
import com.vigorchip.db.dao.FoodBeanDao;
import com.vigorchip.db.entity.ActionBean;
import com.vigorchip.db.entity.CategoryBean;
import com.vigorchip.db.entity.FoodHelpBean;
import com.vigorchip.db.entity.FoodBean;

import java.util.List;

import de.greenrobot.dao.query.QueryBuilder;


/**
 * 完成对某一张表的具体操作
 * Created by LGL on 2016/7/2.
 */
public class DataUtils {

    private DaoManager daoManager;

    //构造方法
    public DataUtils(Context context) {
        daoManager = DaoManager.getInstance();
        daoManager.initManager(context);
    }

    /**
     * 对数据库中Action表的插入操作
     *
     * @param actionBean
     * @return
     */
    public boolean insertAction(ActionBean actionBean) {
        boolean flag = false;
        flag = daoManager.getDaoSession().insert(actionBean) != -1 ? true : false;
        return flag;
    }

    /**
     * 删除所有数据
     */
    public void deleteAllAction() {
        daoManager.getDaoSession().getActionBeanDao().deleteAll();
    }

    /**
     * 批量插入
     *
     * @param actionBeens
     * @return
     */
    public boolean inserMultActions(final List<ActionBean> actionBeens) {
        //标识
        boolean flag = false;
        try {
            //插入操作耗时
            daoManager.getDaoSession().runInTx(new Runnable() {
                @Override
                public void run() {
                    for (ActionBean bean : actionBeens) {
                        daoManager.getDaoSession().insertOrReplace(bean);
                    }
                }
            });
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }


    /**
     * 全部查询
     *
     * @return
     */
    public List<ActionBean> listAllActions() {
        return daoManager.getDaoSession().loadAll(ActionBean.class);
    }


    /**
     * QueryBuilder
     */
    public List<ActionBean> queryBuilderActions(String fid, String person, String step) {
        //查询构建器
        QueryBuilder<ActionBean> queryBuilder = daoManager.getDaoSession().queryBuilder(ActionBean.class);
        return queryBuilder.where(ActionBeanDao.Properties.Fid.eq(fid)).where(ActionBeanDao.Properties.Person.eq(person)).where(ActionBeanDao.Properties.Cur_step.eq(step)).list();
    }

    /**
     * 对数据库中Action表的插入操作
     *
     * @param categoryBean
     * @return
     */
    public boolean insertCategory(CategoryBean categoryBean) {
        boolean flag = false;
        flag = daoManager.getDaoSession().insert(categoryBean) != -1 ? true : false;
        return flag;
    }


    /**
     * 删除所有数据
     */
    public void deleteAllCategory() {
        daoManager.getDaoSession().getCategoryBeanDao().deleteAll();
    }
    /**
     * 批量插入
     *
     * @param categoryBeans
     * @return
     */
    public boolean inserMulCategorys(final List<CategoryBean> categoryBeans) {
        //标识
        boolean flag = false;
        try {
            //插入操作耗时
            daoManager.getDaoSession().runInTx(new Runnable() {
                @Override
                public void run() {
                    for (CategoryBean bean : categoryBeans) {
                        daoManager.getDaoSession().insertOrReplace(bean);
                    }
                }
            });
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }


    /**
     * 全部查询
     *
     * @return
     */
    public List<CategoryBean> listAllCategoryBeans() {
        return daoManager.getDaoSession().loadAll(CategoryBean.class);
    }


    /**
     * QueryBuilder
     */
    public List<CategoryBean> queryBuilderCategoryBeans(String parentId) {
        //查询构建器
        QueryBuilder<CategoryBean> queryBuilder = daoManager.getDaoSession().queryBuilder(CategoryBean.class);
        return queryBuilder.where(CategoryBeanDao.Properties.Parent_id.eq(parentId)).list();
    }

    /**
     * QueryBuilder
     */
    public List<CategoryBean> queryBuilderCategoryByFid(String fid) {
        //查询构建器
        QueryBuilder<CategoryBean> queryBuilder = daoManager.getDaoSession().queryBuilder(CategoryBean.class);
        return queryBuilder.where(CategoryBeanDao.Properties.Fid.eq(fid)).list();
    }

    /**
     * 对数据库中Action表的插入操作
     *
     * @param food
     * @return
     */
    public boolean insertFood(FoodBean food) {
        boolean flag = false;
        flag = daoManager.getDaoSession().insert(food) != -1 ? true : false;
        return flag;
    }

    /**
     * 删除所有数据
     */
    public void deleteAllFoods() {
        daoManager.getDaoSession().getFoodBeanDao().deleteAll();
    }

    /**
     * 批量插入
     *
     * @param foods
     * @return
     */
    public boolean inserMultFoods(final List<FoodBean> foods) {
        //标识
        boolean flag = false;
        try {
            //插入操作耗时
            daoManager.getDaoSession().runInTx(new Runnable() {
                @Override
                public void run() {
                    for (FoodBean bean : foods) {
                        daoManager.getDaoSession().insertOrReplace(bean);
                    }
                }
            });
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }


    /**
     * 全部查询
     *
     * @return
     */
    public List<FoodBean> listAllFoods() {
        return daoManager.getDaoSession().loadAll(FoodBean.class);
    }


    /**
     * QueryBuilder
     */
    public List<FoodBean> queryBuilderFoods(String fid, String person) {
        //查询构建器
        QueryBuilder<FoodBean> queryBuilder = daoManager.getDaoSession().queryBuilder(FoodBean.class);
        return queryBuilder.where(FoodBeanDao.Properties.Fid.eq(fid)).where(FoodBeanDao.Properties.Person.eq(person)).list();
    }

    /**
     * QueryBuilder
     */
    public List<FoodBean> queryBuilderFoodsByFid(String fid) {
        //查询构建器
        QueryBuilder<FoodBean> queryBuilder = daoManager.getDaoSession().queryBuilder(FoodBean.class);
        return queryBuilder.where(FoodBeanDao.Properties.Fid.eq(fid)).list();
    }

    /**
     * 对数据库中Action表的插入操作
     *
     * @param bean
     * @return
     */
    public boolean insertCategoryHelp(FoodHelpBean bean) {
        boolean flag = false;
        flag = daoManager.getDaoSession().insert(bean) != -1 ? true : false;
        return flag;
    }
    /**
     * 删除所有数据
     */
    public void deleteAllFoodHelp() {
        daoManager.getDaoSession().getFoodHelpBeanDao().deleteAll();
    }
    /**
     * 批量插入
     *
     * @param beans
     * @return
     */
    public boolean inserMultFoodHelpBeans(final List<FoodHelpBean> beans) {
        //标识
        boolean flag = false;
        try {
            //插入操作耗时
            daoManager.getDaoSession().runInTx(new Runnable() {
                @Override
                public void run() {
                    for (FoodHelpBean bean : beans) {
                        daoManager.getDaoSession().insertOrReplace(bean);
                    }
                }
            });
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }


    /**
     * 全部查询
     *
     * @return
     */
    public List<FoodHelpBean> listAllFoodHelpBeans() {
        return daoManager.getDaoSession().loadAll(FoodHelpBean.class);
    }


    /**
     * QueryBuilder
     */
    public List<FoodHelpBean> queryBuilderFoodHelpBeansByFid(String fid) {
        //查询构建器
        QueryBuilder<FoodHelpBean> queryBuilder = daoManager.getDaoSession().queryBuilder(FoodHelpBean.class);
        return queryBuilder.where(FoodHelpBeanDao.Properties.Fid.eq(fid)).list();
    }
}
