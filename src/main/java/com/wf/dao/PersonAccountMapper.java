package com.wf.dao;

import java.math.BigDecimal;

/**
 * 账户余额
 * @author nxs
 *
 */
public interface PersonAccountMapper {
	BigDecimal queryBalance(String userId);
}
