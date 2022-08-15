package mei.yun.xiao;

import com.google.common.collect.Lists;

import java.util.List;

public class ShardingDemo {

    public static void main(String[] args) {
        String id = "T20190822000702";

        long hashCode = Math.abs(id.hashCode());
        System.err.println(hashCode);

        for (int i = 64; i >= 0; i--) {
            System.err.print(" ");
            System.err.print(i);
        }
        System.err.println();

        for (int i = 64; i >= 0; i--) {
            System.err.print(i > 9? "  " : " ");
            System.err.print(hashCode >> i & 1);
        }

        /**
         * T20190822000702:
         *  64 63 62 61 60 59 58 57 56 55 54 53 52 51 50 49 48 47 46 45 44 43 42 41 40 39 38 37 36 35 34 33 32 31 30 29 28 27 26 25 24 23 22 21 20 19 18 17 16 15 14 13 12 11 10 9 8 7 6 5 4 3 2 1 0
         *   1  0  0  0  0  0  0  0  0  0  0  0  0  0  0  0  0  0  0  0  0  0  0  0  0  0  0  0  0  0  0  0  0  0  0  1  0  0  0  1  0  0  0  1  0  0  0  0  1  0  1  1  0  1  0 1 1 0 1 0 0 1 0 1 1
         * 不溢出情况下取模: a % (2^n) 等价于 a & (2^n - 1) => 即 a % 2^5 等价于 a & (2^5 - 1)
         * & 0  0  0  0  0  0  0  0  0  0  0  0  0  0  0  0  0  0  0  0  0  0  0  0  0  0  0  1  0  0  0  0  0  0  0  0  0  0  0  0  0  0  0  0  0  0  0  0  0  0  0  0  0  0  0 0 0 0 0 0 1 1 1 1 1
         *=> 0  0  0  0  0  0  0  0  0  0  0  0  0  0  0  0  0  0  0  0  0  0  0  0  0  0  0  1  0  0  0  0  0  0  0  0  0  0  0  0  0  0  0  0  0  0  0  0  0  0  0  0  0  0  0 0 0 0 0 0 0 1 0 1 1 => 8 + 2 + 1 = 11
         * 不溢出情况下除法: a / (2^n) 等价于 a >> n => 即 a / 2^5 等价于 a >> 5
         * / 0  0  0  0  0  1  0  0  0  0  0  0  0  0  0  0  0  0  0  0  0  0  0  0  0  0  0  0  0  0  0  0  0  0  0  0  0  0  0  0  1  0  0  0  1  0  0  0  1  0  0  0  0  1  0 1 1 0 1 0 1 1 0 1 0 ( 0 1 0 1 1)
         * 不溢出情况下取模: a % (2^n) 等价于 a & (2^n - 1) => 即 a % 2^5 等价于 a & (2^5 - 1)
         * & 0  0  0  0  0  0  0  0  0  0  0  0  0  0  0  0  0  0  0  0  0  0  0  0  0  0  0  1  0  0  0  0  0  0  0  0  0  0  0  0  0  0  0  0  0  0  0  0  0  0  0  0  0  0  0 0 0 0 0 0 1 1 1 1 1
         *=> 0  0  0  0  0  0  0  0  0  0  0  0  0  0  0  0  0  0  0  0  0  0  0  0  0  0  0  1  0  0  0  0  0  0  0  0  0  0  0  0  0  0  0  0  0  0  0  0  0  0  0  0  0  0  0 0 0 0 0 0 1 1 0 1 0 => 16 + 8 + 2 = 26
         *
         * => 因此, 如果扩容2倍, 从原来的32库32表, 扩容为64库64表:
         *      1) 会导致从原来库取最后5位, 表取次后5位, 更新为库取最后6位, 表取次后6位
         *      2) 也就是曾经 abs_hashCode 在 2^5 位置为1的, 则需要变动库号, 为0的则不需要变动
         *      3) 同理, 曾经 abs_hashCode 在 2^5、2^6、2^7、2^8、2^9、2^10、2^11 位置为1的, 则需要变动表号, 为0的则不需要变动
         *      4) 综上所述, 发生扩容后, 库号要更改的记录占以前模过的记录的 1/6 * 1/2 = 1/12, 表号要更改的记录占占以前模过的记录的 1/2
         */
        System.err.println();
        System.err.println(Math.abs((long) hashCode) % 32);
        System.err.println(Math.abs((long) hashCode) / 32 % 32);

        System.err.println("模拟扩容 => 从原来的32库32表, 扩容为64库64表: ");

        System.err.println(Math.abs((long) hashCode) % 64);
        System.err.println(Math.abs((long) hashCode) / 64 % 64);
        System.err.println();
    }

    public static String doDbSharding(String shardingValue) {
        List<String> databaseNames = Lists.newArrayList("03");
        Long shardResult = Math.abs((long) shardingValue.hashCode()) % 32;// 取最后5位
        for (String databaseName : databaseNames) {
            String shardValue = String.format("%02d", shardResult);// 格式化为2位整数
            if (databaseName.endsWith(shardValue)){
                return databaseName;
            }
        }
        throw new UnsupportedOperationException();
    }

    public static String doTableSharding(String shardingValue) {
        List<String> tableNames = Lists.newArrayList("16");
        Long shardResult = Math.abs((long) shardingValue.hashCode()) / 32 % 32;// 取次后5位
        for (String tableName : tableNames) {
            String shardValue = String.format("%02d", shardResult);// 格式化为2位整数
            if (tableName.endsWith(shardValue)){
                return tableName;
            }
        }
        throw new UnsupportedOperationException();
    }
}
