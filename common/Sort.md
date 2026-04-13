Divide and conquor
### MergeSort 
O(nLog(n))
```python
def merge_sort(arr):
    if len(arr) <= 1:
        return arr  # Base case: already sorted
    
    mid = len(arr) // 2
    left = merge_sort(arr[:mid])
    right = merge_sort(arr[mid:])
    
    return merge(left, right)

def merge(left, right):
    merged = []
    i = j = 0
    
    # Merge the two sorted halves
    while i < len(left) and j < len(right):
        if left[i] <= right[j]:
            merged.append(left[i])
            i += 1
        else:
            merged.append(right[j])
            j += 1
    
    # Append remaining elements (only one list will have leftovers)
    merged.extend(left[i:])
    merged.extend(right[j:])
    
    return merged


# Example usage:
arr = [38, 27, 43, 3, 9, 82, 10]
print("Merge Sort:", merge_sort(arr))
```

### Quick sort
```python
def quick_sort(arr):
    if len(arr) <= 1:
        return arr  # Base case
    
    pivot = arr[len(arr) // 2]  # Choose pivot (middle here)
    left = [x for x in arr if x < pivot]
    middle = [x for x in arr if x == pivot]  # Keep duplicates here
    right = [x for x in arr if x > pivot]
    
    return quick_sort(left) + middle + quick_sort(right)
```
in-place partition
```python
def quicksort_inplace(arr, low, high):
    if low < high:
        # Partition the array and get pivot index
        pivot_index = partition(arr, low, high)
        # Recursively sort elements before and after partition
        quicksort_inplace(arr, low, pivot_index - 1)
        quicksort_inplace(arr, pivot_index + 1, high)

def partition(arr, low, high):
    pivot = arr[high]  # choose the rightmost element as pivot
    i = low - 1        # pointer for the smaller element

    for j in range(low, high):
        if arr[j] <= pivot:
            i += 1
            arr[i], arr[j] = arr[j], arr[i]  # swap

    arr[i + 1], arr[high] = arr[high], arr[i + 1]  # swap pivot to correct position
    return i + 1

# Example usage
arr = [10, 7, 8, 9, 1, 5]
quicksort_inplace(arr, 0, len(arr) - 1)
print("Sorted array:", arr)
```

pivoting using first elem
```python
def quicksort(arr, low, high):
    if low < high:
        p = partition(arr, low, high)
        quicksort(arr, low, p - 1)
        quicksort(arr, p + 1, high)

def partition(arr, low, high):
    pivot = arr[low]  # First element as pivot
    left = low + 1
    right = high

    while True:
        while left <= right and arr[left] <= pivot:
            left += 1
        while left <= right and arr[right] >= pivot:
            right -= 1
        if left > right:
            break
        arr[left], arr[right] = arr[right], arr[left]

    arr[low], arr[right] = arr[right], arr[low]
    return right

# Example usage
arr = [8, 3, 1, 7, 0, 10, 2]
quicksort(arr, 0, len(arr) - 1)
print(arr)  # [0, 1, 2, 3, 7, 8, 10]
```

pivot with specified element
```python
def quick_sort(arr, low, high, pivot_index=None):
    if low < high:
        # If a pivot_index is given, swap it to low
        if pivot_index is not None:
            arr[low], arr[pivot_index] = arr[pivot_index], arr[low]

        p = partition(arr, low, high)
        quick_sort(arr, low, p - 1, low)  # low pivot for left side
        quick_sort(arr, p + 1, high, p + 1)  # right side

def partition(arr, low, high):
    pivot = arr[low]  # pivot is now at 'low'
    left = low + 1
    right = high

    while True:
        while left <= right and arr[left] <= pivot:
            left += 1
        while left <= right and arr[right] >= pivot:
            right -= 1
        if left > right:
            break
        arr[left], arr[right] = arr[right], arr[left]

    arr[low], arr[right] = arr[right], arr[low]  # put pivot in correct place
    return right


# Example
arr = [8, 3, 1, 7, 0, 10, 2]
pivot_index = 3  # use element at index 3 as pivot (value 7)
quick_sort(arr, 0, len(arr) - 1, pivot_index)
print(arr)

```

#### three-way pivot (for dup)
low → boundary for < pivot
mid → current element being checked
high → boundary for > pivot
Elements equal to the pivot just move mid forward.

![](./images/Threewaypartitioning.png)

Swap and adjust pointers to keep partitions in place.
```python
def three_way_partition(arr, pivot_index):
    pivot = arr[pivot_index]
    low, mid, high = 0, 0, len(arr) - 1

    while mid <= high:
        if arr[mid] < pivot:
            arr[low], arr[mid] = arr[mid], arr[low]
            low += 1
            mid += 1
        elif arr[mid] > pivot:
            arr[mid], arr[high] = arr[high], arr[mid]
            high -= 1
        else:  # arr[mid] == pivot
            mid += 1

# Example
arr = [3, 5, 2, 5, 1, 5, 4, 5, 2]
three_way_partition(arr, 1)  # pivot_index=1 (pivot=5)
print(arr)  # [3, 2, 1, 4, 2, 5, 5, 5, 5]

```

