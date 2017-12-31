package org.seckill.dao;


import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.entity.SuccessKilled;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class SuccessKilledDaoTest {
	@Resource
	private SuccessKilledDao successKilledDao;
	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testInsertSuccessKilled() {
		int i = successKilledDao.insertSuccessKilled((long)1000,(long)11111222);
		System.out.println(i);
	}

	@Test
	public void testQueryByIdWithSeckill() {
		SuccessKilled successKilled = successKilledDao.queryByIdWithSeckill((long)1000,(long)11111222);
		System.out.println(successKilled);
	}
	//SuccessKilled [seckillId=1000, userPhone=11111222, state=0, createTime=Tue Dec 12 20:59:45 CST 2017, 
	//seckill=Seckill [seckillId=1000, name=1000元秒杀iPhone6, number=99, startTime=Tue Dec 12 20:24:04 CST 2017, endTime=Wed Dec 13 00:00:00 CST 2017, createTime=Sat Dec 09 22:21:04 CST 2017]]
}
