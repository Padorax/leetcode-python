2 types: 
1. exactly at position i (e.g., climb stair, 718, result can happen at any position)
2. from start till position i
e.g., house robber: dp[i] - max earned using arr[0:i+1], may not need to robber ith place, thus overral result is exactly dp[n-1]

买卖股票
309

718 max length of repeated subarray (type 1)
1143 LCS (non consecutive, type 2)