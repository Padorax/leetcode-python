# Reverse List
* Iterative
```python
```
* Recursive
```python
```

# Reverse List in K Groups
Reverse if last group does not have K nodes
```python
class Solution:
    def reverseKGroup(self, head: Optional[ListNode], k: int) -> Optional[ListNode]:
        if not head:
            return head
        cur, prev = head, None
        for _ in range(k):
            if not cur:
                break # handle not enough k nodes for a group
            nxt = cur.next
            cur.next = prev
            cur, prev = nxt, cur
        # prev points to 3, cur points to 4
        head.next = self.reverseKGroup(cur, k)
        return prev
```
Do not reverse if last group does not have K nodes
```python
# Definition for singly-linked list.
# class ListNode:
#     def __init__(self, val=0, next=None):
#         self.val = val
#         self.next = next

class Solution:
    def reverseKGroup(self, head: Optional[ListNode], k: int) -> Optional[ListNode]:     
        cur = head
        for _ in range(k):
            if not cur:
                return head
            cur = cur.next

        cur, prev = head, None
        for _ in range(k):
            if not cur:
                break # handle not enough k nodes for a group
            nxt = cur.next
            cur.next = prev
            cur, prev = nxt, cur
        # prev points to 3, cur points to 4
        head.next = self.reverseKGroup(cur, k)
        return prev

```