package CompanyTag.GSCoderPad;

public class OptimalPath {
    public static int optimalPath(int[][] grid) {
        if (grid == null || grid.length == 0 || grid[0].length == 0) return 0;
        int row = grid.length;
        int col = grid[0].length;

        int[] dp = new int[col];
        for (int j = 0; j < col; j++) dp[j] = grid[row-1][j];
        //  !!!          dp[j] = grid[row-1][j] + dp[j-1];

        for (int i = row - 2; i >= 0; i--) {
            dp[0] += grid[i+1][0];
            for (int j = 1; j < col; j++) {
                dp[j] = Math.max(dp[j-1], dp[j]) + grid[i+1][j];
            }
        }
        return dp[col-1];
    }
}
