package com.liangwj.tools2k.db.base;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.liangwj.tools2k.beans.exceptions.IdNotFoundException;
import com.liangwj.tools2k.beans.form.IPageForm;
import com.liangwj.tools2k.beans.others.PageQueryResult;
import com.liangwj.tools2k.db.exceptions.DaoException;
import com.liangwj.tools2k.utils.classUtils.ClassUtil;
import com.liangwj.tools2k.utils.classUtils.MethodUtil;

/**
 * <pre>
 * 使用了多级Cache技术的Domain基类，类似以前的service,但都是对某个表操作的
 * </pre>
 * 
 * @author rock 2016年8月18日
 */
public abstract class BaseDomainWithCache<PO, ID extends Serializable> implements IObjFactoryDao<PO, ID> {

	@Autowired
	private CacheManager cacheManager;

	private Cache cacheOfPo;

	private SafeSave<PO> safeSave;

	protected final String idFieldName;

	/** 从泛型中找到的po的类型 */
	protected final Class<PO> entityClass;

	/** 从泛型中找到的po的类型 */
	protected final Class<ID> idClass;

	private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(BaseDomainWithCache.class);

	/** 可获得id值的方法 */
	protected final Method methodOfGetId;

	public BaseDomainWithCache() {
		this(null);
	}

	@SuppressWarnings("unchecked")
	public BaseDomainWithCache(Class<ID> idClass) {
		this.entityClass = (Class<PO>) ClassUtil.getGenericType(this.getClass(), 0);
		this.idClass = this.buildIdClass(idClass);
		this.methodOfGetId = MethodUtil.getIdMethod(entityClass);

		Assert.notNull(this.entityClass, "应该可找到PO的Class");
		Assert.notNull(this.idClass, "应该可找到ID的Class");
		Assert.notNull(this.methodOfGetId, "应该可找到getId的Method");

		this.safeSave = new SafeSave<>(this.entityClass);
		this.idFieldName = this.methodOfGetId.getName().substring(3).toLowerCase();
	}

	@SuppressWarnings("unchecked")
	private Class<ID> buildIdClass(Class<ID> idClass) {
		if (idClass != null) {
			return idClass;
		} else {
			return (Class<ID>) ClassUtil.getGenericType(this.getClass(), 1);
		}
	}

	/**
	 * 删除po
	 * 
	 * @param po
	 * @throws DaoException
	 */
	@Override
	@Transactional
	public void delete(PO po) throws DaoException {
		Assert.notNull(po, "po 不能为空");

		this.getCommonDao().delete(po);

		// 从cache中删除对象
		this.poCacheDelete(po);
	}

	/**
	 * 获得所有
	 * 
	 * @return
	 * @throws DaoException
	 */
	@Override
	public List<PO> findAll() throws DaoException {
		final String sql = String.format("from %s order by %s desc", this.entityClass.getSimpleName(),
				this.idFieldName);
		return this.findPo(sql, null);
	}

	/**
	 * 可根据输入的vo的类型，执行分页查询
	 * 
	 * @param sqlStartWithFrom 要执行的sql
	 * @param pageForm         分页表单
	 * @param params           参数数组
	 * @return
	 * @throws DaoException
	 */
	@Override
	public PageQueryResult<PO> findInPage(String sqlStartWithFrom, IPageForm pageForm, Object... params)
			throws DaoException {

		final List<PO> items = this.findPo(sqlStartWithFrom, this.idFieldName, pageForm, params);

		String countHsql = "select count(*) " + sqlStartWithFrom;

		final int oraderInt = countHsql.indexOf("order by");
		if (oraderInt != -1) {
			countHsql = countHsql.substring(0, oraderInt);
		}

		final int total = this.getCommonDao().getCount(countHsql, params);
		return new PageQueryResult<>(total, items, pageForm);
	}

	/**
	 * 可根据输入的vo的类型，执行分页查询
	 * 
	 * @param sqlStartWithFrom 要执行的sql
	 * @param idField          id字段, 例如a.id
	 * @param pageForm         分页表单
	 * @param params           参数数组
	 * @return
	 * @throws DaoException
	 */
	public PageQueryResult<PO> findInPage(String sqlStartWithFrom, String idField, IPageForm pageForm, Object... params)
			throws DaoException {
		Assert.notNull(pageForm, "分页表单pageForm不能为空");

		final List<PO> items = this.findPo(sqlStartWithFrom, idField, pageForm, params);

		final String countHsql = "select count(*) " + sqlStartWithFrom;
		final int total = this.getCommonDao().getCount(countHsql, params);
		return new PageQueryResult<>(total, items, pageForm);
	}

