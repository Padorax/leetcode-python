### dfs三种模式
1. Bottom-Up,依赖子节点的返回值汇总结果,算树高、直径、平衡性,较难（通常需要遍历整棵树）
    2246.md
2. Top-Down (无返回),带着参数（状态）往下跑，记录路径,打印所有路径、路径和,很难（无法中断上层递归）
3. Top-Down (有返回),带着参数往下跑，发现目标立刻“叫停”(中断上层递归),迷宫寻路、搜索特定路径, reconsutruct itenerary (stop at first path found)非常简单 (if dfs: return True)
Ex
1
```python
def dfs(node):
    if not node: return 0
    left_res = dfs(node.left)
    right_res = dfs(node.right)
    return combine(left_res, right_res) # 核心是这句组合
```
2
```python
def dfs(node, path):
    path.append(node.val)
    if is_leaf(node):
        self.results.append(list(path)) # 只能在叶子节点处被动触发
    else:
        dfs(node.left, path)
        dfs(node.right, path)
    path.pop() # 回溯
```
3
```python
def dfs(node, path):
    path.append(node.val)
    if node.val == target:
        return True # 一旦找到，通过返回值告诉上层！
    
    for neighbor in get_neighbors(node):
        if dfs(neighbor, path): 
            return True # 只要下层说找到了，我也跟着往上说找到了
    
    path.pop()
    return False
```
决策树：
    Q: 我需要处理全局最优解吗？
        需要：用 Bottom-Up（由子节点汇总）。
        不需要，只需要找到任意解：用 Top-Down 有返回。
    Q: 我需要记录路径吗？
        需要：用 Top-Down（不管是哪种，因为它自带状态携带）。
    Q:我需要尽早结束吗？
        需要：必须有 Boolean 返回值。

2 vs 3: “记录路径（自顶向下）”与“提前剪枝（Boolean 逻辑）”的博弈。
1. 为什么“纯”自顶向下很难剪枝？
如果你的 DFS 是 dfs(node, path) 这种没有返回值的“旅行者”模式，当你发现目标时，你是无法通知正在递归栈上方的父节点停下来的。这意味着：即便你找到了解，程序依然会把剩下的所有分支全部跑完，这在搜索空间巨大的情况下是灾难性的。
2. 解决方案：把“剪枝”加进 Top-Down
要实现提前结束，即使你不传回复杂的计算结果，你也必须传回一个 Boolean 信号。

为什么“Top-Down”仍然存在？（适用场景）
    你可能会问：既然都要返回 Boolean，那为什么不直接用那种“把路径拼回来的” Bottom-Up 呢？
    Top-Down 的优势在于“状态记录”：有些题目不只是要记录路径，还要记录路径上产生的状态。例如：
    “路径和等于 target”：你需要一直带着当前的 current_sum（Top-Down 传递），如果 current_sum 超过了 target，就可以在路径还没走完时就直接剪枝！
    “迷宫最短路（含转向代价）”：你必须携带当前的 direction 和 turn_count（Top-Down 携带状态）。

举个例子：路径和 (Path Sum)
    假设我们要找一条路径，使得路径上的节点值之和等于 target：
    Bottom-Up (返回路径列表)：你需要返回一个列表，计算量大，不方便剪枝。
    Top-Down (携带 cur_sum)：
```python
def dfs(node, cur_sum, path):
    cur_sum += node.val
    path.append(node.val)
    
    # 剪枝点：如果当前和已经超了（假设节点全是正数），直接停！
    if cur_sum > target:
        path.pop()
        return False
        
    if is_leaf(node) and cur_sum == target:
        self.result = list(path)
        return True
        
    for child in children:
        if dfs(child, cur_sum, path):
            return True
            
    path.pop()
    return False
```
总结：如何优雅地“剪枝”？
    无论你用哪种模式，想要剪枝，就必须满足一个条件：递归函数必须能告诉上层：“我找到了！”
    如果没有返回值：你只能靠全局变量通知，但递归的“回退”过程无法立即中断，效率极低。
    如果有 Boolean 返回值：你可以利用 if dfs(...) return True 这种写法，像骨牌一样一层层中断递归。

