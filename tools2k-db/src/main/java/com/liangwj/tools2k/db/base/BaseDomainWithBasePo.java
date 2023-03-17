package com.liangwj.tools2k.db.base;

import java.io.Serializable;
import java.util.List;

import com.liangwj.tools2k.beans.form.IPageForm;
import com.liangwj.tools2k.db.exceptions.DaoException;
import com.liangwj.tools2k.db.po.BasePo;

/**
 * <pre>
 * 使用了多级Cache技术的Domain基类，类似以前的service,但都是对某个表操作的.PO如果是继承自BasePo，则使用此类.存在自动新增deleted标签过滤
 * </pre>
 * 
 * @author 江凯文
 * @param <T>
 */
public abstract class BaseDomainWithBasePo<PO extends BasePo, ID extends Serializable>
		extends BaseDomainWithCache<PO, ID> {

	/**
	 * 通 from开头的sql语句搜索，自动增加deleted的过滤;自己定义时请遵守deleted设置为最末尾参数!
	 * 
	 * @param sqlStartWithFrom
	 * @param pageForm
	 * @param params
	 * @return
	 * @throws DaoException
	 */
	@Override
	protected List<ID> findId(String sqlStartWithFrom, String idField, IPageForm pageForm, Object... params)
			throws DaoException {
		int lastWhe = sqlStartWithFrom.lastIndexOf("where");
		int lastDel=sqlStartWithFrom.lastIndexOf("deleted");
		int field = sqlStartWithFrom.indexOf(entityClass.getSimpleName()) + entityClass.getSimpleName().length();
		if (lastDel == -1 || lastDel < field) {// 判断是否进行了deleted的过滤,防止有表名就是包含deleted
			// 没有则默认为过滤存在删除标记的sql
			if (lastWhe < field) {// 不存在参数，则还要新增where。截取为表名后的下标
				sqlStartWithFrom = String.format("%s where deleted =0 %s", sqlStartWithFrom.substring(0, field),
						sqlStartWithFrom.substring(field));
			} else {// 截取为where单词后的下标
				sqlStartWithFrom = String.format("%s deleted =0 AND %s", sqlStartWithFrom.substring(0, lastWhe + 5),
						sqlStartWithFrom.substring(lastWhe + 5));
			}

		}

		return super.findId(sqlStartWithFrom, idField, pageForm, params);
	}


}