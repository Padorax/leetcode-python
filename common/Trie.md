### Common Trie
```python
class Node:
    def __init__(self):
        self.isEnd = False
        self.children = [None]*26
class PrefixTree:

    def __init__(self):
        self.root = Node()

    def insert(self, word: str) -> None:
        cur = self.root
        for l in word:
            idx = ord(l)-ord('a')
            if not cur.children[idx]:
                cur.children[idx] = Node()
            cur = cur.children[idx]
        cur.isEnd = True 

    def search(self, word: str) -> bool:
        cur = self.root
        for l in word:
            idx = ord(l)-ord('a')
            if not cur.children[idx]:
                return False
            cur = cur.children[idx]
        return cur.isEnd

    def startsWith(self, prefix: str) -> bool:
        cur = self.root
        for l in prefix:
            idx = ord(l)-ord('a')
            if not cur.children[idx]:
                return False
            cur = cur.children[idx]
        return True
        
```

### Trie search with '.'
```python
class Node:
    def __init__(self):
        self.end = False
        self.children = dict()
class WordDictionary:

    def __init__(self):
        self.root = Node()

    def addWord(self, word: str) -> None:
        cur = self.root
        for l in word:
            if l not in cur.children:
                cur.children[l] = Node()
            cur = cur.children[l]
        cur.end = True

    def search(self, word: str) -> bool:
        cur = self.root
        def dfs(pos, cur):
            if pos == len(word):
                return cur and cur.end
            if word[pos] != '.':
                if word[pos] not in cur.children:
                    return False
                else:
                    return dfs(pos+1, cur.children[word[pos]])
            else:
                for l, node in cur.children.items():
                    if dfs(pos+1, node):
                        return True
                return False
        return dfs(0, self.root)
```

### Trie + 后缀索引
Trie 的每一个节点上增加了一个**“后缀索引”**（或者说是当前节点到单词结尾的完整映射）。
这种做法虽然会增加空间复杂度，但在特定场景（比如**前缀匹配**、**自动补全**）下，它的查询速度会快到飞起，因为你不需要再通过 DFS 去深挖子树，而是可以直接“读出”结果。


1. 代码实现
在 `addWord` 的过程中，我们需要在路径上的每一个节点都把当前单词剩余的部分存进去。

```python
class TrieNode:
    def __init__(self):
        self.children = {}
        self.isWord = False
        # 存储从当前节点开始的所有后续单词部分
        # 比如插入 "dog"，'d' 节点的 list 会存 "og"
        self.suffixes = [] 

class WordDictionary:
    def __init__(self):
        self.root = TrieNode()

    def addWord(self, word: str) -> None:
        cur = self.root
        for i, char in enumerate(word):
            if char not in cur.children:
                cur.children[char] = TrieNode()
            
            # 存入从当前字母之后剩下的后缀
            # 比如 "dog"，在 'd' 处存 "og"，在 'o' 处存 "g"，在 'g' 处存 ""
            suffix = word[i+1:]
            cur.children[char].suffixes.append(suffix)
            
            cur = cur.children[char]
        cur.isWord = True

```
2. 这种设计的优缺点分析

优点：查询效率极高
* **前缀搜索**：如果你想找所有以 `do` 开头的词，你走到 `o` 节点后，直接读取 `self.suffixes` 列表，给每个后缀加上 `do` 前缀即可。不需要再遍历整棵子树。
* **复杂度**：从  降到了 。

缺点：空间爆炸
* **冗余存储**：一个长度为  的单词，会被拆分成  个部分分别存入  个节点。
* **空间复杂度**：最坏情况下从  变成 。如果单词很长且很多，内存压力会非常大。

3. 应用场景：搜索引擎自动补全
这种设计在 **Search Autocomplete**（搜索框自动补全）中非常常见。
当你在 Google 搜索框输入 `apple` 时，系统不会在你敲完字母后现去遍历数亿个节点。它通常会在 `a-p-p-l-e` 这个路径的最后一个节点上，直接存好**点击率最高的前 10 个搜索词**。

4. 优化建议：只存 ID 或 限制数量

为了防止空间爆炸，生产环境通常不会存完整的字符串：
1. **存储 Word ID**：`suffixes` 列表里只存整数 ID，最后去一个全局表中查字符串。
2. **Top-K 过滤**：每个节点只存最热门的 10 个后缀，而不是所有的。


总结你的思考

你的这个想法本质上是**“预计算” (Pre-computation)**。

* **标准 Trie**：像是一本地图，你得顺着路走才知道终点在哪。
* **你的 Trie**：在每一个路口都立了一个告示牌，写着：“从这里走下去，你可以到达 A, B 和 C”。

这种用**空间换时间**的极客精神非常到位！

**既然你已经想到了这一步，要不要试着实现一个“带权重的自动补全”？即每个单词有个频率，你在每个节点只存频率最高的前 3 个后缀。这可是大厂系统设计（System Design）的高频面试点。**

# Top K Trie
**“带权重的 Top-K 自动补全”**是系统设计中的经典场景。它不仅考验你对 Trie 数据结构的理解，还考验你对数据规模和性能平衡的把控。我们要实现的逻辑是：每个单词都有一个 weight（搜索频率）。在 addWord 的过程中，路径上的每个节点都要维护一个有序列表，只保留权重最高的前 3 个（或 $K$ 个）单词。1. 带权重的 Trie 节点设计我们不仅要存单词，还要在每个节点实时更新“最火”的候选项。Pythonimport heapq
```python
class TrieNode:
    def __init__(self):
        self.children = {}
        # 存储格式：[(weight1, word1), (weight2, word2), ...]
        # 我们希望它是按权重降序排列的
        self.top_k = []

class AutocompleteSystem:
    def __init__(self):
        self.root = TrieNode()
        self.k = 3  # 每个节点只存前 3 热门的

    def addWord(self, word: str, weight: int) -> None:
        cur = self.root
        for char in word:
            if char not in cur.children:
                cur.children[char] = TrieNode()
            cur = cur.children[char]
            
            # --- 更新当前节点的 Top-K ---
            # 1. 把新单词和权重加进去
            cur.top_k.append((weight, word))
            # 2. 按权重从大到小排序
            cur.top_k.sort(key=lambda x: -x[0])
            # 3. 只保留前 K 个，多余的砍掉
            if len(cur.top_k) > self.k:
                cur.top_k.pop()

    def search(self, prefix: str) -> List[str]:
        cur = self.root
        for char in prefix:
            if char not in cur.children:
                return [] # 前缀不存在，直接返回空
            cur = cur.children[char]
        
        # 重点：搜索到前缀节点后，直接 $O(1)$ 读取预存的结果
        return [word for weight, word in cur.top_k]
```
2. 这里的“系统设计”巧思查询速度达到极致：普通的搜索需要 O(子树节点数)。现在的查询复杂度仅为 O(L) (L 是前缀长度)，因为结果已经“喂到嘴边”了。写时开销换取读时性能：在 addWord 时做了排序（Write-time overhead），但对于搜索引擎来说，读操作（用户搜索）的频率远高于写操作（新词入库），这个置换非常划算。排序优化：在实际高并发系统中，我们不会每次 append 后都 sort。我们会用一个小顶堆来维护这 3 个元素，或者当列表很小时直接进行简单的插入排序。3. 进阶思考：如果权重（频率）会变怎么办？在现实中，某个词可能突然爆火（比如“Switch 2 发售”）。挑战：如果 weight 变了，我们需要更新所有路径节点的 top_k 列表。对策：通常我们会采用异步更新。先记录日志，每隔几分钟用一个离线任务（Offline Job）重新计算 Trie 节点里的 top_k。