top-down path:
besides pass in as dfs param, can use external list as well
```python
res = []
path = [] # 外部列表

def dfs(node):
    if not node: return
    
    # 1. 做出选择：把当前节点加入共享路径
    path.append(node.val)
    
    # 如果是叶子节点，记录答案
    if not node.left and not node.right:
        res.append(list(path)) # 注意：这里必须 copy，否则 res 里全是最后的状态
    
    # 2. 递归进入下一层
    dfs(node.left)
    dfs(node.right)
    
    # 3. 撤销选择：回溯的核心！（把刚才加进去的踢出来）
    path.pop()
```

### Example: Tree Height
top-down: 通过函数参数将父节点的信息（当前深度）传递给子节点。在到达叶子节点或空节点时，更新全局/实例变量
```python
class Solution:
    def maxDepth(self, root: TreeNode) -> int:
        self.max_h = 0
        
        def dfs(node, curHeight):
            if not node:
                return
            
            # 走到这里说明 node 存在，当前高度有效
            # 如果是叶子节点，尝试更新全局最大高度
            if not node.left and not node.right:
                self.max_h = max(self.max_h, curHeight)
            
            # 向下传递：每深一层，高度 +1
            dfs(node.left, curHeight + 1)
            dfs(node.right, curHeight + 1)
            
        dfs(root, 1) # 初始高度从 1 开始（根节点）
        return self.max_h
```
bottom-up: 利用函数的返回值。父节点的高度取决于子节点的高度。这是处理树形 DP 或复杂树结构（如 AVL 树平衡因子）时最常用的方法
```python
class Solution:
    def maxDepth(self, root: TreeNode) -> int:
        def dfs(node):
            # 基准情况：空节点的高度为 0
            if not node:
                return 0
            
            # 递归获取左右子树的高度
            left_h = dfs(node.left)
            right_h = dfs(node.right)
            
            # 当前节点的高度 = 左右子树中较大的那个 + 1（代表当前这一层）
            return max(left_h, right_h) + 1
            
        return dfs(root)
```
结合？有问题！！
1. 逻辑冗余（信息的重复携带）
在标准的自底向上（方法 2）中，高度是通过返回值累加的，不需要参数。
在标准的自顶向下（方法 1）中，高度是通过参数传递的，不需要返回值。
如果你两者都用：
    * 参数 curHeight 已经告诉你从根到这有多远了。
    * 返回值又要把这个值传回给父节点。
    这就像是你去爬山，每走一步都背着一个写着“当前高度”的牌子（参数），爬到山顶后再把这个牌子原封不动地传回给山下的队友（返回值）。
2. 空节点与叶子节点的处理难题
如果你定义“遇到叶子节点返回 curHeight”，那么对于只有一个子节点的节点，逻辑就会变得很尴尬。
Ex:
```python
def dfs(node, curHeight):
    if not node: return 0 # 必须处理空节点，否则会报错
    if not node.left and not node.right:
        return curHeight # 遇到叶子返回当前高度
    
    # 递归时，必须处理只有单边子树的情况
    res = 0
    if node.left:
        res = max(res, dfs(node.left, curHeight + 1))
    if node.right:
        res = max(res, dfs(node.right, curHeight + 1))
    return res
```
总结：什么时候会“结合”？其实有一种场景真的会结合这两者，那就是 “带权重的路径搜索”。

比如：求从根到叶子的最大路径和（每个节点有不同的分值）。
* 参数：传递 current_sum（自顶向下）。
* 返回值：返回 max_sum_found_in_subtree（自底向上）。

但对于纯高度计算：
自底向上是利用了树的递归性质（高度 = 子树高度 + 1），最优雅。
自顶向下是利用了路径遍历的性质（记录最深到了哪），最直观。
把它们混在一起，就像是给自行车装了方向盘——虽然能走，但既失去了自行车的轻便，又没有汽车的稳重。

