package com.liangwj.tools2k.db.logicObj;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import com.liangwj.tools2k.beans.exceptions.BaseApiException;
import com.liangwj.tools2k.beans.exceptions.IdNotFoundException;
import com.liangwj.tools2k.beans.exceptions.InvalidPasswordException;
import com.liangwj.tools2k.beans.exceptions.SimpleApiException;
import com.liangwj.tools2k.beans.form.IPageForm;
import com.liangwj.tools2k.beans.others.IConverter;
import com.liangwj.tools2k.beans.others.PageQueryResult;
import com.liangwj.tools2k.db.base.IObjFactoryDao;
import com.liangwj.tools2k.db.base.WhereBuilder;
import com.liangwj.tools2k.utils.classUtils.ClassUtil;
import com.liangwj.tools2k.utils.other.ConverterUtil;
import com.liangwj.tools2k.utils.other.PasswordEncoder;

/**
 * <pre>
 * 逻辑对象工厂的 基类
 * </pre>
 * 
 * @author rock 2016年8月30日
 * @param <OBJ> 逻辑对象
 * @param <PO>  原始的数据库对象
 * @param <ID>  id的类型
 */
public abstract class BaseObjFactory<OBJ extends BasePoObj<PO>, PO, ID extends Serializable> {

	/**
	 * po到对象的转换器
	 */
	protected IConverter<PO, OBJ> po2Obj = new IConverter<PO, OBJ>() {

		@Override
		public OBJ convert(PO po) {
			return BaseObjFactory.this.convertToObj(po);
		}

	};

	@Autowired
	private ApplicationContext context;

	private final Class<OBJ> objClass;

	@SuppressWarnings("unchecked")
	public BaseObjFactory() {
		this.objClass = (Class<OBJ>) ClassUtil.getGenericType(getClass(), 0);
		Assert.notNull(objClass, "应该可以找到泛型：" + this.getClass().getName());
	}

	/**
	 * 将数据库对象转换成为逻辑对象
	 * 
	 * @param po
	 * @return
	 */
	public OBJ convertToObj(PO po) {
		if (po == null) {
			return null;
		}

		// 对象必须用scope声明为非单例的，然后通过ApplicationContext创建
		final OBJ obj = this.context.getBean(objClass);
		obj.setPo(po);

		return obj;
	}

	/**
	 * 将数据库对象批量转换为逻辑对象
	 *
	 * @param poList 数据库对象列表
	 * @return 逻辑对象列表
	 */
	protected List<OBJ> convertToObjForList(List<PO> poList) {
		return ConverterUtil.convertList(poList, this.po2Obj);
	}

	/**
	 * 创建新的对象，同时保持到数据库
	 * 
	 * @param po po
	 * @return 返回逻辑对象
	 * @throws BaseApiException 逻辑错误
	 */
	@Transactional
	public OBJ create(PO po) throws BaseApiException {

		this.checkBeforeCreate(po);

		this.getDaoForObjFactory().insert(po);
		return this.convertToObj(po);
	}

	/**
	 * 根据主键获得对象
	 * 
	 * @param id
	 * @return
	 */
	public OBJ getById(ID id) {
		if (id == null) {
			return null;
		}

		final PO po = this.getDaoForObjFactory().get(id);
		if (po != null) {
			return this.convertToObj(po);
		} else {
			return null;
		}
	}

	/**
	 * 根据主键获得对象，如果找不到数据就抛错
	 * 
	 * @param id
	 * @return
	 * @throws IdNotFoundException
	 */
	public OBJ getByIdNotNull(ID id) throws IdNotFoundException {
		final OBJ obj = this.getById(id);
		if (obj == null) {
			throw this.createIdNotFoundException(id);
		} else {
			return obj;
		}
	}

	/** 创建 IdNotFoundException 的错误，方便给子类自定义错误信息 */
	protected IdNotFoundException createIdNotFoundException(ID id) {
		final String errorMsg = String.format("找不到 id=%s 的 %s 数据 ", String.valueOf(id), this.objClass.getSimpleName());
		return new IdNotFoundException(errorMsg, id);
	}

	/**
	 * 翻页查询，返回逻辑对象的列表，
	 * 
	 * @param pageForm         翻页数据
	 * @param sqlStartWithForm from开头的sql语句
	 * @param params           sql中的参数
	 * @return
	 */
	public PageQueryResult<OBJ> pageList(IPageForm pageForm, String sqlStartWithForm, Object... params) {
		final PageQueryResult<PO> result = this.getDaoForObjFactory().findInPage(sqlStartWithForm, pageForm, params);
		return result.convert(this.po2Obj);
	}

	public PageQueryResult<OBJ> pageList(IPageForm form, WhereBuilder builder, String orderStr) {
		return this.pageList(form, builder, orderStr, this.po2Obj);
	}

	/**
	 * 进行翻页查询，返回指定的类型的列表，
	 * 
	 * @param pageForm  翻页数据
	 * @param builder   插件条件构造器
	 * @param orderStr  排序字符串
	 * @param converter PO到指定类型的转换器
	 * @return
	 */
	public <TARGET> PageQueryResult<TARGET> pageList(IPageForm pageForm, WhereBuilder builder, String orderStr,
			IConverter<PO, TARGET> converter) {
		final StringBuffer sql = new StringBuffer();

		// 默认是查询所有
		sql.append("from ").append(this.getDaoForObjFactory().getEntityClass().getSimpleName());

		Object[] params = null;

		if (builder != null) {
			// 如果有条件，就拼接 条件
			sql.append(" where").append(builder.getWhereSql());
			if (!builder.getParamsList().isEmpty()) {
				params = builder.getParams();
			}
		}

		sql.append(" order by ");
		if (StringUtils.hasText(orderStr)) {
			// 如果有排序字段，就按排序字段
			sql.append(orderStr);
		} else {
			// 否则就按主键反序
			sql.append(this.getDaoForObjFactory().getIdFieldName()).append(" desc");
		}

		final PageQueryResult<PO> result = this.getDaoForObjFactory().findInPage(sql.toString(), pageForm, params);

		return result.convert(converter);

	}

