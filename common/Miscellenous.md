# gcd
```python
def gcd(a, b):
    return gcd(b, a % b)
```

# Find element appear more than half
moore投票法
摩尔投票法（Boyer-Moore Voting Algorithm）是一个非常天才的算法，它寻找“多数元素”的核心逻辑是：“抵消”。

想象一场混战，不同势力（数字）的人在格斗。如果一个势力的总人数超过了总人数的一半，那么就算他和其他所有人同归于尽（一换一），最后剩下的也一定是这个势力的幸存者。
```python
def findMoreThanHalf(nums):
    count = 0
    candidate = None
    for num in nums:
        if count == 0:
            candidate = num
        if num == candidate:
            count += 1
        else:
            count -= 1
    return candidate
```
or
```python
def majorityElement(nums):
    candidate = None
    count = 0
    
    for num in nums:
        # 如果血条空了，换新的人当候选人
        if count == 0:
            candidate = num
            count = 1
        # 如果是同伴，血条增加
        elif num == candidate:
            count += 1
        # 如果是敌人，血条减少（同归于尽）
        else:
            count -= 1
            
    return candidate
```
扩展：找出所有出现超过 n/3 的数
根据数学原理，出现次数超过 n/3 的数最多只有两个。 我们要维护 两个候选人 和 两个计数器。
逻辑规则：
如果匹配到候选人 A，count1 += 1。
否则如果匹配到候选人 B，count2 += 1。
如果 count1 为 0，换 A 为新数字。
如果 count2 为 0，换 B 为新数字。
如果三个数都互不相同：A 和 B 的血条同时减 1（三者同归于尽）。
```python
def majorityElementN3(nums):
    if not nums: return []
    
    cand1, cand2 = None, None
    count1, count2 = 0, 0
    
    # 1. 投票阶段
    for num in nums:
        if num == cand1:
            count1 += 1
        elif num == cand2:
            count2 += 1
        elif count1 == 0:
            cand1, count1 = num, 1
        elif count2 == 0:
            cand2, count2 = num, 1
        else:
            # 三方混战，两个候选人血条都减一
            count1 -= 1
            count2 -= 1
            
    # 2. 验证阶段（n/3 必须验证）
    res = []
    for c in [cand1, cand2]:
        if nums.count(c) > len(nums) // 3:
            res.append(c)
    
    return list(set(res)) # 去重，防止 cand1 和 cand2 是一样的
```

# Detect Cycle Entrypoint
快慢指针（Floyd 判端算法）的逻辑。在中文语境下，这个算法常被称为**“龟兔赛跑算法”**。

简单来说，它的核心分为两个阶段：

1. 阶段一：检测是否有环（找相遇点）
想象两个人在圆形操场跑步：
兔子（快指针 fast）：一次跑 2 步。
乌龟（慢指针 slow）：一次跑 1 步。
结论：如果操场是圆的（有环），兔子最终一定会从后面追上乌龟（相遇）；如果是一条直线，兔子会先到达终点。

2. 阶段二：寻找环的入口（找入圈点）
这是算法中最神奇的部分。一旦乌龟和兔子在环内相遇，我们执行以下操作：
让乌龟回到链表的起点（Head）。兔子留在原地（相遇点）。现在，两人都以同样的速度（一次 1 步）向前走。
结论：两人再次相遇的地方，绝对就是环的入口。

3. 为什么这个数学逻辑成立？
我们可以用简单的代数来证明：
假设起点到入口的距离为 a。入口到相遇点的距离为 b。环的剩余长度为 c。
当第一次相遇时：
乌龟走了：a+b, 兔子走了：a+b+n(b+c)（n 是兔子多跑的圈数）
因为兔子速度是乌龟的 2 倍：2(a+b)=a+b+n(b+c)
化简后得到：a=(n−1)(b+c)+c
这说明： 从起点走到入口的距离（a），等于从相遇点绕环几圈后再走到入口的距离。这就是为什么两人同步走一定会相遇。

