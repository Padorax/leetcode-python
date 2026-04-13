### BFS
```python
def bfs(root):
    res = []
    if not root: return res

    queue = [root]
    visited = {root}
    while queue:
        node = queue.pop(0)
        res.append(node)
        for nbr in node.neighbors:
            if nbr not in visited:
                queue.append(nbr)
                visited.add(queue)
    return res
```
to get level:
```python
def bfs(root):
    res = []
    if not root: return res

    queue = [root]
    visited = {root}
    while queue:
        level = []
        for _ in range(len(queue)): # this line
            node = queue.pop(0)
            level.append(node)
            for nbr in node.neighbors:
                if nbr not in visited:
                    queue.append(nbr)
                    visited.add(queue)
        res.append(level)
    return res
```
or
```python
def bfs(root):
    res = []
    if not root: return res

    queue = [root]
    lastElem = root
    visited = {root}
    level = []
    while queue:
        node = queue.pop(0)
        level.append(node)
        for nbr in node.neighbors:
            if nbr not in visited:
                queue.append(nbr)
                visited.add(queue)
        
        if node is lastElem: #finish this level
            lastElem = queue[-1]
            queue.append(level)
            level = []
    return res
```

### DFS - recursive
```python
def dfs(root):
    visited = set()
    
    def helper(node):
        # mark as visited
        #(*opt) do some checks (e.g., visited, desired value, exceed border)
        visited.add(node) 
        # for each one of its nbr, if hasn't visisted before, dfs
        for nbr in node.neighbors:
            #(opt) skip checks here and leverage next level *
            if nbr not in visited:
                helper(nbr)
    
    helper(root)
```

### DFS - iterative
```python
def dfs_iter(root):
    stack = [root]
    visited = set()
    res = []

    while stack:
        node = stack.pop()
        # skip if already met
        if node in visisted:
            continue
        visited.add(node) #visit
        res.append(node)

        for nbr in reversed(node.neighbors):
            if nbr not in visited:
                stack.append(nbr)

    return res
```

### Topological sort - bfs/iterative
```python
from collections import defaultdict, deque

def topo_sort_bfs(num_nodes, edges):
    graph = defaultdict(list)
    in_degree = [0] * num_nodes

    # Build graph and in-degree count
    # u(from) -> v(to)
    for u, v in edges:
        graph[u].append(v)
        in_degree[v] += 1

    # Queue for nodes with 0 in-degree
    queue = deque([i for i in range(num_nodes) if in_degree[i] == 0])
    topo_order = []

    while queue:
        node = queue.popleft()
        topo_order.append(node)
        for neighbor in graph[node]:
            in_degree[neighbor] -= 1
            if in_degree[neighbor] == 0:
                queue.append(neighbor)

    if len(topo_order) != num_nodes:
        raise ValueError("Cycle detected! Not a DAG")

    return topo_order

```
### Topological sort - dfs
```python
def topo_sort_dfs(num_nodes, edges):
    graph = defaultdict(list)
    for u, v in edges:
        graph[u].append(v)

    visited = [False] * num_nodes
    topo_order = []
    on_path = [False] * num_nodes  # For cycle detection

    def dfs(node):
        if on_path[node]:
            raise ValueError("Cycle detected! Not a DAG")
        if visited[node]:
            return
        visited[node] = True
        on_path[node] = True
        for neighbor in graph[node]:
            dfs(neighbor)
        on_path[node] = False
        topo_order.append(node)

    for i in range(num_nodes):
        if not visited[i]:
            dfs(i)

    return topo_order[::-1]  # Reverse post-order
```
or
```python
class Solution:
    def findOrder(self, numCourses: int, prerequisites: List[List[int]]) -> List[int]:
        childCourses = dict()
        for c, pc in prerequisites:
            if pc not in childCourses:
                childCourses[pc] = [c]
            else:
                childCourses[pc].append(c)
        
        onStack = set()
        visited = set()
        res = []
        # return True if can traverse without cycle
        def dfs(course):
            if course in onStack:
                return False
            if course in visited:
                return True
            onStack.add(course)
            visited.add(course)
            for nbr in childCourses.get(course, []):
                if not dfs(nbr):
                    return False
            onStack.remove(course)
            res.append(course)
            return True
        for c in range(numCourses):
            if not dfs(c):
                return []
        return res[::-1]
```

