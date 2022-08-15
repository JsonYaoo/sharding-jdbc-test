package mei.yun.xiao;

import org.apache.shardingsphere.api.sharding.standard.PreciseShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingValue;

import java.util.Collection;

public class UserCodeDatabaseAlgorithm implements PreciseShardingAlgorithm<String> {

    /**
     * @param databaseNames 有效的数据源 或者 表 的名字  databaseNames 就为配置文件中的 配置的数据源信息 -> ds0 , ds1
     * @param shardingValue SQL 分片列 对应的实际值
     * @return 返回相应的数据库名
     */
    @Override
    public String doSharding(Collection<String> databaseNames, PreciseShardingValue<String> shardingValue) {
        Long shardResult = Math.abs((long) shardingValue.getValue().hashCode()) % 32;
        for (String databaseName : databaseNames) {
            String shardValue = String.format("%02d", shardResult);
            if (databaseName.endsWith(shardValue)){
                return databaseName;
            }
        }
        throw new UnsupportedOperationException();
    }

    public static void main(String[] args) {
        String ss = "T20190822000702";
        Long shardResult = Math.abs((long) ss.hashCode()) % 32;
        System.out.println(shardResult);
        shardResult = Math.abs((long) ss.hashCode()) / 32 % 32;
        System.out.println(shardResult);

    }
}