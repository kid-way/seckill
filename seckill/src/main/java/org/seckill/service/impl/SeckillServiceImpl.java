package org.seckill.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.seckill.dao.SeckillDao;
import org.seckill.dao.SuccessKilledDao;
import org.seckill.dao.cache.RedisDao;
import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExecution;
import org.seckill.entity.Seckill;
import org.seckill.entity.SuccessKilled;
import org.seckill.enums.SeckillStatEnum;
import org.seckill.exception.RepeatKillException;
import org.seckill.exception.SeckillCloseException;
import org.seckill.exception.SeckillException;
import org.seckill.service.SeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

@Service
public class SeckillServiceImpl implements SeckillService {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private SeckillDao seckillDao;
	
	@Autowired
	private RedisDao redisDao;
	
	@Autowired
	private SuccessKilledDao successKilledDao;
	
	private final String slat = "jdtqouwh$67&Tgjdhfa";
	@Override
	public List<Seckill> getSeckillList() {
		
		return seckillDao.queryAll(0, 4);
	}

	@Override
	public Seckill getById(long seckillId) {
		
		return seckillDao.queryById(seckillId);
	}

	@Override
	public Exposer exportSeckillUrl(long seckillId) {
		Seckill	seckill = redisDao.getSeckill(seckillId);
		if(seckill == null){
			seckill = seckillDao.queryById(seckillId);
			if(seckill == null){
				return new Exposer(false,seckillId);
			}
			else{
				redisDao.putSeckill(seckill);
			}
		}
		Date startTime = seckill.getStartTime();
		Date endTime = seckill.getEndTime();
		Date now = new Date();
		if(now.getTime()<startTime.getTime()||now.getTime()>endTime.getTime()){
			return new Exposer(false,seckillId,now.getTime(),startTime.getTime(),endTime.getTime());
		}
		String md5 = getMd5(seckillId);//todo
		return new Exposer(true,md5,seckillId);
	}
	
	private String getMd5(long seckillId){
		String base = seckillId+"/"+slat;
		String md5 = DigestUtils.md5DigestAsHex(base.getBytes());
		return md5;
	}
	@Override
	@Transactional
	public SeckillExecution executeSeckill(long seckillId, long userPhone,
			String md5) throws SeckillException, RepeatKillException,
			SeckillCloseException {
		if(md5 == null||!md5.equals(getMd5(seckillId))){
			throw new SeckillException("seckill data rewrite");
		}
		Date killTime = new Date();
		try{
			int reduceNumber = seckillDao.reduceNumber(seckillId, killTime);
			if(reduceNumber<=0){
				throw new SeckillCloseException("Seckill closed");
			}else{
				int intInsert = successKilledDao.insertSuccessKilled(seckillId, userPhone);
				if(intInsert<=0){
					throw new RepeatKillException("Seckill repeat");
				}else{
					SuccessKilled successKilled = successKilledDao.queryByIdWithSeckill(seckillId,userPhone);
					return new SeckillExecution(seckillId,SeckillStatEnum.SUCCESS,successKilled);
				}
			}
		}catch(SeckillCloseException e1){
			throw e1;
		}catch(RepeatKillException e2){
			throw e2;
		}catch(Exception e){
			logger.error(e.getMessage());
			throw new SeckillException("seckill inner error:"+e.getMessage());
		}
	}

	@Override
	public SeckillExecution executeSeckillProcedure(long seckillId,
			long userPhone, String md5) {
		if(md5 == null || !md5.equals(getMd5(seckillId))){
			return new SeckillExecution(seckillId,SeckillStatEnum.DATA_REWRITE);
		}
		Date killTime = new Date();
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("seckillId", seckillId);
		map.put("phone", userPhone);
		map.put("killTime", killTime);
		map.put("result", null);
		try{
			seckillDao.killByProcedure(map);
			int result = MapUtils.getInteger(map, "result", -2);
			if(result == 1){
				SuccessKilled sk = successKilledDao.queryByIdWithSeckill(seckillId, userPhone);
				return new SeckillExecution(seckillId,SeckillStatEnum.SUCCESS,sk);
			}else{
				return new SeckillExecution(seckillId,SeckillStatEnum.stateOf(result));
			}
		}catch(Exception e){
			logger.error(e.getMessage(),e);
			return new SeckillExecution(seckillId,SeckillStatEnum.INNER_ERROR);
		}
	}
}
