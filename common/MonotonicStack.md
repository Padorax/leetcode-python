左边第一个小于	单调递增 (Bottom -> Top)	从左往右
左边第一个大于	单调递减 (Bottom -> Top)	从左往右
右边第一个小于	单调递增 (Bottom -> Top)	从右往左 (或从左往右看弹出时)
右边第一个大于	单调递减 (Bottom -> Top)	从右往左 (或从左往右看弹出时)

Sliding Window Maximum (递减)
Daily temperature (右边第一个大于)

Example: 
1. 左边第一个小于
```python
def findFirstSmallerLeft(nums):
    stack = []  # 存储索引
    res = [-1] * len(nums)
    
    for i in range(len(nums)):
        # 1. 弹出所有不符合“小于当前数”条件的栈顶
        while stack and nums[stack[-1]] >= nums[i]:
            stack.pop()
        
        # 2. 此时栈顶就是左边第一个小于它的索引
        if stack:
            res[i] = stack[-1]
            
        # 3. 当前索引入栈，为后续元素做参考
        stack.append(i)
        
    return res

# 测试
# nums = [2, 1, 5, 6, 2, 3]
# res  = [-1, -1, 1, 2, 1, 4]
```

2. 左边第一个大于
```python
def findFirstGreaterLeft(nums):
    stack = []
    res = [-1] * len(nums)
    
    for i in range(len(nums)):
        # 寻找大于：所以把小于等于当前数的全部弹出
        # 它们不可能是再右边数符合条件的左边第一个最大的
        while stack and nums[stack[-1]] <= nums[i]:
            stack.pop()
        
        if stack:
            res[i] = stack[-1]
        
        stack.append(i)
    return res
```
3. 右边第一个小于
```python
def findFirstSmallerRight(nums):
    stack, res = [], [-1]*len(nums)
    for i in range(len(nums)):
        while stack and nums[stack[-1]] > nums[i]:
            res[stack.pop()] = i
        stack.append(i)
    return res
```

4.  First greater on right (<-)
```python
def findFirstGreaterRight(nums):
    stack = []
    res = [-1] * len(nums)
    
    # 从右往左遍历
    for i in range(len(nums) - 1, -1, -1):
        # 只要栈顶比当前数小，它就不可能是右边第一个大的，弹出
        while stack and nums[stack[-1]] <= nums[i]:
            stack.pop()
        
        if stack:
            res[i] = stack[-1]
            
        stack.append(i)
    return res
```
or (->)
```python
def findFirstGreaterRight(nums):
    stack, res = [], [-1] * len(nums)
    for i range(len(nums)):
        while stack and nums[stack[-1]] < nums[i]:
            res[stack.pop()] = i
        stack.append(i)
    return res
```
