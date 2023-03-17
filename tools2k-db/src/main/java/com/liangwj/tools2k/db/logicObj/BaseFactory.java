package com.liangwj.tools2k.db.logicObj;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

import com.liangwj.tools2k.beans.exceptions.BaseApiException;
import com.liangwj.tools2k.beans.exceptions.IdNotFoundException;
import com.liangwj.tools2k.db.po.BasePo;
import com.liangwj.tools2k.utils.other.TimeLimitMap;

/**
 * <pre>
 * 分类逻辑对象的工厂类，这个文件在生成时默认不覆盖.PO如果是继承自BasePo，则使用此类
 * </pre>
 * 
 * @author 梁颂声
 */
public abstract class BaseFactory<OBJ extends BasePoObj<PO>, PO extends BasePo, ID extends Serializable>
		extends BaseObjFactory<OBJ, PO, ID> {

	/**
	 * 新建默认对象
	 * 
	 * @return
	 */
	public abstract PO createDefaultPo();

	public TimeLimitMap<ID, OBJ> allMap = new TimeLimitMap<>(3, TimeUnit.SECONDS, 1000);

	@Override
	public OBJ getById(ID id) {
		final OBJ obj = super.getById(id);

		if (obj != null && obj.getPo().isDeleted()) {
			return null;
		}

		return obj;
	}

	/**
	 * 从allmap拿的形式获取信息，推荐在循环查相同id的时候使用
	 * 
	 * @param id
	 * @return
	 */
	public OBJ getByIdOfAllMap(ID id) {
		OBJ res = allMap.get(id);
		if (res == null) {
			res = getById(id);
			allMap.put(id, res);
		}
		return res;
	}

	@Override
	public OBJ getByIdNotNull(ID id) throws IdNotFoundException {

		final OBJ obj = super.getByIdNotNull(id);

		if (obj.getPo().isDeleted()) {
			throw this.createIdNotFoundException(id);
		}

		return obj;
	}

	@Override
	public void delete(OBJ obj) throws BaseApiException {

		// 删除前先检查一下
		obj.checkBeforeDelete();
		obj.getPo().setDeleted(true);
		this.update(obj);
		// 删除后可能需要检查一下
		obj.afterDelete();
	}

	/**
	 * 删除对象前需要去除唯一索引的绑定
	 * 
	 */
	protected abstract void checkBeforeDelete(OBJ obj) throws BaseApiException;

	/**
	 * 这个是危险操作，真的在数据库中删除数据
	 * 
	 * @param obj
	 * @throws BaseApiException
	 */
	public void trueDel(OBJ obj) throws BaseApiException {
		super.delete(obj);
	}

}