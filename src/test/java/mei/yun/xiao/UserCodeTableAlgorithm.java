package mei.yun.xiao;

import org.apache.shardingsphere.api.sharding.standard.PreciseShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingValue;

import java.util.Collection;

public class UserCodeTableAlgorithm implements PreciseShardingAlgorithm<String> {

    /**
     * @param tableNames 有效的数据源 或者 表 的名字  tableNames 就为配置文件中的 配置的数据源信息 -> ds0 , ds1
     * @param shardingValue SQL 分片列 对应的实际值
     * @return 返回相应的表名
     */
    @Override
    public String doSharding(Collection<String> tableNames, PreciseShardingValue<String> shardingValue) {
        Long shardResult = Math.abs((long) shardingValue.getValue().hashCode()) / 32 % 32;
        for (String tableName : tableNames) {
            String shardValue = String.format("%02d", shardResult);
            if (tableName.endsWith(shardValue)){
                return tableName;
            }
        }
        throw new UnsupportedOperationException();
    }

}