### Graph representation
adjacency matric, edge, neighbors (undirected)/fromNode, toNode(directed)

### Cycle
1. directed graph: bfs or dfs (meet any visited node, topological sort - on_path)
2. undirected: similar, but when check any neighbors[node] visited, exclude fromNode
dfs
```python
class Solution:
    def validTree(self, n: int, edges: List[List[int]]) -> bool:
        g = [[] for _ in range(n)]
        for a, b in edges:
            g[a].append(b)
            g[b].append(a)
        vis = set()

        # prev is parent (we come to n from prev), as we have undirected graph, need to exclude parent when checking cycle
        def dfs(n, prev):
            vis.add(n)
            for nbr in g[n]:
                if nbr == prev: continue
                if nbr in vis: return False
                if not dfs(nbr, n):
                    return False
            return True
        
        if not dfs(0, None):
            return False
        return len(vis) == n
```
### note 
compared to directed graph (topological sort):
1. do not need on_stack track - as in directed, a->b->c and a->c does not form cycle, but in undirected graph, a-b-c, a-c will be a graph, so just use visited to track all visited nodes, not on_stack to track current path
2. when check children/neighbor of current node, need to remember parent of current node and exclude from children

BFS
```python
class Solution:
    def validTree(self, n: int, edges: List[List[int]]) -> bool:
        g = [[] for _ in range(n)]
        for a, b in edges:
            g[a].append(b)
            g[b].append(a)

        q = [(0, None)]
        cnt = 0
        visited = {0}
        while q:
            cur, prev = q.pop(0)
            for nbr in g[cur]:
                if nbr == prev: continue
                if nbr in visited: return False
                visited.add(nbr)
                q.append((nbr, cur))
        return len(visited) == n
```
Note: if just need to traverse, no need to check cycle, then no need to record `prev`, as it's already added to `visisted`

### Dijkstra
不适用有负边的情况
1
```python
def dijkstra(graph, start):
    # distances 记录全局最短路径
    distances = {node: float('inf') for node in graph}
    distances[start] = 0
    pq = [(0, start)] # (距离, 节点)

    while pq:
        current_dist, u = heapq.heappop(pq)

        # 【核心逻辑：懒惰删除】
        # 如果当前弹出的距离已经不是最新的了，说明它是之前“被更新掉”的残余，直接跳过
        if current_dist > distances[u]:
            continue

        for v, weight in graph[u].items():
            distance = current_dist + weight
            
            # 如果发现更短的路径
            if distance < distances[v]:
                distances[v] = distance
                # 不更新原有的，而是直接推入一个新的，让堆自己去排优
                # 次优distance record会在L238被discard
                heapq.heappush(pq, (distance, v))
    
    return distances
```
or
2
```python
def dijkstra(graph, start):
    pq = [(0, start)] # (距离, 节点)
    final_distances = {} # 相当于你的 visited + 结果存储 第一次弹出即最优！

    while pq:
        d, u = heapq.heappop(pq)

        # 【核心判断】如果 u 已经在字典里，说明之前已经 pop 出过更短的路径了
        if u in final_distances:
            continue
        
        # 第一次 pop 出来，锁定最短路径
        final_distances[u] = d

        for v, weight in graph[u].items():
            if v not in final_distances:
                heapq.heappush(pq, (d + weight, v))
                
    return final_distances
```
特性	   1预初始化 distances[v] = d + weight	               2你的思路 (u in final_distances)
入堆时机	只有当新路径比 distances[v] 更短时才入堆。	           只要 v 还没被确定，就盲目入堆。
堆的大小	堆会保持得相对较小（因为有 if d < distances[v] 过滤）。	堆会变得很大。同一个节点 v 可能会被塞进去很多次，直到它第一次被 pop。
适用场景	内存受限，或者图非常稠密时。	                        逻辑更直接，适合处理动态生成的图。

