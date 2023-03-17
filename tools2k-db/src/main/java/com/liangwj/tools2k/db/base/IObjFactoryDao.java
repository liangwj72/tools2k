package com.liangwj.tools2k.db.base;

import java.io.Serializable;
import java.util.List;

import com.liangwj.tools2k.beans.form.IPageForm;
import com.liangwj.tools2k.beans.others.PageQueryResult;

/**
 * <pre>
 * 用于给对象工厂基类获取数据的接口
 * </pre>
 * 
 * @author rock 2016年9月18日
 */
public interface IObjFactoryDao<T, K extends Serializable> {

	void delete(T po);

	List<T> find(String sqlStartWithFrom, Object... params);

	T findOne(String sqlStartWithFrom, Object... params);

	List<T> findAll();

	PageQueryResult<T> findInPage(String sqlStartWithForm, IPageForm form, Object... params);

	T get(K id);

	Class<T> getEntityClass();

	String getIdFieldName();

	void insert(T po);

	void update(T po);

}