### Cacheable or not?
dfs with return value一般是可以cache的，也就是说返回值只依赖于当前的node ，而不依赖于之前的路径，所以结合起来后，如果是node 1->2->3，dfs(2,3) dfs(1,3)和dfs(3,3)返回的都是3，因为这是依赖于这条路径的，但对我们来说除了1的返回结果都是无用的。
效率问题：
如果一个节点被多次访问（比如在非树结构的图中），标准的 dfs(node) 只需要计算一次；而你的 dfs(node, curHeight) 则需要为每一个不同的 curHeight 都重新计算一遍。这让原本 $O(N)$ 的复杂度退化成了路径总数。

**什么时候返回值才真正“有用”？**
返回值只有在 “汇总子树信息” 时才有用。
* 无用的返回值（你的结合版）：子节点只是把父节点传给它的 curHeight 原封不动（或比较后）再传回去。这叫_“信息回传”_。
* 有用的返回值（标准自底向上）：子节点告诉父节点：“我这里有 2 层。”父节点根据这个信息决定：“那加上我这一层，我这就有 3 层了。”这叫_“信息加工”_。

### When need to define a separate/inner dfs method?
when function signature does not match:
1. input param mismatch
ex: 
subset(arr) -> dfs(start, curPath)
generateParenthesis(n) -> dfs(curPath, leftNum, rightNum)
treeHeight(root) -> dfs(node, curHeight)
2. return value mismatch
ex: 
unbalancedTree(root) -> bool =>
    dfs(node) -> int: -1 means mismatch, otherwise node height
    dfs(node) -> int, bool : int for height, bool for whether current node is balanced or not
topological_sort(node) -> List[int] =>
    dfs(node) -> bool: top down with return value, need to exit early if any node has cycle

### Example: word search with (first) path
方案 A：通过返回值“顺藤摸瓜”（最优雅）
你不需要一个真正的全局变量去实时更新，而是利用 递归的“归”过程。
```python
def dfs(r, c, index):
    # 终止条件：匹配完成
    if index == len(word):
        return [] # 返回一个空列表表示后续路径开启
    
    # 越界或字符不匹配
    if not match(r, c, word[index]):
        return None 

    # 标记已访问（回溯常用技巧）
    temp, board[r][c] = board[r][c], "#"
    
    # 尝试四个方向
    for nr, nc in neighbors(r, c):
        res = dfs(nr, nc, index + 1)
        if res is not None: # 如果下方传来了成功的信号
            board[r][c] = temp # 恢复现场
            return [(r, c)] + res # 把当前坐标拼在路径最前面带回去
            
    board[r][c] = temp # 恢复现场
    return None
```
特点：路径是随着 True（或非 None）的信号一层层“拼”出来的。

方案 B：全局变量/引用传递（最常用）
如果你觉得上面的列表拼接太慢，可以维护一个全局的 path 容器
```python
self.success_path = []

def dfs(r, c, index, current_path):
    # 1. 做出选择
    current_path.append((r, c))
    
    # 2. 判断是否完成
    if index == len(word) - 1:
        self.success_path = list(current_path) # 拷贝一份最终答案
        return True
    
    # 3. 递归探索
    for nr, nc in neighbors(r, c):
        # check nr/nc range is in bound plus value is desired
        if dfs(nr, nc, index + 1, current_path):
            return True # 一路绿灯返回
            
    # 4. 撤销选择（回溯的关键）
    current_path.pop()
    return False
```
特点：current_path 就像是一个临时的“草稿本”，只有当最后一步确认成功时，我们才把草稿本的内容复印到 self.success_path（最终答案）上。
深度对比：为什么必须有 Boolean？
    如果没有 Boolean 返回值，只靠全局变量：你会发现你的 path 会记录下所有尝试过的路径，而不是仅仅那条成功的路径。
    就像走迷宫，你每走一步都画线，如果你不回头擦掉死胡同里的线（回溯），或者不告诉后面的人“这条路通了”（Boolean），最后你得到的是一团乱麻。
