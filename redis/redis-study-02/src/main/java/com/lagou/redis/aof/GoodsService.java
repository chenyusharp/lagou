package com.lagou.redis.aof;

import java.util.List;

/**
 * Date: 2022/3/23
 * <p>
 * Description:
 *
 * @author xiazhenyu
 */
public interface GoodsService {


    int selectCountByMerchantId(long merchantId);


    List<Goods> selectGoodsListByMerchantId(int start, int pageSize, long merchantId);


    int batchDeletedByIds(List<Long> goodsIdList);


}
