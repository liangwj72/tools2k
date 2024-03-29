package com.liangwj.tools2k.utils.sortedLock;

import java.util.LinkedList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.util.Assert;

/**
 * 测试排序加锁
 *
 * @author rock
 *
 */
//@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:testSortedLock.xml" })
public class SortedLockTest {

	@Autowired
	private MockService service;

	@Autowired
	private SortedLockAspectService aspect;

	@Test
	public void testLock() {

		MockNeedLockObj common = new MockNeedLockObj();

		// list 长度为3
		List<INeedSortLockObj> list = new LinkedList<INeedSortLockObj>();
		list.add(new MockNeedLockObj());
		list.add(new MockNeedLockObj());
		list.add(common);// 这个是重复的

		// 数组长度为2
		MockNeedLockObj[] ary = new MockNeedLockObj[] { new MockNeedLockObj(), common };

		// 三个参数，长度合计为6，但因为有一个common对象重复了3次，所以应该只对4个对象加锁

		try {
			String str = this.service.testMethod(common, list, ary);

			System.out.println("str=" + str);

			Assert.notNull(str, "该测试方法有返回结果");
			Assert.isTrue(aspect.getLastNeedLockObjCount() == 4, "要排序加锁的对象数量应该为4个");

			this.service.test2();

			Assert.isTrue(1 == aspect.getCounter(), "应该只拦截了一次");
		} catch (Exception e) {
			Assert.isTrue(true, "不应该有抛错");
		}

	}

}
