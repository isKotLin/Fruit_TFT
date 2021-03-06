package com.vigorchip.db.dao;

import android.database.sqlite.SQLiteDatabase;

import java.util.Map;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.AbstractDaoSession;
import de.greenrobot.dao.identityscope.IdentityScopeType;
import de.greenrobot.dao.internal.DaoConfig;

import com.vigorchip.db.entity.CategoryBean;
import com.vigorchip.db.entity.FoodBean;
import com.vigorchip.db.entity.ActionBean;
import com.vigorchip.db.entity.FoodHelpBean;

import com.vigorchip.db.dao.CategoryBeanDao;
import com.vigorchip.db.dao.FoodBeanDao;
import com.vigorchip.db.dao.ActionBeanDao;
import com.vigorchip.db.dao.FoodHelpBeanDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see de.greenrobot.dao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig categoryBeanDaoConfig;
    private final DaoConfig foodBeanDaoConfig;
    private final DaoConfig actionBeanDaoConfig;
    private final DaoConfig foodHelpBeanDaoConfig;

    private final CategoryBeanDao categoryBeanDao;
    private final FoodBeanDao foodBeanDao;
    private final ActionBeanDao actionBeanDao;
    private final FoodHelpBeanDao foodHelpBeanDao;

    public DaoSession(SQLiteDatabase db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        categoryBeanDaoConfig = daoConfigMap.get(CategoryBeanDao.class).clone();
        categoryBeanDaoConfig.initIdentityScope(type);

        foodBeanDaoConfig = daoConfigMap.get(FoodBeanDao.class).clone();
        foodBeanDaoConfig.initIdentityScope(type);

        actionBeanDaoConfig = daoConfigMap.get(ActionBeanDao.class).clone();
        actionBeanDaoConfig.initIdentityScope(type);

        foodHelpBeanDaoConfig = daoConfigMap.get(FoodHelpBeanDao.class).clone();
        foodHelpBeanDaoConfig.initIdentityScope(type);

        categoryBeanDao = new CategoryBeanDao(categoryBeanDaoConfig, this);
        foodBeanDao = new FoodBeanDao(foodBeanDaoConfig, this);
        actionBeanDao = new ActionBeanDao(actionBeanDaoConfig, this);
        foodHelpBeanDao = new FoodHelpBeanDao(foodHelpBeanDaoConfig, this);

        registerDao(CategoryBean.class, categoryBeanDao);
        registerDao(FoodBean.class, foodBeanDao);
        registerDao(ActionBean.class, actionBeanDao);
        registerDao(FoodHelpBean.class, foodHelpBeanDao);
    }
    
    public void clear() {
        categoryBeanDaoConfig.getIdentityScope().clear();
        foodBeanDaoConfig.getIdentityScope().clear();
        actionBeanDaoConfig.getIdentityScope().clear();
        foodHelpBeanDaoConfig.getIdentityScope().clear();
    }

    public CategoryBeanDao getCategoryBeanDao() {
        return categoryBeanDao;
    }

    public FoodBeanDao getFoodBeanDao() {
        return foodBeanDao;
    }

    public ActionBeanDao getActionBeanDao() {
        return actionBeanDao;
    }

    public FoodHelpBeanDao getFoodHelpBeanDao() {
        return foodHelpBeanDao;
    }

}