	/**
	 * 根据条件，寻找一个对象
	 * 
	 * @param sqlStartWithFrom
	 * @param params
	 * @return
	 * @throws DaoException
	 */
	@Override
	public PO findOne(String sqlStartWithFrom, Object... params) throws DaoException {
		final List<PO> list = this.findPo(sqlStartWithFrom, idFieldName, IPageForm.ONE, params);
		if (list != null && list.size() > 0) {
			return list.get(0);
		} else {
			return null;
		}

	}

	@Override
	public List<PO> find(String sqlStartWithFrom, Object... params) {
		final List<PO> list = this.findPo(sqlStartWithFrom, idFieldName, null, params);
		return list;
	}

	/**
	 * 通 from开头的sql语句搜索
	 * 
	 * @param sqlStartWithFrom
	 * @param pageForm
	 * @param params
	 * @return
	 * @throws DaoException
	 */
	public List<PO> findPo(String sqlStartWithFrom, IPageForm pageForm, Object... params) throws DaoException {
		final List<ID> list = this.findId(sqlStartWithFrom, this.idFieldName, pageForm, params);
		return this.idListToPoList(list);
	}

	/**
	 * 通 from开头的sql语句搜索
	 * 
	 * @param sqlStartWithFrom
	 * @param idField
	 * @param pageForm
	 * @param params
	 * @return
	 * @throws DaoException
	 */
	public List<PO> findPo(String sqlStartWithFrom, String idField, IPageForm pageForm, Object... params)
			throws DaoException {
		final List<ID> list = this.findId(sqlStartWithFrom, idField, pageForm, params);
		return this.idListToPoList(list);
	}

	/**
	 * 获得po
	 * 
	 * @param id
	 * @return
	 * @throws DaoException
	 */
	@Override
	public PO get(ID id) throws DaoException {
		Assert.notNull(id, "id不能为空");

		PO po = this.poCachePoGet(id);
		if (po == null) {
			log.debug("重数据库 {} 中获取数据  id={}", this.entityClass.getSimpleName(), id);
			po = this.getCommonDao().get(this.entityClass, id);
			if (po != null) {
				this.poCachePut(po);
			}
		}
		return po;
	}

	@Override
	public Class<PO> getEntityClass() {
		return entityClass;
	}

	/**
	 * insert到数据库
	 * 
	 * @param po
	 * @throws DaoException
	 */
	@Override
	@Transactional
	public void insert(PO po) throws DaoException {
		Assert.notNull(po, "po 不能为空");

		this.safeSave.process(po);

		this.getCommonDao().insert(po);

		// 保存时，将对象放到cache中
		this.poCachePut(po);
	}

	/**
	 * update记录
	 * 
	 * @param po
	 * @throws DaoException
	 */
	@Override
	@Transactional
	public void update(PO po) throws DaoException {
		Assert.notNull(po, "po 不能为空");

		this.safeSave.process(po);

		this.getCommonDao().update(po);

		// 保存时，将对象放到cache中
		this.poCachePut(po);
	}

	private List<PO> findPoInIds(Collection<ID> idSet) throws DaoException {
		if (idSet.isEmpty()) {
			return new LinkedList<>();
		} else {
			final String hsql = String.format("from %s where %s in (?1)", this.entityClass.getSimpleName(),
					this.idFieldName);
			return this.getCommonDao().find(hsql, null, this.entityClass, idSet);
		}

	}

	/**
	 * 根据id获得cache的key
	 * 
	 * @param id
	 * @return key
	 */
	private String getCacheKeyForID(ID id) {
		final String key = String.format("%s:%s", this.cacheOfPo.getName(), String.valueOf(id));
		return key;
	}

	/**
	 * 根据po获得cache的key
	 * 
	 * @param po
	 * @return
	 * @throws DaoException
	 */
	private String getCacheKeyForPO(PO po) {
		final ID id = this.getIdFromPO(po);
		return getCacheKeyForID(id);
	}

	/**
	 * 从po对象中获取id的值
	 * 
	 * @param po
	 * @return
	 * @throws DaoException
	 */
	@SuppressWarnings("unchecked")
	private ID getIdFromPO(PO po) {
		try {
			final ID id = (ID) this.methodOfGetId.invoke(po);
			Assert.notNull(id, "id 不能为空");
			return id;

		} catch (final Throwable e) {
			throw new RuntimeException("无法获得 ID " + po, e);
		}
	}

	/**
	 * 是否需要cache
	 * 
	 * @return
	 */
	private boolean isNeedCache() {
		return !this.isCacheDisable() && this.cacheManager != null;
	}

	private void poCacheDelete(PO po) {
		if (!this.isNeedCache()) {
			return;
		}

		if (po == null) {
			return;
		}

		final String key = this.getCacheKeyForPO(po);

		this.cacheOfPo.evict(key);

	}

	/**
	 * 从cache中获得po
	 * 
	 * @param id
	 * @return
	 */
	private PO poCachePoGet(ID id) {
		if (!this.isNeedCache() || id == null) {
			return null;
		}

		final String key = this.getCacheKeyForID(id);
		final PO po = this.cacheOfPo.get(key, this.entityClass);
		return po;
	}