#### quick sort with 3-way
```python
def quicksort_3way(arr, low=0, high=None):
    if high is None:
        high = len(arr) - 1
    if low >= high:
        return

    lt, i, gt = low, low + 1, high
    pivot = arr[low]

    while i <= gt:
        if arr[i] < pivot:
            arr[lt], arr[i] = arr[i], arr[lt]
            lt += 1
            i += 1
        elif arr[i] > pivot:
            arr[i], arr[gt] = arr[gt], arr[i]
            gt -= 1
        else:  # arr[i] == pivot
            i += 1

    # Recursively sort the partitions
    quicksort_3way(arr, low, lt - 1)
    quicksort_3way(arr, gt + 1, high)


# Example usage:
data = [3, 5, 2, 3, 1, 5, 3, 2, 3]
quicksort_3way(data)
print(data)  # [1, 2, 2, 3, 3, 3, 3, 5, 5]
```

### partition with extra O(n) space
```python
def partition(arr):
    pivot_idx = 0 # can change
    n = len(arr)
    res = [arr[pivot_idx]]*n

    l, r = 0, n-1 # point to boundary of smaller than (pivot) and bigger than
    for num in arr:
        if num < arr[pivot_idx]:
            res[l] = num
            l += 1
        elif num > arr[pivot_idx]:
            res[r] = num
            r -= 1
    return res
```

### QuickSelect找到top k大的数
* 2-way
```python
def quick_select(nums, k):
    def partition(left, right):
        # 随机选一个 pivot 并换到最后
        pivot_idx = random.randint(left, right)
        pivot_val = nums[pivot_idx]
        nums[pivot_idx], nums[right] = nums[right], nums[pivot_idx]
        
        store_idx = left
        for i in range(left, right):
            # 如果当前值比基准小，就换到左边的“小数值区”
            if nums[i] < pivot_val:
                nums[store_idx], nums[i] = nums[i], nums[store_idx]
                store_idx += 1
        
        # 最后把 pivot 换回中间：左边都比它小，右边都比它大
        nums[store_idx], nums[right] = nums[right], nums[store_idx]
        return store_idx

    def select(left, right, target_k):
        if left >= right:
            return
        
        pivot_pos = partition(left, right)
        
        if pivot_pos == target_k:
            return
        elif pivot_pos > target_k:
            select(left, pivot_pos - 1, target_k)
        else:
            select(pivot_pos + 1, right, target_k)

    # 我们要找的是下标为 k-1 的位置
    select(0, len(nums) - 1, k - 1)
    return nums[:k]

# 测试一下
example = [10, 4, 5, 8, 6, 11, 26]
print(quick_select(example, 3)) # 输出前3个最小的数
```
store_idx 的含义：它始终指向“第一个不比 pivot 小的元素”的位置。你可以把它想象成一个边界线，线的左边全是已经确认过的小个子。原地交换 (In-place)：这个算法非常省内存，因为它直接在原数组上“左右横跳”，不需要开辟新空间。随机 Pivot 的意义：如果不随机选（比如总选最后一个），遇到已经排好序的数组，复杂度会退化到 O(n^2)。随机选一下，就像洗牌一样，能保证平均时间复杂度稳在 $O(n)$。

QuickSelect (Lomuto Partition 2-way)：它像一个“粗鲁”的搬运工。它只管把比基准（Pivot）小的扔左边，剩下的全堆右边。右边那些比 Pivot 大的还是等于 Pivot 的，它根本不在乎，这就导致了你说的“右边很混乱”。

* 3-way
```python
import random

def quick_select_3way(nums, k):
    def select(left, right, target_k):
        if left >= right:
            return
        
        # 1. 随机选 pivot 并定义三路指针
        pivot = nums[random.randint(left, right)]
        lt = left      # [left...lt-1] 是小于 pivot 的区
        i = left       # [lt...i-1] 是等于 pivot 的区
        gt = right     # [gt+1...right] 是大于 pivot 的区
        
        # 2. 三路切分核心逻辑
        while i <= gt:
            if nums[i] < pivot:
                nums[lt], nums[i] = nums[i], nums[lt]
                lt += 1
                i += 1
            elif nums[i] > pivot:
                nums[gt], nums[i] = nums[i], nums[gt]
                gt -= 1
                # 换回来的 nums[i] 还没看过，所以 i 不移动
            else:
                i += 1
        
        # 此时数组布局为：[小于区] [等于区(lt...gt)] [大于区]
        
        # 3. 决定下一步往哪走
        # target_k 是我们要找的下标（从0开始）
        if lt <= target_k <= gt:
            # 运气爆棚，我们要找的第 k 个数就在“等于区”里
            return 
        elif target_k < lt:
            # 在左边“小于区”找
            select(left, lt - 1, target_k)
        else:
            # 在右边“大于区”找
            select(gt + 1, right, target_k)

    # 找第 k 小，对应的下标是 k-1
    select(0, len(nums) - 1, k - 1)
    return nums[:k]

# 测试：有大量重复元素
example = [3, 2, 3, 1, 2, 4, 5, 5, 6, 2, 2, 2]
print(quick_select_3way(example, 4)) # 寻找前4个小的数
```
3-Way Partition (Dutch National Flag Algorithm)：它像一个有强迫症的分类员。它把元素严格分成三堆：小于、等于、大于。
既然 QuickSelect 已经能找到前 K 个了，为什么要搞这么复杂？

答案是：为了对付“大量重复元素”。

如果一个数组里全是重复的数字（比如一万个 7），普通的 QuickSelect 每次可能只排好一个元素，导致复杂度退化到 O(n^2)。

3-Way Partition 一次性就把所有的 7 都归位了。下次递归时，它直接跳过整个“等于 7”的区间，速度极快。

For iterative see 973.