Ext: 如果我们要找的不是“一个”路径，而是“所有”可能的路径? (boolean不再需要，keep dfs until return - either at boundary or value does not match)

### Example Coin Change
#### (Number of) Distinct combinations
Topd-down
```python
class Solution:
    def combinationSum(self, coins: List[int], amount: int) -> List[List[int]]:
        results = []
        
        def backtrack(remaining, start_index, path):
            # 1. 成功出口：凑齐了
            if remaining == 0:
                results.append(list(path)) # 记得拷贝一份
                return
            
            # 2. 失败出口：超额了
            if remaining < 0:
                return
            
            # 3. 搜索：从当前硬币开始往后遍历
            #start_index: 当前应该从哪个硬币开始选（这是去重的关键：为了避免 {1, 2} 和 {2, 1} 被重复记录，我们只允许在当前索引或索引之后的硬币里选）。
            for i in range(start_index, len(coins)):
                path.append(coins[i])
                # 注意：这里传 i 而不是 i + 1，是因为每个硬币可以用多次
                backtrack(remaining - coins[i], i, path)
                path.pop() # 回溯，撤销选择
                
        backtrack(amount, 0, [])
        return results
```
or
```python
def coinChange(coins, target):
    res = []
    def dfs(start, cur, rem):
        if rem == 0:
            res.append(cur)
            return
        for i in range(start, len(coins)):
            if coins[i] <= target:
                dfs(i, cur+[coins[i]], rem-coins[i])
    dfs(0, [], target)
    return res
```
Q:如果面试官问：“如果我不仅要组合，还要排列（即 {1, 2} 和 {2, 1} 算作两种）该怎么办？”
    答案：去掉 start_index 的限制，每次循环都从 0 开始
Q:如果硬币数组本身就有重复的，比如 coins = [1, 1, 2]，要凑出 2？
    上面的代码会给出 [1, 1] 和 [1, 1]（因为两个 1 被视为不同的索引）。
    为了去重，你需要在循环里加一个判断：
```python
for i in range(start_index, len(coins)):
    # 只有当这个硬币和上一个硬币不同，或者它是当前层的第一个选择时，才递归
    if i > start_index and coins[i] == coins[i-1]:
        continue
    ...
```

Bottom-up
```python
class Solution:
    def combinationSum(self, coins: List[int], amount: int) -> List[List[int]]:
        
        def dfs(remaining, start_index):
            # 基础情况：凑齐了，返回一个包含空路径的列表
            if remaining == 0:
                return [[]]
            # 失败情况：超额或没钱了
            if remaining < 0:
                return []
            
            all_paths = []
            
            # 搜索：遍历每一个硬币
            for i in range(start_index, len(coins)):
                coin = coins[i]
                # 递归获取子树返回的所有可行路径
                # ！只能从i（含i)往后， 不能用之前的硬币
                sub_paths = dfs(remaining - coin, i)
                
                # 如果有结果，就把当前硬币拼上去
                for path in sub_paths:
                    all_paths.append([coin] + path)
            
            return all_paths # 把自己这一层攒到的所有方案全部返回
            
        return dfs(amount, 0)
```
or
```python
def coinChange(coins, target):
    # return ditinct combinations that sum up to rem, using coins[start:]
    def dfs(start, rem):
        if rem == 0:
            return [[]]
        res = []
        for i in range(start, len(coins)):
            if coins[i] <= rem:
                # ** note start/i will never exceed coins length
                combs = dfs(i, rem-coins[i])
                for comb in combs:
                    res.append([coins[i]] + comb)
        return res
    return dfs(0, target)
```
or
这个更接近DP的思路（只需要number of combination,不需要返回具体的combination)
dp[i][j] = dp[i-1][j] + dp[i][j-coins[`i`]]
```python
def coinChange(coins, target):
    def dfs(start, rem):
        if rem == 0: return [[]] # start combination
        # ** need to manually check if start is out of bound
        if start == len(coins) or rem < 0:return [] # failure
        res = dfs(start+1, rem) #Either 1. do not use coins[start]

        # Or 2. use start in result
        combs = dfs(start, rem - coins[start])
        for comb in combs:
            res.append([coins[start]] + comb)
        return res

    return dfs(0, target)
```
优点：
    函数式编程风格：它完全没有副作用（Side Effects），没有 self.results 这种全局变量，非常干净。
    逻辑清晰：符合递归的定义：问题的解 = 每一个分支解的集合。
