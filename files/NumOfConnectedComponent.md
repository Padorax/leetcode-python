# Number of Connected Components in an Undirected Graph
You have a graph of n nodes. You are given an integer n and an array edges where edges[i] = [aᵢ, bᵢ] indicates that there is an edge between aᵢ and bᵢ in the graph.

Return the number of connected components in the graph.

```python
class Solution:
    def countComponents(self, n: int, edges: List[List[int]]) -> int:
        parent = list(range(n))
        def find(x):
            if parent[x] != x:
                parent[x] = find(parent[x])
            return parent[x]
        def union(a, b):
            pa, pb = find(a), find(b)
            if pa == pb:
                return False
            parent[pa] = pb
            return True
        
        cnt = n
        for n1, n2 in edges:
            if union(n1, n2):
                cnt -= 1
        return cnt
```