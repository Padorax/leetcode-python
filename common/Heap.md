1. 找到最大的k个
* max-heap, 每次heap pop top都是最大。缺点 - heap size是总共element个数n
* min-heap
pros: 可以把heap的size控制在K以内
```python
import heapq

def findTopK(nums: List[int], K: int) -> List[int]:
    h = []
    for num in nums:
        if len(h) < K:
            heapq.heappush(h, num)
        elif h[0] < num:
            heapq.heapreplace(h, num) # or heapq.heappop(), heapq.heappush(h, num)
        #or 
        # heapq.heappush(h, num)
        # if len(h) > K: heapq.heappop()
    
    res = []
    while h:
        res.append(heapq.heappop(h))
    return res
```

2. merge M sorted - 给定M个sorted array/list: (say共N个element)
2.1 merge to sorted
say 升序排列,维护一个size = M的queue指向每一个list的head，每次heappop最小值后把当前值的next enqueue
```python
def mergeKSorted(nodes:List[ListNode]):
    h = []
    head = ListNode()
    for node in nodes:
        heapq.heappush(h, [node.val, node]) #最小元素只可能来自每个list的head
    
    cur = head
    while h:
        _, node = heapq.heappop(h)
        if node.next:
            heapq.heappush(h, [node.next.val, node.next]) #当前最小node的next enqueue参与最小值竞争
        cur.next = node
        cur = cur.next
    return head.next
```

2.2 从M个sorted list中找到最小（大）的K个element
- (1) 此时也可以采用option 1 - 把所有元素遍历一遍，维护size=K的min堆，time complexity = `O(NlogK)`
not recommended, 没有用到partially sorted的特性
- (2) 或者同上，只track M个list的head和next
```python
...
while h and len(res) < K:
```
time complexity = `O(KlogM)`
- (3) furthermore,当M>>k时，可以进一步优化：instead of 维护一个size=M的heap来track min head, 可以先采用1的方法，遍历M list head， 通过一个size=K的*max Heap*来找到最小的K个list head (`O(MlogK)`)
再把这K个head加到*min Heap*，通过2.1找到最小的K个 `O(KlogK)`

Example: Design Twitter (355)