巨大的陷阱（为什么面试通常不推荐）：
内存爆炸：
    回溯法（Backtracking）在任何时刻只维护一条当前路径（一个 path 数组）。
    但这种带返回值的 DFS，每递归一层，就会创建成千上万个列表副本。如果组合方案特别多，内存会瞬间溢出。
性能损耗：
    频繁的 [coin] + path 拼接操作，涉及大量的数组拷贝和创建，时间复杂度会比回溯法高出一个量级。
* 回溯法 (Top-Down 无返回)：它是 “深度优先”的遍历者，它一次只走一条路，走不通就撤销，极其节省内存。
* 带返回值的 DFS (Bottom-Up 汇聚)：它是 “穷举式”的组合者，它试图把整个解空间树在内存里“重建”出来。

#### Number of coin change (don't need actual combination, just count)
```python
class Solution:
    def change(self, amount: int, coins: List[int]) -> int:
        dp = [0] * (amount + 1)
        dp[0] = 1
        
        for coin in coins:
            for j in range(coin, amount + 1):
                # 这一行等价于：dp_new[j] = dp_old[j] + dp_new[j - coin]
                dp[j] += dp[j - coin]
                
        return dp[amount]
```
and this is wrong!
```python
class Solution:
    def coinChange(self, coins: List[int], amount: int) -> int:
        coins.sort(reverse=True)
        mp = {0:0}
        def dfs(amt):
            if amt < 0: return -1
            if amt in mp:
                return mp[amt]
            cnt = 0
            for c in coins:
                if amt >= c:
                    cnt += dfs(amt-c)+1
            if cnt == 0: cnt = -1
            mp[amt] = cnt
            return cnt
        return dfs(amount)
```
#### Minimum number of coin changes
bottom up (with return value), with memo
```python
class Solution:
    def coinChange(self, coins: List[int], amount: int) -> int:
        coins.sort(reverse=True)
        mp = {0:0}
        def dfs(amt):
            if amt < 0: return -1
            if amt in mp:
                return mp[amt]
            cnt = float('inf')
            for c in coins:
                if amt >= c:
                    val = dfs(amt-c)
                    if val != -1:
                        cnt = min(cnt, dfs(amt-c)+1)
            if cnt == float('inf'): cnt = -1
            mp[amt] = cnt
            return cnt
        return dfs(amount)
```

DP: dp[i] is the number of minimum coins to get i
dp[i] = min(dp[i], dp[i-coin]+1)
注意这里我们don't care about dup: ex - [1, 5], 6
dp[1] = 1, dp[5]= 1(just one 5)
dp[6] = min(dp[1]+1, dp[5]+1)
但实际上对应的两种组合[1, 5]和[5, 1]是重复的，但我们求最小值所以无所谓（但如果是要具体的组合，则不行）
```python
def coinChange(self, coins, amount):
    dp = [0] + [float('inf') for i in range(amount)]
    for i in range(1, amount+1):
        for coin in coins:
            if i - coin >= 0:
                dp[i] = min(dp[i], dp[i-coin] + 1)
    if dp[-1] == float('inf'):
        return -1
    return dp[-1]
```

#
whether curPath (list) or visited (set), can be recursive dfs call parameter, or outside variable (shared across recursion)