	/**
	 * 根据查询条件，获取对象列表
	 * 
	 * @param sqlStartWithFrom 以from开头的sql，不能有 select字样
	 * @param params           参数
	 * @return 对象列表
	 */
	public List<OBJ> find(String sqlStartWithFrom, Object... params) {
		final List<PO> poList = this.getDaoForObjFactory().find(sqlStartWithFrom, params);
		return ConverterUtil.convertList(poList, this.po2Obj);
	}

	public List<OBJ> findAll() {
		final List<PO> poList = this.getDaoForObjFactory().findAll();
		return ConverterUtil.convertList(poList, this.po2Obj);
	}

	/**
	 * 根据查询条件，获取一个对象
	 * 
	 * @param sqlStartWithFrom 以from开头的sql，不能有 select字样
	 * @param params           参数
	 * @return 对象列表
	 */
	public OBJ findOne(String sqlStartWithFrom, Object... params) {
		final PO po = this.getDaoForObjFactory().findOne(sqlStartWithFrom, params);
		if (po != null) {
			return this.convertToObj(po);
		}
		return null;
	}

	/**
	 * 更新对象
	 * 
	 * @param obj            要保存的数据
	 * @param cleanListCache
	 */
	@Transactional
	@Deprecated
	public void update(OBJ obj, boolean cleanListCache) throws BaseApiException {
		this.update(obj);
	}

	/**
	 * 更新对象
	 * 
	 * @param obj 要保存的数据
	 */
	@Transactional
	public void update(OBJ obj) throws BaseApiException {
		if (obj == null) {
			return;
		}
		// 更新之前，调用一个方法，方便子类进行检查
		obj.checkBeforeUpdate();

		// 检查后再更新
		this.getDaoForObjFactory().update(obj.getPo());
	}

	/**
	 * 返回基础的domain
	 * 
	 * @return
	 */
	protected abstract IObjFactoryDao<PO, ID> getDaoForObjFactory();

	/**
	 * 这个是危险操作，在数据库中删除数据
	 * 
	 * @param obj 要删除的对象
	 * @throws BaseApiException 逻辑错误
	 */
	@Transactional
	public void delete(OBJ obj) throws BaseApiException {
		if (obj == null) {
			return;
		}

		// 删除前先检查一下
		obj.checkBeforeDelete();

		// 如果没有问题才真的删除
		this.getDaoForObjFactory().delete(obj.getPo());

		// 删除后可能需要检查一下
		obj.afterDelete();

	}

	/**
	 * 该方法在create前执行，子类可覆盖该方法，进行一些额外的逻辑判断
	 * 
	 * @param po 要创建的数据对象
	 * @throws BaseApiException 逻辑错误
	 */
	protected void checkBeforeCreate(PO po) throws BaseApiException {

	}

	/**
	 * 该部分为连续密码错误5次，锁定账号30分钟
	 */
	protected final Map<String, Integer> errorIpMap = new ConcurrentHashMap<String, Integer>();

	protected final Map<String, Long> errorTimeMap = new ConcurrentHashMap<String, Long>();

	public Map<String, Integer> getErrorIpMap() {
		return errorIpMap;
	}

	public Map<String, Long> getErrorTimeMap() {
		return errorTimeMap;
	}

	public void checkLock(String key) throws SimpleApiException {
		Long lockTime = errorTimeMap.get(key);
		if (lockTime == null || lockTime == 0L) {
			return;
		}
		if (new Date().getTime() - lockTime < 60 * 1000 * 30) {
			throw new SimpleApiException("该账号已被锁定30分钟!");
		}
	}

	public void updErrorTimeMap(String key) {
		errorTimeMap.put(key, new Date().getTime());
	}

	public void checkPassword(String account, String orgPassword, String passwordInForm)
			throws InvalidPasswordException, SimpleApiException {
		checkPassword(account, orgPassword, passwordInForm, true);
	}

	public void checkPassword(String account, String orgPassword, String passwordInForm, boolean encodePsw)
			throws InvalidPasswordException, SimpleApiException {
		try {
			PasswordEncoder.checkPassword(passwordInForm, orgPassword, encodePsw);

		} catch (InvalidPasswordException e) {
			errorPassword(account);
		}
		resetErrorIp(account);
	}

	/**
	 * 登录成功后，需要调用清0
	 * 
	 * @param key
	 */
	public void resetErrorIp(String key) {
		getErrorIpMap().put(key, 0); // 重置计算
	}

	public void errorPassword(String account) throws InvalidPasswordException, SimpleApiException {
		checkLock(account); // 如果已触发锁定，则不会进入以下内容
		Integer num = getErrorIpMap().get(account);
		if (num == null) {
			num = 0;
			getErrorIpMap().put(account, num);
		}
		getErrorIpMap().put(account, ++num); // 记录错误次数
		if (num >= 5) { // 连续错误达到5次，进入锁定
			updErrorTimeMap(account); // 开始记录锁定时间
			getErrorIpMap().put(account, 0); // 重置计算
			checkLock(account);
		}
		throw new InvalidPasswordException();
	}

}
