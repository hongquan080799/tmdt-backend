package com.abc.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.abc.entity.CTDH;
import com.abc.entity.CTDH_ID;

public interface CtdhRepository extends JpaRepository<CTDH, CTDH_ID> {
	
}
