BST traversal

### Recursive
```python
class Solution(object):
    def inorderTraversal(self, root):
        """
        :type root: TreeNode
        :rtype: List[int]
        """
        ret = []
        self.inorder(root, ret)
        return ret
    
    def inorder(self, root, ret):
        if not root:
            return
        self.inorder(root.left, ret)
        ret.append(root.val)
        self.inorder(root.right, ret)
```

### Inorder - iterative
```python
class Solution(object):
    def inorderTraversal(self, root):
        ans = []
        stack = []
        cur = root
        while cur or stack:
            # while node is not null, push stack
            while cur:
                stack.append(cur)
                cur = cur.left
            # node is null, pop stack
            cur = stack.pop()
            # add current val
            ans.append(cur.val)
            # right sub-tree
            # cannot check if cur.right!! otherwise if not right, cur won't be upated using last elem from stack
            cur = cur.right
        return ans
```

### Pre-order - iterative
```python
    def preorderTraversal(self, root: Optional[TreeNode]) -> List[int]:
        if not root:
            return []
        result = []
        stack = [root]
        
        while stack:
            node = stack.pop()
            result.append(node.val)
            # Push right first so that left is processed first
            if node.right:
                stack.append(node.right)
            if node.left:
                stack.append(node.left)

        return result
```

### Post-order (iterative)
1 way is to do a middle-> right -> left pre-order traveral, then print result in a reverse way. The other:
```python
def postOrderTraversal(root):
    if not root: return []

    stack = [(root, False)]
    res = []
    while stack:
        node, visited = stack.pop()
        if visited:
            res.append(node.val)
        else:
            stack.append((node, True))
            if node.right:
                stack.append((node.right, False))
            if node.left:
                stack.append((node.left, False))
    
    return res
```
or (iterative)
```python 
class Solution:
    def diameterOfBinaryTree(self, root: Optional[TreeNode]) -> int:
        stack = [root]
        mp = {None: (0, 0)}

        while stack:
            node = stack[-1]

            if node.left and node.left not in mp:
                stack.append(node.left)
            elif node.right and node.right not in mp:
                stack.append(node.right)
            else:
                node = stack.pop()

                leftHeight, leftDiameter = mp[node.left] #---
                rightHeight, rightDiameter = mp[node.right] # ---

                mp[node] = (1 + max(leftHeight, rightHeight),
                           max( leftHeight + rightHeight, leftDiameter, rightDiameter))

        return mp[root][1]
```
or (iterative)
```python
def postOrder(root):
# res is the output
    res, toVisit = [], []
    cur, pre = root, None

    while cur or toVisit:
        while cur:
            toVisit.append(cur)# add root
            cur = cur.left# add left child

        cur = toVisit[-1]# already at the most left
        # if no right child or has already visited right child, visit root
        if not cur.right or cur.right == pre:
            toVisit.pop()
            res.append(cur.val)
            pre = cur
            cur = None
        else:
            # visit right child first if haven't
            cur = cur.right

    return res
```

### Level-order traverasl: iterative
```python
    def levelOrder(self, root: Optional[TreeNode]) -> List[List[int]]:
        res = []
        if not root: return res
        
        queue = [root]
        while queue:
            curLevel = []
            for _ in range(len(queue)):
                curNode = queue.pop(0)
                curLevel.append(curNode.val)
                if curNode.left:
                    queue.append(curNode.left)
                if curNode.right:
                    queue.append(curNode.right)
            
            res.append(curLevel)
        return res
```

### Level-order traversal: dfs
```python
def levelOrder(root):
    ans = []
    def dfs(root, level):
        if not root: return
        # no element in curretn level, initialize an empty list
        if len(ans) <= level:
            ans.append([])
        # add node value to current level
        ans[level].append(root.val)

        dfs(root.left, level + 1)
        dfs(root.right, level + 1)

    dfs(root, 0)
    return ans
```
### Serialize
```python
    def serialize(self, root: Optional[TreeNode]) -> str:
        if not root: return ""
        queue = [root]
        res = []
        while queue:
            cur = queue.pop(0)
            if cur is None:
                res.append("null")
                continue
            res.append(str(cur.val))
            queue.append(cur.left)
            queue.append(cur.right)
        return ','.join(res)
```

### Construct tree from arr (297)
```python
def construcTree(arr):
    node = TreeNode(arr[0])
    queue = [node]
    i = 1
    while i < len(arr):
        cur = queue.pop(0)
        if arr[i]:
            cur.left = TreeNode(arr[i])
            queue.append(cur.left)
        i += 1
        if arr[i]:
            cur.right = TreeNode(arr[i])
            queue.append(cur.right)
        i += 1
    return node
```

### construct tree from inorder and preorder
```python
def buildTree(preorder, inorder):
    """
    :type preorder: List[int]
    :type inorder: List[int]
    :rtype: TreeNode
    """
    inMap = {}
    for i, v in enumerate(inorder):
        inMap[v] = i

    def build(p_start, p_end, i_start, i_end):
        if p_start == p_end:
            return None

        root_idx = inMap[preorder[p_start]]
        root = TreeNode(preorder[p_start])
        left_len = root_idx - i_start

        root.left = build(p_start+1, p_start+left_len+1, i_start, root_idx)
        root.right = build(p_start+left_len+1, p_end, root_idx+1, i_end)
    return build(0, len(preorder), 0, len(inorder))
```