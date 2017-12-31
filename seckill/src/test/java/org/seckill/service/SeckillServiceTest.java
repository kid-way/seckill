package org.seckill.service;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExecution;
import org.seckill.entity.Seckill;
import org.seckill.exception.RepeatKillException;
import org.seckill.exception.SeckillCloseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({
	"classpath:spring/spring-dao.xml",
	"classpath:spring/spring-service.xml"})
public class SeckillServiceTest {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	private SeckillService seckillService;
	@Before
	public void setUp() throws Exception {
	}

	@Test
	//list=[Seckill [seckillId=1000, name=1000元秒杀iPhone6, number=99, startTime=Tue Dec 12 20:24:04 CST 2017, endTime=Wed Dec 13 00:00:00 CST 2017, createTime=Sat Dec 09 22:21:04 CST 2017], 
	//Seckill [seckillId=1001, name=500元秒杀iPad2, number=198, startTime=Tue Dec 12 20:31:54 CST 2017, endTime=Wed Dec 13 00:00:00 CST 2017, createTime=Sat Dec 09 22:21:04 CST 2017], 
	//Seckill [seckillId=1002, name=200元秒杀小米6, number=500, startTime=Tue Dec 12 00:00:00 CST 2017, endTime=Wed Dec 13 00:00:00 CST 2017, createTime=Sat Dec 09 22:21:04 CST 2017], 
	//Seckill [seckillId=1003, name=100元秒杀小米note, number=300, startTime=Tue Dec 12 00:00:00 CST 2017, endTime=Wed Dec 13 00:00:00 CST 2017, createTime=Sat Dec 09 22:21:04 CST 2017]]
	public void testGetSeckillList() {
		List<Seckill> list = seckillService.getSeckillList();
		logger.info("list={}",list);
	}

	@Test
	public void testGetById() {
		Long seckillId = 1000L;
		Seckill seckill = seckillService.getById(seckillId);
		logger.info("seckill={}", seckill);
	}

	@Test
	public void testExportSeckillUrl() {
		Long seckillId = 1000L;
		Exposer exposer = seckillService.exportSeckillUrl(seckillId);
		logger.info("exposer={}", exposer);
	}

	@Test
	public void testExecuteSeckill() {
		Exposer exposer = seckillService.exportSeckillUrl(1000l);
		if(exposer.isExposed()){
			try{
				SeckillExecution seckillExecution = seckillService.executeSeckill(1000l, 34562718078L,"919b5abf5bd918d49c903f8cb2535644");
				logger.info("seckillExecution={}", seckillExecution);
			}catch(RepeatKillException e){
				logger.error(e.getMessage());
			}catch(SeckillCloseException e){
				logger.error(e.getMessage());
			}
		}else{
			logger.warn("exposer={}",exposer);
		}
		
	}
	
	@Test
	public void execuXtteSeckillProcedure(){
		long seckillId = 1000;
		long phone = 13543711444l;
		Exposer exposer = seckillService.exportSeckillUrl(seckillId);
		if(exposer.isExposed()){
			String md5 = exposer.getMd5();
			SeckillExecution execution = seckillService.executeSeckillProcedure(seckillId, phone, md5);
			logger.info(execution.getStateInfo());
		}
	}
}
