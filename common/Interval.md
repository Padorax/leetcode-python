# Questions
57 Insert Interval
56 Merge Intervals
435 Non-overlapping Intervals
253 Meeting Rooms II
1851 Minimum Interval to Include Each Query

# Lowest Price Range
Description: given a list of price in a range (start_time, end_time, price), return a sorted list with different possible inervals and the least price during the interval
ex:
Input: (1, 20, 13), (7, 10, 8), (3, 8, 15), (1, 5, 20)
Output: (1, 7, 13), (7, 10, 8), (10, 20, 13)

Input: (7, 10, 8), (3, 8, 5), (1, 5, 20), (1, 20, 4)
Output: (1, 20, 4)

Input: (3, 6, 2), (1, 9, 3), (5, 8, 1)
Outpu: (1, 3, 3), (3, 5, 2), (5, 8, 1), (8, 9, 3)

### Solution 1 - BST
```python
class Interval:
    def __init__(self, start_time, end_time, price):
        self.start = start_time
        self.end = end_time
        self.price = price
    
    def __repr__(self):
        return f"({self.start}, {self.end}, {self.price})"

class Node:
    def __init__(self, interval: Interval):
        self.data = interval
        self.left = None
        self.right = None

class BinarySearchTree:
    def __init__(self):
        self.root = None

    def insert(self, start, end, price):
        """外部调用的接口"""
        if start >= end:
            return
        new_interval = Interval(start, end, price)
        if not self.root:
            self.root = Node(new_interval)
        else:
            self._insert_recursive(self.root, start, end, price)

    def _insert_recursive(self, curr_node, s, e, p):
        """核心逻辑：区间拆分与空间递归"""
        if s >= e or not curr_node:
            return
        
        # 获取当前节点区间的边界
        root_s = curr_node.data.start
        root_e = curr_node.data.end

        # 1. 处理左侧可能的空隙: [s, min(e, root_s)]
        if s < root_s:
            left_end = min(e, root_s)
            if not curr_node.left:
                curr_node.left = Node(Interval(s, left_end, p))
            else:
                self._insert_recursive(curr_node.left, s, left_end, p)

        # 2. 处理右侧可能的空隙: [max(s, root_e), e]
        if e > root_e:
            right_start = max(s, root_e)
            if not curr_node.right:
                curr_node.right = Node(Interval(right_start, e, p))
            else:
                self._insert_recursive(curr_node.right, right_start, e, p)

    def get_sorted_intervals(self):
        """中序遍历获取有序的、非重叠的最小价格区间"""
        res = []
        self._inorder(self.root, res)
        
        # 最后的优化：合并相邻且价格相同的区间
        if not res: return []
        
        merged = [res[0]]
        for i in range(1, len(res)):
            prev = merged[-1]
            curr = res[i]
            # 如果时间连续且价格相同，合并
            if curr.start == prev.end and curr.price == prev.price:
                merged[-1] = Interval(prev.start, curr.end, prev.price)
            else:
                merged.append(curr)
        return merged

    def _inorder(self, node, res):
        if not node:
            return
        self._inorder(node.left, res)
        res.append(node.data)
        self._inorder(node.right, res)

# --- 使用示例 ---
def solve(input_data):
    # 第一步：必须按价格升序排列！
    sorted_input = sorted(input_data, key=lambda x: x[2])
    
    bst = BinarySearchTree()
    for s, e, p in sorted_input:
        bst.insert(s, e, p)
        
    return bst.get_sorted_intervals()

# 测试用例 3
# (3, 6, 2), (1, 9, 3), (5, 8, 1)
example_input = [(3, 6, 2), (1, 9, 3), (5, 8, 1)]
print(solve(example_input))
```

