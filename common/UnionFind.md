https://www.geeksforgeeks.org/dsa/introduction-to-disjoint-set-data-structure-or-union-find-algorithm/

### Plain
```python
class UnionFind:
    def __init__(self, size):
        # Initialize the parent array with each
        # element as its own representative
        self.parent = list(range(size))

    def find(self, i):
        # If i itself is root or representative
        if self.parent[i] == i:
            return i

        # Else recursively find the representative
        # of the parent
        return self.find(self.parent[i])

    def unite(self, i, j):
        # Representative of set containing i
        irep = self.find(i)

        # Representative of set containing j
        jrep = self.find(j)

        # Make the representative of i's set
        # be the representative of j's set
        self.parent[irep] = jrep
```

### Improvements
Compression (to improve find())
0-1-2-3 => 
  0
1 2 3
1. Union by Rank
```python
class DisjointUnionSets:
    def __init__(self, n):
        self.rank = [0] * n
        self.parent = list(range(n))

    def find(self, i):
        root = self.parent[i]

        if self.parent[root] != root:
            self.parent[i] = self.find(root)
            return self.parent[i]

        return root

    def unionSets(self, x, y):
        xRoot = self.find(x)
        yRoot = self.find(y)

        if xRoot == yRoot:
            return

        # Union by Rank
        if self.rank[xRoot] < self.rank[yRoot]:
            self.parent[xRoot] = yRoot
        elif self.rank[yRoot] < self.rank[xRoot]:
            self.parent[yRoot] = xRoot
        else:
            self.parent[yRoot] = xRoot
            self.rank[xRoot] += 1
```

2. Union by Size
```python
class DisjointUnionSets:
    def __init__(self, n):
        self.rank = [0] * n
        self.parent = list(range(n))

    def find(self, i):

        root = self.parent[i]

        if self.parent[root] != root:
            self.parent[i] = self.find(root)
            return self.parent[i]

        return root

    def unionSets(self, x, y):
        xRoot = self.find(x)
        yRoot = self.find(y)

        if xRoot == yRoot:
            return

        # Union by Rank
        if self.rank[xRoot] < self.rank[yRoot]:
            self.parent[xRoot] = yRoot
        elif self.rank[yRoot] < self.rank[xRoot]:
            self.parent[yRoot] = xRoot
        else:
            self.parent[yRoot] = xRoot
            self.rank[xRoot] += 1
```
求连通分量（connected components)，必定process as undirected graph
### BFS/DFS
这种方法的逻辑很直观：“只要我能走到的地方，都属于我的领土。”

* 核心逻辑：维护一个 visited 集合。遍历每一个节点，如果它还没被访问过，说明发现了一个新的连通分量。从这个点开始做一次完整的 BFS 或 DFS，把所有能碰到的邻居都标记为已访问。
* 适用场景：图已经完整给出（静态图），且你需要一次性找出所有成分。
```python
def find_partitions_by_search(nodes, adj):
    visited = set()
    partitions = []
    
    for node in nodes:
        if node not in visited:
            # 发现新分量
            current_partition = []
            queue = [node]
            visited.add(node)
            
            while queue:
                curr = queue.pop(0) # BFS 用 pop(0), DFS 用 pop()
                current_partition.append(curr)
                for neighbor in adj[curr]:
                    if neighbor not in visited:
                        visited.add(neighbor)
                        queue.append(neighbor)
            
            partitions.append(current_partition)
    return partitions
```
Union find
```python
class UnionFind:
    def __init__(self, nodes):
        self.parent = {node: node for node in nodes}
    
    def find(self, i):
        if self.parent[i] == i:
            return i
        # 路径压缩 (Path Compression)
        self.parent[i] = self.find(self.parent[i])
        return self.parent[i]

    def union(self, i, j):
        root_i = self.find(i)
        root_j = self.find(j)
        if root_i != root_j:
            self.parent[root_i] = root_j

def find_partitions_by_uf(nodes, adj):
    uf = UnionFind(nodes)
    # 遍历所有边进行合并
    for u in adj:
        for v in adj[u]:
            uf.union(u, v)
    
    # 按照根节点对分量进行归类
    root_to_partition = {}
    for node in nodes:
        root = uf.find(node)
        if root not in root_to_partition:
            root_to_partition[root] = []
        root_to_partition[root].append(node)
        
    return list(root_to_partition.values())
```