	/**
	 * 将对象放到cache中
	 * 
	 * @param po
	 * @throws DaoException
	 */

	private void poCachePut(PO po) {
		if (!this.isNeedCache() || po == null) {
			return;
		}

		final String key = this.getCacheKeyForPO(po);
		this.cacheOfPo.put(key, po);

		if (log.isDebugEnabled()) {
			log.debug("将对象{} 放到 Cache:{} , key={}", this.entityClass.getSimpleName(), this.cacheOfPo.getName(), key);
		}
	}

	/**
	 * 注入完成后，初始cache
	 */
	@PostConstruct
	protected void checkOnInit() {
		if (log.isDebugEnabled()) {
			log.debug("初始化 {} PO={},ID={} Cache={}", this.getClass().getSimpleName(), this.entityClass.getSimpleName(),
					this.idClass.getSimpleName(), this.isNeedCache());
		}

		if (this.isNeedCache()) {
			final String nameForPo = String.format("dbCache:%s:po", this.entityClass.getSimpleName());
			this.cacheOfPo = this.cacheManager.getCache(nameForPo);
		}
	}

	protected List<ID> findId(String sqlStartWithFrom, String idField, IPageForm pageForm, Object... params)
			throws DaoException {
		Assert.notNull(sqlStartWithFrom, "sqlStartWithFrom 不能为空");
		Assert.notNull(idField, "idField 不能为空");
		Assert.isTrue(sqlStartWithFrom.startsWith("from"), "sql 必须以from开头");

		// 构建完整的sql
		final String hsql = String.format("select %s %s", idField, sqlStartWithFrom);

		return this.getCommonDao().find(hsql, pageForm, this.idClass, params);
	}

	protected abstract ICommonDao getCommonDao();

	/**
	 * 将id的列表转化成为po的列表
	 * 
	 * @param idList
	 * @return
	 * @throws DaoException
	 */
	public List<PO> idListToPoList(List<ID> idList) throws DaoException {
		if (idList == null) {
			return null;
		}

		List<PO> list;

		if (this.isNeedCache()) {
			final Map<ID, PO> map = new HashMap<>();

			// 如果有cache，就先检查哪些id不在cache中
			final Set<ID> notInCacheId = new HashSet<>();
			for (final ID id : idList) {
				final PO po = this.poCachePoGet(id);
				if (po == null) {
					// 如果cache没有，就将ID记录下来，准备批量查询
					notInCacheId.add(id);
				} else {
					// 如果 cache中有，就放到map中
					map.put(id, po);
				}
			}

			// 将那些不再cache中的数据进行批量查询
			if (!notInCacheId.isEmpty()) {
				if (log.isDebugEnabled()) {
					log.debug("查询 {} 时， 需要批量加载数据到cache, ids={}", this.entityClass.getSimpleName(), notInCacheId);
				}

				// 如果存在id尚未读取到缓存，就批量读取
				final List<PO> poListNotInCache = this.findPoInIds(notInCacheId);
				for (final PO po : poListNotInCache) {
					// 将po放到cache中
					this.poCachePut(po);

					// 将po放到map中
					final ID id = this.getIdFromPO(po);
					map.put(id, po);
				}
			}

			// 将map中的数据，重新组成list，并返回
			list = new LinkedList<>();
			for (final ID id : idList) {
				final PO po = map.get(id);
				if (po != null) {
					list.add(map.get(id));
				}
			}

			// 因为有list时，一定有po，所以没必须做批量查询了

		} else {
			// 如果cache被屏蔽了，就直接返回批量查询的结果
			list = this.findPoInIds(idList);
		}

		return list;
	}

	/**
	 * 是否禁止cache
	 * 
	 * @return
	 */
	protected boolean isCacheDisable() {
		return false;
	}

	/** 清空所有的cache，用于在批量删除以后 */
	public void resetAllCache() {
		if (this.cacheOfPo != null) {
			this.cacheOfPo.clear();
		}
	}

	@Override
	public String getIdFieldName() {
		return idFieldName;
	}

	/**
	 * 根据主键获得对象，如果找不到数据就抛错
	 * 
	 * @param id
	 * @return
	 * @throws IdNotFoundException
	 */
	public PO getByIdNotNull(ID id) throws IdNotFoundException {
		final PO obj = this.get(id);
		if (obj == null) {
			throw this.createIdNotFoundException(id);
		} else {
			return obj;
		}
	}

	/** 创建 IdNotFoundException 的错误，方便给子类自定义错误信息 */
	protected IdNotFoundException createIdNotFoundException(ID id) {
		final String errorMsg = String.format("找不到 id=%s 的 %s 数据 ", String.valueOf(id), this.entityClass.getSimpleName());
		return new IdNotFoundException(errorMsg, id);
	}

}