```python
class ListNode:
    def __init__(self, x):
        self.val = x
        self.next = None

# --- 第一步：手动创建一个带环的链表 ---
node1 = ListNode(1)
node2 = ListNode(2)
node3 = ListNode(3)
node4 = ListNode(4)
node5 = ListNode(5)

node1.next = node2
node2.next = node3
node3.next = node4
node4.next = node5
node5.next = node3  # 5 指向 3，制造环，入口是 3

# --- 第二步：快慢指针检测并寻找入口 ---
def find_cycle_entry(head):
    slow = head
    fast = head
    
    # 1. 检测是否有环
    has_cycle = False
    while fast and fast.next:
        slow = slow.next
        fast = fast.next.next
        if slow == fast:
            has_cycle = True
            break
            
    if not has_cycle:
        return None
        
    # 2. 寻找入口
    slow = head  # 乌龟回起点
    while slow != fast:
        slow = slow.next
        fast = fast.next  # 两人等速前进
        
    return slow.val  # 返回入口的值

print(f"环的入口值是: {find_cycle_entry(node1)}") 
# 输出应该是 3
```

### Scanning Line
253.md, 1851.md

### Greedy
Gas station (134.md), 2412 minimum money required before transactions

### Backtracing
common ways to record `visited`
1. implicitly - e.g., subset
```python
def dfs(start, cur):
    for i in range(start, n):
        # only go forward, after we select arr[i], next start will be i+1
        # won't go back to used position automatically
        dfs(i+1, cur+[arr[i]])
```
2. use `visited` set to record
```python
def dfs(visited, node):
# def dfs(node): # or use visited from outside, do not pass around
    for nbr in node.neighbors:
        visited.add(nbr)
        dfs(nbr)
        # !!!当dfs回退（回到分岔）时如果需要消除之前的记录
        visited.remove(nbr)
```
类似topological sort的on_stack，track的是当前一条路（不分岔）的记录。当需要从nbr回退到parent(i.e., node)时， 开始new nbr前，需要消除对当前nbr的记录

 2 typical ways
```python
def dfs(path, node):
    if len(path) == n:
        res.append(path)
        return
    for nbr in node.neighbors:
        # new path var specifically for this traverse
        dfs(path + [nbr], nbr)
#
def dfs(path, node):
    if len(path) == n:
        # need to get a deep copy as path will change for next nbr
        res.append(path.copy())
        return
    for nbr in node.neighbors:
        # suitable if not easy to create a new path variable
        path.append(nbr)
        dfs(path, nbr)
        # revert to original value for next nbr
        path.pop()
```
ex: wordSearch, traverse a matrix
```python
tmp = grid[i][j]
grid[i][j] = '.'
dfs(i+1, j)..
grid[i][j] = tmp
```

3. instead of mark visited 'node', sometimes we need to mark 'edge' (reconstruct flight - 332), or minimum account balance - reduce account remaining balance while iterating


### Dedup
subset:
```python
def dfs(start, cur):
    res.append(cur)
    if start == len(arr):
        #res.append(cur)
        return
    for i in range(start, n):
        # 任何当前重复的i能选出的组合，都可以用上一个i实现
        # i == start: 不再同一层
        if i > start and arr[i] == arr[i-1]:
            continue
        # note: arr[i]代表我们选了第i个元素
        # dfs(i+1...) 代表i选过了就不能再用了（如果可以重复用 -> dfs(i...)
        dfs(i+1, cur+[arr[i]])

dfs(0, [])
```
combination:
```python
# ex: [1, 1, 2]
def dfs(start):
    res = []
    # [1, 2], [2, 1]
    combs = dfs(start+1)
    for comb in combs:
        for i in range(len(combs)+1):
            res.append(comb[:i]+arr[start]+comb[i:])
            # [1 | 1, 2] stop
            # [1 | 2, 1], [2, |1|, 1]
            if i < len(combs) and comb[i] == arr[start]:
                break
    return res
```
caveat: in subset example, each result is distinct as starting position is different, so we can handle exit condition with start == n.
But in tree: must handle at leaf node, as if we do it at children of leaf (None), both left and right will yield same results
### 
running length
decode string (stripe)
jump game/group string 
    update maxDist/endPos for current i first
    compared to prefix sum, first check count THEN after prefixSum count
palindrome and permutation (while match ==target) - stop at first "condition break", for ex palindrom - no need to check first (i, j) additionally

sliding window: fixed size (simply sliding) vs varying size