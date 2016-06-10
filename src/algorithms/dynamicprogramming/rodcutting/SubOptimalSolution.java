package algorithms.dynamicprogramming.rodcutting;

import static algorithms.dynamicprogramming.rodcutting.RodPriceTable.getOptimalRevenue;
import static algorithms.dynamicprogramming.rodcutting.RodPriceTable.getPrice;
import static algorithms.dynamicprogramming.rodcutting.RodPriceTable.setOptimalRevenue;

/**
 * The optimal revenue for a rod of length n
 * is the maximum of the sum of the left segment + the optimal revenue of the right segment
 * for all lengths greater than or equal to one and less than or equal n
 *
 * Rn = max (Pi + R n-i) for 1 <= i <= n
 */
public class SubOptimalSolution
{
        public static void main(String[] args)
        {
                int rodLength = 5;
//                System.out.println(findOptimalRevenue_recur(rodLength));
                System.out.println(findOptimalRevenue_memoized_topdown(rodLength));
        }

        private static int findOptimalRevenue_recur(int n)
        {
                if (n == 0)
                {
                        return 0;
                }
                int price = Integer.MIN_VALUE;
                for (int i = 1; i <= n; i++)
                {
                        price = Math.max(price, getPrice(i) + findOptimalRevenue_recur(n - i));
                }
                return price;
        }

        private static int findOptimalRevenue_memoized_topdown(int n)
        {
                final int optimalRevenue = getOptimalRevenue(n);
                if (optimalRevenue > 0)
                {
                        return optimalRevenue;
                }
                if (n == 0)
                {
                        setOptimalRevenue(0, 0);
                        return 0;
                }
                int price = Integer.MIN_VALUE;
                for (int i = 1; i <= n; i++)
                {
                        price = Math.max(price, getPrice(i) + findOptimalRevenue_memoized_topdown(n - i));
                }
                setOptimalRevenue(n, price);
                return price;
        }
}