package dynamicprogramming.rodcutting;

import java.util.HashMap;
import java.util.Map;

public class RodPriceTable
{
        private static final Map<Integer, Integer> PRICE_TABLE = new HashMap<Integer, Integer>(10);
        private static final Map<Integer, Integer> OPTIMAL_REVENUE_TABLE = new HashMap<Integer, Integer>(10);

        static
        {
                PRICE_TABLE.put(1, 1);
                PRICE_TABLE.put(2, 5);
                PRICE_TABLE.put(3, 8);
                PRICE_TABLE.put(4, 9);
                PRICE_TABLE.put(5, 10);
                PRICE_TABLE.put(6, 17);
                PRICE_TABLE.put(7, 17);
                PRICE_TABLE.put(8, 20);
                PRICE_TABLE.put(9, 24);
                PRICE_TABLE.put(10, 30);

                for (Integer length : PRICE_TABLE.keySet())
                {
                        OPTIMAL_REVENUE_TABLE.put(length, Integer.MIN_VALUE);
                }
        }

        public static int getPrice(int length)
        {
                final Integer price = PRICE_TABLE.get(length);
                return price == null ? Integer.MIN_VALUE : price;
        }

        public static int getOptimalRevenue(int length)
        {
                final Integer revenue = OPTIMAL_REVENUE_TABLE.get(length);
                return revenue == null ? Integer.MIN_VALUE : revenue;
        }

        public static void setOptimalRevenue(int length, int revenue)
        {
                OPTIMAL_REVENUE_TABLE.put(length, revenue);
        }
}