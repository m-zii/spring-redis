package com.example.facade;

import org.springframework.stereotype.Component;

import com.example.repository.RedisLockRepository;
import com.example.service.StockService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class LettuceLockStockFacade {

	private final RedisLockRepository redisLockRepository;
	private final StockService stockService;
	
	public void decrease(Long key, Long quantity) throws InterruptedException {
		while (!redisLockRepository.lock(key)) {
			Thread.sleep(100);
		}

		try {
			stockService.decrease(key, quantity);
		} finally {
			redisLockRepository.unlock(key);
		}
	}
}
