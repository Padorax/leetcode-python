```python
lo, hi = 0, len(nums)- 1
while lo <= hi:
    mid = lo + (hi - lo) // 2
    if nums[mid] == target:
        return mid
    elif nums[mid] < target:
        lo = mid + 1
    else:
        hi = mid - 1
return -1

# OR
while lo < hi:
    mid = lo + (hi - lo) // 2
    if nums[mid] < target:
        lo = mid + 1
    else:
        hi = mid
return nums[lo] == target
```
If we need to handle duplicates/find pos to insert, cannot just stop when nums[mid] == target. One way is to first do a normal binary search, find one position of target, then move left until the number is no longer target, that's the left end we want. Another way is as below.
### Solution
```python
class Solution(object):
    def searchInsert(self, nums, target):
        """
        :type nums: List[int]
        :type target: int
        :rtype: int
        """
        lo, hi = 0, len(nums) # import here to handle target > nums[-1]
        while lo < hi:
            mid  = (lo + hi) // 2
            if nums[mid] < target:
                lo = mid + 1
            else:
                hi = mid
        return lo
```

### Find min from rotated sorted arr
```python
def findMin(nums):
    l, r = 0, len(nums) - 1
    while l < r:
        mid = l + ((r - l) >> 1)
        if nums[mid] > nums[r]:
            l = mid + 1
        else:
            r = mid
    
    return nums[l]
```

### search in rotated sorted arr
```python
class Solution(object):
    def search(self, nums, target):
        lo, hi = 0, len(nums) - 1
        while lo <= hi:
            mid = (lo + hi) // 2
            if nums[mid] == target: return mid

            # left part is ordered
            if nums[lo] <= nums[mid]:
                # note nums[mid] != target 
                if nums[lo] <= target < nums[mid]:
                    hi = mid - 1
                else:
                    lo = mid + 1
            # right part is ordered
            else:
                if nums[mid] < target <= nums[hi]:
                    lo = mid + 1
                else:
                    hi = mid - 1
        
        return -1
    def search2(self, nums: List[int], target: int) -> int:
        n = len(nums)
        l, r = 0, n-1
        while l < r:
            mid = (l + r) // 2
            if nums[0] <= nums[mid]:
                if nums[0] <= target <= nums[mid]:
                    r = mid
                else:
                    l = mid + 1
            else:
                if nums[mid] < target <= nums[n - 1]:
                    l = mid + 1
                else:
                    r = mid
        return l if nums[l] == target else -1
```

### find first and last pos of element in sorted arr
```python
def searchRange(nums: List[int], target: int) -> List[int]:
    l = bisect_left(nums, target)
    r = bisect_left(nums, target + 1)
    return [-1, -1] if l == r else [l, r - 1]
```
or
```python
    def searchRange2(self, nums, target):
        """
        :type nums: List[int]
        :type target: int
        :rtype: List[int]
        """
        lo = self.firstGreaterEqual(nums, target)
        if lo >= len(nums) or nums[lo] != target:#handle empty set
            return [-1, -1]
        else:
            return [lo, self.firstGreaterEqual(nums, target + 1) - 1]

    def firstGreaterEqual(self, nums, target):
        # end = len(nums) not len(nums)-1, very important here. As this will give u the right pos if target > nums[-1]
        start, end = 0, len(nums)
        while start < end:
            mid = start + ((end - start) >> 1)
            if nums[mid] < target:
                start = mid + 1
            else:
                end = mid
        return start
```

### Note
1. right = n or n-1?
find target: n-1; find insertion pos: n (in case target > max(nums))
2. check: l < r or l <= r?
l < r: condition - l = mid +1; r = mid; finally check nums[l] == target
l <= r: condition - nums[mid] == target: return mid; l = mid + 1; r = mid -1; return -1