* Time complexity: O(ElogV)
* Space complexity: O(V+E)
Where V is the number of vertices and E is the number of edges.

# Bellman Ford Algorithm
如果说 Dijkstra 是“走一步看一步”的聪明人，Bellman-Ford 就是一个“推倒重来 V-1 次”的死磕派。核心思想（松弛操作 Relaxation）：
对于图中的所有边，我们尝试进行更新：如果 dist[u] + weight(u, v) < dist[v]，就更新 dist[v]。
为什么要循环 V-1次？(i.e., # of edge)
    在一个有 V 个节点的图中，任意两点间的最短路径最多只包含 V-1 条边。每完整遍历一遍所有的边，最短路径的“确定范围”就会至少向前推进一个节点。
绝活：检测负环如果在 V-1 次循环后，你再进行第 V 次遍历，发现竟然还能更新距离，那说明图中存在负环（绕一圈距离变小，可以无限刷低距离。
```python
def bellman_ford(V, edges, start):
    # edges 格式: [(u, v, weight), ...]
    # 1. 初始化距离
    dist = [float('inf')] * V
    dist[start] = 0

    # 2. 执行 V-1 次松弛
    # 每一次循环，都能确定路径长度为 i 的所有点的最短路径
    for _ in range(V - 1):
        for u, v, w in edges:
            # u必须是可到达的，否则w<0时会用一个不可到达的u来更新v (inf-100=9999)
            if dist[u] != float('inf') and dist[u] + w < dist[v]:
                dist[v] = dist[u] + w

    # 3. 额外执行第 V 次检测负环
    # 如果还能更新，说明存在负环，最短路径会无限减小
    for u, v, w in edges:
        if dist[u] != float('inf') and dist[u] + w < dist[v]:
            return "Graph contains negative weight cycle"

    return dist
```
```python
def networkDelayTime(times: List[List[int]], n: int, k: int) -> int:
    # n node from 1...n
        dist = [float('inf')] * n
        dist[k - 1] = 0
        for _ in range(n - 1):
            for u, v, w in times:
                if dist[u - 1] + w < dist[v - 1]:
                    dist[v - 1] = dist[u - 1] + w
        max_dist = max(dist)
        return max_dist if max_dist < float('inf') else -1
```

# SPFA
更聪明的 Bellman-Ford: SPFA (Shortest Path Faster Algorithm) 实际上是 Bellman-Ford 的队列优化版。
* 痛点：Bellman-Ford 每次都要检查“所有”边，但其实很多边在某轮迭代中根本不会发生更新。
* 改进逻辑：只有当一个点 `u` 的距离 dist[u] 刚刚被缩小了，它周围的邻居 `v` 才有可能被缩小。
* 操作流程：建立一个队列，先把起点放进去。弹出 `u`，遍历 `u` 的邻居 `v`。如果通过 `u` 能让 `v` 变短，且 `v` 不在队列中，就把 `v`加入队列。重复直到队列为空。
* 小贴士：SPFA 在平均情况下极快，时间复杂度接近 O(k*E)（k 是常数）。但在极端构造的网格图中，它会退化回 Bellman-Ford 的 O(V*E)。
```python
def spfa(graph, start, V):
    # graph 格式: {u: [(v1, w1), (v2, w2)]}
    dist = [float('inf')] * V
    dist[start] = 0
    queue = deque([start])
    # 记录节点是否已经在队列中，避免重复入队
    in_queue = [False] * V
    in_queue[start] = True
    
    # 记录每个点入队次数，用于检测负环
    count = [0] * V

    while queue:
        u = queue.popleft()
        in_queue[u] = False
        
        # 只有 u 的距离变短了，它的邻居 v 才有可能变短
        for v, w in graph[u]:
            if dist[u] + w < dist[v]:
                dist[v] = dist[u] + w
                if not in_queue[v]:
                    queue.append(v)
                    in_queue[v] = True
                    count[v] += 1
                    # 如果一个点入队超过 V 次，必然存在负环
                    if count[v] >= V:
                        return "存在负环！"
    return dist
```

### Hierholzer 欧拉通路
322