### Solution 2 - Scanning Line
```python
import heapq

def get_least_prices(intervals):
    # 1. 拆分事件: (时间, 类型, 价格)
    # 类型: 1 代表进入, -1 代表离开
    events = []
    for s, e, p in intervals:
        events.append((s, 1, p))
        events.append((e, -1, p))
    
    # 2. 按时间排序
    events.sort()
    
    res = []
    active_prices = [] # 最小堆
    # 记录每个价格出现的次数，用于处理重复价格和懒删除
    price_counts = {} 
    
    prev_time = events[0][0]
    
    for curr_time, type, price in events:
        # 当时间推进时，如果堆不为空，当前的堆顶就是上一个区间 [prev_time, curr_time] 的最小值
        if curr_time > prev_time:
            # 懒删除：弹出所有已经不再视野内的价格
            while active_prices and price_counts.get(active_prices[0], 0) == 0:
                heapq.heappop(active_prices)
            
            if active_prices:
                min_p = active_prices[0]
                # 合并相邻且价格相同的区间
                if res and res[-1][2] == min_p and res[-1][1] == prev_time:
                    res[-1] = (res[-1][0], curr_time, min_p)
                else:
                    res.append((prev_time, curr_time, min_p))
        
        # 处理当前事件
        if type == 1: # 进入
            heapq.heappush(active_prices, price)
            price_counts[price] = price_counts.get(price, 0) + 1
        else: # 离开
            price_counts[price] -= 1
            
        prev_time = curr_time
        
    return res

# 测试例子 1
# Input: (1, 20, 13), (7, 10, 8), (3, 8, 15), (1, 5, 20)
# Output: [(1, 7, 13), (7, 10, 8), (10, 20, 13)]
```

#### why `if curr_time > prev_time`?
这个问题问到了扫描线算法的**灵魂**。

简单来说，`if curr_time > prev_time` 是为了**“结算”两个时间点之间的空白区间**。

1. 核心直觉：区间是在“跳跃”中诞生的
扫描线并不是连续滑动的，它是从一个事件点“瞬间移动”到下一个事件点。
* `prev_time`：上一个事件发生的时间。
* `curr_time`：当前正在处理的事件时间。

**在这两个时间点之间，没有任何新的价格进入或离开。** 意味着这段时间内的“最低价格”是**恒定不变**的。

2. 举个例子说明（Trace 逻辑）
假设你有两个事件：
1.  `(时间 1, 进入, 价格 10)`
2.  `(时间 5, 进入, 价格 8)`

**执行步骤：**
* **当我们在时间 1 时**：
    * `curr_time = 1`, `prev_time` 初始也是 `1`（或者 0）。
    * `if curr_time > prev_time` 为 **False**。我们不结算，只是把 `10` 丢进堆里。
    * 最后更新 `prev_time = 1`。

* **当我们在时间 5 时**：
    * `curr_time = 5`, `prev_time = 1`。
    * `if curr_time > prev_time` 为 **True**！
    * **重点来了**：这代表时间 $1$ 到 $5$ 之间形成了一个有效的区间。在这段 $4$ 个单位的时间里，堆里的最小值（$10$）一直统治着市场。
    * 于是我们记录：`(1, 5, 10)`。
    * 结算完后，我们再把时间 $5$ 的新价格 $8$ 丢进堆里。

3. 为什么要加这个判断？（防坑指南）

如果没有这个 `if`，或者在处理多个**同一时间发生**的事件时，就会出大错：

情况 A：同一时刻有多个事件
比如在 `时间 10`，价格 `A` 离开，价格 `B` 进入。
* 如果你的循环里没有 `if curr_time > prev_time`，你可能会在处理 `A` 离开的一瞬间，记录一个长度为 $0$ 的区间 `(10, 10, ...)`。
* 有了这个判断，只有当时间**真的往前走了一段**（比如到了 $11$），我们才会回头去结算 $10$ 到 $11$ 这一段。

情况 B：起始点的初始化
在循环开始前，`prev_time` 通常设为第一个事件的时间。如果不加判断，第一次循环就会产出一个 `(start, start, price)` 的无效点。

---

4. 形象化的理解
你可以把扫描线想象成**装修刷墙**：
* 每一个事件点（`curr_time`）都是你决定“换一种油漆颜色”或者“停止刷漆”的时刻。
* 当你站在 `curr_time` 这个点时，你回头看一眼 `prev_time`：**“从上次换色到现在，我这一段路刷的是什么颜色？”**
* 如果 `curr_time == prev_time`，说明你还在原地换桶，还没往前走，自然不需要结算。

总结
`curr_time > prev_time` 就像是里程表的结算器：**只有车动了（时间流逝了），我们才计算这段路程的油耗（区间价格）。**

**这个解释能让你明白为什么结算要放在“处理当前事件”之前了吗？要不要我再解释一下为什么“懒删除”必须要放在这个 `if` 里面？**