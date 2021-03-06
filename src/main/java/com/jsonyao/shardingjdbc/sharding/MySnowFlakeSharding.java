package com.jsonyao.shardingjdbc.sharding;

import org.apache.shardingsphere.api.sharding.standard.PreciseShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingValue;
import org.springframework.util.CollectionUtils;

import java.util.Collection;

/**
 * 自定义雪花算法ID分片逻辑
 */
public class MySnowFlakeSharding implements PreciseShardingAlgorithm<Long> {

    /**
     * 执行具体分片策略
     * @param collection                实际分片结点
     * @param preciseShardingValue      分片参数(逻辑表、分片列、分片列值)
     * @return
     */
    @Override
    public String doSharding(Collection<String> collection, PreciseShardingValue<Long> preciseShardingValue) {
        if(CollectionUtils.isEmpty(collection) || preciseShardingValue == null){
            throw new RuntimeException("自定义分片异常...");
        }

        // 分片哈希
        Long idValue = preciseShardingValue.getValue();
        Long hash = Long.valueOf(Math.abs(idValue.hashCode()) % collection.size());

        // 哈希到的分片
        String[] shards = collection.toArray(new String[0]);
        String shard = shards[(int) hash.longValue()];
        System.out.println(String.format("ID值: %s, 得到的哈希: %s, 哈希后得到的分片: %s", idValue, hash, shard));
        return shard;
    }
}
